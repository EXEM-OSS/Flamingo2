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

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.monitoring.CLDBRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/monitoring/cldb")
public class CLDBController extends DefaultController {

    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response metrics(@RequestParam String clusterName) {
        Response response = new Response();

        try {
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            CLDBRemoteService service = engineService.getCLDBRemoteService();

            response.setSuccess(true);
            response.getList().addAll(service.selectCLDBMetrics(engineConfig));
        } catch (Exception ex) {
            throw new ServiceException("You can not get the system metrics of MapR CLDB.", ex);
        }

        return response;
    }
}
