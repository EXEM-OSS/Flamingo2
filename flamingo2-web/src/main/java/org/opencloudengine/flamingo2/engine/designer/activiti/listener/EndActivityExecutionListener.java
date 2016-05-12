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

import org.activiti.engine.delegate.DelegateExecution;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRepository;
import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.Set;

public class EndActivityExecutionListener extends DefaultServiceTaskExecutionListener {

    /**
     * SLF4J Logging
     */
    public Logger logger = LoggerFactory.getLogger(StartActivityExecutionListener.class);

    @Override
    public void onNotify(DelegateExecution execution) throws Exception {
        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
        WorkflowHistoryRepository workflowHistoryRepository = context.getBean(WorkflowHistoryRepository.class);
        WorkflowHistory wf = (WorkflowHistory) execution.getVariable("wh");
        workflowHistoryRepository.updateCurrentStep(wf);

        if (logger.isDebugEnabled()) {
            Set<String> names = execution.getVariableNames();
            for (String name : names) {
                logger.debug("[Activity Variables] {}={}", name, execution.getVariable(name));
            }
        }

        Date endTime = new Date();
        execution.setVariableLocal("endTime", endTime);
    }
}