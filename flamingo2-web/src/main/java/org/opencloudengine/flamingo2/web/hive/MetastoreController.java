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
package org.opencloudengine.flamingo2.web.hive;

import org.apache.thrift.TException;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.hive.HiveMetastoreService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.system.HdfsBrowserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hive/metastore")
public class MetastoreController extends DefaultController {

    @Autowired
    private HdfsBrowserAuthService hdfsBrowserAuthService;

    @RequestMapping(value = "/databases", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getDatabases(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);

            HiveMetastoreService service = engineService.getHiveMetastoreServcice();

            response.getList().addAll(service.getAllDatabases(engineConfig));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (TException ex) {
            throw new ServiceException("Unable to get databases", ex);
        }
        return response;
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getTables(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            String database = params.get("database").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            response.getList().addAll(service.getTables(engineConfig, database));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get tables", ex);
        }
        return response;
    }

    @RequestMapping(value = "/columns", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getColumns(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            String database = params.get("database").toString();
            String table = params.get("table").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            response.getList().addAll(service.getColumns(engineConfig, database, table));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get columns", ex);
        }
        return response;
    }

    @RequestMapping(value = "/partitions", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getPartitions(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            String database = params.get("database").toString();
            String table = params.get("table").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            response.getList().addAll(service.getPartitions(engineConfig, database, table));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get partitions", ex);
        }
        return response;
    }

    @RequestMapping(value = "/createDB", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response createDB(@RequestBody Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            String username = getSessionUsername();

            if (Boolean.valueOf(params.get("external").toString())) {
                List<String> paths = hdfsBrowserAuthService.getHdfsBrowserPatternAll(username);
                String hdfsAuthPattern = hdfsBrowserAuthService.validateHdfsPathPattern((String) params.get("location"), paths);
                params.put("username", username);
                params.put("hdfsPathPattern", hdfsAuthPattern);
                params.put("condition", "createDbDir");
                hdfsBrowserAuthService.getHdfsBrowserUserDirAuth(params);
            }

            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            service.createDatabase(engineConfig, params);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to create a database", ex);
        }
        return response;
    }

    @RequestMapping(value = "/dropDB", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response dropDB(@RequestBody Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            String database = params.get("database").toString();

            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            service.dropDatabase(engineConfig, database);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to drop a database", ex);
        }
        return response;
    }

    @RequestMapping(value = "/createTable", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response createTable(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            String username = getSessionUsername();

            if (params.get("tableType").toString().equals("EXTERNAL_TABLE")) {
                List<String> paths = hdfsBrowserAuthService.getHdfsBrowserPatternAll(username);
                String hdfsAuthPattern = hdfsBrowserAuthService.validateHdfsPathPattern((String) params.get("location"), paths);
                params.put("username", username);
                params.put("hdfsPathPattern", hdfsAuthPattern);
                params.put("condition", "createTableDir");
                hdfsBrowserAuthService.getHdfsBrowserUserDirAuth(params);
            }

            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            service.createTable(engineConfig, params);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to create a table", ex);
        }
        return response;
    }

    @RequestMapping(value = "/dropTable", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response dropTable(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            String database = params.get("database").toString();
            String table = params.get("table").toString();

            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            service.dropTable(engineConfig, database, table);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to drop a table", ex);
        }
        return response;
    }

    @RequestMapping(value = "/tableInfo", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getTableInfo(@RequestParam Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            String database = params.get("database").toString();
            String table = params.get("table").toString();

            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            Map returnMap = service.getTableInfo(engineConfig, database, table);
            response.setMap(returnMap);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get a table", ex);
        }
        return response;
    }

    @RequestMapping(value = "/alterTable", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response alterTable(@RequestBody Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();

            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            HiveMetastoreService service = engineService.getHiveMetastoreServcice();
            service.alterTable(engineConfig, params);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to alter a table", ex);
        }
        return response;
    }

    /**
     * 현재 세션의 사용자명을 가져온다.
     *
     * @return username 로그인한 사용자명
     */
    private String getSessionUsername() {
        return SessionUtils.getUsername();
    }
}



