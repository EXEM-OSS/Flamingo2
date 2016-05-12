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

import java.util.Map;

public class WorkflowTask {

    private String taskId;
    private String taskName;
    private String wid;
    private String activiti;
    private Map properties;

    public WorkflowTask() {

    }

    public WorkflowTask(String taskId, String taskName, String wid, String activiti, Map properties) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.wid = wid;
        this.activiti = activiti;
        this.properties = properties;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getActiviti() {
        return activiti;
    }

    public void setActiviti(String activiti) {
        this.activiti = activiti;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "WorkflowTask{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", wid='" + wid + '\'' +
                ", activiti='" + activiti + '\'' +
                ", properties=" + properties +
                '}';
    }
}
