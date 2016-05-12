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
 * Pivotal HAWQ Browser Controller.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
@Controller
@RequestMapping("/hawq/browser")
public class HawqBrowserController extends DefaultController {

    private Logger logger = LoggerFactory.getLogger(HawqBrowserController.class);

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    @ResponseBody
    public Response connect(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.setObject(this.getEngineService(clusterName).getHawqService().connect(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to connect HAWQ", ex);
        }

        return response;
    }

    @RequestMapping(value = "/databases", method = RequestMethod.GET)
    @ResponseBody
    public Response databases(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getDatabases(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get databases", ex);
        }

        return response;
    }

    @RequestMapping(value = "/schemas", method = RequestMethod.GET)
    @ResponseBody
    public Response schemas(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getSchemas(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get schemas", ex);
        }

        return response;
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    @ResponseBody
    public Response tables(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getTables(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get tables", ex);
        }

        return response;
    }

    @RequestMapping(value = "/views", method = RequestMethod.GET)
    @ResponseBody
    public Response views(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getViews(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get views", ex);
        }

        return response;
    }

    @RequestMapping(value = "/functions", method = RequestMethod.GET)
    @ResponseBody
    public Response functions(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getFunctions(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get functions", ex);
        }

        return response;
    }

    @RequestMapping(value = "/externalTables", method = RequestMethod.GET)
    @ResponseBody
    public Response externalTables(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getExternalTables(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get external tables", ex);
        }

        return response;
    }

    @RequestMapping(value = "/columns", method = RequestMethod.GET)
    @ResponseBody
    public Response columns(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getColumns(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get columns", ex);
        }

        return response;
    }

    @RequestMapping(value = "/objectMetadatas", method = RequestMethod.GET)
    @ResponseBody
    public Response objectMetadatas(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getObjectMetadatas(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get metadatas", ex);
        }

        return response;
    }

    @RequestMapping(value = "/partitionDetail", method = RequestMethod.GET)
    @ResponseBody
    public Response partitionDetail(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.setObject(this.getEngineService(clusterName).getHawqService().getPartitionDetail(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get a partition", ex);
        }

        return response;
    }

    @RequestMapping(value = "/partitionsTree", method = RequestMethod.GET)
    @ResponseBody
    public Response partitionsTree(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getPartitionsTree(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get partition trees", ex);
        }

        return response;
    }

    @RequestMapping(value = "/objectDef", method = RequestMethod.GET)
    @ResponseBody
    public Response objectDef(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.setObject(this.getEngineService(clusterName).getHawqService().getObjectDef(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get object definition", ex);
        }

        return response;
    }

    @RequestMapping(value = "/dropDatabase", method = RequestMethod.POST)
    @ResponseBody
    public Response dropDatabase(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.setObject(this.getEngineService(clusterName).getHawqService().dropDatabase(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to drop a database", ex);
        }

        return response;
    }

    @RequestMapping(value = "/dropSchema", method = RequestMethod.POST)
    @ResponseBody
    public Response dropSchema(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().dropSchema(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to drop a schema", ex);
        }

        return response;
    }

    @RequestMapping(value = "/tablespaces", method = RequestMethod.GET)
    @ResponseBody
    public Response tablespaces(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getTablespaces(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get table space", ex);
        }

        return response;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public Response users(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getUsers(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get users", ex);
        }

        return response;
    }

    @RequestMapping(value = "/createDatabase", method = RequestMethod.POST)
    @ResponseBody
    public Response createDatabase(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().createDatabase(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to create a database", ex);
        }

        return response;
    }

    @RequestMapping(value = "/createSchema", method = RequestMethod.POST)
    @ResponseBody
    public Response createSchema(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().createSchema(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to create a schema", ex);
        }

        return response;
    }

    @RequestMapping(value = "/dropTable", method = RequestMethod.POST)
    @ResponseBody
    public Response dropTable(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().dropTable(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to drop a table", ex);
        }

        return response;
    }

    @RequestMapping(value = "/createTable", method = RequestMethod.POST)
    @ResponseBody
    public Response createTable(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().createTable(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to create a table", ex);
        }

        return response;
    }

    @RequestMapping(value = "/dropExternalTable", method = RequestMethod.POST)
    @ResponseBody
    public Response dropExternalTable(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().dropExternalTable(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to drop a external table", ex);
        }

        return response;
    }

    @RequestMapping(value = "/customFormatter", method = RequestMethod.GET)
    @ResponseBody
    public Response customFormatter(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getCustomFormatter(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get a custom formatter", ex);
        }

        return response;
    }

    @RequestMapping(value = "/createExternalTable", method = RequestMethod.POST)
    @ResponseBody
    public Response createExternalTable(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().createExternalTable(params);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to create a external table", ex);
        }

        return response;
    }

    @RequestMapping(value = "/dropView", method = RequestMethod.POST)
    @ResponseBody
    public Response dropView(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().dropView(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to drop a view", ex);
        }

        return response;
    }

    @RequestMapping(value = "/changeDatabase", method = RequestMethod.GET)
    @ResponseBody
    public Response changeDatabase(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            String defaultSchema = this.getEngineService(clusterName).getHawqService().getDefaultSchema(params);
            response.setObject(defaultSchema);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to change a database", ex);
        }

        return response;
    }

    @RequestMapping(value = "/dropFunction", method = RequestMethod.POST)
    @ResponseBody
    public Response dropFunction(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().dropFunction(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to drop a function", ex);
        }

        return response;
    }

    @RequestMapping(value = "/tableDetail", method = RequestMethod.GET)
    @ResponseBody
    public Response tableDetail(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.setObject(this.getEngineService(clusterName).getHawqService().tableDetail(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get a table detail", ex);
        }

        return response;
    }

    @RequestMapping(value = "/alterTable", method = RequestMethod.POST)
    @ResponseBody
    public Response alterTable(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().alterTable(params);
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to alter a table", ex);
        }

        return response;
    }

    @RequestMapping(value = "/alterColumn", method = RequestMethod.POST)
    @ResponseBody
    public Response alterColumn(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().alterColumn(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to alter a column", ex);
        }

        return response;
    }

    @RequestMapping(value = "/constraints", method = RequestMethod.GET)
    @ResponseBody
    public Response constraints(@RequestParam Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getConstraints(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get contraints", ex);
        }

        return response;
    }

    @RequestMapping(value = "/alterConstraint", method = RequestMethod.POST)
    @ResponseBody
    public Response alterConstraint(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().alterConstraint(params));
            response.setSuccess(true);
        } catch (SQLException ex) {
            throw new ServiceException("Unable to get a contraint", ex);
        }

        return response;
    }
}