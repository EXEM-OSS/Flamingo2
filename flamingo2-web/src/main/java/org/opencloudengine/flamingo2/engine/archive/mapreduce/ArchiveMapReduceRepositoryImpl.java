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
package org.opencloudengine.flamingo2.engine.archive.mapreduce;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.opencloudengine.flamingo2.model.rest.MapReduceHistory;
import org.opencloudengine.flamingo2.model.rest.YarnApplicationHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ArchiveMapReduceRepositoryImpl extends PersistentRepositoryImpl<MapReduceHistory, Long> implements ArchiveMapReduceRepository {

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Autowired
    public ArchiveMapReduceRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<Map<String, Object>> getMapReduceJobSummary(Map summaryMap) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectMapReduceJobSummary", summaryMap);
    }

    @Override
    public List<YarnApplicationHistory> getAllMapReduceJobs(Map allMRJobsMap) {
        int page = (int) allMRJobsMap.get("page");
        int limit = (int) allMRJobsMap.get("limit");
        int startRow = 0;

        /**
         * SQL 조회 방법
         * 한 페이지 당 10 (limit) 개 행 : 조회 범위 -> 0 ~ 9 행, startSQLRecord : 0
         *
         * Case 1 : 첫 페이지
         * Case 2 : 이전/다음/랜덤 페이지
         */
        if (page == 0) {
            allMRJobsMap.put("startRow", startRow);
        } else {
            startRow = (page * limit) - limit;
            allMRJobsMap.put("startRow", startRow);
        }

        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectAllMapReduceJobs", allMRJobsMap);
    }

    @Override
    public int getTotalCountOfAllMapReduceJobs(Map allMRJobsMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectTotalCountByUserLevel", allMRJobsMap);
    }

    @Override
    public Map<String, Object> getMapReduceJobReport(Map archiveMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectMapReduceJobReport", archiveMap);
    }

    @Override
    public Map<String, Object> getMapReduceJobCounters(Map archiveMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectMapReduceJobCounters", archiveMap);
    }

    @Override
    public Map<String, Object> getMapReduceJobConfiguration(Map archiveMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectMapReduceJobConfiguration", archiveMap);
    }

    @Override
    public Map<String, Object> getMapReduceJobTask(Map archiveMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectMapReduceJobTask", archiveMap);
    }
}