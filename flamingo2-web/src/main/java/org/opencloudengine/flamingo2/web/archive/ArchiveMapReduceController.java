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
package org.opencloudengine.flamingo2.web.archive;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.archive.mapreduce.ArchiveMapReduceRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHolder;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.remote.EngineLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * Archive MapReduce REST Controller
 *
 * @author Myeongha KIM
 * @since 2.0
 */
@RestController
@RequestMapping("/archive/mapreduce")
public class ArchiveMapReduceController {
    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(ArchiveMapReduceController.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Remote Repository에서 Yarn Application Summary Chart 정보를 조회한다.
     *
     * @param clusterName 클러스터명
     * @param startDate   시작일
     * @param endDate     종료일
     * @param filter      조회유형
     * @return summaryMap
     */
    @RequestMapping(value = "summary", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getMapReduceJobSummary(@RequestParam String clusterName,
                               @RequestParam(defaultValue = "") String startDate,
                               @RequestParam(defaultValue = "") String endDate,
                               @RequestParam(defaultValue = "ALL") String filter) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveMapReduceRemoteService amrrs = engineService.getArchiveMapReduceRemoteService();
        String username = getSessionUsername();
        int userLevel = getSessionUserLevel();

        Map summaryMap = new HashMap();
        summaryMap.put("username", username);
        summaryMap.put("userLevel", userLevel);
        summaryMap.put("clusterName", clusterName);
        summaryMap.put("startDate", startDate);
        summaryMap.put("endDate", endDate);
        summaryMap.put("filter", filter);

        Response response = new Response();
        response.getList().addAll(amrrs.getMapReduceJobSummary(summaryMap));
        response.setSuccess(true);
        return response;
    }

    /**
     * Remote Repository에서 모든 Archive Yan Application을 조회한다.
     *
     * @param clusterName 클러스터명
     * @param startDate   시작일
     * @param endDate     종료일
     * @param filter      조회유형
     * @param mrJobName   맴리듀스잡명
     * @param page        페이지
     * @param limit       그리드 사이즈
     * @return allAppMap
     */
    @RequestMapping(value = "allMapReduceJobs", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getAllMapReduceJobs(@RequestParam String clusterName,
                                       @RequestParam(defaultValue = "") String startDate,
                                       @RequestParam(defaultValue = "") String endDate,
                                       @RequestParam(defaultValue = "ALL") String filter,
                                       @RequestParam(defaultValue = "") String mrJobName,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int limit) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveMapReduceRemoteService amrrs = engineService.getArchiveMapReduceRemoteService();
        String username = getSessionUsername();
        int userLevel = getSessionUserLevel();

        Map allMRJobsMap = new HashMap();
        allMRJobsMap.put("username", username);
        allMRJobsMap.put("userLevel", userLevel);
        allMRJobsMap.put("clusterName", clusterName);
        allMRJobsMap.put("startDate", startDate);
        allMRJobsMap.put("endDate", endDate);
        allMRJobsMap.put("filter", filter);
        allMRJobsMap.put("mrJobName", mrJobName);
        allMRJobsMap.put("page", page);
        allMRJobsMap.put("limit", limit);

        int totalRows = amrrs.getTotalCountOfAllMapReduceJobs(allMRJobsMap);

        Response response = new Response();
        response.getList().addAll(amrrs.getAllMapReduceJobs(allMRJobsMap));
        response.setTotal(totalRows);
        response.setSuccess(true);
        return response;
    }

    /**
     * 그리드에서 선택한 Applcation의 상세 정보를 Remote Repository에서 가져온다.
     *
     * @param clusterName   클러스터명
     * @param jobId         Job ID
     * @return applicationReportMap
     */
    @RequestMapping(value = "mapReduceJobReport", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getMapReduceJobReport(@RequestParam String clusterName, @RequestParam String jobId) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveMapReduceRemoteService amrrs = engineService.getArchiveMapReduceRemoteService();

        Map archiveMap = new HashMap();
        archiveMap.put("clusterName", clusterName);
        archiveMap.put("jobId", jobId);

        Map mapReduceJobReportMap = amrrs.getMapReduceJobReport(archiveMap);

        Date startTime = (Date) mapReduceJobReportMap.get("start_time");
        Date finishTime = (Date) mapReduceJobReportMap.get("finish_time");

        mapReduceJobReportMap.put("duration", getDuration(startTime, finishTime));

        Response response = new Response();
        response.getMap().putAll(mapReduceJobReportMap);
        response.setSuccess(true);
        return response;
    }

    /**
     * 그리드에서 선택한 Application의 Job Counters 정보를 Remote Repository에서 가져온다.
     *
     * @param clusterName   클러스터명
     * @param jobId         Job ID
     * @param state         State
     * @return jobCounterGroupList
     */
    @RequestMapping(value = "mapReduceJobCounters", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Map> getMapReduceJobCounters(@RequestParam String clusterName, @RequestParam String jobId, @RequestParam String state) throws IOException {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveMapReduceRemoteService amrrs = engineService.getArchiveMapReduceRemoteService();

        Map archiveMap = new HashMap();
        archiveMap.put("clusterName", clusterName);
        archiveMap.put("jobId", jobId);

        Map<String, Object> mrJobCounters = amrrs.getMapReduceJobCounters(archiveMap);
        Map jobCounters = objectMapper.readValue(mrJobCounters.get("counters").toString(), Map.class);
        Map counterMap = (Map) jobCounters.get("jobCounters");
        List<Map> jobCounterGroupList = (List<Map>) counterMap.get("counterGroup");

        for (Map counterGroup : jobCounterGroupList) {
            List<Map> counterList = (List<Map>) counterGroup.get("counter");
            int cgi = jobCounterGroupList.indexOf(counterGroup);

            for (Map counter : counterList) {
                int ci = counterList.indexOf(counter);
                counter.put("leaf", true);
                counterList.set(ci, counter);
            }

            counterGroup.put("name", counterGroup.get("counterGroupName"));
            counterGroup.put("leaf", false);
            counterGroup.put("children", counterList);
            counterGroup.remove("counter");
            jobCounterGroupList.set(cgi, counterGroup);
        }

        return jobCounterGroupList;
    }

    /**
     * 그리드에서 선택한 Application의 Job Configuration 정보를 Remote Repository에서 가져온다.
     *
     * @param clusterName   클러스터명
     * @param jobId         Job ID
     * @return mapReduceJobConfMap
     */
    @RequestMapping(value = "mapReduceJobConfiguration", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getMapReduceJobConfiguration(@RequestParam String clusterName, @RequestParam String jobId) throws IOException {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveMapReduceRemoteService amrrs = engineService.getArchiveMapReduceRemoteService();

        Map archiveMap = new HashMap();
        archiveMap.put("clusterName", clusterName);
        archiveMap.put("jobId", jobId);

        Map<String, Object> mapReduceJobConfMap = amrrs.getMapReduceJobConfiguration(archiveMap);
        Map confMap = objectMapper.readValue(mapReduceJobConfMap.get("configuration").toString(), Map.class);

        SortedMap sortedMap = new TreeMap();
        sortedMap.putAll(confMap);

        return sortedMap;
    }

    @RequestMapping(value = "mapReduceJobTask", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getMapReduceJobTask(@RequestParam String clusterName, @RequestParam String jobId) throws IOException {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveMapReduceRemoteService amrrs = engineService.getArchiveMapReduceRemoteService();

        Map archiveMap = new HashMap();
        archiveMap.put("clusterName", clusterName);
        archiveMap.put("jobId", jobId);

        Map<String, Object> mapReduceJobTaskMap = amrrs.getMapReduceJobTask(archiveMap);
        Map jobTasksMap = objectMapper.readValue(mapReduceJobTaskMap.get("tasks").toString(), Map.class);

        Response response = new Response();
        response.getList().addAll((List) jobTasksMap.get("task"));
        response.setSuccess(true);

        return response;
    }

    /**
     * 현재 세션의 사용자명을 가져온다.
     *
     * @return username
     */
    private String getSessionUsername() {
        return SessionUtils.getUsername();
    }

    /**
     * 현재 세션의 사용자 등급 정보를 가져온다.
     *
     * @return level
     */
    private int getSessionUserLevel() {
        return SessionUtils.getLevel();
    }

    private long getDuration(Date startTime, Date finishTime) {
        return DateUtils.getDiffSeconds(finishTime, startTime);
    }
}
