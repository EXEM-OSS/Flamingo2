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

import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;

import java.util.List;

public class WorkflowHistoryRemoteServiceImpl implements WorkflowHistoryRemoteService {

    @Override
    public void updateCurrentStep(WorkflowHistory workflowHistory) {
        WorkflowHistoryRepository workflowHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowHistoryRepository.class);
        workflowHistoryRepository.updateCurrentStep(workflowHistory);
    }

    @Override
    public void updateStatus(WorkflowHistory workflowHistory) {
        WorkflowHistoryRepository workflowHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowHistoryRepository.class);
        workflowHistoryRepository.updateStatus(workflowHistory);
    }

    @Override
    public WorkflowHistory selectByJobId(String jobId) {
        WorkflowHistoryRepository workflowHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowHistoryRepository.class);
        return workflowHistoryRepository.selectByJobId(jobId);
    }

    @Override
    public WorkflowHistory selectByIdentifier(String identifier) {
        WorkflowHistoryRepository workflowHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowHistoryRepository.class);
        return workflowHistoryRepository.selectByIdentifier(identifier);
    }

    @Override
    public WorkflowHistory getWorkflowHistory(String jobId) {
        WorkflowHistoryRepository workflowHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowHistoryRepository.class);
        return workflowHistoryRepository.selectByJobId(jobId);
    }

    @Override
    public List<WorkflowHistory> selectByCondition(String startDate, String endDate, int start, int limit, String username, String workflowName, String status, String sf_parentIdentifier) {
        WorkflowHistoryRepository workflowHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowHistoryRepository.class);
        return workflowHistoryRepository.selectByCondition(startDate, endDate, start, limit, username, workflowName, status, sf_parentIdentifier);
    }

    @Override
    public int selectTotalCountByUsername(String startDate, String endDate, int start, int limit, String username, String workflowName, String status, String sf_parentIdentifier) {
        WorkflowHistoryRepository workflowHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(WorkflowHistoryRepository.class);
        return workflowHistoryRepository.selectTotalCountByUsername(startDate, endDate, start, limit, username, workflowName, status, sf_parentIdentifier);
    }

}
