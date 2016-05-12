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

import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.uengine.kernel.ProcessInstance;

import java.io.Serializable;
import java.sql.Timestamp;

public class TaskHistory implements Serializable {

    long id;
    String identifier;
    String processId;
    String taskId;
    String cls = "x-tree-noicon";
    String name;
    String vars;
    String username;
    Timestamp startDate;
    Timestamp endDate;
    long duration;
    String year;
    String month;
    String day;
    String status;
    String logDirectory;
    long treeId;

    public TaskHistory() {
    }

    public TaskHistory(String identifier, String taskId) {
        this.identifier = identifier;
        this.taskId = taskId;
    }

    public TaskHistory(Workflow workflow, ProcessInstance instance, String identifier, String taskId, String taskName) throws Exception {
        this.identifier = identifier;
        this.processId = workflow.getWorkflowId();
        this.taskId = taskId;
        this.name = taskName;
        this.username = (String) instance.get("username");
        this.startDate = new Timestamp(System.currentTimeMillis());
        this.duration = 0;
        this.year = DateUtils.parseDate(startDate, "yyyy");
        this.month = DateUtils.parseDate(startDate, "mm");
        this.day = DateUtils.parseDate(startDate, "dd");
        this.status = "RUNNING";
        this.logDirectory = instance.get("logdir") + "/" + taskId;
        this.treeId = (int) workflow.getWorkflowTreeId();
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLogDirectory() {
        return logDirectory;
    }

    public void setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getTreeId() {
        return treeId;
    }

    public void setTreeId(long treeId) {
        this.treeId = treeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVars() {
        return vars;
    }

    public void setVars(String vars) {
        this.vars = vars;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


}