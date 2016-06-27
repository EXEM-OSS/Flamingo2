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

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class Stage implements Serializable {
    private String appID;

    private Integer jobID;

    private Integer stageID;

    private Integer attemptID;

    private String name;

    private String details;

    private Long submitted = null;

    private Long completed = null;

    private Integer tasks = 0;

    private Integer taskComplete = 0;

    private Integer taskFailed = 0;

    private Long inputBytes = Long.valueOf(0);

    private Long inputRecords = Long.valueOf(0);

    private Long outputBytes = Long.valueOf(0);

    private Long outputRecords = Long.valueOf(0);

    private Long shuffleReadBytes = Long.valueOf(0);

    private Long shuffleReadRecords = Long.valueOf(0);

    private Long shuffleWriteBytes = Long.valueOf(0);

    private Long shuffleWriteRecords = Long.valueOf(0);

    private String status;

    public Boolean isSkip = true;

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

    public Integer getStageID() {
        return stageID;
    }

    public void setStageID(Integer stageID) {
        this.stageID = stageID;
    }

    public Integer getAttemptID() {
        return attemptID;
    }

    public void setAttemptID(Integer attemptID) {
        this.attemptID = attemptID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public Integer getTasks() {
        return tasks;
    }

    public void setTasks(Integer tasks) {
        this.tasks = tasks;
    }

    public Integer getTaskComplete() {
        return taskComplete;
    }

    public void setTaskComplete(Integer taskComplete) {
        this.taskComplete = taskComplete;
    }

    public Integer getTaskFailed() {
        return taskFailed;
    }

    public void setTaskFailed(Integer taskFailed) {
        this.taskFailed = taskFailed;
    }

    public Long getInputBytes() {
        return inputBytes;
    }

    public void setInputBytes(Long inputBytes) {
        this.inputBytes = inputBytes;
    }

    public Long getInputRecords() {
        return inputRecords;
    }

    public void setInputRecords(Long inputRecords) {
        this.inputRecords = inputRecords;
    }

    public Long getOutputBytes() {
        return outputBytes;
    }

    public void setOutputBytes(Long outputBytes) {
        this.outputBytes = outputBytes;
    }

    public Long getOutputRecords() {
        return outputRecords;
    }

    public void setOutputRecords(Long outputRecords) {
        this.outputRecords = outputRecords;
    }

    public Long getShuffleReadBytes() {
        return shuffleReadBytes;
    }

    public void setShuffleReadBytes(Long shuffleReadBytes) {
        this.shuffleReadBytes = shuffleReadBytes;
    }

    public Long getShuffleReadRecords() {
        return shuffleReadRecords;
    }

    public void setShuffleReadRecords(Long shuffleReadRecords) {
        this.shuffleReadRecords = shuffleReadRecords;
    }

    public Long getShuffleWriteBytes() {
        return shuffleWriteBytes;
    }

    public void setShuffleWriteBytes(Long shuffleWriteBytes) {
        this.shuffleWriteBytes = shuffleWriteBytes;
    }

    public Long getShuffleWriteRecords() {
        return shuffleWriteRecords;
    }

    public void setShuffleWriteRecords(Long shuffleWriteRecords) {
        this.shuffleWriteRecords = shuffleWriteRecords;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
