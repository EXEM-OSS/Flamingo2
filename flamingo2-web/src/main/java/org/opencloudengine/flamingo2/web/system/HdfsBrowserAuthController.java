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

import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * File System Browser Authority REST Controller
 *
 * @author Myeongha KIM
 * @since 2.0
 */
@RestController
@RequestMapping("/system/authority")
public class HdfsBrowserAuthController extends DefaultController {

    @Autowired
    private HdfsBrowserAuthService hdfsBrowserAuthService;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(HdfsBrowserAuthController.class);

    /**
     * HDFS 파일시스템의 트리 경로에 각각 설정된 브라우저의 기능 사용 권한 목록을 가져온다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "hdfsBrowserAuthList", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getHdfsAuthAll() {
        List<Map> hdfsBrowserAuthList = hdfsBrowserAuthService.getHdfsAuthAll();

        Response response = new Response();
        response.getList().addAll(hdfsBrowserAuthList);
        response.setTotal(response.getList().size());
        response.setSuccess(true);
        return response;
    }

    /**
     * HDFS 권한 설정 그리드에서 선택한 패턴의 상세 정보를 가져온다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "hdfsBrowserAuthDetail", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response getHdfsBrowserAuthDetail(@RequestBody Map hdfsAuthMap) {
        Map hdfsAuthDetailMap = hdfsBrowserAuthService.getHdfsBrowserAuthDetail(hdfsAuthMap);

        Response response = new Response();
        response.getMap().putAll(hdfsAuthDetailMap);
        response.setSuccess(true);
        return response;
    }

    /**
     * 사용자 권한 목록을 가져온다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "userAuthAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getUserAuthAll() {
        List<Map> userAuthList = hdfsBrowserAuthService.getUserAuthAll();

        Response response = new Response();
        response.getList().addAll(userAuthList);
        response.setTotal(response.getList().size());
        response.setSuccess(true);
        return response;
    }

    /**
     * 사용자 등급 목록을 가져온다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "userLevelAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getUserLevelAll() {
        List<Map> userAuthList = hdfsBrowserAuthService.getUserLevelAll();

        Response response = new Response();
        response.getList().addAll(userAuthList);
        response.setTotal(response.getList().size());
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "createHdfsBrowserAuth", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response createHdfsBrowserAuth(@RequestBody Map hdfsBrowserAuthMap) {
        boolean createHdfsAuth = hdfsBrowserAuthService.createHdfsBrowserAuth(hdfsBrowserAuthMap);

        Response response = new Response();
        response.setSuccess(createHdfsAuth);
        return response;
    }

    @RequestMapping(value = "deleteHdfsBrowserAuth", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response deleteHdfsBrowserAuth(@RequestBody Map hdfsBrowserAuthMap) {
        boolean deletedHdfsAuth = hdfsBrowserAuthService.deleteHdfsBrowserAuth(hdfsBrowserAuthMap);

        Response response = new Response();
        response.setSuccess(deletedHdfsAuth);
        return response;
    }

    @RequestMapping(value = "updateHdfsBrowserAuth", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Response updateHdfsBrowserAuth(@RequestBody Map hdfsBrowserAuthMap) {
        boolean updatedHdfsAuth = hdfsBrowserAuthService.updateHdfsBrowserAuth(hdfsBrowserAuthMap);

        Response response = new Response();
        response.setSuccess(updatedHdfsAuth);
        return response;
    }
}