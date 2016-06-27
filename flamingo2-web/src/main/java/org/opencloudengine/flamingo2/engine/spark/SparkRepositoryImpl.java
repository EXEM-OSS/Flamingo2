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
package org.opencloudengine.flamingo2.engine.spark;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 14..
 */
@Repository
public class SparkRepositoryImpl extends PersistentRepositoryImpl<Object, Object> implements SparkRepository {

    @Autowired
    public SparkRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public String getNamespace() {
        return this.NAMESPACE;
    }

    @Override
    public List<Map> selectJobChart() {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectJobChart");
    }

    @Override
    public List<Map> selectApplications(Map params) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectApplications", params);
    }

    @Override
    public List<Map> selectJobs(Map params) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectJobs", params);
    }

    @Override
    public List<Map> selectStages(Map params) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectStages", params);
    }

    @Override
    public List<Map> selectExecutors(Map params) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectExecutors", params);
    }

    @Override
    public List<Map> selectStorage(Map params) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectStorage", params);
    }

    @Override
    public Map selectTimeline(Map params) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectTimeline", params);
    }

    @Override
    public Map selectStageDetail(Map params) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectStageDetail", params);
    }
}
