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
package org.opencloudengine.flamingo2.engine.designer;

import org.opencloudengine.flamingo2.model.rest.Workflow;

import java.util.Map;

public interface DesignerService {

    void delete(Map map);

    String loadDesignerXml(Long treeId);

    String loadBpmnXml(Long treeId);

    Workflow getWorkflow(Long treeId);

    Workflow getWorkflow(String workflowId);

    Map<String, Object> save(String parentTreeId, String treeId, String processId, String xml, String username);

    Map<String, Object> copy(Map<String, Object> map, String username);

    String[] idList(String logDirectory, String prefix);

    void killProccess(String logDirectory);

    boolean rename(long treeId, String workflowName);
}
