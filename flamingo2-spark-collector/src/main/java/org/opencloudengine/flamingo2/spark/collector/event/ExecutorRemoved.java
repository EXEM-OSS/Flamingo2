package org.opencloudengine.flamingo2.spark.collector.event;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 20..
 */
public class ExecutorRemoved implements Serializable {
    @SerializedName ("Timestamp") private Long timestamp;
    @SerializedName ("Executor ID") private String executorID;
    @SerializedName ("Removed Reason") private String removedReason;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExecutorID() {
        return executorID;
    }

    public void setExecutorID(String executorID) {
        this.executorID = executorID;
    }

    public String getRemovedReason() {
        return removedReason;
    }

    public void setRemovedReason(String removedReason) {
        this.removedReason = removedReason;
    }
}
