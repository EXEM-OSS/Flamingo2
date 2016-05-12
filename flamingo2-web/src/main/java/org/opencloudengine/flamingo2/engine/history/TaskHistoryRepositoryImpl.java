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
package org.opencloudengine.flamingo2.engine.history;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskHistoryRepositoryImpl extends PersistentRepositoryImpl<TaskHistory, Long> implements TaskHistoryRepository {

    @Override
    public String getNamespace() {
        return this.NAMESPACE;
    }

    @Autowired
    public TaskHistoryRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<TaskHistory> selectByIdentifier(String identifier) {
        return this.getSqlSessionTemplate().selectList(this.NAMESPACE + ".selectByIdentifier", identifier);
    }

    @Override
    public TaskHistory selectByTaskIdAndIdentifier(TaskHistory taskHistory) {
        return this.getSqlSessionTemplate().selectOne(this.NAMESPACE + ".selectByTaskIdAndIdentifier", taskHistory);
    }

    @Override
    public void updateByTaskIdAndIdentifier(TaskHistory taskHistory) {
        this.getSqlSessionTemplate().update(this.NAMESPACE + ".updateByTaskIdAndIdentifier", taskHistory);
    }
}