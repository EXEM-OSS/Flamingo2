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
package org.opencloudengine.flamingo2.web.visual;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.engine.visual.VisualService;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 시각화 관련 UI 기능을 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/visual")
public class VisualController extends DefaultController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(VisualController.class);

    @RequestMapping(value = "/loadHdfs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response loadHdfs(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineService engineService = this.getEngineService(clusterName);

            VisualService service = engineService.getVisualService();
            Map resultMap = service.loadHdfs(params);

            if ((boolean) resultMap.get("success")) {
                response.setSuccess(true);
                response.getMap().putAll(resultMap);
            } else {
                response.setSuccess(false);
            }
        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            logger.info(ex.toString());
        }
        return response;
    }

    @RequestMapping(value = "/listVariablesHdfs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response listVariablesHdfs(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineService engineService = this.getEngineService(clusterName);

            VisualService service = engineService.getVisualService();
            Map resultMap = service.listVariablesHdfs(params);

            if ((boolean) resultMap.get("success")) {
                response.setSuccess(true);
                response.getMap().putAll(resultMap);
            } else {
                response.setSuccess(false);
            }

        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            logger.info(ex.toString());
        }
        return response;
    }

    @RequestMapping(value = "/createPng", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response createPng(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineService engineService = this.getEngineService(clusterName);

            VisualService service = engineService.getVisualService();
            Map resultMap = service.createPng(params);

            if ((boolean) resultMap.get("success")) {
                response.setSuccess(true);
                response.getMap().putAll(resultMap);
            } else {
                response.setSuccess(false);
            }

        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            logger.info(ex.toString());
        }
        return response;
    }

    /**
     * 파일을 업로드한다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> upload(HttpServletRequest req) throws IOException {
        Response response = new Response();

        if (!(req instanceof DefaultMultipartHttpServletRequest)) {
            response.setSuccess(false);
            response.getError().setCause("Invalid Request.");
            response.getError().setMessage("Invalid Request.");
            String json = new ObjectMapper().writeValueAsString(response);
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }

        try {
            DefaultMultipartHttpServletRequest request = (DefaultMultipartHttpServletRequest) req;
            logger.debug("Uploaded File >> Path : {}, Filename : {}, Size: {} bytes", new Object[]{
                    request.getParameter("path"),
                    request.getFile("file").getOriginalFilename(),
                    request.getFile("file").getSize()
            });

            String clusterName = request.getParameter("clusterName");
            Map params = new HashMap();

            EngineService engineService = this.getEngineService(clusterName);
            VisualService service = engineService.getVisualService();
            Map resultMap = service.saveFile(request.getFile("file"), request.getParameter("options"));

            response.getMap().putAll(resultMap);
            response.setSuccess(true);
            String json = new ObjectMapper().writeValueAsString(response);
            HttpStatus statusCode = HttpStatus.OK;
            return new ResponseEntity(json, statusCode);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));

            String json = new ObjectMapper().writeValueAsString(response);
            HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

            logger.debug(ExceptionUtils.getFullStackTrace(ex));

            return new ResponseEntity(json, statusCode);
        }
    }

    @RequestMapping(value = "/listVariablesLocal", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response listVariablesLocal(@RequestBody Map params) {

        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineService engineService = this.getEngineService(clusterName);

            VisualService service = engineService.getVisualService();
            Map resultMap = service.listVariablesLocal(params);

            if ((boolean) resultMap.get("success")) {
                response.setSuccess(true);
                response.getMap().putAll(resultMap);
            } else {
                response.setSuccess(false);
            }

        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            logger.info(ex.toString());
        }
        return response;
    }

    @RequestMapping(value = "/reloadData", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response reloadData(@RequestParam Map params) {
        Response response = new Response();
        try {
            String clusterName = params.get("clusterName").toString();
            EngineService engineService = this.getEngineService(clusterName);

            VisualService service = engineService.getVisualService();
            Map resultMap = service.reloadData(params);

            if ((boolean) resultMap.get("success")) {
                response.setSuccess(true);
                response.getMap().putAll(resultMap);
            } else {
                response.setSuccess(false);
            }

        } catch (IOException ex) {
            throw new ServiceException("You can not reload data.", ex);
        }
        return response;
    }
}
