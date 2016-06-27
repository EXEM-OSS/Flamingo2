/*
 * Copyright (C) 2011 Flamingo Project (https://github.com/OpenCloudEngine/flamingo2).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.spark.collector.event;

import com.google.gson.annotations.SerializedName;
import org.opencloudengine.flamingo2.spark.collector.event.element.TaskInfo;
import org.opencloudengine.flamingo2.spark.collector.event.element.TaskMetrics;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 8..
 */
public class TaskEnd implements Serializable {
    @SerializedName ("Stage ID") private Integer stageID;
    @SerializedName ("Stage Attempt ID") private Integer stageAttemptID;
    @SerializedName ("Task Type") private String taskType;
    @SerializedName ("Task End Reason") private Map taskEndReason;
    @SerializedName ("Task Info") private TaskInfo taskInfo;
    @SerializedName ("Task Metrics") private TaskMetrics taskMetrics;

    public Integer getStageID() {
        return stageID;
    }

    public void setStageID(Integer stageID) {
        this.stageID = stageID;
    }

    public Integer getStageAttemptID() {
        return stageAttemptID;
    }

    public void setStageAttemptID(Integer stageAttemptID) {
        this.stageAttemptID = stageAttemptID;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Map getTaskEndReason() {
        return taskEndReason;
    }

    public void setTaskEndReason(Map taskEndReason) {
        this.taskEndReason = taskEndReason;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public TaskMetrics getTaskMetrics() {
        return taskMetrics;
    }

    public void setTaskMetrics(TaskMetrics taskMetrics) {
        this.taskMetrics = taskMetrics;
    }
}