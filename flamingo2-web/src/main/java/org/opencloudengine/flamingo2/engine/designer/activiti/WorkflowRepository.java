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

import org.opencloudengine.flamingo2.core.repository.PersistentRepository;
import org.opencloudengine.flamingo2.model.rest.Workflow;

import java.util.Map;

public interface WorkflowRepository extends PersistentRepository<Map, Map> {

    public static final String NAMESPACE = WorkflowRepository.class.getName();

    Map selectByWid(String processId);

    boolean deleteByWid(String processId);

    boolean deleteByTreeId(long treeId);

    Workflow selectByTreeId(long treeId);

    Workflow selectByWorkflowId(String workflowId);

    boolean rename(Map map);
}
