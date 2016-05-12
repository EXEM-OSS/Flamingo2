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
package org.opencloudengine.flamingo2.engine.scheduler;

import org.opencloudengine.flamingo2.engine.backend.UserEventRepository;
import org.opencloudengine.flamingo2.engine.designer.GlobalAttributes;
import org.opencloudengine.flamingo2.engine.designer.activiti.WorkflowService;
import org.opencloudengine.flamingo2.engine.designer.activiti.task.Transformer;
import org.opencloudengine.flamingo2.engine.history.TaskHistoryRepository;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRepository;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import java.util.Properties;

public abstract class DefaultQuartzJob implements Job, WorkflowJob {

    WorkflowService workflowService;

    GlobalAttributes globalAttributes;

    Properties config;

    WorkflowHistoryRepository workflowHistoryRepository;

    JobExecutionContext jobExecutionContext;

    UserEventRepository eventRepository;

    TaskHistoryRepository taskHistoryRepository;

    Transformer transformer;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();

        this.jobExecutionContext = jobExecutionContext;
        this.workflowService = context.getBean(WorkflowService.class);
        this.globalAttributes = context.getBean(GlobalAttributes.class);
        this.config = context.getBean("config", Properties.class);
        this.workflowHistoryRepository = context.getBean(WorkflowHistoryRepository.class);
        this.transformer = context.getBean(Transformer.class);
        this.taskHistoryRepository = context.getBean(TaskHistoryRepository.class);
        this.eventRepository = context.getBean(UserEventRepository.class);

        executeInternal();
    }

}
