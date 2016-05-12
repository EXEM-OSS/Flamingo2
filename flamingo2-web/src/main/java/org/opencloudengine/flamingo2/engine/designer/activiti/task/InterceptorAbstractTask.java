/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.engine.designer.activiti.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencloudengine.flamingo2.engine.designer.GlobalAttributes;
import org.opencloudengine.flamingo2.engine.history.TaskHistory;
import org.opencloudengine.flamingo2.engine.history.TaskHistoryRepository;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRepository;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.model.rest.State;
import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.util.el.ELEvaluator;
import org.opencloudengine.flamingo2.util.el.ELService;
import org.opencloudengine.flamingo2.util.el.ELUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHolder;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.remote.EngineLookupService;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.uengine.kernel.ProcessInstance;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public abstract class InterceptorAbstractTask extends AbstractTask {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(InterceptorAbstractTask.class);

    private static ObjectMapper objectMapper = new ObjectMapper();
    private Logger exceptionLogger = LoggerFactory.getLogger("flamingo.exception");
    private WorkflowHistoryRepository workflowHistoryRepository;
    private TaskHistoryRepository taskHistoryRepository;
    private TaskHistory taskHistory;
    private WorkflowHistory workflowHistory;
    private GlobalAttributes globalAttributes;
    private ELService elService;
    private ELEvaluator evaluator;
    private ProcessInstance instance;

    /**
     * 스크립트 변수, 워크플로우 변수, 태스크 변수, 글로벌 변수를 모두 결합한 속성.
     */
    protected Properties variable;
    protected String clusterName;
    protected String working;
    protected String username;

    @Override
    public void doExecute(final ProcessInstance instance, Map params) throws Exception {
        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
        this.workflowHistoryRepository = context.getBean(WorkflowHistoryRepository.class);
        this.taskHistoryRepository = context.getBean(TaskHistoryRepository.class);
        this.globalAttributes = context.getBean(GlobalAttributes.class);
        this.elService = context.getBean(ELService.class);
        this.evaluator = getElService().createEvaluator();
        this.instance = instance;
        this.variable = getMergedScriptVariables();

        this.working = params != null && params.get("working") != null && !StringUtils.isEmpty(resolve(params.get("working").toString())) ? resolve(params.get("working").toString()) : (instance.get("", "logdir").toString() + "/" + getTaskId());
        this.clusterName = (String) instance.get("clusterName");
        this.username = (String) instance.get("username");

        logger.info("Working Directory : {}", working);
        logger.info("Cluster : {}", clusterName);

        variable.setProperty("working", working);
        variable.setProperty("taskId", this.getTaskId());

        if (this.getAsync()) {  // Asynchronous Invocation
            preRun(instance);
            new Thread() { // FIXME 쓰레드 관리가 필요한 것으로 보임. Thread Pool Executor (Watchdog을 적용해서 Timeout이나 뭐 기타 정책이 반영되어야하지 않을지)...
                @Override
                public void run() {
                    try {
                        runTask(instance);
                        if (!isSubflow) {
                            postRun(instance);
                            fireComplete(instance);
                        }
                    } catch (Exception ex) {
                        exceptionLogger.warn("You can not run a task in the workflow.", ex);
                        getWorkflowHistory().setException(ExceptionUtils.getFullStackTrace(ex));
                        if (ex.getCause() != null) getWorkflowHistory().setCause(ex.getCause().getMessage());
                        try {
                            updateTaskHistoryAsFailed(instance);
                            updateHistoryAsFailed(getWorkflowHistory());
                        } catch (Exception e) {
                            exceptionLogger.warn("You can not save history information.", e);
                        }
                    }
                }
            }.start();
        } else { // Synchronous Invocation
            preRun(instance);
            try {
                runTask(instance);
                if (!isSubflow) {
                    postRun(instance);
                    fireComplete(instance);
                }
            } catch (Exception ex) {
                exceptionLogger.warn("You can not run a task in the workflow.", ex);
                getWorkflowHistory().setException(ExceptionUtils.getFullStackTrace(ex));
                if (ex.getCause() != null) getWorkflowHistory().setCause(ex.getCause().getMessage());
                try {
                    updateTaskHistoryAsFailed(instance);
                    updateHistoryAsFailed(getWorkflowHistory());
                } catch (Exception e) {
                    exceptionLogger.warn("You can not save history information.", e);
                }
            }
        }
    }

    protected void preRun(ProcessInstance instance) throws Exception {
        WorkflowHistory workflowHistory = updateCurrentStep(instance);
        this.workflowHistory = workflowHistory;

        // AsyncWorkflowServiceImpl 에서 간이로 인서트시킨 타스크들에 대해 업데이트한다.
        insertTaskHistory(instance, workflowHistory);

        // globalAttributes 저장소에 타스크의 시작 시그널을 남긴다.
        globalAttributes.setTaskStatus(instance, this.getTaskId(), "RUNNING");

        // Websocket 이 있다면 타스크가 시작되었음을 알린다.
        this.updateSocketTaskStatus();
    }

    public abstract void runTask(ProcessInstance instance) throws Exception;

    protected void postRun(ProcessInstance instance) throws Exception {
        updateTaskHistoryAsFinished(instance);
    }

    private WorkflowHistory updateCurrentStep(ProcessInstance instance) throws Exception {
        WorkflowHistory history = (WorkflowHistory) instance.get("workflowHistory");
        history.setCurrentAction(this.getTaskName());
        workflowHistoryRepository.updateCurrentStep(history);
        return history;
    }

    private void insertTaskHistory(ProcessInstance instance, WorkflowHistory workflowHistory) throws Exception {
        Workflow workflow = (Workflow) instance.get("workflow");
        taskHistory = new TaskHistory(workflow, instance, workflowHistory.getJobStringId(), this.getTaskId(), this.getTaskName());
        taskHistory.setTaskId(this.getTaskId());
        instance.set(this.getTaskId(), taskHistory);
        taskHistoryRepository.updateByTaskIdAndIdentifier(taskHistory);
        this.taskHistory = taskHistoryRepository.selectByTaskIdAndIdentifier(taskHistory);
    }

    private void updateTaskHistoryAsFinished(ProcessInstance instance) throws Exception {
        TaskHistory taskHistory = taskHistoryRepository.selectByTaskIdAndIdentifier(new TaskHistory((String) instance.get("identifier"), this.getTaskId()));
        taskHistory.setEndDate(new Timestamp(System.currentTimeMillis()));
        taskHistory.setStatus(State.SUCCEEDED.toString());
        taskHistory.setDuration(DateUtils.getDiff(taskHistory.getEndDate(), taskHistory.getStartDate()));
        taskHistoryRepository.updateByTaskIdAndIdentifier(taskHistory);

        // globalAttributes 저장소에 타스크의 성공 시그널을 남긴다.
        globalAttributes.setTaskStatus(instance, this.getTaskId(), State.SUCCEEDED.toString());

        // Websocket 이 있다면 타스크가 종료되었음을 알린다.
        this.updateSocketTaskStatus();
    }

    public void updateTaskHistoryAsFailed(ProcessInstance instance) throws Exception {
        TaskHistory taskHistory = taskHistoryRepository.selectByTaskIdAndIdentifier(new TaskHistory((String) instance.get("identifier"), this.getTaskId()));
        taskHistory.setEndDate(new Timestamp(System.currentTimeMillis()));
        taskHistory.setStatus("FAILED");
        taskHistory.setDuration(DateUtils.getDiff(taskHistory.getEndDate(), taskHistory.getStartDate()));
        taskHistoryRepository.updateByTaskIdAndIdentifier(taskHistory);

        // globalAttributes 저장소에 타스크의 실패 시그널을 남긴다.
        globalAttributes.setTaskStatus(instance, this.getTaskId(), State.FAILED.toString());

        // Websocket 이 있다면 타스크가 실패하었음을 알린다.
        this.updateSocketTaskStatus();
    }

    public void updateSocketTaskStatus() throws IOException {
        List<TaskHistory> taskHistories = taskHistoryRepository.selectByIdentifier(this.getIdentifier());

        Map<String, Object> sendMessage = new HashMap<>();
        sendMessage.put("data", taskHistories);
        sendMessage.put("command", "taskHistories");
        sendMessage.put("username", getUser().getUsername());
        sendMessage.put("identifier", this.getIdentifier());

        // 웹소켓을 통해 로그 실행 통지를 한다.
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        WebSocketUtil webSocketUtil = applicationContext.getBean(WebSocketUtil.class);
        String message = objectMapper.writeValueAsString(sendMessage);
        webSocketUtil.PushNotification(getUser().getWebsocketKey(), "/topic/workflow", message);
    }

    private void updateHistoryAsFailed(WorkflowHistory history) {
        history.setEndDate(new Timestamp(System.currentTimeMillis()));
        history.setStatus(State.FAILED);
        history.setElapsed(DateUtils.getDiff(history.getEndDate(), history.getStartDate()));
        workflowHistoryRepository.update(history);
    }

    public ProcessInstance getInstance() {
        return instance;
    }

    public ELEvaluator getEvaluator() {
        return evaluator;
    }

    public ELService getElService() {
        return elService;
    }

    public String getIdentifier() {
        return this.workflowHistory.getJobStringId();
    }

    public TaskHistory getTaskHistory() {
        return taskHistory;
    }

    public WorkflowHistory getWorkflowHistory() {
        return workflowHistory;
    }

    public EngineService getEngineService(String clusterName) {
        EngineConfig engineConfig = getEngineConfig(clusterName);
        return EngineLookupService.lookup(engineConfig);
    }

    public EngineConfig getEngineConfig(String clusterName) {
        return ConfigurationHolder.getEngine(clusterName);
    }


    public String encloseSpace(String value) {
        return count(value, " ") > 0 ? enclose(value) : value;
    }

    public String enclose(String value) {
        return "\"" + value + "\"";
    }

    public int count(String value, String sub) {
        return org.springframework.util.StringUtils.countOccurrencesOf(value, sub);
    }

    /**
     * 문자열에 포함된 <tt>${var}</tt> 형식의 expression을 해석한다.
     *
     * @param message 문자열
     * @return expression을 해석한 문자열
     */
    public String resolve(String message) {
        if (message == null) {
            return "";
        }

        try {
            String resolved = ELUtils.resolve(this.variable, message);
            return getEvaluator().evaluate(resolved, String.class);
        } catch (Exception ex) {
            return message;
        }
    }

    /**
     * UI의 스크립트 변수, UI의 워크플로우 변수, 프로세스 엔진의 태스크 변수, 프로세스 엔진의 글로벌 변수를 모두 결합하여 변수 목록을 구성한다.
     * 이 변수 목록은 expression을 해석할 떄 사용한다.
     *
     * @return 변수 목록
     */
    public Properties getMergedScriptVariables() throws Exception {
        Properties merged = new Properties();

        Map mergedParams = this.getMergedParams();
        if (mergedParams != null) {
            Set set = mergedParams.keySet();
            for (Object key : set) {
                Object value = mergedParams.get(key);
                merged.put(key, value);
            }
            merged.putAll(getScriptVariables());
        }

        Map vars = (Map) getInstance().get("iv");
        merged.putAll(vars);

        return merged;
    }

    /**
     * 스크립트 내에서 적용할 변수의 key와 value를 반환한다.
     *
     * @return 스크립트 변수
     */
    public Properties getScriptVariables() {
        if (getValues("variableNames") == null) {
            return new Properties();
        }
        String[] variableNames = getValues("variableNames");
        String[] variableValues = getValues("variableValues");

        Properties vars = new Properties();
        for (int i = 0; i < variableNames.length; i++) {
            vars.put(variableNames[i], variableValues[i]);
        }
        return vars;
    }
}
