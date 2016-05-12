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
package org.opencloudengine.flamingo2.engine.fs.audit;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.opencloudengine.flamingo2.model.rest.AuditHistory;
import org.opencloudengine.flamingo2.model.rest.NowStatus;
import org.opencloudengine.flamingo2.model.rest.Top10;
import org.opencloudengine.flamingo2.model.rest.Trends;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.opencloudengine.flamingo2.util.StringUtils.isEmpty;

@Repository
public class AuditRepositoryImpl extends PersistentRepositoryImpl<AuditHistory, Long> implements AuditRepository {

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Autowired
    public AuditRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<AuditHistory> selectByCondition(Map auditConditionMap) {
        int nextPage = (int) auditConditionMap.get("nextPage");
        int limit = (int) auditConditionMap.get("limit");
        int startRow = 0;

        /**
         * SQL 조회 방법
         * 한 페이지 당 10 (limit) 개 행 : 조회 범위 -> 0 ~ 9 행, startSQLRecord : 0
         *
         * Case 1 : 첫 페이지
         * Case 2 : 이전/다음/랜덤 페이지
         */
        if (nextPage == 0) {
            auditConditionMap.put("startRow", startRow);
        } else {
            startRow = (nextPage * limit) - limit;
            auditConditionMap.put("startRow", startRow);
        }

        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectByCondition", auditConditionMap);
    }

    @Override
    public int getTotalCountByCondition(String startDate, String endDate, String path, String auditType, String username) {
        Map<String, String> params = new HashMap<>();

        if (!isEmpty("startDate")) params.put("startDate", startDate);
        if (!isEmpty("endDate")) params.put("endDate", endDate);
        if (!isEmpty("path")) params.put("path", path);
        if (!isEmpty("auditType")) params.put("auditType", auditType);
        if (!isEmpty("username")) params.put("username", username);

        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".getTotalCountByUsername", params);
    }

    @Override
    public List<Top10> auditTop10(String startDate, String endDate, String searchType, String username) {
        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("username", username);

        switch (searchType) {
            case "USER":
                params.put("searchType", "USERNAME");
                break;
            case "ACT":
                params.put("searchType", "AUDIT_TYPE");
                break;
            case "FILE_TYPE":
                params.put("searchType", "FILE_TYPE");
                break;
            case "METHOD":
                params.put("searchType", "REQ_TYPE");
                break;
        }

        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectTop10", params);
    }

    @Override
    public List<NowStatus> auditNowStatus(String startDate, String endDate, String searchType, String username) {
        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("username", username);

        switch (searchType) {
            case "USER":
                params.put("searchType", "USERNAME");
                break;
            case "ACT":
                params.put("searchType", "AUDIT_TYPE");
                break;
            case "FILE_TYPE":
                params.put("searchType", "FILE_TYPE");
                break;
            case "METHOD":
                params.put("searchType", "REQ_TYPE");
                break;
        }

        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectNowStatus", params);
    }

    @Override
    public List<Trends> auditTrend(String startDate, String endDate, String searchType, String username) {
        Map<String, String> conditionMap = new HashMap<>();
        conditionMap.put("startDate", startDate);
        conditionMap.put("endDate", endDate);
        conditionMap.put("username", username);

        switch (searchType) {
            case "USER":
                conditionMap.put("searchType", "USERNAME");
                break;
            case "ACT":
                conditionMap.put("searchType", "AUDIT_TYPE");
                break;
            case "FILE_TYPE":
                conditionMap.put("searchType", "FILE_TYPE");
                break;
            case "METHOD":
                conditionMap.put("searchType", "REQ_TYPE");
                break;
        }

        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectTrend", conditionMap);
    }

    /**
     * 입력한 시작 날짜 및 종료 날짜에 SimpleDateFormat 형태로 변환한다.
     * DB에는 yyyy-MM-dd HH 형태로 들어 있기 때문에 yyyy-MM-dd 형태로 조회해야 함.
     *
     * @param startDate 시작 날짜 (yyyy-MM-dd)
     * @param endDate   죵료 날짜 (yyyy-MM-dd)
     * @return 변환된 날짜 정보
     */
    private String[] calcDate(String startDate, String endDate) {
        Calendar calendar = Calendar.getInstance();
        String dateFormat[] = {"%Y-%m-%d %H", "%Y-%m-%d", "%Y-%m", "%Y"};
        SimpleDateFormat hoursFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        SimpleDateFormat daysFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat monthsFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat yearsFormat = new SimpleDateFormat("yyyy");
        Date startTime;
        Date endTime;
        String sqlDateFormat;
        String params[] = new String[3];

        try {
            if ("".equals(startDate) && "".equals(endDate)) {
                sqlDateFormat = dateFormat[0];

                calendar.setTime(new Date());
                calendar.add(Calendar.HOUR, -11);
                startDate = hoursFormat.format(calendar.getTime());

                calendar.add(Calendar.HOUR, 12);
                endDate = hoursFormat.format(calendar.getTime());
            } else {
                startTime = yearsFormat.parse(startDate);
                endTime = yearsFormat.parse(endDate);

//                long difference = (endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24);

                int compareDate = startTime.compareTo(endTime);

                System.out.println("compareDate :: " + compareDate);
                calendar.setTime(new Date());
                calendar.setTime(startTime);

                if (compareDate <= 0) {
                    startDate = hoursFormat.format(startTime);
                    sqlDateFormat = dateFormat[0];
                    calendar.add(Calendar.HOUR, 24);
                    endDate = hoursFormat.format(calendar.getTime());
                } else if (compareDate > 0 && compareDate <= 31) {
                    startDate = daysFormat.format(startTime);
                    sqlDateFormat = dateFormat[1];
                    calendar.add(Calendar.DATE, compareDate + 1);
                    endDate = daysFormat.format(calendar.getTime());
                } else if (compareDate > 31 && compareDate <= 365) {
                    startDate = monthsFormat.format(startTime);
                    sqlDateFormat = dateFormat[2];
                    calendar.add(Calendar.MONTH, (compareDate / 30) + 1);
                    endDate = monthsFormat.format(calendar.getTime());
                } else {
                    startDate = yearsFormat.format(startTime);
                    sqlDateFormat = dateFormat[3];
                    calendar.add(Calendar.YEAR, (compareDate / 30 / 12) + 1);
                    endDate = yearsFormat.format(calendar.getTime());
                }
            }
        } catch (ParseException e) {
            throw new ServiceException("Unable to parse the date.", e);
        }

        params[0] = startDate;
        params[1] = endDate;
        params[2] = sqlDateFormat;

        return params;
    }
}