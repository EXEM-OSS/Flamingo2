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

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.backend.UserEvent;
import org.opencloudengine.flamingo2.engine.designer.activiti.WorkflowRepository;
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
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 원격에서 호출가능한 Engine의 Scheduler 서비스.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class SchedulerRemoteServiceImpl implements SchedulerRemoteService {

    /**
     * Engine의 Scheduler 서비스
     */
    JobScheduler jobScheduler;

    @Override
    public List getRecentMetrics(String clusterName) {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);

        // FIXME : to MyBATIS
        String query = "select (@row:=@row+1) as num, jvmMaxMemory, jvmTotalMemory, jvmFreeMemory, jvmUsedMemory, total, running, DATE_FORMAT(reg_dt,'%Y-%m-%d %T') as time from FL_CL_ENGINE, (SELECT @row := 0) r WHERE system ='{}' AND reg_dt > DATE_ADD(now(), INTERVAL -6 HOUR) ORDER BY reg_dt asc";
        return jdbcTemplate.queryForList(MessageFormatter.format(query, clusterName).getMessage());
    }

    @Override
    public void runImmediatly(Map<String, Object> params) {
        UserEvent userEvent = (UserEvent) params.get("event");
        Workflow workflow = (Workflow) params.get("workflow");
        jobScheduler.startJobImmediatly(userEvent.getIdentifier(), workflow.getWorkflowId(), params);
    }

    @Override
    public boolean prepareRun(Map params) {
        UserEvent userEvent = (UserEvent) params.get("event");
        User user = (User) params.get("user");
        Workflow workflow = (Workflow) params.get("workflow");

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        WorkflowHistoryRepository workflowHistoryRepository = applicationContext.getBean(WorkflowHistoryRepository.class);
        WorkflowRepository workflowRepository = applicationContext.getBean(WorkflowRepository.class);
        TaskHistoryRepository taskHistoryRepository = applicationContext.getBean(TaskHistoryRepository.class);
        Transformer transformer = applicationContext.getBean(Transformer.class);

        String workflowId = workflow.getWorkflowId();
        String designerXml = workflow.getDesignerXml();
        List<WorkflowTask> taskList;
        try {
            taskList = transformer.getTaskList(designerXml, workflowId);
        } catch (Exception e) {
            throw new ServiceException("Unable to retreive tasks of workflow.", e);
        }

        workflowRepository.selectByTreeId(workflow.getWorkflowTreeId());

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
        history.setTotalStep(taskList.size());
        history.setCurrentStep(0);
        history.setStatus(State.PREPARING);
        history.setJobType("WORKFLOW");
        history.setLogPath("");

        try {
            workflowHistoryRepository.insert(history);
        } catch (Exception e) {
            throw new ServiceException("Unable to create a workflow's history", e);
        }

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
            throw new ServiceException("Unable to create a task's history", e);
        }

        return true;
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }
}
