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

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

public class ActivitiUtils {

    public static String getDefinitionId(DelegateExecution execution) {
        return getProcessDefinition(execution).getId();
    }

    public static String getDeploymentId(DelegateExecution execution) {
        return getProcessDefinition(execution).getDeploymentId();
    }

    public static String getTaskId(DelegateExecution execution) {

        ExecutionEntity ee = (ExecutionEntity) execution;
        return ee.getActivity().getId();
    }

    public static String getTaskName(DelegateExecution execution) {
        ExecutionEntity ee = (ExecutionEntity) execution;
        return (String) ee.getActivity().getProperties().get("name");
    }

    public static ProcessDefinition getProcessDefinition(DelegateExecution execution) {
        ExecutionEntity ee = (ExecutionEntity) execution;
        return (ProcessDefinition) ee.getActivity().getProcessDefinition();
    }

    public static ProcessInstance getProcessInstance(DelegateExecution execution) {
        ExecutionEntity ee = (ExecutionEntity) execution;
        return ee.getProcessInstance();
    }

    public static BpmnModel getBpmnModel(RepositoryService repositoryService, String definitionId) {
        return repositoryService.getBpmnModel(definitionId);
    }
}
