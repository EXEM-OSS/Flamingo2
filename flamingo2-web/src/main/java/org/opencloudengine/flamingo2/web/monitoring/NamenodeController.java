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

import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.hadoop.NamenodeRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// import org.opencloudengine.flamingo2.core.rest.Response;

/**
 * Flamingo Engine에서 제공하는 Namenode 서비스와 연계하여 네임노드와 관련된 모니터링 정보를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping(value = "/monitoring/namenode")
public class NamenodeController extends DefaultController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response info(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();
        Map namenodeInfo = service.getNamenodeInfo(engineConfig);
        Map metrics = service.getConfiguration(engineConfig);

        Map map = new HashMap();
        map.putAll(namenodeInfo);
        map.put("dfsReplication", metrics.get("dfs.replication"));
        map.put("dfsBlockSize", metrics.get("dfs.blocksize"));

        Response response = new Response();
        response.setSuccess(true);
        response.setMap(map);
        return response;
    }

    @RequestMapping(value = "/dfsusage", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response dfsusage(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getRecentTrend(engineConfig));
        return response;
    }

    @RequestMapping(value = "/blockstatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response blockstatus(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();
        Map namenodeInfo = service.getNamenodeInfo(engineConfig);

        Response response = new Response();
        response.setSuccess(true);
        response.getList().add(getBlockStatus("Corrupt Rep", "corruptReplicaBlocks", (Long) namenodeInfo.get("corruptReplicaBlocks")));
        response.getList().add(getBlockStatus("Pending Rep", "pendingReplicationBlocks", (Long) namenodeInfo.get("pendingReplicationBlocks")));
        response.getList().add(getBlockStatus("Scheduled Rep", "scheduledReplicationBlocks", (Long) namenodeInfo.get("scheduledReplicationBlocks")));
        response.getList().add(getBlockStatus("Under Replicated", "underReplicatedBlocks", (Long) namenodeInfo.get("underReplicatedBlocks")));
        response.getList().add(getBlockStatus("Missing", "missingBlocks", (Long) namenodeInfo.get("missingBlocks")));
        return response;
    }

    private Map getBlockStatus(String name, String type, long count) {
        Map map = new HashMap();
        map.put("name", name);
        map.put("type", type);
        map.put("count", count);
        return map;
    }

    @RequestMapping(value = "/configuration", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response configuration(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getMap().putAll(service.getConfiguration(engineConfig));
        return response;
    }

    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response metrics(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getMap().putAll(service.getMetrics(engineConfig));
        return response;
    }

    @RequestMapping(value = "/datanodes", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response dataNodes(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getDataNodes(engineConfig));
        return response;
    }

    @RequestMapping(value = "/nodes/live", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response liveNodes(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getLiveNodes(engineConfig));
        return response;
    }

    @RequestMapping(value = "/nodes/dead", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response deadNodes(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getDeadNodes(engineConfig));
        return response;
    }

    @RequestMapping(value = "/nodes/decommission", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response decommissionNodes(@RequestParam String clusterName) throws Exception {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getDecommissioningNodes(engineConfig));
        return response;
    }
}