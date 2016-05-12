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

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.hive.HiveQueryRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.logging.StringUtils;
import org.opencloudengine.flamingo2.util.EscapeUtils;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hive/query")
public class QueryController extends DefaultController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    private EngineService engineService;

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public Response execute(@RequestBody Map params, HttpServletRequest request) {
        Response response = new Response();
        HttpSession session = request.getSession();

        String uuid = params.get("uuid").toString();
        String query = EscapeUtils.unescape((String) params.get("query"));
        String clusterName = params.get("clusterName").toString();

        EngineConfig engineConfig = this.getEngineConfig(clusterName);

        params.put("websocketKey", session.getAttribute("websocketKey"));
        params.put("hiveserver2Url", engineConfig.getHiveServerUrl());
        params.put("hiveserver2Username", engineConfig.getHiveServerUsername());
        params.put("engineConfig", engineConfig);

        response.setSuccess(true);
        params.put("username", SessionUtils.getUsername());

        HiveQueryRemoteService hiveQueryRemoteService = engineService.getHiveQueryService();
        try {
            hiveQueryRemoteService.validateQuery(query);
        } catch (Exception ex) {
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            response.setSuccess(false);
            response.getError().setMessage("Invalid Hive Query");
            return response;
        }

        boolean exists = hiveQueryRemoteService.isExist(SessionUtils.getUsername());
        if (exists) {
            try {
                boolean end = hiveQueryRemoteService.isEnd(SessionUtils.getUsername());
                if (!end) {
                    response.setSuccess(false);
                    response.getError().setMessage("Hive Query is already running. Please running again after completion.");
                    return response;
                }
            } catch (Exception ex) {
                // 종료 여부를 확인할 수 없으므로 세션이 종료되었으니 삭제한다.
                response.setSuccess(false);
            }
        }

        hiveQueryRemoteService.remove(uuid);

        //hiveQueryRemoteService.executeAsync(params);
        hiveQueryRemoteService.execute(params);
        logger.debug("Hive Query : \n{}", query);
        return response;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Response cancel(@RequestBody Map params, HttpServletRequest request) {
        Response response = new Response();

        try {
            String uuid = params.get("uuid").toString();
            String clusterName = params.get("clusterName").toString();
            EngineConfig engineConfig = this.getEngineConfig(clusterName);
            HiveQueryRemoteService hiveQueryRemoteService = engineService.getHiveQueryService();
            hiveQueryRemoteService.remove(uuid);
            response.setSuccess(true);
        } catch (Exception ex) {
            throw new ServiceException("You can not terminate the query.", ex);
        }
        return response;
    }

    @RequestMapping(value = "/getLog", method = RequestMethod.GET)
    public Response getLog(@RequestParam Map params, HttpServletRequest request) {
        Response response = new Response();

        try {
            response.setSuccess(true);
            HiveQueryRemoteService hiveQueryRemoteService = engineService.getHiveQueryService();
            String error = hiveQueryRemoteService.getErrorLog(params.get("uuid").toString());
            if (!StringUtils.isEmpty(error)) {
                // 에러인 경우는 에러 메시지를 보낸다.
                response.getMap().put("end", true);
                response.getMap().put("log", EscapeUtils.escape(error));
            } else {
                // 정상 처리한 경우 2가지 경우가 발생한다. 장시간 동작하는 쿼리의 경우와 즉시 나오는 경우로써
                // 장시간 걸리는 로그의 경우 log 메시지가 발생한다.
                String log = hiveQueryRemoteService.getLog(params.get("uuid").toString());
                boolean isEnd = hiveQueryRemoteService.isEnd(params.get("uuid").toString());
                response.getMap().put("end", isEnd);
                response.getMap().put("log", EscapeUtils.escape(log));
                response.getMap().put("jobEnd", org.springframework.util.StringUtils.countOccurrencesOf(log, "Ended Job") > 0);
            }
        } catch (Exception ex) {
            response.setSuccess(false);
        }

        return response;
    }

    @RequestMapping(value = "/getLogAsync", method = RequestMethod.POST)
    public Response getLogAsync(@RequestBody Map params, HttpServletRequest request) {
        Response response = new Response();
        HttpSession session = request.getSession();
        String clusterName = params.get("clusterName").toString();
        EngineConfig engineConfig = this.getEngineConfig(clusterName);

        HiveQueryRemoteService hiveQueryRemoteService = engineService.getHiveQueryService();
        params.put("websocketKey", session.getAttribute("websocketKey"));
        params.put("engineConfig", engineConfig);

        hiveQueryRemoteService.getLogAsync(params);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public Response getResults(@RequestParam Map params) {
        Response response = new Response();
        try {
            HiveQueryRemoteService hiveQueryRemoteService = engineService.getHiveQueryService();
            Map[] results = hiveQueryRemoteService.getResults(params.get("uuid").toString());
            List<Map> maps = Arrays.asList(results);
            response.setSuccess(true);
            response.getList().addAll(maps);
        } catch (Exception ex) {
            response.setSuccess(false);
        }

        return response;
    }

    @RequestMapping(value = "/getPage", method = RequestMethod.GET)
    public Response getPage(@RequestParam Map params) {
        Response response = new Response();
        HiveQueryRemoteService hiveQueryRemoteService = engineService.getHiveQueryService();

        Map[] results = hiveQueryRemoteService.getPage(params);
        List<Map> maps = Arrays.asList(results);
        response.getList().addAll(maps);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/downloadResult", method = RequestMethod.GET)
    public ResponseEntity downloadResult(@RequestParam Map params, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();

        try {
            HiveQueryRemoteService hiveQueryRemoteService = engineService.getHiveQueryService();
            byte[] bytes = hiveQueryRemoteService.getResultToCsv(params.get("uuid").toString());

            res.setHeader("Content-Length", "" + bytes.length);
            res.setHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            res.setHeader("Content-Disposition", MessageFormatter.format("form-data; name={}; filename={}", "test", "test.csv").getMessage());
            res.setStatus(200);
            FileCopyUtils.copy(bytes, res.getOutputStream());
            res.flushBuffer();
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex) {
            logger.warn("Unable to download a result of Hive Query", ex);
            headers.set("message", ex.getMessage());
            if (ex.getCause() != null) headers.set("cause", ex.getCause().getMessage());
            return new ResponseEntity(headers, HttpStatus.BAD_REQUEST);
        }
    }
}
