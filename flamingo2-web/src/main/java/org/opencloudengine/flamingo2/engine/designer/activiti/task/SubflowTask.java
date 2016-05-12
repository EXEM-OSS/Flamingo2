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
package org.opencloudengine.flamingo2.engine.designer.activiti.task;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.backend.UserEvent;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRepository;
import org.opencloudengine.flamingo2.engine.scheduler.DefaultSubflowJob;
import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.uengine.kernel.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

public class SubflowTask extends InterceptorAbstractTask {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(SubflowTask.class);

    @Override
    public void runTask(final ProcessInstance instance) throws Exception {

        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
        final WorkflowHistoryRepository workflowHistoryRepository = context.getBean(WorkflowHistoryRepository.class);
        DefaultSubflowJob defaultSubflowJob = context.getBean(DefaultSubflowJob.class);

        Map<String, Object> map = new HashMap<>();

        final UserEvent userEvent = UserEvent.create(getUser().getUsername(), "서브 워크플로우 '" + subflowName + "'을 실행중입니다.", "RUNNING");
        map.put("event", userEvent);
        map.put("treeId", subflowTreeId);
        map.put("subflowMergedParams", subflowMergedParams);
        map.put("user", getUser());

        //루트 워크플로우와 부모 워크플로우의 정보를 담고 depth 를 증가시킨다.
        map.put("sf_parentIdentifier", getIdentifier());
        map.put("sf_rootIdentifier", instance.get("sf_rootIdentifier", ""));
        map.put("sf_depth", ((int) instance.get("sf_depth", "") + 1));
        map.put("sf_taskId", getTaskId());

        defaultSubflowJob.excute(map);

        new Thread() {
            @Override
            public void run() {
                String identifier = userEvent.getIdentifier();
                boolean isFinished = false;
                while (!isFinished) {
                    try {
                        synchronized (Thread.currentThread()) {
                            Thread.currentThread().wait(1000);
                        }
                        WorkflowHistory workflowHistory = workflowHistoryRepository.selectByIdentifier(identifier);
                        if (workflowHistory != null) {
                            String status = workflowHistory.getStatus().toString();

                            switch (status) {
                                case "SUCCESS":
                                    postRun(instance);
                                    fireComplete(instance);
                                    isFinished = true;
                                    break;
                                case "FAILED":
                                    updateTaskHistoryAsFailed(instance);
                                    throw new ServiceException("Failed to execute sub workflow \"" + identifier + "\"");
                                case "KILL":
                                    updateTaskHistoryAsFailed(instance);
                                    throw new ServiceException("Forced stop to execute sub workflow \"" + identifier + "\".");
                            }
                        }
                    } catch (Exception ex) {
                        throw new ServiceException("Failed to execute sub workflow \"" + identifier + "\"");
                    }
                }
            }
        }.start();
    }
}
