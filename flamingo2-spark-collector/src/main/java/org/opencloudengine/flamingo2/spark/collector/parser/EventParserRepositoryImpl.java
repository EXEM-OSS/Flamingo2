/*
 * Copyright (C) 2011 Flamingo Project (https://github.com/OpenCloudEngine/flamingo2).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.spark.collector.parser;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.spark.collector.common.repository.PersistentRepositoryImpl;
import org.opencloudengine.flamingo2.spark.collector.parser.value.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
@Repository
public class EventParserRepositoryImpl extends PersistentRepositoryImpl<String, Object> implements EventParserRepository {

    @Override
    public String getNamespace() {
        return this.NAMESPACE;
    }

    @Autowired
    public EventParserRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<Map> selectApp(Map params) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectApp", params);
    }

    @Override
    public int countApp(Map params) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".countApp", params);
    }

    @Override
    public void insertApps(Apps apps) {
        this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertApps", apps);
    }

    @Override
    public void insertJob(Job job) {
        this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertJob", job);
    }

    @Override
    public void insertStage(Stage stage) {
        this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertStage", stage);
    }

    @Override
    public void insertStageDetail(Map params) {
        this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertStageDetail", params);
    }

    @Override
    public void insertExecutor(Executor executor) {
        this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertExecutor", executor);
    }

    @Override
    public void insertStorage(Storage storage) {
        this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertStorage", storage);
    }

    @Override
    public void insertTimeline(Timeline timeline) {
        this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertTimeline", timeline);
    }
}
