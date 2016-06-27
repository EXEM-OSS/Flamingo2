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
package org.opencloudengine.flamingo2.spark.collector.event.element;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hyokun Park on 15. 8. 8..
 */
public class TaskInfo implements Serializable {
    @SerializedName ("Task ID") private Integer taskID;
    @SerializedName ("Index") private Integer index;
    @SerializedName ("Attempt") private Integer attempt;
    @SerializedName ("Launch Time") private Long launchTime;
    @SerializedName ("Executor ID") private String executorID;
    @SerializedName ("Host") private String host;
    @SerializedName ("Locality") private String locality;
    @SerializedName ("Speculative") private Boolean speculative;
    @SerializedName ("Getting Result Time") private Long gettingResultTime;
    @SerializedName ("Finish Time") private Long finishTime;
    @SerializedName ("Failed") private Boolean failed;
    @SerializedName ("Accumulables") private List accumulables;

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public Long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Long launchTime) {
        this.launchTime = launchTime;
    }

    public String getExecutorID() {
        return executorID;
    }

    public void setExecutorID(String executorID) {
        this.executorID = executorID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Boolean getSpeculative() {
        return speculative;
    }

    public void setSpeculative(Boolean speculative) {
        this.speculative = speculative;
    }

    public Long getGettingResultTime() {
        return gettingResultTime;
    }

    public void setGettingResultTime(Long gettingResultTime) {
        this.gettingResultTime = gettingResultTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean getFailed() {
        return failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public List getAccumulables() {
        return accumulables;
    }

    public void setAccumulables(List accumulables) {
        this.accumulables = accumulables;
    }
}