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
package org.opencloudengine.flamingo2.web.spark;

import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.spark.SparkRemoteService;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Hyokun Park on 15. 8. 14..
 */
@RestController
@RequestMapping("/spark/monitoring")
public class SparkMonitoringController extends DefaultController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(SparkMonitoringController.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "getJobChart", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getJobChart(@RequestParam String clusterName) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(clusterName);
            SparkRemoteService service = engineService.getSparkRemoteService();

            response.getList().addAll(service.getJobChart());
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getApplications", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getApplications(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            response.getList().addAll(service.getApplications(params));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getJobs", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getJobs(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            response.getList().addAll(service.getJobs(params));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getStages", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getStages(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            response.getList().addAll(service.getStages(params));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getEnvironment", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getEnvironment(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineConfig engineConfig = this.getEngineConfig(params.get("clusterName").toString());
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            response.getList().addAll(service.getEnvironment(engineConfig, params));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getExecutors", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getExecutors(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            response.getList().addAll(service.getExecutors(params));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getStorage", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getStorage(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            response.getList().addAll(service.getStorage(params));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getJobsTimeline", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getJobsTimeline(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            params.put("type", "Executor");

            List<Map> timeList = objectMapper.readValue(service.getTimeline(params).get("json").toString(), List.class);

            params.put("type", "Jobs");

            timeList.addAll(objectMapper.readValue(service.getTimeline(params).get("json").toString(), List.class));

            response.getList().addAll(timeList);
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getStagesTimeline", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getStagesTimeline(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            params.put("type", "Stages");

            List<Map> timeList = objectMapper.readValue(service.getTimeline(params).get("json").toString(), List.class);

            params.put("type", "Executor");
            params.remove("jobid");

            timeList.addAll(objectMapper.readValue(service.getTimeline(params).get("json").toString(), List.class));

            response.getList().addAll(timeList);
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getStageDetailChart", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getStageDetailChart(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            Map detailMap = service.getStageDetail(params);

            response.getList().addAll(objectMapper.readValue(detailMap.get("json").toString(), List.class));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "getTaskTimeline", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getTaskTimeline(@RequestParam Map params) {
        Response response = new Response();

        try {
            EngineConfig engineConfig = this.getEngineConfig(params.get("clusterName").toString());
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            SparkRemoteService service = engineService.getSparkRemoteService();

            Map resultMap = service.getTaskTimeline(engineConfig, params);

            Map groupMap = (Map) resultMap.get("group");
            List groupList = new ArrayList();

            Object keys[] = groupMap.keySet().toArray();

            for (Object key : keys) {
                Map group = new HashMap();
                group.put("id", key.toString());
                group.put("content", key.toString() + " / " + groupMap.get(key).toString());
                groupList.add(group);
            }

            response.getList().addAll((List) resultMap.get("list"));
            response.getMap().put("group", groupList);
            response.setTotal(response.getList().size());
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }
}
