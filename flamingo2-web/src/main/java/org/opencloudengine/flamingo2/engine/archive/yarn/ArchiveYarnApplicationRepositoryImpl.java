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
package org.opencloudengine.flamingo2.engine.archive.yarn;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.opencloudengine.flamingo2.model.rest.YarnApplicationHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ArchiveYarnApplicationRepositoryImpl extends PersistentRepositoryImpl<YarnApplicationHistory, Long> implements ArchiveYarnApplicationRepository {

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Autowired
    public ArchiveYarnApplicationRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<Map<String, Object>> getYarnApplicationSummary(Map summaryMap) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectYarnSummary", summaryMap);
    }

    @Override
    public List<YarnApplicationHistory> getAllApplications(Map allAppMap) {
        int page = (int) allAppMap.get("page");
        int limit = (int) allAppMap.get("limit");
        int startRow = 0;

        /**
         * SQL 조회 방법
         * 한 페이지 당 10 (limit) 개 행 : 조회 범위 -> 0 ~ 9 행, startSQLRecord : 0
         *
         * Case 1 : 첫 페이지
         * Case 2 : 이전/다음/랜덤 페이지
         */
        if (page == 0) {
            allAppMap.put("startRow", startRow);
        } else {
            startRow = (page * limit) - limit;
            allAppMap.put("startRow", startRow);
        }

        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectAllApplications", allAppMap);
    }

    @Override
    public int getTotalCountOfYarnAllApplications(Map allAppMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectTotalCountByUserLevel", allAppMap);
    }

    @Override
    public Map<String, Object> getApplicationReport(Map archiveMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectApplicationReport", archiveMap);
    }
}