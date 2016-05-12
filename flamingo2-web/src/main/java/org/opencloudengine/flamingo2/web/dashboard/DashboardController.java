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
import org.opencloudengine.flamingo2.util.ExceptionUtils;
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
     * @param jobType      워크플로우 작업 타입
     * @param page         페이지
     * @param start        시작 페이지
     * @param limit        조회 제한 개수
     * @param node         히스토리 목록이 속한 상위 노드 정보
     * @return Workflow History List
     */
    @RequestMapping(value = "/workflows", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getWorkflows(@RequestParam(defaultValue = "") String clusterName,
                                 @RequestParam(defaultValue = "") String startDate,
                                 @RequestParam(defaultValue = "") String endDate,
                                 @RequestParam(defaultValue = "") String status,
                                 @RequestParam(defaultValue = "") String workflowName,
                                 @RequestParam(defaultValue = "") String jobType,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "0") int start,
                                 @RequestParam(defaultValue = "16") int limit,
                                 @RequestParam(defaultValue = "") String node) {

        Response response = new Response();
        EngineService engineService = getEngineService(clusterName);
        WorkflowHistoryRemoteService workflowHistoryRemoteService = engineService.getWorkflowHistoryRemoteService();
        int level = SessionUtils.getLevel();
        String username = level == 1 ? "" : SessionUtils.getUsername();

        ArrayList<Map> arrayList = new ArrayList<>();

        List<WorkflowHistory> workflowHistories = workflowHistoryRemoteService.selectByCondition(startDate, endDate, start, limit, username, workflowName, status, "");
        for (WorkflowHistory workflowHistory : workflowHistories) {
            Map map = getNodeForWorkflow(workflowHistory, node);
            arrayList.add(map);
        }
        int total = workflowHistoryRemoteService.selectTotalCountByUsername(startDate, endDate, start, limit, username, workflowName, status, "");
        response.setTotal(total);

        response.setLimit(arrayList.size());
        response.getList().addAll(arrayList);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/task/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getTasksOfJob(@RequestParam(defaultValue = "") String clusterName,
                                  @RequestParam(defaultValue = "") String sort,
                                  @RequestParam(defaultValue = "DESC") String dir,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "0") int start,
                                  @RequestParam(defaultValue = "16") int limit,
                                  @RequestParam(defaultValue = "") String orderby,
                                  @RequestParam(defaultValue = "") String identifier) {

        EngineService engineService = getEngineService(clusterName);

        Response response = new Response();
        List<TaskHistory> taskHistories = engineService.getTaskHistoryRemoteService().selectByIdentifier(identifier);

        response.setLimit(taskHistories.size());
        response.getList().addAll(taskHistories);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/task/get", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getTask(@RequestParam(defaultValue = "") String clusterName,
                            @RequestParam(defaultValue = "") String identifier,
                            @RequestParam(defaultValue = "") String taskId) {
        Response response = new Response();
        EngineService engineService = getEngineService(clusterName);
        TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
        TaskHistory history = new TaskHistory();
        history.setIdentifier(identifier);
        history.setTaskId(taskId);
        TaskHistory taskHistory = taskHistoryRemoteService.selectByTaskIdAndIdentifier(history);
        response.setObject(taskHistory);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/task/log", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getTaskLog(@RequestParam(defaultValue = "") String clusterName, @RequestParam(defaultValue = "") Long id) {
        EngineService engineService = getEngineService(clusterName);

        Response response = new Response();
        try {
            TaskHistory taskHistories = engineService.getTaskHistoryRemoteService().select(id);
            String filename = null;
            String task = taskHistories.getLogDirectory() + "/task.log";
            if (new File(task).exists() && new File(task).length() == 0) {
                String err = taskHistories.getLogDirectory() + "/err.log";
                if (new File(err).exists()) {
                    filename = err;
                }
            } else {
                filename = task;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtils.copyFile(new File(filename), baos);
            response.getMap().put("text", new String(baos.toByteArray()));
            response.setSuccess(true);
        } catch (Exception ex) {
            // FIXME 여기 WholeBodyException을 수정해야하지 않을까??
            response.setSuccess(false);
            response.getError().setMessage("Unable to load a log file.");
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
        }
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
     * @param status 상태코드
     * @param sort   정렬할 컬럼명
     * @param dir    정렬 방식(ASC, DESC)
     * @param start  시작 페이지
     * @param limit  페이지당 건수
     * @return 워크플로우 실행 이력 목록
     */
    @RequestMapping(value = "actions", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getActions(@RequestParam(defaultValue = "") String clusterName,
                               @RequestParam(defaultValue = "") String identifier,
                               @RequestParam(defaultValue = "ALL") String status,
                               @RequestParam(defaultValue = "ID") String sort,
                               @RequestParam(defaultValue = "DESC") String dir,
                               @RequestParam(defaultValue = "0") int start,
                               @RequestParam(defaultValue = "16") int limit) {

        Response response = new Response();
        EngineService engineService = getEngineService(clusterName);
        List<TaskHistory> taskHistories = engineService.getTaskHistoryRemoteService().selectByIdentifier(identifier);
        response.getList().addAll(taskHistories);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "logs", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getLogs(@RequestParam(defaultValue = "") String clusterName,
                            @RequestParam(defaultValue = "") String identifier,
                            @RequestParam(defaultValue = "") String taskId,
                            @RequestParam(defaultValue = "") String tabConditionKey) {
        Response response = new Response();
        EngineService engineService = getEngineService(clusterName);
        TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();

        String log;
        String script;
        String command;
        String error;
        Map<String, Object> map = new HashMap<>();

        switch (tabConditionKey) {
            case "log":
                log = taskHistoryRemoteService.getTaskLog(identifier, taskId);
                map.put("log", log);
                break;
            case "script":
                script = taskHistoryRemoteService.getScript(identifier, taskId);
                map.put("script", script);
                break;
            case "command":
                command = taskHistoryRemoteService.getCommand(identifier, taskId);
                map.put("command", command);
                break;
            case "error":
                error = taskHistoryRemoteService.getError(identifier, taskId);
                map.put("error", error);
                break;
        }

        response.getMap().putAll(map);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "log", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getLog(@RequestParam(defaultValue = "") String clusterName, @RequestParam(defaultValue = "") String identifier, @RequestParam(defaultValue = "") String taskId) {
        Response response = new Response();
        EngineService engineService = getEngineService(clusterName);
        TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
        String log = taskHistoryRemoteService.getTaskLog(identifier, taskId);
        response.setObject(log);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "script", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getScript(@RequestParam(defaultValue = "") String clusterName, @RequestParam(defaultValue = "") String identifier, @RequestParam(defaultValue = "") String taskId) {
        Response response = new Response();
        EngineService engineService = getEngineService(clusterName);
        TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
        String script = taskHistoryRemoteService.getScript(identifier, taskId);
        response.setObject(script);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "kill", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response jobKill(@RequestParam(defaultValue = "") String clusterName, @RequestParam(defaultValue = "") String identifier, @RequestParam(defaultValue = "") String type) {
        Response response = new Response();
        EngineConfig engineConfig = getEngineConfig(clusterName);
        EngineService engineService = getEngineService(clusterName);

        // FIXME type이 workflow일 경우 처리
        if ("task".equals(type)) {
            TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
            List<TaskHistory> taskHistory = taskHistoryRemoteService.selectByIdentifier(identifier);
            String[] idList = engineService.getDesignerRemoteService().idList(taskHistory.get(0).getLogDirectory(), "app.");

            // applicationId가 없으면 워크플로우를 하둡에 던지기 전이고 또한 java, python, r 등의 모듈이라고 볼 수 있다. 따라서 RUNNIG 중인 프로세스를 킬할 수 있다.
            if (idList != null && idList.length > 0) {
                for (String file : idList) {
                    if (file.startsWith("app.")) {
                        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
                        service.killApplication(StringUtils.removePrefix(file, "app.", true), engineConfig);
                        taskHistory.get(0).setStatus(State.FAILED.toString());
                        taskHistoryRemoteService.updateByTaskIdAndIdentifier(taskHistory.get(0));
                    }
                }
            } else if ("RUNNING".equals(taskHistory.get(0).getStatus())) {
                engineService.getDesignerRemoteService().killProccess(taskHistory.get(0).getLogDirectory());
                taskHistory.get(0).setStatus(State.FAILED.toString());
                taskHistoryRemoteService.updateByTaskIdAndIdentifier(taskHistory.get(0));
            }
        }

        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "yarnId", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response yarnId(@RequestParam(defaultValue = "") String clusterName, @RequestParam(defaultValue = "") String identifier, @RequestParam(defaultValue = "") String type) {
        Response response = new Response();
        EngineService engineService = getEngineService(clusterName);

        // FIXME type이 workflow일 경우 처리
        if ("task".equals(type)) {
            TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
            List<TaskHistory> taskHistory = taskHistoryRemoteService.selectByIdentifier(identifier);
            String[] idList = engineService.getDesignerRemoteService().idList(taskHistory.get(0).getLogDirectory(), "app.");

            if (idList != null && idList.length > 0) {
                for (String file : idList) {
                    if (file.startsWith("app.")) {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", StringUtils.removePrefix(file, "app.", true));
                        response.getList().add(map);
                    }
                }
            }
        }

        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "mrId", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response mrId(@RequestParam(defaultValue = "") String clusterName, @RequestParam(defaultValue = "") String identifier, @RequestParam(defaultValue = "") String type) {
        Response response = new Response();
        EngineService engineService = getEngineService(clusterName);

        // FIXME type이 workflow일 경우 처리
        if ("task".equals(type)) {
            TaskHistoryRemoteService taskHistoryRemoteService = engineService.getTaskHistoryRemoteService();
            List<TaskHistory> taskHistory = taskHistoryRemoteService.selectByIdentifier(identifier);
            String[] idList = engineService.getDesignerRemoteService().idList(taskHistory.get(0).getLogDirectory(), "hadoop.");

            if (idList != null && idList.length > 0) {
                for (String file : idList) {
                    if (file.startsWith("hadoop.")) {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", StringUtils.removePrefix(file, "hadoop.", true));
                        response.getList().add(map);
                    }
                }
            }
        }

        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "timeseries", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response timeseries(@RequestParam String clusterName, @RequestParam String status) {
        Response response = new Response();
        response.setSuccess(true);
        int level = SessionUtils.getLevel();

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        String query = null;
        if (level != 1) { // 일반 사용자의 경우 자기것만 보여줘야 함
            if ("ALL".equals(status)) {
                query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE USERNAME = '" + SessionUtils.getUsername() + "' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
            } else if ("SUCCESS".equals(status)) {
                query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, 'SUCCESS' as type, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE USERNAME = '" + SessionUtils.getUsername() + "' AND STATUS = 'SUCCESS' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
            } else {
                query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, 'FAILED' as type, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE USERNAME = '" + SessionUtils.getUsername() + "' AND STATUS <> 'SUCCESS' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
            }
        } else {
            if ("ALL".equals(status)) {
                query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
            } else if ("SUCCESS".equals(status)) {
                query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, 'SUCCESS' as type, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE STATUS = 'SUCCESS' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
            } else {
                query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(START_DATE),'%Y-%m-%d %H') as time, 'FAILED' as type, START_DATE from FL_WORKFLOW_HISTORY, (SELECT @row := 0) r WHERE AND STATUS <> 'SUCCESS' AND START_DATE > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_DATE,'%Y-%m-%d %H') ORDER BY START_DATE asc";
            }
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(MessageFormatter.format(query, clusterName).getMessage());
        response.getList().addAll(list);
        return response;
    }
}
