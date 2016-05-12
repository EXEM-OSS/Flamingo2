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
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/monitoring/alarm")
public class AlarmController extends DefaultController {

    @Autowired
    AlarmService service;

    @RequestMapping(value = "/getAlarm", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getAlarm(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);

            response.getMap().putAll(service.getAlarm(engineConfig));
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
        }
        return response;
    }
}
