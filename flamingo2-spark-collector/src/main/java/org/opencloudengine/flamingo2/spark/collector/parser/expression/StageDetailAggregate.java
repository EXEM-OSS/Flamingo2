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
import org.opencloudengine.flamingo2.spark.collector.event.StageSubmitted;
import org.opencloudengine.flamingo2.spark.collector.event.TaskEnd;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Executor;
import org.opencloudengine.flamingo2.spark.collector.parser.value.StageDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class StageDetailAggregate implements Serializable {
    private List<String> stageIDs;

    private Map<String, StageDetail> stageDetailMap;

    public StageDetailAggregate() {
        stageIDs = new ArrayList<>();
        stageDetailMap = new HashMap<>();
    }

    public List<String> getStageIDs() {
        return stageIDs;
    }

    public void setStageIDs(List<String> stageIDs) {
        this.stageIDs = stageIDs;
    }

    public Map<String, StageDetail> getStageDetailMap() {
        return stageDetailMap;
    }

    public void setStageDetailMap(Map<String, StageDetail> stageDetailMap) {
        this.stageDetailMap = stageDetailMap;
    }

    public void add(StageSubmitted stageSubmitted) {
        String stageID = stageSubmitted.getStageInfo().getStageID() + "-" + stageSubmitted.getStageInfo().getStageAttemptID();
        stageIDs.add(stageID);
        stageDetailMap.put(stageID, new StageDetail());
    }

    public void add(TaskEnd taskEnd) {
        String stageID = taskEnd.getStageID() + "-" + taskEnd.getStageAttemptID();
        StageDetail stageDetail = stageDetailMap.get(stageID);

        stageDetail.setStageID(taskEnd.getStageID());
        stageDetail.setAttemptID(taskEnd.getStageAttemptID());

        if (taskEnd.getTaskMetrics() == null) {
            return;
        }

        //gettingResultTime
        if (taskEnd.getTaskInfo().getGettingResultTime() > 0) {
            stageDetail.addGettingResultTime(taskEnd.getTaskInfo().getGettingResultTime());
        }

        //executorDeserializeTime
        if (taskEnd.getTaskMetrics().getExecutorDeserializeTime() > 0) {
            stageDetail.addExecutorDeserializeTime(taskEnd.getTaskMetrics().getExecutorDeserializeTime());
        }

        //executorRunTime
        if (taskEnd.getTaskMetrics().getExecutorRunTime() > 0) {
            stageDetail.addExecutorRunTime(taskEnd.getTaskMetrics().getExecutorRunTime());
        }

        //resultSerializationTime
        if (taskEnd.getTaskMetrics().getResultSerializationTime() > 0) {
            stageDetail.addResultSerializationTime(taskEnd.getTaskMetrics().getResultSerializationTime());
        }

        //jvmGCTime
        if (taskEnd.getTaskMetrics().getjVMGCTime() > 0) {
            stageDetail.addJvmGCTime(taskEnd.getTaskMetrics().getjVMGCTime());
        }

        //resultSize
        if (taskEnd.getTaskMetrics().getResultSize() > 0) {
            stageDetail.addResultSize(taskEnd.getTaskMetrics().getResultSize());
        }

        //memoryBytesSpilled
        if (taskEnd.getTaskMetrics().getMemoryBytesSpilled() > 0) {
            stageDetail.addMemoryBytesSpilled(taskEnd.getTaskMetrics().getMemoryBytesSpilled());
        }

        //diskBytesSpilled
        if (taskEnd.getTaskMetrics().getDiskBytesSpilled() > 0) {
            stageDetail.addDiskBytesSpilled(taskEnd.getTaskMetrics().getDiskBytesSpilled());
        }
    }

    public String getJson() {
        Gson gson = new Gson();

        return gson.toJson(stageDetailMap);
    }
}
