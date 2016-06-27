package org.opencloudengine.flamingo2.web.tajo;

import org.apache.thrift.TException;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.hadoop.NamenodeRemoteService;
import org.opencloudengine.flamingo2.engine.hive.HiveMetastoreService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.tajo.TajoRemoteService;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Park on 15. 7. 14..
 */
@RestController
@RequestMapping("/tajo")
public class TajoController extends DefaultController {

    @RequestMapping(value = "/databases", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getDatabases(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);

            TajoRemoteService service = engineService.getTajoRemoteService();

            response.getList().addAll(service.getAllDatabases(engineConfig));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get databases", ex);
        }
        return response;
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getTables(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);

            TajoRemoteService service = engineService.getTajoRemoteService();

            response.getList().addAll(service.getTables(engineConfig, params.get("database").toString()));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get tables", ex);
        }
        return response;
    }

    @RequestMapping(value = "/columns", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getColumns(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);

            TajoRemoteService service = engineService.getTajoRemoteService();

            response.getList().addAll(service.getColumns(engineConfig, params.get("database").toString(), params.get("table").toString()));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get tables", ex);
        }
        return response;
    }

    @RequestMapping(value = "/partitions", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getPartitions(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);

            TajoRemoteService service = engineService.getTajoRemoteService();

            response.getList().addAll(service.getPartitions(engineConfig, params.get("database").toString(), params.get("table").toString()));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get tables", ex);
        }
        return response;
    }

    @RequestMapping(value = "/createDB", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response createDB(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            TajoRemoteService service = engineService.getTajoRemoteService();
            service.createDatabase(engineConfig, params);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to create a Tajo Table", ex);
        }
        return response;
    }

    @RequestMapping(value = "/dropDB", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response dropDB(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            TajoRemoteService service = engineService.getTajoRemoteService();
            service.dropDatabase(engineConfig, params.get("database").toString());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to drop a Tajo Database", ex);
        }
        return response;
    }

    @RequestMapping(value = "/createTable", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response createTable(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            TajoRemoteService service = engineService.getTajoRemoteService();
            NamenodeRemoteService namenodeRemoteService = engineService.getNamenodeRemoteService();
            Map namenodeInfo = namenodeRemoteService.getNamenodeInfo(engineConfig);

            params.put("hostName", namenodeInfo.get("hostName"));
            params.put("port", namenodeInfo.get("port"));

            service.createTable(engineConfig, params);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to create a Tajo Table", ex);
        }
        return response;
    }

    @RequestMapping(value = "/dropTable", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response dropTable(@RequestBody Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            TajoRemoteService service = engineService.getTajoRemoteService();
            service.dropTable(engineConfig, params.get("database").toString(), params.get("table").toString());
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to drop a Tajo Database", ex);
        }
        return response;
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public Response execute(@RequestBody Map params, HttpServletRequest request) {
        Response response = new Response();
        HttpSession session = request.getSession();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            TajoRemoteService service = engineService.getTajoRemoteService();

            params.put("websocketKey", session.getAttribute("websocketKey"));
            Map resultMap = service.execute(engineConfig, params);

            if ((boolean) resultMap.get("error")) {
                response.setSuccess(false);
                response.getError().setCause(resultMap.get("cause").toString());
            }
            else {
                response.setSuccess(true);
                response.getMap().putAll(resultMap);
            }


        } catch (Exception ex) {
            throw new ServiceException("Unable to execute a Tajo Query", ex);
        }
        return response;
    }

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getResults(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);

            TajoRemoteService service = engineService.getTajoRemoteService();

            response.getMap().putAll(service.getResults(engineConfig, params));
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get results", ex);
        }
        return response;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Response cancel(@RequestBody Map params, HttpServletRequest request) {
        Response response = new Response();
        HttpSession session = request.getSession();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            EngineService engineService = this.getEngineService(clusterName);
            TajoRemoteService service = engineService.getTajoRemoteService();

            service.closeQuery(engineConfig, params);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to Close a Tajo Query", ex);
        }
        return response;
    }
}
