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
package org.opencloudengine.flamingo2.web.fs;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.fs.audit.FileSystemAuditRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.model.rest.*;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * File System Audit REST Controller.
 *
 * @author Jae Hee, Lee
 * @author Myeong Ha, KIM
 * @since 2.0
 */
@Controller
@RequestMapping("/fs/audit")
public class AuditController extends DefaultController {

    /**
     * 지정한 조건의 파일 처리 이력을 조회한다.
     * <p/>
     * auditConditionMap 처리 이력 조건 정보
     *
     * @return 파일 처리 목록
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response getAuditHistories(@RequestParam String clusterName,
                                      @RequestParam(defaultValue = "") String startDate,
                                      @RequestParam(defaultValue = "") String endDate,
                                      @RequestParam(defaultValue = "") String path,
                                      @RequestParam(defaultValue = "ALL") String auditType,
                                      @RequestParam(defaultValue = "0") int nextPage,
                                      @RequestParam(defaultValue = "10") int limit) {

        EngineService engineService = this.getEngineService(clusterName);
        FileSystemAuditRemoteService service = engineService.getFileSystemAuditRemoteService();
        int level = getSessionUserLevel();
        String username = level == 1 ? "" : getSessionUsername();
        int totalRows = service.getTotalCountOfAuditHistories(startDate, endDate, path, auditType, username);

        Map auditConditionMap = new HashMap();

        auditConditionMap.put("level", level);
        auditConditionMap.put("username", username);
        auditConditionMap.put("startDate", startDate);
        auditConditionMap.put("endDate", endDate);
        auditConditionMap.put("path", path);
        auditConditionMap.put("auditType", auditType);
        auditConditionMap.put("nextPage", nextPage);
        auditConditionMap.put("limit", limit);

        List<AuditHistory> auditHistories = service.getAuditHistories(auditConditionMap);

        Response response = new Response();
        response.getList().addAll(auditHistories);
        response.setTotal(totalRows);
        response.setSuccess(true);

        return response;
    }

    /**
     * 지정한 조건의 top10을 조회한다.
     *
     * @param clusterName Hadoop Cluster명
     * @param searchType  조회 유형
     * @param startDate   시작 날짜
     * @param endDate     종료 날짜
     * @return top10 목록
     */
    @RequestMapping(value = "top10", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response top10(@RequestParam(defaultValue = "") String clusterName,
                          @RequestParam(defaultValue = "ACT") String searchType,
                          @RequestParam(defaultValue = "") String startDate,
                          @RequestParam(defaultValue = "") String endDate) {
        EngineService engineService = this.getEngineService(clusterName);
        FileSystemAuditRemoteService service = engineService.getFileSystemAuditRemoteService();
        int level = getSessionUserLevel();
        String username = level == 1 ? "" : getSessionUsername();

        Response response = new Response();
        List<Top10> top10s = service.auditTop10(startDate, endDate, searchType, username);

        response.getList().addAll(top10s);
        response.setTotal(top10s.size());
        response.setSuccess(true);

        return response;
    }

    /**
     * 지정한 조건의 nowStatus를 조회한다.
     *
     * @param clusterName Hadoop Cluster명
     * @param searchType  조회 유형
     * @param startDate   시작 날짜
     * @param endDate     종료 날짜
     * @return top10 목록
     */
    @RequestMapping(value = "nowStatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response nowStatus(@RequestParam(defaultValue = "") String clusterName,
                              @RequestParam(defaultValue = "ACT") String searchType,
                              @RequestParam(defaultValue = "") String startDate,
                              @RequestParam(defaultValue = "") String endDate) {

        EngineService engineService = this.getEngineService(clusterName);
        FileSystemAuditRemoteService service = engineService.getFileSystemAuditRemoteService();
        int level = getSessionUserLevel();
        String username = level == 1 ? "" : getSessionUsername();

        Response response = new Response();
        List<NowStatus> nowStatuses = service.auditNowStatus(startDate, endDate, searchType, username);

        response.getList().addAll(nowStatuses);
        response.setTotal(nowStatuses.size());
        response.setSuccess(true);

        return response;
    }

    /**
     * 선택한 시작 날짜 및 종료 날짜에 해당하는 Audit Log를 조회한다.
     *
     * @param clusterName Hadoop Cluster명
     * @param searchType  조회 유형
     * @param startDate   시작 날짜 (yyyy-MM-dd HH)
     * @param endDate     종료 날짜 (yyyy-MM-dd HH)
     * @return trend 목록
     */
    @RequestMapping(value = "trend", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response trend(@RequestParam(defaultValue = "") String clusterName,
                          @RequestParam(defaultValue = "ACT") String searchType,
                          @RequestParam(defaultValue = "") String startDate,
                          @RequestParam(defaultValue = "") String endDate) {

        EngineService engineService = this.getEngineService(clusterName);
        FileSystemAuditRemoteService service = engineService.getFileSystemAuditRemoteService();
        int level = getSessionUserLevel();
        String username = level == 1 ? "" : getSessionUsername();

        SimpleDateFormat hoursFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        SimpleDateFormat daysFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        List<Trend> trendList;
        Date startTime;
        Date endTime;

        try {
            if ("".equals(startDate) && "".equals(endDate)) {
                calendar.setTime(new Date());
                calendar.add(Calendar.HOUR, -12);
                trendList = trends(hoursFormat, calendar, Calendar.HOUR, 12);
            } else if ("".equals(startDate) && !"".equals(endDate)) {
                calendar.setTime(daysFormat.parse(endDate));
                calendar.add(Calendar.HOUR, -1);
                trendList = trends(hoursFormat, calendar, Calendar.HOUR, 24);
            } else {
                startTime = daysFormat.parse(startDate);
                calendar.setTime(new Date());
                endTime = (endDate.equals("")) ? daysFormat.parse(daysFormat.format(calendar.getTime())) : daysFormat.parse(endDate);
                long difference = (endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24);
                calendar.setTime(startTime);
                calendar.add(Calendar.DATE, -1);
                trendList = trends(daysFormat, calendar, Calendar.DATE, (int) difference + 1);
                calendar.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            throw new ServiceException("Unable to parse the date.", e);
        }

        List<Trends> trendsList = service.auditTrend(startDate, endDate, searchType, username);
        HashMap<String, String> trendTitle = new HashMap<>();

        for (Trends trends : trendsList) {
            String trendsSearchType = trends.getSearchType();
            if (!trendTitle.containsKey(trendsSearchType)) {
                trendTitle.put(trendsSearchType, "data" + (trendTitle.size() + 1));
            }

            /**
             * 날짜가 같은 필드에 데이터 삽입
             */
            for (Trend trend : trendList) {
                if (trend.getTime().equals(trends.getTime())) {
                    Integer position = Integer.parseInt(trendTitle.get(trendsSearchType).replaceAll("[^\\d]", ""));
                    trend.setData(position, trend.getData(position) + trends.getCount());
                }
            }
        }

        Response response = new Response();
        response.getMap().putAll(trendTitle);
        response.getList().addAll(trendList);
        response.setTotal(trendList.size());
        response.setSuccess(true);

        return response;
    }

    private ArrayList<Trend> trends(SimpleDateFormat format, Calendar calendar, int increament, int times) {
        ArrayList<Trend> trends = new ArrayList<>();

        for (int i = 0; i < times; i++) {
            calendar.add(increament, 1);
            trends.add(new Trend(format.format(calendar.getTime()), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        }

        return trends;
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
