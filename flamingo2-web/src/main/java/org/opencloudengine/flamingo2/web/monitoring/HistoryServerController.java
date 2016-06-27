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
package org.opencloudengine.flamingo2.web.monitoring;

import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.hadoop.HistoryServerRemoteService;
import org.opencloudengine.flamingo2.engine.hadoop.MapReduceRemoteService;
import org.opencloudengine.flamingo2.engine.hadoop.ResourceManagerRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.opencloudengine.flamingo2.logging.StringUtils.isEmpty;

@RestController
@RequestMapping(value = "/monitoring/application/history")
public class HistoryServerController extends DefaultController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(HistoryServerController.class);

    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> info(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();

        return service.getHistoryServerInfo(engineConfig.getHistoryServerUrl());
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response jobs(@RequestParam String clusterName) {
        Response response = new Response();
        try {
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();
            ResourceManagerRemoteService resourceManagerService = engineService.getResourceManagerRemoteService();

            Map jobsMap = service.getJobs(engineConfig.getHistoryServerUrl());
            Map jobMap = (Map) jobsMap.get("jobs");
            List<Map> jobList = (List) jobMap.get("job");

            List<Map> runningList = resourceManagerService.getRunningMRJobs(engineConfig);

            logger.info(String.valueOf(runningList.size()));

            jobList.addAll(0, runningList);

            response.getList().addAll(jobList);
            response.setTotal(response.getList().size());
            response.setSuccess(true);

            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "/jobs/timeseries", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response timeseries(@RequestParam String clusterName) {
        Response response = new Response();
        response.setSuccess(true);
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        String query = "SELECT (@row:=@row+1) as num, COUNT(*) as sum, DATE_FORMAT(MAX(START_TIME),'%Y-%m-%d %H') as time, START_TIME FROM FL_CL_MR_DUMP, (SELECT @row := 0) r WHERE SYSTEM ='{}' AND START_TIME > DATE_ADD(NOW(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(START_TIME,'%Y-%m-%d %H') ORDER BY START_TIME asc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(MessageFormatter.format(query, clusterName).getMessage());
        response.getList().addAll(list);
        return response;
    }

    @RequestMapping(value = "/jobs/job", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response job(@RequestParam String jobId, @RequestParam String state, @RequestParam String clusterName) {
        Response response = new Response();

        try {
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();
            ResourceManagerRemoteService resourceManagerService = engineService.getResourceManagerRemoteService();

            Map<String, Object> results = new HashMap<>();
            if (state.equals("RUNNING") && resourceManagerService.getJobStatus(jobId, engineConfig).equals("RUNNING")) {
                results = resourceManagerService.getRunningMRJobReport(jobId, engineConfig);
                results.put("duration", getDuration(results));
                results.put("avgReduceTime", 0);
                results.put("avgMergeTime", 0);
                results.put("avgShuffleTime",0 );
                results.put("avgMapTime", 0);
                response.getMap().putAll(results);
            }
            else {
                results = service.getJob(engineConfig.getHistoryServerUrl(), jobId);
                Map job = (Map) results.get("job");
                job.put("duration", getDuration(job));
                response.getMap().putAll(job);
            }

            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }

    }

    private long getDuration(Map job) {
        Long start = (Long) job.get("startTime");
        Long finish = (Long) job.get("finishTime");
        return DateUtils.getDiffSeconds(new Date(finish), new Date(start));
    }

    @RequestMapping(value = "/jobs/job/info", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> jobInfo(@RequestParam String jobId, @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();

        return service.getJobInfo(engineConfig.getHistoryServerUrl(), jobId);
    }

    @RequestMapping(value = "/jobs/job/attempts", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> jobAttempts(@RequestParam String jobId, @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();

        return service.getAttempts(engineConfig.getHistoryServerUrl(), jobId);
    }

    /**
     * MapReduce Job의 Job Counter를 반환한다.
     * 최초 UI에서 expand를 하므로 강제로 호출이된다. 따라서 기본값을 빈값으로 파라미터를 모두 설정하고 빈값이 들어오면 빈값을 리턴한다.
     */
    @RequestMapping(value = "/jobs/job/counters", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Map> jobCounters(@RequestParam(defaultValue = "") String jobId,
                                 @RequestParam(defaultValue = "") String state,
                                 @RequestParam(defaultValue = "") String clusterName) {

        try {
            if (isEmpty(jobId) || isEmpty(clusterName)) { // FL2-20-5
                return new ArrayList();
            }
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();
            ResourceManagerRemoteService resourceManagerService = engineService.getResourceManagerRemoteService();
            MapReduceRemoteService mapReduceRemoteService = engineService.getMapReduceRemoteService();

            Map counters = new HashMap();

            if (state.equals("RUNNING") && resourceManagerService.getJobStatus(jobId, engineConfig).equals("RUNNING")) {
                counters = mapReduceRemoteService.getCounters(engineConfig.getWapAddress() + ":" + engineConfig.getWapPort(), jobId);
            }
            else {
                counters = service.getCounters(engineConfig.getHistoryServerUrl(), jobId);
            }

            Map jobCounters = (Map) counters.get("jobCounters");
            List<Map> counterGroupList = (List<Map>) jobCounters.get("counterGroup");

            for (Map counterGroup : counterGroupList) {
                List<Map> counterList = (List<Map>) counterGroup.get("counter");
                int cgi = counterGroupList.indexOf(counterGroup);

                for (Map counter : counterList) {
                    int ci = counterList.indexOf(counter);
                    counter.put("leaf", true);
                    counterList.set(ci, counter);
                }

                counterGroup.put("name", counterGroup.get("counterGroupName"));
                counterGroup.put("leaf", false);
                counterGroup.put("children", counterList);
                counterGroup.remove("counter");
                counterGroupList.set(cgi, counterGroup);
            }

            jobCounters.put("counterGroup", counterGroupList);
            counters.put("jobCounters", jobCounters);

            return counterGroupList;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "/jobs/job/configuration", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> jobConfiguration(@RequestParam String jobId,
                                                @RequestParam String state,
                                                @RequestParam String clusterName) {

        try {
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();
            ResourceManagerRemoteService resourceManagerService = engineService.getResourceManagerRemoteService();
            MapReduceRemoteService mapReduceRemoteService = engineService.getMapReduceRemoteService();

            Map<String, Object> jobConf = new HashMap<>();
            if (state.equals("RUNNING") && resourceManagerService.getJobStatus(jobId, engineConfig).equals("RUNNING")) {
                jobConf = mapReduceRemoteService.getJobConf(engineConfig.getWapAddress() + ":" + engineConfig.getWapPort(), jobId);
            }
            else {
                jobConf = service.getJobConf(engineConfig.getHistoryServerUrl(), jobId);
            }

            SortedMap configuration = new TreeMap();
            configuration.putAll(jobConf);

            return configuration;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }

    }

    @RequestMapping(value = "/jobs/job/tasks", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response tasks(@RequestParam String jobId, @RequestParam String state, @RequestParam String clusterName) {
        Response response = new Response();
        try {
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();
            ResourceManagerRemoteService resourceManagerService = engineService.getResourceManagerRemoteService();

            if (state.equals("RUNNING") && resourceManagerService.getJobStatus(jobId, engineConfig).equals("RUNNING") ) {
                response.getList().addAll(resourceManagerService.getRunningMRTasks(jobId, engineConfig));
            }
            else {
                Map tasksMap = service.getTasks(engineConfig.getHistoryServerUrl(), jobId);
                Map taskMap = (Map) tasksMap.get("tasks");

                response.getList().addAll((List) taskMap.get("task"));
            }

            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "/jobs/job/tasks/task/counters", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> taskCounters(@RequestParam String jobId,
                                            @RequestParam String taskId,
                                            @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();

        return service.getTaskCounters(engineConfig.getHistoryServerUrl(), jobId, taskId);
    }

    @RequestMapping(value = "/jobs/job/tasks/task/attempts", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> taskAttempts(@RequestParam String jobId,
                                            @RequestParam String taskId,
                                            @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();

        return service.getTaskAttempts(engineConfig.getHistoryServerUrl(), jobId, taskId);
    }

    @RequestMapping(value = "/jobs/job/tasks/task/attempts/attempt/counters", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> taskAttemptsCounters(@RequestParam String jobId,
                                                    @RequestParam String taskId,
                                                    @RequestParam String attemptId,
                                                    @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        HistoryServerRemoteService service = engineService.getHistoryServerRemoteService();

        return service.getTaskAttemptsCounters(engineConfig.getHistoryServerUrl(), jobId, taskId, attemptId);
    }
}