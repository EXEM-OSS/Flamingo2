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
package org.opencloudengine.flamingo2.engine.designer.activiti;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class WorkflowRepositoryImpl extends PersistentRepositoryImpl<Map, Map> implements WorkflowRepository {

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Autowired
    public WorkflowRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public Map selectByWid(String processId) {
        return this.getSqlSessionTemplate().selectOne(NAMESPACE + ".selectByWid", processId);
    }

    @Override
    public boolean deleteByWid(String processId) {
        return this.getSqlSessionTemplate().delete(NAMESPACE + ".deleteByWid", processId) > 0;
    }

    @Override
    public boolean deleteByTreeId(long treeId) {
        return this.getSqlSessionTemplate().delete(NAMESPACE + ".deleteByTreeId", treeId) > 0;
    }

    @Override
    public Workflow selectByTreeId(long treeId) {
        return this.getSqlSessionTemplate().selectOne(NAMESPACE + ".selectByTreeId", treeId);
    }

    @Override
    public Workflow selectByWorkflowId(String workflowId) {
        return this.getSqlSessionTemplate().selectOne(NAMESPACE + ".selectByWorkflowId", workflowId);
    }

    @Override
    public boolean rename(Map map) {
        return this.getSqlSessionTemplate().update(NAMESPACE + ".updateWorkflowName", map) > 0;
    }

}