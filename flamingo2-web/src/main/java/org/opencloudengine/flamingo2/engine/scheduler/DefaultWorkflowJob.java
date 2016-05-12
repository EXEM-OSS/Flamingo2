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
import org.apache.commons.lang.math.RandomUtils;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.backend.UserEvent;
import org.opencloudengine.flamingo2.engine.backend.UserEventRepository;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRepository;
import org.opencloudengine.flamingo2.model.rest.State;
import org.opencloudengine.flamingo2.model.rest.User;
import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.opencloudengine.flamingo2.util.*;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.uengine.kernel.*;
import org.uengine.processpublisher.BPMNUtil;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.opencloudengine.flamingo2.util.JsonUtils.unmarshal;
import static org.opencloudengine.flamingo2.util.StringUtils.unescape;

public class DefaultWorkflowJob extends DefaultQuartzJob {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(DefaultWorkflowJob.class);

    /**
     * Jackson Json
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void executeInternal() {
        JobExecutionContext context = this.jobExecutionContext;

        // 워크플로우 실행시 필요한 정보를 가져온다.
        final UserEvent userEvent = (UserEvent) context.getMergedJobDataMap().get("event");
        final User user = (User) context.getMergedJobDataMap().get("user");
        Workflow workflow = (Workflow) context.getMergedJobDataMap().get("workflow");

        // 워크플로우의 실행 ID를 이용하여 메타데이터를 꺼낸다.
        String identifier = userEvent.getIdentifier();
        Map<String, Object> data = metadata(context.getMergedJobDataMap(), workflow, identifier, user);

        final String name = workflow.getWorkflowName();
        String workflowXml = workflow.getWorkflowXml();

        // 사용자에게 워크플로우를 실행하고 있음을 알린다.
        eventRepository.insert(userEvent);
        data.put("event", userEvent);

        // 워크플로우의 실행 이력을 불러온다.
        final WorkflowHistory workflowHistory = workflowHistoryRepository.selectByIdentifier(userEvent.getIdentifier());
        data.put("workflowHistory", workflowHistory);
        data.put("currentStep", new AtomicInteger());

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
                                userEvent.setName("Workflow '" + name + "' is successful");
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
            // BPMN 엔진에서 파싱가능한 변수를 추가한다.
            instance.set("vars", data.get("variable"));

            // 워크플로우 각 타스크 결과물들을 저장할 임시저장소를 등록한다.
            globalAttributes.registJobResultMap(instance);

            updateHistoryAsRunning(workflowHistory);
            instance.execute();

            // 웹소켓을 통해 워크플로우가 실행되었음을 알린다.
            updateSocketWorkflowStatus(userEvent.getIdentifier(), user);

        } catch (ServiceException ex) {
            ex.printStackTrace();
            userEvent.setName("Workflow '" + name + "' is failed.");
            userEvent.setStatus("FAILED");
            userEvent.setMessage(ex.getRecentLog());
            eventRepository.updateByIdentifier(userEvent);
            updateHistoryAsFailed(workflowHistory, ex);

            // 웹소켓을 통해 워크플로우가 실패하였음을 알린다.
            updateSocketWorkflowStatus(userEvent.getIdentifier(), user);
        } catch (Exception ex) {
            ex.printStackTrace();
            userEvent.setName("Workflow '" + name + "' is failed.");
            userEvent.setStatus("FAILED");
            userEvent.setMessage(ex.toString());
            eventRepository.updateByIdentifier(userEvent);
            updateHistoryAsFailed(workflowHistory, ex);

            // 웹소켓을 통해 워크플로우가 실패하였음을 알린다.
            updateSocketWorkflowStatus(userEvent.getIdentifier(), user);
        }
    }

    public static String taskBasePath(String logDir, String processId, String jobId, Date current, User user) {
        return logDir + "/" + DateUtils.parseDate(current, "yyyy") + "/" + DateUtils.parseDate(current, "MM") + "/" + DateUtils.parseDate(current, "dd") + "/" + user.getUsername() + "/tasks/" + processId + "/" + jobId;
    }

    private void updateHistoryAsRunning(WorkflowHistory history) {
        history.setEndDate(new Timestamp(System.currentTimeMillis()));
        history.setStatus(State.RUNNING);
        history.setElapsed(DateUtils.getDiff(history.getEndDate(), history.getStartDate()));
        workflowHistoryRepository.update(history);
    }

    private void updateHistoryAsFailed(WorkflowHistory history, Exception ex) {
        history.setEndDate(new Timestamp(System.currentTimeMillis()));
        history.setStatus(State.FAILED);
        history.setElapsed(DateUtils.getDiff(history.getEndDate(), history.getStartDate()));
        history.setException(ExceptionUtils.getFullStackTrace(ex));
        workflowHistoryRepository.update(history);
    }

    private void updateHistoryAsFinished(WorkflowHistory history) {
        history.setEndDate(new Timestamp(System.currentTimeMillis()));
        history.setStatus(State.SUCCEEDED);
        history.setElapsed(DateUtils.getDiff(history.getEndDate(), history.getStartDate()));
        workflowHistoryRepository.update(history);
    }

    private Map<String, Object> metadata(Map<String, Object> data, Workflow workflow, String identifier, User user) { // FIXME 필요한 데이터에 대해서 데이터 구조에 맞게 정리가 필요
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
        metadata.put("variable", getWorkflowVariable(workflow));
        metadata.put("identifier", identifier);
        metadata.put("startDate", date);
        metadata.put("steps", 0); // FIXME steps 값에 대한 정리 필요
        metadata.put("current", 0);

        String logdir = taskBasePath(ConfigurationHelper.getHelper().get("flamingo.workflow.logging.dir"), workflow.getWorkflowId(), identifier, date, user);
        metadata.put("logdir", logdir);

        Map vars = new HashMap();
        vars.put("user", user);
        vars.put("username", user.getUsername());
        vars.put("uid", user.getId());
        vars.put("name", user.getName());
        vars.put("email", user.getEmail());
        vars.put("processId", workflow.getWorkflowId());
        vars.put("identifier", identifier);
        vars.put("username", user.getUsername());
        vars.put("YYYYMMDD", DateUtils.getCurrentYyyymmdd());
        vars.put("YYYY", DateUtils.parseDate(date, "yyyy"));
        vars.put("MM", DateUtils.parseDate(date, "mm"));
        vars.put("DD", DateUtils.parseDate(date, "dd"));
        vars.put("hh", DateUtils.parseDate(date, "HH"));
        vars.put("mm", DateUtils.parseDate(date, "mm"));
        vars.put("ss", DateUtils.parseDate(date, "ss"));
        vars.put("RANDOM", RandomUtils.nextLong());
        vars.put("UUID", UUIDUtils.generateUUID());
        vars.put("logdir", logdir);

        metadata.put("iv", vars);
        metadata.put("sf_rootIdentifier", identifier);
        metadata.put("sf_depth", 0);

        if (logger.isDebugEnabled()) {
            Set<String> strings = metadata.keySet();
            for (String key : strings) {
                logger.debug("[Workflow Metadata] {}={}", key, metadata.get(key));
            }
        }

        return metadata;
    }

    private Object getWorkflowVariable(Workflow workflow) {
        try {
            if (!StringUtils.isEmpty(workflow.getVariable())) {
                return unmarshal(unescape(workflow.getVariable()));
            }
            return "";
        } catch (Exception ex) {
            throw new ServiceException("Unable to parse the workflow variables.", ex);
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

            Map sendMessage = new HashMap();
            sendMessage.put("data", workflowHistory);
            sendMessage.put("command", "workflowHistory");
            sendMessage.put("username", user.getUsername());
            sendMessage.put("identifier", identifier);

            String message = objectMapper.writeValueAsString(sendMessage);
            webSocketUtil.PushNotification(user.getWebsocketKey(), "/topic/workflow", message);
        } catch (Exception e) {
            // 웹소켓 전송에 실패해도 따로 대응하지 않는다.
        }
    }
}
