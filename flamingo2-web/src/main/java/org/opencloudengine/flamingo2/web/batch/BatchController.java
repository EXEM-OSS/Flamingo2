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
package org.opencloudengine.flamingo2.web.batch;

import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.batch.BatchService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.scheduler.SchedulerRemoteService;
import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHolder;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/batch")
public class BatchController extends DefaultController {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response jvmheap(@RequestParam String clusterName) {
        EngineService engineService = this.getEngineService(clusterName);
        SchedulerRemoteService service = engineService.getSchedulerRemoteService();
        List list = service.getRecentMetrics(clusterName);

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(list);
        return response;
    }

    @RequestMapping(value = "regist", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response regist(@RequestBody Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
            EngineService engineService = this.getEngineService(clusterName);

            String workflowId = params.get("workflowId").toString();
            Workflow workflow = engineService.getDesignerRemoteService().getWorkflow(workflowId);
            params.put("workflow", workflow);
            params.put("username", SessionUtils.getUsername());
            params.put("user_id", SessionUtils.getId());

            String jobId = engineService.getBatchService().regist(params);
            response.setSuccess(true);
            response.getMap().put("jobId", jobId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to regist batch job", ex);
        }
        return response;
    }

    @RequestMapping(value = "get", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response get(@RequestBody Map<String, String> params) {
        Response response = new Response();
        EngineService engineService = this.getEngineService(params.get("clusterName"));

        Workflow workflow = engineService.getBatchService().get(params.get("jobId"));
        response.setSuccess(true);
        response.setObject(workflow);
        return response;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response list(@RequestParam Map<String, String> params) {

        Response response = new Response();
        EngineService engineService = this.getEngineService(params.get("clusterName"));

        List<Map> jobs = engineService.getBatchService().getJobs();
        response.getList().addAll(jobs);
        response.setTotal(jobs.size());
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "suspend", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response suspend(@RequestBody Map params) {
        Response response = new Response();
        EngineService engineService = this.getEngineService(params.get("clusterName").toString());
        engineService.getBatchService().suspend(params);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "resume", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response resume(@RequestBody Map params) {
        Response response = new Response();
        EngineService engineService = this.getEngineService(params.get("clusterName").toString());
        engineService.getBatchService().resume(params);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "stop", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response stop(@RequestBody Map params) {
        Response response = new Response();
        EngineService engineService = this.getEngineService(params.get("clusterName").toString());
        engineService.getBatchService().stop(params);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/getJob", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getJob(@RequestParam Map params) {
        try {
            Response response = new Response();
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());
            BatchService service = engineService.getBatchService();

            Map jobMap = service.getJob(params);
            Object[] objects = jobMap.keySet().toArray();

            Workflow workflow = (Workflow) jobMap.get("WORKFLOW");

            jobMap.put("workflowId", workflow.getWorkflowId());
            jobMap.put("createDate", workflow.getCreateDate().toString());
            jobMap.put("writer", workflow.getUsername());
            jobMap.put("status", workflow.getStatus());

            response.getMap().putAll(jobMap);
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response update(@RequestBody Map params) {
        try {
            Response response = new Response();
            EngineService engineService = this.getEngineService(params.get("clusterName").toString());

            engineService.getBatchService().update(params);
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }
}