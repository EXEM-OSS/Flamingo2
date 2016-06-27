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
public class StageInfo implements Serializable {
    @SerializedName ("Stage ID") private Integer stageID;
    @SerializedName ("Stage Attempt ID") private  Integer stageAttemptID;
    @SerializedName ("Stage Name") private  String stageName;
    @SerializedName ("Number of Tasks") private Integer numberofTasks;
    @SerializedName ("RDD Info") private List<RDDInfo> rDDInfo;
    @SerializedName ("Parent IDs") private List<Integer> parentIDs;
    @SerializedName ("Details") private String details;
    @SerializedName ("Submission Time") private Long submissionTime;
    @SerializedName ("Completion Time") private Long completionTime;
    @SerializedName ("Failure Reason") private String failureReason = null;
    @SerializedName ("Accumulables") private List accumulables;

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

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getNumberofTasks() {
        return numberofTasks;
    }

    public void setNumberofTasks(Integer numberofTasks) {
        this.numberofTasks = numberofTasks;
    }

    public List<RDDInfo> getrDDInfo() {
        return rDDInfo;
    }

    public void setrDDInfo(List<RDDInfo> rDDInfo) {
        this.rDDInfo = rDDInfo;
    }

    public List<Integer> getParentIDs() {
        return parentIDs;
    }

    public void setParentIDs(List<Integer> parentIDs) {
        this.parentIDs = parentIDs;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Long submissionTime) {
        this.submissionTime = submissionTime;
    }

    public Long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Long completionTime) {
        this.completionTime = completionTime;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public List getAccumulables() {
        return accumulables;
    }

    public void setAccumulables(List accumulables) {
        this.accumulables = accumulables;
    }

    public Boolean isFailure() {
        if (this.failureReason == null) {
            return false;
        }
        return true;
    }
}












