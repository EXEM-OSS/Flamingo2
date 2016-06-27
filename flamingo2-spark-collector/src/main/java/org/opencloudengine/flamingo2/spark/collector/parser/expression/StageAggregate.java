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
package org.opencloudengine.flamingo2.spark.collector.parser.expression;

import com.google.gson.Gson;
import org.opencloudengine.flamingo2.spark.collector.event.*;
import org.opencloudengine.flamingo2.spark.collector.event.element.StageInfo;
import org.opencloudengine.flamingo2.spark.collector.event.element.TaskMetrics;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Stage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class StageAggregate implements Serializable {
    private List<String> stageIDs;

    private Map<String, Stage> stageMap;

    private Map<Integer, Integer> stageJobMap;

    public StageAggregate() {
        stageIDs = new ArrayList<>();
        stageMap = new HashMap<>();
        stageJobMap = new HashMap<>();
    }

    public List<String> getStageIDs() {
        return stageIDs;
    }

    public void setStageIDs(List<String> stageIDs) {
        this.stageIDs = stageIDs;
    }

    public Map<String, Stage> getStageMap() {
        return stageMap;
    }

    public void setStageMap(Map<String, Stage> stageMap) {
        this.stageMap = stageMap;
    }

    public void add(JobStart jobStart) {
        for (StageInfo stageInfo : jobStart.getStageInfos()) {
            String stageID = stageInfo.getStageID() + "-" + stageInfo.getStageAttemptID();
            stageIDs.add(stageID);
            Stage stage = new Stage();
            stage.setStageID(stageInfo.getStageID());
            stage.setAttemptID(stageInfo.getStageAttemptID());
            stage.setJobID(jobStart.getJobID());
            stageMap.put(stageID, stage);
            stageJobMap.put(stageInfo.getStageID(), jobStart.getJobID());
        }
    }

    public void add(StageSubmitted stageSubmitted) {
        String stageID = stageSubmitted.getStageInfo().getStageID() + "-" + stageSubmitted.getStageInfo().getStageAttemptID();
        Stage stage = null;
        if (stageSubmitted.getStageInfo().getStageAttemptID() > 0) {
            stage = new Stage();
            stage.setStageID(stageSubmitted.getStageInfo().getStageID());
            stage.setAttemptID(stageSubmitted.getStageInfo().getStageAttemptID());
            stage.setJobID(stageJobMap.get(stageSubmitted.getStageInfo().getStageID()));
            stageIDs.add(stageID);
        } else {
            stage = stageMap.get(stageID);
        }

        stage.setName(stageSubmitted.getStageInfo().getStageName());
        stage.setDetails(stageSubmitted.getStageInfo().getDetails());
        stage.isSkip = false;
        stageMap.put(stageID, stage);
    }

    public void add(StageCompleted stageCompleted) {
        String stageID = stageCompleted.getStageInfo().getStageID() + "-" + stageCompleted.getStageInfo().getStageAttemptID();
        Stage stage = stageMap.get(stageID);

        stage.setSubmitted(stageCompleted.getStageInfo().getSubmissionTime());
        stage.setCompleted(stageCompleted.getStageInfo().getCompletionTime());
        stage.isSkip = false;

        if (stageCompleted.getStageInfo().getFailureReason() != null) {
            stage.setStatus("Failed");
        } else {
            stage.setStatus("Succeeded");
        }

        stageMap.put(stageID, stage);
    }

    public void add(TaskStart taskStart) {
        String stageID = taskStart.getStageID() + "-" + taskStart.getStageAttemptID();
        Stage stage = stageMap.get(stageID);

        stage.setTasks(stage.getTasks() + 1);

        stageMap.put(stageID, stage);
    }

    public void add(TaskEnd taskEnd) {
        String stageID = taskEnd.getStageID() + "-" + taskEnd.getStageAttemptID();
        Stage stage = stageMap.get(stageID);
        TaskMetrics taskMetrics = taskEnd.getTaskMetrics();

        if (taskEnd.getTaskEndReason().get("Reason").equals("Success")) {
            stage.setTaskComplete(stage.getTaskComplete() + 1);
        } else {
            stage.setTaskFailed(stage.getTaskFailed() + 1);
        }

        if (taskMetrics == null) {
            return;
        }

        if (taskMetrics.getInputMetrics() != null) {
            stage.setInputBytes(stage.getInputBytes() + taskMetrics.getInputMetrics().getBytesRead());
            stage.setInputRecords(stage.getInputRecords() + taskMetrics.getInputMetrics().getRecordsRead());
        }

        if (taskMetrics.getOutputMetrics() != null) {
            stage.setOutputBytes(stage.getOutputBytes() + taskMetrics.getOutputMetrics().getBytesWritten());
            stage.setOutputRecords(stage.getOutputRecords() + taskMetrics.getOutputMetrics().getRecordsWritten());
        }

        if (taskMetrics.getShuffleReadMetrics() != null) {
            stage.setShuffleReadBytes(stage.getShuffleReadBytes() + taskMetrics.getShuffleReadMetrics().getTotalBytesRead());
            stage.setShuffleReadRecords(stage.getShuffleReadRecords() + taskMetrics.getShuffleReadMetrics().getTotalRecordsRead());
        }

        if (taskMetrics.getShuffleWriteMetrics() != null) {
            stage.setShuffleWriteBytes(stage.getShuffleWriteBytes() + taskMetrics.getShuffleWriteMetrics().getShuffleBytesWritten());
            stage.setShuffleWriteRecords(stage.getShuffleWriteRecords() + taskMetrics.getShuffleWriteMetrics().getShuffleRecordsWritten());
        }

        stageMap.put(stageID, stage);
    }

    public String getJson() {
        Gson gson = new Gson();

        return gson.toJson(stageMap);
    }
}
