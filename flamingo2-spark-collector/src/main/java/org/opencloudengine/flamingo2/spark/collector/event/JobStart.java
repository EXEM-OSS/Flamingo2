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
import org.opencloudengine.flamingo2.spark.collector.event.element.StageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 8..
 */
public class JobStart implements Serializable {
    @SerializedName ("Job ID") private Integer jobID;
    @SerializedName ("Submission Time") private Long submissionTime;
    @SerializedName ("Stage Infos") private List<StageInfo> stageInfos;
    @SerializedName ("Stage IDs") private List<Integer> stageIDs;
    @SerializedName ("Properties") private Map properties;

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public Long getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Long submissionTime) {
        this.submissionTime = submissionTime;
    }

    public List<StageInfo> getStageInfos() {
        return stageInfos;
    }

    public void setStageInfos(List<StageInfo> stageInfos) {
        this.stageInfos = stageInfos;
    }

    public List<Integer> getStageIDs() {
        return stageIDs;
    }

    public void setStageIDs(List<Integer> stageIDs) {
        this.stageIDs = stageIDs;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }
}






