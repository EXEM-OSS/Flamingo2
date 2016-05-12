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

import org.uengine.kernel.ProcessInstance;

import java.util.Map;

public interface GlobalAttributes {

    public static final String NAMESPACE = GlobalAttributes.class.getName();

    void registJobResultMap(ProcessInstance instance) throws Exception;

    Map getJobMap(ProcessInstance instance) throws Exception;

    void setJobMap(ProcessInstance instance, Map jobMap) throws Exception;

    String getResolvedJsonAttr(ProcessInstance instance, String taskId, Map params) throws Exception;

    String getTaskStatus(ProcessInstance instance, String taskId, String lookupTaskId) throws Exception;

    void setTaskStatus(ProcessInstance instance, String taskId, String status) throws Exception;

}
