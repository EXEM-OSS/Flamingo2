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
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Pivotal HAWQ Editor Controller.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
@Controller
@RequestMapping("/hawq/editor")
public class HawqEditorController extends DefaultController {

    private Logger logger = LoggerFactory.getLogger(HawqEditorController.class);

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    @ResponseBody
    public Response execute(@RequestBody Map<String, Object> params, HttpSession session) {
        Response response = new Response();
        params.put("websocketKey", session.getAttribute("websocketKey"));

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            this.getEngineService(clusterName).getHawqService().getQueryResultAsync(params);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get a result of HAWQ Query", ex);
        }

        return response;
    }

    @RequestMapping(value = "/viewPlan", method = RequestMethod.POST)
    @ResponseBody
    public Response viewPlan(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.getList().addAll(this.getEngineService(clusterName).getHawqService().getQueryPlan(params));
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to get a plan of HAWQ Query", ex);
        }

        return response;
    }

    @RequestMapping(value = "/killSession", method = RequestMethod.POST)
    @ResponseBody
    public Response killSession(@RequestBody Map<String, Object> params) {
        Response response = new Response();

        try {
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            params.put("engineConfig", engineConfig);
            response.setObject(this.getEngineService(clusterName).getHawqService().killSession(params));
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("Unable to kill a session", ex);
        }

        return response;
    }

    @RequestMapping(value = "/downloadResult", method = RequestMethod.POST)
    public ResponseEntity downloadResult(@RequestParam Map<String, Object> params, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();

        try {
            String clusterName = params.get("clusterName").toString();
            byte[] bytes = this.getEngineService(clusterName).getHawqService().getResultToCsv(params);

            res.setHeader("Content-Length", "" + bytes.length);
            res.setHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            res.setHeader("Content-Disposition", MessageFormatter.format("form-data; name={}; filename={}", "test", "test.csv").getMessage());
            res.setStatus(200);
            FileCopyUtils.copy(bytes, res.getOutputStream());
            res.flushBuffer();
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex) {
            logger.warn("Unable to download a result of HAWQ Query", ex);
            headers.set("message", ex.getMessage());
            if (ex.getCause() != null) headers.set("cause", ex.getCause().getMessage());
            return new ResponseEntity(headers, HttpStatus.BAD_REQUEST);
        }
    }
}