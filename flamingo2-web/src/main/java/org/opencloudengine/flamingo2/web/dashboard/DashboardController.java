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
package org.opencloudengine.flamingo2.web.dashboard;

import org.apache.commons.io.FileUtils;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.hadoop.ResourceManagerRemoteService;
import org.opencloudengine.flamingo2.engine.history.TaskHistory;
import org.opencloudengine.flamingo2.engine.history.TaskHistoryRemoteService;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.model.rest.State;
import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard REST Controller
 *
 * @author Myeong Ha, Kim
 * @author Seung Pil, Park
 * @author Jae Hee, Lee
 * @author Byoung Gon, Kim
 * @since 2.0
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends DefaultController {

    /**
     * Workflow Monitoring History 목록을 조회한다.
     *
     * @param clusterName  클러스터명
     * @param startDate    시작 날짜
     * @param endDate      마지막 날짜
     * @param status       워크플로우 작업 상태
     * @param workflowName 워크플로우명
     * @param start        시작 페이지
     * @param limit        조회 제한 개수
     * @param node         히스토리 목록이 속한 상위 노드 정보
     * @return Workflow History List
     */
    @RequestMapping(value = "/workflows", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response workflows(@RequestParam(defaultValue = "") String clusterName,
                              @RequestParam(defaultValue = "") String startDate,
                              @RequestParam(defaultValue = "") String endDate,
                              @RequestParam(defaultValue = "") String status,
                              @RequestParam(defaultValue = "") String workflowName,
                              @RequestParam(defaultValue = "0") int start,
                              @RequestParam(defaultValue = "16") int limit,
                              @RequestParam(defaultValue = "") String node) {
        EngineService engineService = getEngineService(clusterName);

        WorkflowHistoryRemoteService workflowHistoryRemoteService = engineService.getWorkflowHistoryRemoteService();

        String username = SessionUtils.getLevel() == 1 ? "" : SessionUtils.getUsername();

        List<Map> histories;
        int total;

        try {
            histories = new ArrayList<>();
            List<WorkflowHistory> workflowHistories = workflowHistoryRemoteService.selectByCondition(startDate, endDate, start, limit, username, workflowName, status, "");
            for (WorkflowHistory workflowHistory : workflowHistories) {
                Map map = getNodeForWorkflow(workflowHistory, node);
                histories.add(map);
            }
        } catch (Exception ex) {
            throw new ServiceException("Unable to get workflows.", ex);
        }

        try {
            total = workflowHistoryRemoteService.selectTotalCountByUsername(startDate, endDate, start, limit, username, workflowName, status, "");
        } catch (Exception ex) {
            throw new ServiceException("Unable to get workflow count.", ex);
        }

        Response response = new Response();
        response.setTotal(total);
        response.setLimit(histories.size());
        response.getList().addAll(histories);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/task/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response tasks(@RequestParam(defaultValue = "") String clusterName,
                          @RequestParam(defaultValue = "") String identifier) {
        EngineService engineService = getEngineService(clusterName);

        List<TaskHistory> taskHistories;

        try {
            taskHistories = engineService.getTaskHistoryRemoteService().selectByIdentifier(identifier);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get workflow count.", ex);
        }

        Response response = new Response();
        response.setLimit(taskHistories.size());
        response.getList().addAll(taskHistories);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/task/get", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response task(@RequestParam(defaultValue = "") String clusterName,
                         @RequestParam(defaultValue = "") String identifier,
                         @RequestParam(defaultValue = "") String taskId) {
        EngineService engineService = getEngineService(clusterName);

        TaskHistory taskHistory;

        try {
            TaskHistory history = new TaskHistory();
            history.setIdentifier(identifier);
            history.setTaskId(taskId);

            TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
            taskHistory = taskHistoryRemoteService.selectByTaskIdAndIdentifier(history);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get workflow count.", ex);
        }

        Response response = new Response();
        response.setObject(taskHistory);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/task/log", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response taskLog(@RequestParam(defaultValue = "") String clusterName,
                            @RequestParam(defaultValue = "") Long id) {
        EngineService engineService = getEngineService(clusterName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            TaskHistoryRemoteService taskHistoryService = engineService.getTaskHistoryRemoteService();
            TaskHistory taskHistories = taskHistoryService.select(id);

            File file = new File(taskHistories.getLogDirectory() + "/task.log");
            File errFile = new File(taskHistories.getLogDirectory() + "/err.log");

            if (file.exists() && file.length() == 0 && errFile.exists()) {
                file = errFile;
            }

            FileUtils.copyFile(new File(file.getPath()), baos);
        } catch (Exception ex) {
            throw new ServiceException("Unable to read logs.", ex);
        }

        Response response = new Response();
        response.getMap().put("text", new String(baos.toByteArray()));
        response.setSuccess(true);
        return response;
    }

    private WorkflowHistory getSubflow(TaskHistory taskHistory, List<WorkflowHistory> workflowHistories) {
        String taskId = taskHistory.getTaskId();
        for (WorkflowHistory workflowHistory : workflowHistories) {
            if (workflowHistory != null && workflowHistory.getSf_taskId().equals(taskId))
                return workflowHistory;
        }
        return null;
    }

    private Map getNodeForWorkflow(WorkflowHistory workflowHistory, String node) {
        Map<String, Object> map = new HashMap<>();
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        map.put("id", node + "/" + workflowHistory.getJobStringId());
        map.put("rowid", workflowHistory.getId());
        map.put("workflowId", workflowHistory.getWorkflowId());
        map.put("jobId", workflowHistory.getJobId());
        map.put("jobStringId", workflowHistory.getJobStringId());
        map.put("workflowName", workflowHistory.getWorkflowName());
        map.put("workflowXml", workflowHistory.getWorkflowXml());
        map.put("currentAction", workflowHistory.getCurrentAction());
        map.put("jobName", workflowHistory.getJobName());
        map.put("variable", workflowHistory.getVariable());
        map.put("startDate", sdFormat.format(workflowHistory.getStartDate()));
        map.put("endDate", sdFormat.format(workflowHistory.getEndDate()));
        map.put("elapsed", workflowHistory.getElapsed());
        map.put("cause", workflowHistory.getCause());
        map.put("currentStep", workflowHistory.getCurrentStep());
        map.put("totalStep", workflowHistory.getTotalStep());
        map.put("exception", workflowHistory.getException());
        map.put("status", workflowHistory.getStatus());
        map.put("username", workflowHistory.getUsername());
        map.put("jobType", workflowHistory.getJobType());
        map.put("logPath", workflowHistory.getLogPath());
        map.put("sf_parentIdentifier", workflowHistory.getSf_parentIdentifier());
        map.put("sf_rootIdentifier", workflowHistory.getSf_rootIdentifier());
        map.put("sf_depth", workflowHistory.getSf_depth());
        map.put("sf_taskId", workflowHistory.getSf_taskId());

        map.put("cls", "");
        map.put("iconCls", "x-tree-noicon");
        map.put("text", workflowHistory.getWorkflowName());
        map.put("leaf", false);
        map.put("type", "workflow");
        return map;
    }

    private Map getNodeForTask(TaskHistory taskHistory, String node) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", node + "/" + taskHistory.getTaskId());
        map.put("rowid", taskHistory.getId());
        map.put("taskId", taskHistory.getTaskId());
        map.put("identifier", taskHistory.getIdentifier());
        map.put("status", taskHistory.getStatus());

        map.put("cls", "");
        map.put("iconCls", "x-tree-noicon");
        map.put("text", taskHistory.getName());
        map.put("leaf", true);
        map.put("type", "task");
        return map;
    }

    /**
     * 지정한 조건의 워크플로우 실행 이력을 조회한다.
     *
     * @return 워크플로우 실행 이력 목록
     */
    @RequestMapping(value = "actions", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response actions(@RequestParam(defaultValue = "") String clusterName,
                            @RequestParam(defaultValue = "") String identifier) {
        EngineService engineService = getEngineService(clusterName);

        List<TaskHistory> taskHistories;

        try {
            TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
            taskHistories = taskHistoryRemoteService.selectByIdentifier(identifier);
        } catch (Exception ex) {
            throw new ServiceException("Unable to load task history.", ex);
        }

        Response response = new Response();
        response.getList().addAll(taskHistories);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "logs", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response logs(@RequestParam(defaultValue = "") String clusterName,
                         @RequestParam(defaultValue = "") String identifier,
                         @RequestParam(defaultValue = "") String taskId,
                         @RequestParam(defaultValue = "") String tabConditionKey) {
        EngineService engineService = getEngineService(clusterName);

        Map<String, Object> map = new HashMap<>();

        try {
            TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();

            switch (tabConditionKey) {
                case "log":
                    map.put("log", taskHistoryRemoteService.getTaskLog(identifier, taskId));
                    break;
                case "command":
                    map.put("command", taskHistoryRemoteService.getCommand(identifier, taskId));
                    break;
                case "script":
                    map.put("script", taskHistoryRemoteService.getScript(identifier, taskId));
                    break;
                case "error":
                    map.put("error", taskHistoryRemoteService.getError(identifier, taskId));
                    break;
                default:
                    throw new ServiceException("Unable to receive tab number.");
            }
        } catch (Exception ex) {
            throw new ServiceException("Unable to read log.", ex);
        }

        Response response = new Response();
        response.getMap().putAll(map);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "kill", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response kill(@RequestParam(defaultValue = "") String clusterName,
                         @RequestParam(defaultValue = "") String identifier,
                         @RequestParam(defaultValue = "") String taskId,
                         @RequestParam(defaultValue = "") String type) {
        EngineConfig engineConfig = getEngineConfig(clusterName);
        EngineService engineService = getEngineService(clusterName);

        TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
        WorkflowHistoryRemoteService workflowHistoryRemoteService = engineService.getWorkflowHistoryRemoteService();

        if ("task".equals(type)) {
            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setIdentifier(identifier);
            taskHistory.setTaskId(taskId);

            taskHistory = taskHistoryRemoteService.selectByTaskIdAndIdentifier(taskHistory);

            if ("RUNNING".equals(taskHistory.getStatus())) {
                String[] idList = engineService.getDesignerRemoteService().idList(taskHistory.getLogDirectory(), "app.");
                if (idList != null && idList.length > 0) {
                    for (String file : idList) {
                        if (file.startsWith("app.")) {
                            ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
                            service.killApplication(StringUtils.removePrefix(file, "app.", true), engineConfig);
                            taskHistory.setStatus(State.KILLED.toString());
                            taskHistoryRemoteService.updateByTaskIdAndIdentifier(taskHistory);
                        }
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new ServiceException("Unable to wait for process kill.", e);
                }

                engineService.getDesignerRemoteService().killProccess(taskHistory.getLogDirectory());
                taskHistory.setStatus(State.KILLED.toString());
                taskHistoryRemoteService.updateByTaskIdAndIdentifier(taskHistory);
            }

        } else {
            List<TaskHistory> taskHistoryList = taskHistoryRemoteService.selectByIdentifier(identifier);
            for (TaskHistory history : taskHistoryList) {
                if ("RUNNING".equals(history.getStatus())) {
                    String[] idList = engineService.getDesignerRemoteService().idList(history.getLogDirectory(), "app.");
                    if (idList != null && idList.length > 0) {
                        for (String file : idList) {
                            if (file.startsWith("app.")) {
                                ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
                                service.killApplication(StringUtils.removePrefix(file, "app.", true), engineConfig);
                                history.setStatus(State.KILLED.toString());
                                taskHistoryRemoteService.updateByTaskIdAndIdentifier(history);
                            }
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new ServiceException("Unable to wait for process kill.", e);
                    }

                    engineService.getDesignerRemoteService().killProccess(history.getLogDirectory());
                    history.setStatus(State.KILLED.toString());
                    taskHistoryRemoteService.updateByTaskIdAndIdentifier(history);

                    WorkflowHistory workflowHistory = workflowHistoryRemoteService.selectByIdentifier(identifier);
                    workflowHistory.setStatus(State.KILLED);
                    workflowHistoryRemoteService.updateStatus(workflowHistory);
                }
            }
        }

        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "yarnId", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response yarnId(@RequestParam(defaultValue = "") String clusterName,
                           @RequestParam(defaultValue = "") String identifier,
                           @RequestParam(defaultValue = "") String taskId,
                           @RequestParam(defaultValue = "") String type) {
        EngineService engineService = getEngineService(clusterName);

        TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();

        List<Map> idList = new ArrayList<>();

        try {
            if ("task".equals(type)) {
                TaskHistory taskHistory = new TaskHistory();
                taskHistory.setIdentifier(identifier);
                taskHistory.setTaskId(taskId);

                taskHistory = taskHistoryRemoteService.selectByTaskIdAndIdentifier(taskHistory);
                String[] ids = engineService.getDesignerRemoteService().idList(taskHistory.getLogDirectory(), "app.");

                if (ids != null && ids.length > 0) {
                    for (String file : ids) {
                        if (file.startsWith("app.")) {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", StringUtils.removePrefix(file, "app.", true));
                            idList.add(map);
                        }
                    }
                }
            } else {
                List<TaskHistory> taskHistoryList = taskHistoryRemoteService.selectByIdentifier(identifier);
                for (TaskHistory history : taskHistoryList) {
                    String[] ids = engineService.getDesignerRemoteService().idList(history.getLogDirectory(), "app.");
                    if (ids != null && ids.length > 0) {
                        for (String file : ids) {
                            if (file.startsWith("app.")) {
                                Map<String, String> map = new HashMap<>();
                                map.put("id", StringUtils.removePrefix(file, "app.", true));
                                idList.add(map);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new ServiceException("Unable to get yarnId.", ex);
        }

        Response response = new Response();
        response.getList().addAll(idList);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "mrId", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response mrId(@RequestParam(defaultValue = "") String clusterName,
                         @RequestParam(defaultValue = "") String identifier,
                         @RequestParam(defaultValue = "") String taskId,
                         @RequestParam(defaultValue = "") String type) {
        EngineService engineService = getEngineService(clusterName);

        TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();

        List<Map> idList = new ArrayList<>();

        try {
            if ("task".equals(type)) {
                TaskHistory taskHistory = new TaskHistory();
                taskHistory.setIdentifier(identifier);
                taskHistory.setTaskId(taskId);

                taskHistory = taskHistoryRemoteService.selectByTaskIdAndIdentifier(taskHistory);
                String[] ids = engineService.getDesignerRemoteService().idList(taskHistory.getLogDirectory(), "hadoop.");

                if (ids != null && ids.length > 0) {
                    for (String file : ids) {
                        if (file.startsWith("hadoop.")) {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", StringUtils.removePrefix(file, "hadoop.", true));
                            idList.add(map);
                        }
                    }
                }
            } else {
                List<TaskHistory> taskHistoryList = taskHistoryRemoteService.selectByIdentifier(identifier);
                for (TaskHistory history : taskHistoryList) {
                    String[] ids = engineService.getDesignerRemoteService().idList(history.getLogDirectory(), "hadoop.");
                    if (ids != null && ids.length > 0) {
                        for (String file : ids) {
                            if (file.startsWith("hadoop.")) {
                                Map<String, String> map = new HashMap<>();
                                map.put("id", StringUtils.removePrefix(file, "hadoop.", true));
                                idList.add(map);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new ServiceException("Unable to get mrId.", ex);
        }

        Response response = new Response();
        response.getList().addAll(idList);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "timeseries", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response timeseries(@RequestParam String clusterName,
                               @RequestParam String status) {
        Response response = new Response();
        response.setSuccess(true);
        int level = SessionUtils.getLevel();

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        String query;
        if (level != 1) { // 일반 사용자의 경우 자기것만 보여줘야 함
            switch (status) {
                case "ALL":
                    query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE USERNAME = '" + SessionUtils.getUsername() + "' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
                    break;
                case "SUCCESS":
                    query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, 'SUCCESS' as type, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE USERNAME = '" + SessionUtils.getUsername() + "' AND STATUS = 'SUCCESS' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
                    break;
                default:
                    query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, 'FAILED' as type, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE USERNAME = '" + SessionUtils.getUsername() + "' AND STATUS <> 'SUCCESS' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
                    break;
            }
        } else {
            switch (status) {
                case "ALL":
                    query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
                    break;
                case "SUCCESS":
                    query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, 'SUCCESS' as type, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE STATUS = 'SUCCESS' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
                    break;
                default:
                    query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, 'FAILED' as type, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE AND STATUS <> 'SUCCESS' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
                    break;
            }
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(MessageFormatter.format(query, clusterName).getMessage());
        response.getList().addAll(list);
        return response;
    }
}
