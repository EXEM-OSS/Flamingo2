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
package org.opencloudengine.flamingo2.web.hawq;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

/**
 * Pivotal HAWQ Auth Controller.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
@Controller
@RequestMapping("/hawq/auth")
public class HawqAuthController extends DefaultController {

    private Logger logger = LoggerFactory.getLogger(HawqAuthController.class);

    @RequestMapping(value = "/resourceQueues", method = RequestMethod.GET)
    @ResponseBody
    public Response resourceQueues(@RequestParam Map<String, Object> params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getRQueues(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get resource queues", ex);
        }
        return response;
    }

    @RequestMapping(value = "/resourceQueue", method = RequestMethod.GET)
    @ResponseBody
    public Response resourceQueue(@RequestParam Map<String, Object> params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.setObject(this.getEngineService(clusterName).getHawqService().getRQueue(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get a resource queue", ex);
        }
        return response;
    }

    @RequestMapping(value = "/createResourceQueue", method = RequestMethod.POST)
    @ResponseBody
    public Response createResourceQueue(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().createResourceQueue(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to create a resource queue", ex);
        }

        return response;
    }

    @RequestMapping(value = "/dropResourceQueue", method = RequestMethod.POST)
    @ResponseBody
    public Response dropResourceQueue(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().dropResourceQueue(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to drop a resource queue", ex);
        }

        return response;
    }

    @RequestMapping(value = "/groupRoles", method = RequestMethod.GET)
    @ResponseBody
    public Response groupRoles(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getGroupRoles(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get group roles", ex);
        }

        return response;
    }

    @RequestMapping(value = "/loginRoles", method = RequestMethod.GET)
    @ResponseBody
    public Response loginRoles(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getLoginRoles(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get login roles", ex);
        }

        return response;
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    @ResponseBody
    public Response role(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.setObject(this.getEngineService(clusterName).getHawqService().role(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get a role", ex);
        }

        return response;
    }

    @RequestMapping(value = "/createRole", method = RequestMethod.POST)
    @ResponseBody
    public Response createRole(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().createRole(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to create a role", ex);
        }

        return response;
    }

    @RequestMapping(value = "/alterRole", method = RequestMethod.POST)
    @ResponseBody
    public Response alterRole(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().alterRole(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to alter a role", ex);
        }

        return response;
    }

    @RequestMapping(value = "/dropRole", method = RequestMethod.POST)
    @ResponseBody
    public Response dropRole(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().dropRole(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to drop a role", ex);
        }

        return response;
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    @ResponseBody
    public Response sessions(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getSessions(params));
            response.setStart(Integer.parseInt(params.get("start").toString()));
            response.setLimit(Integer.parseInt(params.get("limit").toString()));
            response.setTotal(this.getEngineService(clusterName).getHawqService().getSessionsTotal(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get sessions", ex);
        }

        return response;
    }

    @RequestMapping(value = "/lockTables", method = RequestMethod.GET)
    @ResponseBody
    public Response lockTables(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getLockTables(params));
            response.setStart(Integer.parseInt(params.get("start").toString()));
            response.setLimit(Integer.parseInt(params.get("limit").toString()));
            response.setTotal(this.getEngineService(clusterName).getHawqService().getLockTablesTotal(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to lock tables", ex);
        }

        return response;
    }

}