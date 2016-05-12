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

import org.hyperic.sigar.SigarProxy;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.remote.SystemMetricsRemoteService;
import org.opencloudengine.flamingo2.util.SystemUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/monitoring/flamingo")
public class FlamingoController extends DefaultController {

    @Autowired
    private SigarProxy sigar;

    @RequestMapping(value = "/web", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response web() {
        try {
            Response response = new Response();
            response.setSuccess(true);
            Map map = SystemUtils.getSystemMetrics(sigar);
            response.getMap().putAll(map);
            return response;
        } catch (Exception ex) {
            throw new ServiceException("You can not get the system metrics of Flamingo Web", ex);
        }
    }

    @RequestMapping(value = "/engine", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response engine(@RequestParam String clusterName) {
        try {
            Response response = new Response();
            response.setSuccess(true);
            EngineService engineService = this.getEngineService(clusterName);
            SystemMetricsRemoteService remoteService = engineService.getSystemMetricsRemoteService();
            response.getMap().putAll(remoteService.getSystemMetrics());
            return response;
        } catch (Exception ex) {
            throw new ServiceException("You can not get the system metrics of Flamingo Engine", ex);
        }
    }

}