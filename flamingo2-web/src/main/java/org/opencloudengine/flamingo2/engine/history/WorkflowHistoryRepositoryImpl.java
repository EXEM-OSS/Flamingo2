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
import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.opencloudengine.flamingo2.util.StringUtils.isEmpty;

@Repository
public class WorkflowHistoryRepositoryImpl extends PersistentRepositoryImpl<WorkflowHistory, Long> implements WorkflowHistoryRepository {

    @Override
    public String getNamespace() {
        return this.NAMESPACE;
    }

    @Autowired
    public WorkflowHistoryRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public void updateCurrentStep(WorkflowHistory workflowHistory) {
        this.getSqlSessionTemplate().update(this.getNamespace() + ".updateCurrentStep", workflowHistory);
    }

    @Override
    public WorkflowHistory selectByJobId(String jobId) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectByJobId", jobId);
    }

    @Override
    public WorkflowHistory selectByIdentifier(String identifier) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectByIdentifier", identifier);
    }

    @Override
    public List<WorkflowHistory> selectByCondition(String startDate, String endDate, int start, int limit, String username, String workflowName, String status, String sf_parentIdentifier) {
        Map<String, java.io.Serializable> params = new HashMap<>();
        if (!isEmpty("startDate")) params.put("startDate", startDate);
        if (!isEmpty("endDate")) params.put("endDate", endDate);
        if (!isEmpty("start")) params.put("start", start);
        if (!isEmpty("limit")) params.put("limit", limit);
        if (!isEmpty("username")) params.put("username", username);
        if (!isEmpty("workflowName")) params.put("workflowName", workflowName);
        if (!isEmpty("status")) params.put("status", status);
        if (!isEmpty("sf_parentIdentifier")) params.put("sf_parentIdentifier", sf_parentIdentifier);

        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectByCondition", params);
    }

    @Override
    public int selectTotalCountByUsername(String startDate, String endDate, int start, int limit, String username, String workflowName, String status, String sf_parentIdentifier) {
        Map<String, java.io.Serializable> params = new HashMap<>();
        if (!isEmpty("startDate")) params.put("startDate", startDate);
        if (!isEmpty("endDate")) params.put("endDate", endDate);
        if (!isEmpty("username")) params.put("username", username);
        if (!isEmpty("workflowName")) params.put("workflowName", workflowName);
        if (!isEmpty("status")) params.put("status", status);
        if (!isEmpty("sf_parentIdentifier")) params.put("sf_parentIdentifier", sf_parentIdentifier);

        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".totalCount", params);
    }

    @Override
    public void updateStatus(WorkflowHistory workflowHistory) {
        this.getSqlSessionTemplate().update(this.getNamespace() + ".updateStatus", workflowHistory);
    }
}
