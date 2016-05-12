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

@RestController
@RequestMapping(value = "/monitoring/datanode")
public class DatanodeController extends DefaultController {

    @RequestMapping(value = "/livenodes", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response livenodes(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getLiveNodes(engineConfig));
        return response;
    }

    @RequestMapping(value = "/deadnodes", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response deadnodes(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getDeadNodes(engineConfig));
        return response;
    }

    @RequestMapping(value = "/decommisioningnodes", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response decommisioningnodes(@RequestParam String clusterName) {
        EngineConfig engineConfig = this.getEngineConfig(clusterName);
        EngineService engineService = this.getEngineService(clusterName);
        NamenodeRemoteService service = engineService.getNamenodeRemoteService();

        Response response = new Response();
        response.setSuccess(true);
        response.getList().addAll(service.getDecommissioningNodes(engineConfig));
        return response;
    }
}