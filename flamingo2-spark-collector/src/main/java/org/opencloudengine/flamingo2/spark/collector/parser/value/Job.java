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
package org.opencloudengine.flamingo2.spark.collector.parser.value;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class Job implements Serializable {

    private String appID;

    private Integer jobID;

    private String jobName;

    private Long submitted;

    private Long completed;

    private Integer stages = 0;

    private Integer stageSkipped = 0;

    private Integer stageCompleted = 0;

    private Integer stageFailed = 0;

    private Integer tasks = 0;

    private Integer taskSkipped = 0;

    private Integer taskCompleted = 0;

    private Integer taskFailed = 0;

    private String description;

    private String status;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public Long getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Long submitted) {
        this.submitted = submitted;
    }

    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public Integer getStages() {
        return stages;
    }

    public void setStages(Integer stages) {
        this.stages = stages;
    }

    public Integer getStageSkipped() {
        return stageSkipped;
    }

    public void setStageSkipped(Integer stageSkipped) {
        this.stageSkipped = stageSkipped;
    }

    public Integer getStageCompleted() {
        return stageCompleted;
    }

    public void setStageCompleted(Integer stageCompleted) {
        this.stageCompleted = stageCompleted;
    }

    public Integer getStageFailed() {
        return stageFailed;
    }

    public void setStageFailed(Integer stageFailed) {
        this.stageFailed = stageFailed;
    }

    public Integer getTasks() {
        return tasks;
    }

    public void setTasks(Integer tasks) {
        this.tasks = tasks;
    }

    public Integer getTaskSkipped() {
        return taskSkipped;
    }

    public void setTaskSkipped(Integer taskSkipped) {
        this.taskSkipped = taskSkipped;
    }

    public Integer getTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(Integer taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public Integer getTaskFailed() {
        return taskFailed;
    }

    public void setTaskFailed(Integer taskFailed) {
        this.taskFailed = taskFailed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
