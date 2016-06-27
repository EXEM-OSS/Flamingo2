package org.opencloudengine.flamingo2.spark.collector.parser.value;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 20..
 */
public class Timeline implements Serializable {
    private String appID;

    private Integer jobID;

    private Integer stageID;

    private Integer attemptID;

    private String type;

    private String json;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
