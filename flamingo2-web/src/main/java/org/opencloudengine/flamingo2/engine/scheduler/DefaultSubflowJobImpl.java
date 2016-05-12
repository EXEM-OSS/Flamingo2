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
package org.opencloudengine.flamingo2.engine.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.backend.UserEvent;
import org.opencloudengine.flamingo2.engine.backend.UserEventRepository;
import org.opencloudengine.flamingo2.engine.designer.GlobalAttributes;
import org.opencloudengine.flamingo2.engine.designer.activiti.WorkflowService;
import org.opencloudengine.flamingo2.engine.designer.activiti.WorkflowTask;
import org.opencloudengine.flamingo2.engine.designer.activiti.task.Transformer;
import org.opencloudengine.flamingo2.engine.history.TaskHistory;
import org.opencloudengine.flamingo2.engine.history.TaskHistoryRepository;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRepository;
import org.opencloudengine.flamingo2.model.rest.State;
import org.opencloudengine.flamingo2.model.rest.User;
import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.uengine.kernel.*;
import org.uengine.processpublisher.BPMNUtil;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.opencloudengine.flamingo2.util.JsonUtils.unmarshal;
import static org.opencloudengine.flamingo2.util.StringUtils.unescape;

/**
 * Created by cloudine on 2015. 5. 11..
 */
@Service
public class DefaultSubflowJobImpl implements DefaultSubflowJob {

    /**
     * Jackson Json
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    WorkflowService workflowService;

    @Autowired
    GlobalAttributes globalAttributes;

    @Autowired
    private UserEventRepository eventRepository;

    @Autowired
    private WorkflowHistoryRepository workflowHistoryRepository;

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    @Autowired
    private Transformer transformer;

    @Override
    public void excute(Map params) {
        //서브 워크플로우 변수를 가져온다.
        Map subflowMergedParams = (Map) params.get("subflowMergedParams");

        // 워크플로우 실행시 필요한 정보를 가져온다.
        final UserEvent userEvent = (UserEvent) params.get("event");
        final User user = (User) params.get("user");

        Workflow workflow = getWorkflow(params);

        // 워크플로우의 실행 ID를 이용하여 메타데이터를 꺼낸다.
        String identifier = userEvent.getIdentifier();
        Map data = metadata(params, workflow, identifier, user, subflowMergedParams);

        final String name = workflow.getWorkflowName();
        String workflowXml = workflow.getWorkflowXml();

        // 사용자에게 워크플로우를 실행하고 있음을 알린다.
        eventRepository.insert(userEvent);
        data.put("event", userEvent);

        String workflowId = workflow.getWorkflowId();
        String designerXml = workflow.getDesignerXml();
        List<WorkflowTask> taskList;
        try {
            taskList = transformer.getTaskList(designerXml, workflowId);
        } catch (Exception e) {
            throw new ServiceException("Task Retreiving failed.", e);
        }

        // 워크플로우의 실행 이력을 불러온다.
        final WorkflowHistory workflowHistory = insertHistory(data, taskList.size());
        data.put("workflowHistory", workflowHistory);
        data.put("currentStep", new AtomicInteger());

        // 타스크의 실행 이력들을 스탠바이 상태로 인서트한다.
        try {
            for (int i = 0; i < taskList.size(); i++) {
                WorkflowTask workflowTask = taskList.get(i);
                TaskHistory taskHistory = new TaskHistory();
                taskHistory.setId(i);
                taskHistory.setIdentifier(userEvent.getIdentifier());
                taskHistory.setTaskId(workflowTask.getTaskId());
                taskHistory.setName(workflowTask.getTaskName());
                taskHistory.setStatus("STANDBY");
                taskHistory.setVars((String) workflowTask.getProperties().get("script"));
                taskHistoryRepository.insert(taskHistory);
            }
        } catch (Exception e) {
            throw new ServiceException("TaskHistory creation failed.", e);
        }

        try {
            ProcessInstance.USE_CLASS = DefaultProcessInstance.class;

            ByteArrayInputStream bis = new ByteArrayInputStream(workflowXml.getBytes());
            ProcessDefinition processDefinition = BPMNUtil.adapt(bis);
            processDefinition.afterDeserialization();

            processDefinition.setActivityFilters(new ActivityFilter[]{
                    new SensitiveActivityFilter() {
                        @Override
                        public void beforeExecute(Activity activity, ProcessInstance processInstance) throws Exception {

                        }

                        @Override
                        public void afterExecute(Activity activity, ProcessInstance processInstance) throws Exception {

                        }

                        @Override
                        public void afterComplete(Activity activity, ProcessInstance processInstance) throws Exception {

                        }

                        @Override
                        public void onPropertyChange(Activity activity, ProcessInstance processInstance, String s, Object o) throws Exception {

                        }

                        @Override
                        public void onDeploy(ProcessDefinition processDefinition) throws Exception {

                        }

                        @Override
                        public void onEvent(Activity activity, ProcessInstance processInstance, String s, Object o) throws Exception {
                            if (activity instanceof EndActivity && Activity.ACTIVITY_STOPPED.equals(s)) {
                                ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
                                UserEventRepository eventRepo = applicationContext.getBean(UserEventRepository.class);
                                userEvent.setName("Subworkflow" + name + "' is success");
                                userEvent.setStatus("FINISHED");
                                eventRepo.updateByIdentifier(userEvent);

                                updateHistoryAsFinished(workflowHistory);

                                // 웹소켓을 통해 워크플로우가 성공하였음을 알린다.
                                updateSocketWorkflowStatus(userEvent.getIdentifier(), user);
                            }
                        }
                    }
            });

            ProcessInstance instance = processDefinition.createInstance();

            instance.setInstanceId(identifier);
            Set<String> set = data.keySet();
            for (String key : set) {
                instance.set(key, data.get(key));
            }

            //Bpmn 엔진에서 파싱가능한 변수를 추가한다.
            instance.set("vars", data.get("variable"));

            //워크플로우 각 타스크 결과물들을 저장할 임시저장소를 등록한다.
            globalAttributes.registJobResultMap(instance);

            updateHistoryAsRunning(workflowHistory);
            instance.execute();

            //웹소켓을 통해 워크플로우가 실행되었음을 알린다.
            updateSocketWorkflowStatus(userEvent.getIdentifier(), user);

        } catch (ServiceException ex) {
            ex.printStackTrace();
            userEvent.setName("Subworklow '" + name + "' failed.");
            userEvent.setStatus(State.FAILED.toString());
            userEvent.setMessage(ex.getRecentLog());
            eventRepository.updateByIdentifier(userEvent);
            updateHistoryAsFailed(workflowHistory);

            //웹소켓을 통해 워크플로우가 실패하였음을 알린다.
            updateSocketWorkflowStatus(userEvent.getIdentifier(), user);
        } catch (Exception ex) {
            ex.printStackTrace();
            userEvent.setName("Subworklow '" + name + "' failed.");
            userEvent.setStatus(State.FAILED.toString());
            userEvent.setMessage(ex.toString());
            eventRepository.updateByIdentifier(userEvent);
            updateHistoryAsFailed(workflowHistory);

            //웹소켓을 통해 워크플로우가 실패하였음을 알린다.
            updateSocketWorkflowStatus(userEvent.getIdentifier(), user);
        }
    }

    public static String taskBasePath(String home, String processId, String jobId, Date current, User user) {
        return home + "/logs" + "/" + DateUtils.parseDate(current, "yyyy") + "/" + DateUtils.parseDate(current, "MM") + "/" + DateUtils.parseDate(current, "dd") + "/" + user.getUsername() + "/tasks/" + processId + "/" + jobId;
    }

    private WorkflowHistory insertHistory(Map params, int totalsize) {
        Workflow workflow = (Workflow) params.get("workflow");
        UserEvent userEvent = (UserEvent) params.get("event");
        User user = (User) params.get("user");

        WorkflowHistory history = new WorkflowHistory();
        history.setWorkflowName(workflow.getWorkflowName());
        history.setWorkflowId(workflow.getWorkflowId());
        history.setJobId(0); // FIXME jobId is uncertain
        history.setJobStringId(userEvent.getIdentifier());
        history.setJobName(workflow.getWorkflowName() + "_" + userEvent.getIdentifier());
        history.setWorkflowXml(workflow.getWorkflowXml());
        history.setVariable(workflow.getVariable());
        history.setStartDate(new Timestamp(System.currentTimeMillis()));
        history.setEndDate(new Timestamp(System.currentTimeMillis()));
        history.setUsername(user.getUsername());
        history.setElapsed(0);
        history.setTotalStep(totalsize);
        history.setCurrentStep(0);
        history.setStatus(State.PREPARING);
        history.setJobType("WORKFLOW");
        history.setLogPath((String) params.get("logdir"));
        history.setSf_parentIdentifier((String) params.get("sf_parentIdentifier"));
        history.setSf_rootIdentifier((String) params.get("sf_rootIdentifier"));
        history.setSf_depth((int) params.get("sf_depth"));
        history.setSf_taskId((String) params.get("sf_taskId"));

        workflowHistoryRepository.insert(history);
        return workflowHistoryRepository.selectByIdentifier(userEvent.getIdentifier());
    }

    private void updateHistoryAsRunning(WorkflowHistory history) {
        history.setEndDate(new Timestamp(System.currentTimeMillis()));
        history.setStatus(State.RUNNING);
        history.setElapsed(DateUtils.getDiff(history.getEndDate(), history.getStartDate()));
        workflowHistoryRepository.update(history);
    }

    private void updateHistoryAsFailed(WorkflowHistory history) {
        history.setEndDate(new Timestamp(System.currentTimeMillis()));
        history.setStatus(State.FAILED);
        history.setElapsed(DateUtils.getDiff(history.getEndDate(), history.getStartDate()));
        workflowHistoryRepository.update(history);
    }

    private void updateHistoryAsFinished(WorkflowHistory history) {
        history.setEndDate(new Timestamp(System.currentTimeMillis()));
        history.setStatus(State.SUCCEEDED);
        history.setElapsed(DateUtils.getDiff(history.getEndDate(), history.getStartDate()));
        workflowHistoryRepository.update(history);
    }

    private Map metadata(Map data, Workflow workflow, String identifier, User user, Map subflowMergedParams) { // FIXME 필요한 데이터에 대해서 데이터 구조에 맞게 정리가 필요
        Map<String, Object> metadata = new HashMap<>();
        metadata.putAll(data);
        Date date = new Date();
        metadata.put("user", user);
        metadata.put("username", user.getUsername());
        metadata.put("uid", user.getId());
        metadata.put("name", user.getName());
        metadata.put("email", user.getEmail());
        metadata.put("processId", workflow.getWorkflowId());
        metadata.put("workflow", workflow);
        metadata.put("variable", getWorkflowVariable(workflow, subflowMergedParams));
        metadata.put("identifier", identifier);
        metadata.put("startDate", date);
        metadata.put("steps", 0); // FIXME steps 값에 대한 정리 필요
        metadata.put("current", 0);
        metadata.put("yyyyMMdd", DateUtils.getCurrentYyyymmdd());
        metadata.put("yyyy", DateUtils.parseDate(date, "yyyy"));
        metadata.put("MM", DateUtils.parseDate(date, "mm"));
        metadata.put("dd", DateUtils.parseDate(date, "dd"));
        metadata.put("HH", DateUtils.parseDate(date, "HH"));
        metadata.put("mm", DateUtils.parseDate(date, "mm"));
        metadata.put("ss", DateUtils.parseDate(date, "ss"));
        metadata.put("logdir", taskBasePath(ConfigurationHelper.getHelper().get("flamingo.workflow.logging.dir"), workflow.getWorkflowId(), identifier, date, user));

        metadata.put("sf_parentIdentifier", data.get("sf_parentIdentifier"));
        metadata.put("sf_rootIdentifier", data.get("sf_rootIdentifier"));
        metadata.put("sf_depth", data.get("sf_depth"));
        metadata.put("sf_taskId", data.get("sf_taskId"));

        return metadata;
    }

    private Workflow getWorkflow(Map data) {
        Long treeId = Long.parseLong((String) data.get("treeId"));
        return workflowService.getByTreeId(treeId);
    }

    private Map getWorkflowVariable(Workflow workflow, Map subflowMergedParams) {
        try {
            Map vars = unmarshal(unescape(workflow.getVariable()));
            vars.put("global", subflowMergedParams);

            return vars;
        } catch (Exception ex) {
            throw new ServiceException("Failed parsing variables of subworkflow ", ex);
        }
    }

    public void updateSocketWorkflowStatus(String identifier, User user) {
        try {
            // 웹소켓을 통해 로그 실행 통지를 한다.
            ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
            WebSocketUtil webSocketUtil = applicationContext.getBean(WebSocketUtil.class);
            WorkflowHistoryRepository historyRepository = applicationContext.getBean(WorkflowHistoryRepository.class);

            WorkflowHistory workflowHistory = historyRepository.selectByIdentifier(identifier);
            DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            workflowHistory.setStartDateSimple(sdFormat.format(workflowHistory.getStartDate()));
            workflowHistory.setEndDateSimple(sdFormat.format(workflowHistory.getEndDate()));
            Map<String, Object> sendMessage = new HashMap<>();
            sendMessage.put("data", workflowHistory);
            sendMessage.put("command", "workflowHistory");
            sendMessage.put("username", user.getUsername());
            sendMessage.put("identifier", identifier);
            sendMessage.put("duration", DateUtils.getDiff(workflowHistory.getEndDate(), workflowHistory.getStartDate()));
            sendMessage.put("endDate", workflowHistory.getEndDate());

            String message = objectMapper.writeValueAsString(sendMessage);
            webSocketUtil.PushNotification(user.getWebsocketKey(), "/topic/workflow", message);
        } catch (Exception e) {
            //웹소켓 전송에 실패해도 따로 대응하지 않는다.
        }
    }
}
