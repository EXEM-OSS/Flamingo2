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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/menu")
public class MenuController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MenuService service;

    @RequestMapping(value = "select", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Map<String, Object>> select(@RequestParam Map<String, Object> params) {
        try {
            params.put("locale", ConfigurationHelper.getHelper().get("default.locale"));
            params.put("level", SessionUtils.getLevel());
            return service.select(params);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "selectNode", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response selectNode(@RequestParam Map<String, Object> params) {

        Response response = new Response();
        try {
            params.put("locale", ConfigurationHelper.getHelper().get("default.locale"));
            response.getList().addAll(service.selectNode(params));
            response.setTotal(response.getList().size());
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            logger.debug(ex.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response save(@RequestBody Map<String, Object> params) {
        Response response = new Response();
        try {

            ArrayList newRecords = objectMapper.readValue(params.get("newRecords").toString(), ArrayList.class);
            ArrayList updatedRecords = objectMapper.readValue(params.get("updatedRecords").toString(), ArrayList.class);
            ArrayList removedRecords = objectMapper.readValue(params.get("removedRecords").toString(), ArrayList.class);

            for (Object record : newRecords) {
                service.insert((Map<String, Object>) record);
            }

            for (Object record : updatedRecords) {
                service.update((Map<String, Object>) record);
            }

            for (Object record : removedRecords) {
                service.delete((Map<String, Object>) record);
            }

            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            logger.debug(ex.getMessage());
        }
        return response;
    }
}