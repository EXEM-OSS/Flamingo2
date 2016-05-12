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
package org.opencloudengine.flamingo2.engine.designer.activiti.listener;

import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ActivityExecutionListener extends DefaultServiceTaskExecutionListener {

    /**
     * SLF4J Logging
     */
    public Logger logger = LoggerFactory.getLogger(ActivityExecutionListener.class);

    @Override
    public void onNotify(DelegateExecution execution) throws Exception {
        if (logger.isDebugEnabled()) {
            Set<String> names = execution.getVariableNames();
            for (String name : names) {
                logger.debug("[Activity Variables] {}={}", name, execution.getVariable(name));
            }
        }

        ExecutionEntity ee = (ExecutionEntity) execution;
        String taskName = (String) ee.getActivity().getProperties().get("name");
        String taskId = ee.getActivity().getId();

        logger.info("Process ID : {}", execution.getProcessInstanceId());
        logger.info("Task ID : {}", taskId);
        logger.info("Task Name : {}", taskName);

        HistoryService historyService = execution.getEngineServices().getHistoryService();
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        logger.info("Task end time: {}", historicTaskInstance.getEndTime());
    }
}