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
package org.opencloudengine.flamingo2.web.system;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.hadoop.ResourceManagerRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/system/license")
public class LicenseController extends DefaultController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(LicenseController.class);

    @Autowired
    LicenseService licenseService;

    @Value("#{config['license.filename']}")
    String licenseFilename;

    @RequestMapping(value = "serverId", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getServerId() {
        Response response = new Response();
        response.getMap().put("serverId", licenseService.getServerId());
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "licenseInfo", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getLicenseInfo(@RequestParam Map params) {
        Response response = new Response();

        try {
            response.getMap().putAll(licenseService.getLicenseInfo());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get License Information", ex);
        }

        return response;
    }

    @RequestMapping(value = "regist", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response regist(@RequestBody Map map) {
        Response response = new Response();

        try {
            licenseService.regist("temp", (String) map.get("license"));

            if (!licenseService.isValid("temp")) {
                response.setSuccess(false);
            } else {
                licenseService.regist(licenseFilename, (String) map.get("license"));
                response.getMap().putAll(licenseService.getLicenseInfo());
                response.setSuccess(true);
            }

        } catch (Exception ex) {
            response.setSuccess(false);
        } finally {
            if (!response.isSuccess()) {
                licenseService.deleteLicense(licenseFilename);
            }
            licenseService.deleteLicense("temp");
        }

        return response;
    }

    @RequestMapping(value = "valid", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response valid(@RequestParam String clusterName) {
        Response response = new Response();
        try {
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            ResourceManagerRemoteService service = engineService.getResourceManagerRemoteService();
            Map<String, Object> resourceManagerInfo = service.getResourceManagerInfo(engineConfig);
            Map<String, Object> cluster = (Map<String, Object>) resourceManagerInfo.get("cluster");

            int nodeSum = Integer.parseInt(cluster.get("lostNodes").toString());
            nodeSum += Integer.parseInt(cluster.get("activeNodes").toString());
            nodeSum += Integer.parseInt(cluster.get("unhealthyNodes").toString());
            nodeSum += Integer.parseInt(cluster.get("decommissionedNodes").toString());
            nodeSum += Integer.parseInt(cluster.get("rebootedNodes").toString());

            int maxNode = Integer.parseInt(licenseService.getMaxNode(licenseFilename));

            if (nodeSum > maxNode) {
                response.getMap().put("isValid", false);
                response.getMap().put("maxNode", maxNode);
            } else {
                response.getMap().put("isValid", true);
            }

            response.setSuccess(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setSuccess(false);
        }

        return response;
    }
}