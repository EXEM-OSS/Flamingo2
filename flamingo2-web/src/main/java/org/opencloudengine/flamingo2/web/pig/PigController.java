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
package org.opencloudengine.flamingo2.web.pig;

import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.pig.PigRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/pig")
public class PigController extends DefaultController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(PigController.class);

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public Response execute(@RequestBody Map params, HttpServletRequest request) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineService engineService = this.getEngineService(clusterName);
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            PigRemoteService pigRemoteService = engineService.getPigRemoteService();

            HttpSession session = request.getSession();
            params.put("websocketKey", session.getAttribute("websocketKey"));

            pigRemoteService.executeAsync(engineConfig, params);

            response.setSuccess(true);
        } catch (Exception e) {
            response.setSuccess(false);
        }

        return response;
    }
}
