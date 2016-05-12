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

import org.apache.commons.lang.WordUtils;
import org.opencloudengine.flamingo2.core.exception.WholeBodyException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.hadoop.ResourceManagerRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "/monitoring/resourcemanager")
public class ResourceManagerController extends DefaultController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> info(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        return service.getResourceManagerInfo(engineConfig);
    }

    @RequestMapping(value = "/clustermetrics", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> clusterMetrics(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        return service.getClusterMetrics(engineConfig);
    }

    @RequestMapping(value = "/systemmetrics", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map systemMetrics(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        return service.getSystemMetrics(engineConfig);
    }

    @RequestMapping(value = "/configuration", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response configuration(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        Response response = new Response();
        response.setSuccess(true);

        SortedMap configuration = new TreeMap();
        configuration.putAll(service.getConfiguration(engineConfig));
        Set<String> keys = configuration.keySet();
        for (String key : keys) {
            HashMap kv = new HashMap();
            kv.put("key", key);
            kv.put("value", WordUtils.wrap((String) configuration.get(key), 100, "<br/>", true));
            response.getList().add(kv);
        }
        return response;
    }

    @RequestMapping(value = "/queues", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List queues(@RequestParam String clusterName, @RequestParam String node) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        List allQueues = service.getAllQueues(engineConfig);
        for (Object obj : allQueues) {
            Map queue = (Map) obj;
            String name = (String) queue.get("name");
            queue.put("name", org.apache.commons.lang.StringUtils.splitPreserveAllTokens(name, ".")[1]);
            queue.put("text", org.apache.commons.lang.StringUtils.splitPreserveAllTokens(name, ".")[1]);
            queue.put("queue", name);
            queue.put("leaf", true);
        }
        return allQueues;
    }

    @RequestMapping(value = "/app/move", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response appToQueue(@RequestParam String applicationId, @RequestParam String clusterName, @RequestParam String queue) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        service.moveApplicationAcrossQueues(applicationId, queue, engineConfig);

        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/apps/running", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getRunningApplications(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getRunningApplications(engineConfig));
        return response;
    }

    @RequestMapping(value = "/apps/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getAllApplications(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.listApplications(null, null, true, engineConfig));
        return response;
    }

    @RequestMapping(value = "/apps/timeseries", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response timeseries(@RequestParam String clusterName) {
        Response response = new Response();
        response.setSuccess(true);

        // FIXME : to MyBATIS, Remote
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        String query = "select (@row:=@row+1) as num, count(*) as sum, DATE_FORMAT(MAX(startTime),'%Y-%m-%d %H') as time, startTime from FL_CL_YARN_DUMP, (SELECT @row := 0) r WHERE system ='{}' AND startTime > DATE_ADD(now(), INTERVAL -7 DAY) GROUP BY DATE_FORMAT(startTime,'%Y-%m-%d %H') ORDER BY startTime asc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(MessageFormatter.format(query, clusterName).getMessage());
        response.getList().addAll(list);
        return response;
    }

    @RequestMapping(value = "/app/statinfo", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response appStatInfo(@RequestParam String applicationId, @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getMap().putAll(service.getAppStatInfo(applicationId, engineConfig));
        return response;
    }

    /**
     * 애플리케이션의 기본 요약 정보를 가져온다.
     * Application Master 정보는 Tracking URL에서 추적할 수 있으며
     * MapReduce, Spark 등의 Application Type에 따라서 Application Master는 다르게 설정된다.
     * MapReduce의 경우 Application Master를 통해서 실행 정보를 추적할 수 있으며 Spark의 경우 현재 불가능한다.
     *
     * @param applicationId 애플리케이션 ID
     * @param clusterName   클러스터명
     */
    @RequestMapping(value = "/app/report", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response applicationReport(@RequestParam String applicationId, @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getMap().putAll(service.getApplicationReport(applicationId, engineConfig));
        return response;
    }

    /**
     * 애플리케이션의 컨테이너 로그를 Resource Manager에서 다운로드하여 클라이언트로 전송한다.
     * 클라이언트에서는 다운로드가 진행된다.
     *
     * @param applicationId 애플리케이션 ID
     * @param appOwner      애플리케이션을 실행한 소유자
     * @param clusterName   클러스터명
     */
    @RequestMapping(value = "/app/download", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity downloadLog(HttpServletResponse res, @RequestParam String applicationId, @RequestParam String appOwner, @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        try {
            String applicationLog = service.getApplicationLog(applicationId, appOwner, engineConfig);
            byte[] bytes = applicationLog.getBytes();

            res.setHeader("Content-Length", "" + bytes.length);
            res.setHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            res.setHeader("Content-Disposition", MessageFormatter.format("attachment; path={}; filename={}", applicationId, applicationId + ".log").getMessage());
            res.setStatus(200);
            FileCopyUtils.copy(bytes, res.getOutputStream());
            res.flushBuffer();
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex) {
            throw new WholeBodyException("You can not handle the Application Log.", ex);
        }
    }

    /**
     * 선택한 애플리케이션을 강제 종료한다.
     *
     * @param applicationId 애플리케이션 ID
     * @param clusterName   클러스터명
     */
    @RequestMapping(value = "/app/kill", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response kill(@RequestParam String applicationId, @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        service.killApplication(applicationId, engineConfig);

        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    /**
     * 애플리케이션의 컨테이너 로그를 Resource Manager에서 다운로드하여 클라이언트로 전송한다.
     *
     * @param applicationId 애플리케이션 ID
     * @param appOwner      애플리케이션을 실행한 소유자
     * @param clusterName   클러스터명
     * @return 애플리케이션의 컨테이너 로그
     */
    @RequestMapping(value = "/app/log", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> applicationLog(@RequestParam String applicationId, @RequestParam String appOwner, @RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();

        try {
            String applicationLog = service.getApplicationLog(applicationId, appOwner, engineConfig);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(applicationLog, headers, HttpStatus.OK);
        } catch (Exception ex) {
            throw new WholeBodyException("Unable to get the Application Log.", ex);
        }
    }

    @RequestMapping(value = "/nodestatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response nodeStatus(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        Map<String, Object> resourceManagerInfo = service.getResourceManagerInfo(engineConfig);
        Map<String, Object> cluster = (Map<String, Object>) resourceManagerInfo.get("cluster");

        Response response = new Response();
        response.setSuccess(true);
        response.getList().add(getItem(cluster, "lostNodes", "Lost"));
        response.getList().add(getItem(cluster, "activeNodes", "Active"));
        response.getList().add(getItem(cluster, "unhealthyNodes", "Unhealthy"));
        response.getList().add(getItem(cluster, "decommissionedNodes", "Decommission"));
        response.getList().add(getItem(cluster, "rebootedNodes", "Reboot"));
        return response;
    }

    @RequestMapping(value = "/appstatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response applicationStatus(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        Map<String, Object> resourceManagerInfo = service.getResourceManagerInfo(engineConfig);
        Map<String, Object> cluster = (Map<String, Object>) resourceManagerInfo.get("cluster");

        Response response = new Response();
        response.setSuccess(true);
        response.getList().add(getItem(cluster, "appsRunning", "Running"));
        response.getList().add(getItem(cluster, "appsFailed", "Failed"));
        response.getList().add(getItem(cluster, "appsSubmitted", "Submitted"));
        response.getList().add(getItem(cluster, "appsPending", "Pending"));
        response.getList().add(getItem(cluster, "appsKilled", "Killed"));
        response.getList().add(getItem(cluster, "appsCompleted", "Completed"));
        return response;
    }

    @RequestMapping(value = "/containerstatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response containerStatus(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        Map<String, Object> resourceManagerInfo = service.getResourceManagerInfo(engineConfig);
        Map<String, Object> cluster = (Map<String, Object>) resourceManagerInfo.get("cluster");

        Response response = new Response();
        response.setSuccess(true);
        response.getList().add(getItem(cluster, "containersPending", "Pending"));
        response.getList().add(getItem(cluster, "containersReserved", "Reserved"));
        response.getList().add(getItem(cluster, "containersAllocated", "Allocated"));
        return response;
    }

    @RequestMapping(value = "/jvmheap", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response jvmheap(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
        Map<String, Object> jvmHeap = service.getJVMHeap(engineConfig);
        Response response = new Response();
        response.setSuccess(true);
        // FIXME response.getList().add(getItem(jvmHeap, "maxMemory", "Max"));
        response.getList().add(getItem(jvmHeap, "freeMemory", "Free"));
        response.getList().add(getItem(jvmHeap, "usedMemory", "Used"));
        return response;
    }

    private Map getItem(Map<String, Object> values, String key, String title) {
        Map map = new HashMap();
        map.put("name", title);
        map.put("value", values.get(key));
        return map;
    }

}