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
package org.opencloudengine.flamingo2.web.archive;

import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.archive.yarn.ArchiveYarnRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.model.rest.YarnApplicationHistory;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHolder;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.remote.EngineLookupService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Archive Yarn Application REST Controller
 *
 * @author Myeongha KIM
 * @since 2.0
 */
@RestController
@RequestMapping("/archive/yarn")
public class ArchiveYarnController {

    /**
     * Remote Repository에서 Yarn Application Summary Chart 정보를 조회한다.
     *
     * @param clusterName 클러스터명
     * @param startDate   시작일
     * @param endDate     종료일
     * @param filter      조회유형
     * @return summaryMap
     */
    @RequestMapping(value = "summary", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getYarnApplicationSummary(@RequestParam String clusterName,
                               @RequestParam(defaultValue = "") String startDate,
                               @RequestParam(defaultValue = "") String endDate,
                               @RequestParam(defaultValue = "ALL") String filter) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveYarnRemoteService ayars = engineService.getArchiveYarnApplicationRemoteService();
        String username = getSessionUsername();
        int userLevel = getSessionUserLevel();

        Map summaryMap = new HashMap();
        summaryMap.put("username", username);
        summaryMap.put("userLevel", userLevel);
        summaryMap.put("clusterName", clusterName);
        summaryMap.put("startDate", startDate);
        summaryMap.put("endDate", endDate);
        summaryMap.put("filter", filter);

        Response response = new Response();
        response.getList().addAll(ayars.getYarnApplicationSummary(summaryMap));
        response.setSuccess(true);
        return response;
    }

    /**
     * Remote Repository에서 모든 Archive Yan Application을 조회한다.
     *
     * @param clusterName 클러스터명
     * @param startDate   시작일
     * @param endDate     종료일
     * @param filter      조회유형
     * @param appName     애플리케이션명
     * @param page        페이지
     * @param limit       그리드 사이즈
     * @return allAppMap
     */
    @RequestMapping(value = "allApplications", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getAllApplications(@RequestParam String clusterName,
                                       @RequestParam(defaultValue = "") String startDate,
                                       @RequestParam(defaultValue = "") String endDate,
                                       @RequestParam(defaultValue = "ALL") String filter,
                                       @RequestParam(defaultValue = "") String appName,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int limit) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveYarnRemoteService ayars = engineService.getArchiveYarnApplicationRemoteService();
        String username = getSessionUsername();
        int userLevel = getSessionUserLevel();

        Map allAppMap = new HashMap();
        allAppMap.put("username", username);
        allAppMap.put("userLevel", userLevel);
        allAppMap.put("clusterName", clusterName);
        allAppMap.put("startDate", startDate);
        allAppMap.put("endDate", endDate);
        allAppMap.put("filter", filter);
        allAppMap.put("appName", appName);
        allAppMap.put("page", page);
        allAppMap.put("limit", limit);

        int totalRows = ayars.getTotalCountOfYarnAllApplications(allAppMap);

        Response response = new Response();
        response.getList().addAll(ayars.getAllApplications(allAppMap));
        response.setTotal(totalRows);
        response.setSuccess(true);
        return response;
    }

    /**
     * 그리드에서 선택한 Applcation의 상세 정보를 Remote Repository에서 가져온다.
     *
     * @param clusterName   클러스터명
     * @param applicationId Application ID
     * @return applicationReportMap
     */
    @RequestMapping(value = "appReport", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Response getApplicationReport(@RequestParam String clusterName, @RequestParam String applicationId) {
        EngineConfig engineConfig = ConfigurationHolder.getEngine(clusterName);
        EngineService engineService = EngineLookupService.lookup(engineConfig);
        ArchiveYarnRemoteService ayars = engineService.getArchiveYarnApplicationRemoteService();

        Map archiveMap = new HashMap();
        archiveMap.put("clusterName", clusterName);
        archiveMap.put("applicationId", applicationId);

        Map applicationReportMap = ayars.getApplicationReport(archiveMap);

        Response response = new Response();
        response.getMap().putAll(applicationReportMap);
        response.setSuccess(true);
        return response;
    }

    /**
     * 현재 세션의 사용자명을 가져온다.
     *
     * @return username
     */
    private String getSessionUsername() {
        return SessionUtils.getUsername();
    }

    /**
     * 현재 세션의 사용자 등급 정보를 가져온다.
     *
     * @return level
     */
    private int getSessionUserLevel() {
        return SessionUtils.getLevel();
    }
}
