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
package org.opencloudengine.flamingo2.engine.realtime.spark.streaming;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.opencloudengine.flamingo2.model.rest.IoTService;
import org.opencloudengine.flamingo2.model.rest.SparkStreaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SparkStreamingRepositoryImpl extends PersistentRepositoryImpl<SparkStreaming, Long> implements SparkStreamingRepository {

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Autowired
    public SparkStreamingRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<IoTService> selectIoTServices(Map iotMap) {
        String columnsType = String.valueOf(iotMap.get("columnsType"));

        if (columnsType.equalsIgnoreCase("data") || columnsType.equalsIgnoreCase("custom")) {
            return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectIoTServiceColumns", iotMap);
        } else {
            return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectIoTServices", iotMap);
        }
    }

    @Override
    public int selectTotalCountOfSparkStreamingApps(Map sparkStreamingMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectTotalCountByUserLevel", sparkStreamingMap);
    }

    @Override
    public List<SparkStreaming> selectAllSparkStreamingApps(Map sparkStreamingMap) {
        int page = (int) sparkStreamingMap.get("page");
        int limit = (int) sparkStreamingMap.get("limit");
        int startRow = 0;

        /**
         * SQL 조회 방법
         * 한 페이지 당 10 (limit) 개 행 : 조회 범위 -> 0 ~ 9 행, startSQLRecord : 0
         *
         * Case 1 : 첫 페이지
         * Case 2 : 이전/다음/랜덤 페이지
         */
        if (page == 0) {
            sparkStreamingMap.put("startRow", startRow);
        } else {
            startRow = (page * limit) - limit;
            sparkStreamingMap.put("startRow", startRow);
        }

        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectAllSparkStreamingApps", sparkStreamingMap);
    }

    @Override
    public List<Map<String, Object>> selectAllSparkStreamingAppsSummary(Map sparkStreamingMap) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectRunningSparkStreamingCPUUsage", sparkStreamingMap);
    }

    @Override
    public int insertSparkStreamingApp(Map sparkStreamingMap) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertSparkStreamingApp", sparkStreamingMap);
    }

    @Override
    public boolean updateSparkStreamingApp(Map sparkStreamingMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateSparkStreamingApp", sparkStreamingMap) > 0;
    }

    @Override
    public boolean deleteSparkStreamingApp(String applicationId) {
        return this.getSqlSessionTemplate().delete(this.getNamespace() + ".deleteSparkStreamingApp", applicationId) > 0;
    }

    @Override
    public int selectOptimalServer(String agent) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectOptimalServer", agent);
    }
}
