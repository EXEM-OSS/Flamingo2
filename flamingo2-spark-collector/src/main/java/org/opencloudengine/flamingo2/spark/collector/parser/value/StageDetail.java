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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class StageDetail implements Serializable {
    private String appID;

    private Integer jobID;

    private Integer stageID;

    private Integer attemptID;

    /*Normal distribution*/
    private Map<Integer, Integer> executorDeserializeTime;

    private Map<Integer, Integer> executorRunTime;

    private Map<Integer, Integer> resultSerializationTime;

    private Map<Long, Integer> gettingResultTime;

    private Map<Integer, Integer> jvmGCTime;

    /*Min Max Avg*/
    private AggregateInteger resultSize;

    /*Min Max Avg*/
    private AggregateLong memoryBytesSpilled;

    /*Min Max Avg*/
    private AggregateLong diskBytesSpilled;

    public StageDetail() {
        executorDeserializeTime = new HashMap<>();
        executorRunTime = new HashMap<>();
        resultSerializationTime = new HashMap<>();
        gettingResultTime = new HashMap<>();
        jvmGCTime = new HashMap<>();
        resultSize = new AggregateInteger();
        memoryBytesSpilled = new AggregateLong();
        diskBytesSpilled = new AggregateLong();
    }

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

    public Map<Integer, Integer> getExecutorDeserializeTime() {
        return executorDeserializeTime;
    }

    public void setExecutorDeserializeTime(Map<Integer, Integer> executorDeserializeTime) {
        this.executorDeserializeTime = executorDeserializeTime;
    }

    public Map<Integer, Integer> getExecutorRunTime() {
        return executorRunTime;
    }

    public void setExecutorRunTime(Map<Integer, Integer> executorRunTime) {
        this.executorRunTime = executorRunTime;
    }

    public Map<Integer, Integer> getResultSerializationTime() {
        return resultSerializationTime;
    }

    public void setResultSerializationTime(Map<Integer, Integer> resultSerializationTime) {
        this.resultSerializationTime = resultSerializationTime;
    }

    public Map<Long, Integer> getGettingResultTime() {
        return gettingResultTime;
    }

    public void setGettingResultTime(Map<Long, Integer> gettingResultTime) {
        this.gettingResultTime = gettingResultTime;
    }

    public Map<Integer, Integer> getJvmGCTime() {
        return jvmGCTime;
    }

    public void setJvmGCTime(Map<Integer, Integer> jvmGCTime) {
        this.jvmGCTime = jvmGCTime;
    }

    public AggregateInteger getResultSize() {
        return resultSize;
    }

    public void setResultSize(AggregateInteger resultSize) {
        this.resultSize = resultSize;
    }

    public AggregateLong getMemoryBytesSpilled() {
        return memoryBytesSpilled;
    }

    public void setMemoryBytesSpilled(AggregateLong memoryBytesSpilled) {
        this.memoryBytesSpilled = memoryBytesSpilled;
    }

    public AggregateLong getDiskBytesSpilled() {
        return diskBytesSpilled;
    }

    public void setDiskBytesSpilled(AggregateLong diskBytesSpilled) {
        this.diskBytesSpilled = diskBytesSpilled;
    }

    public void addExecutorDeserializeTime(Integer time) {
        if (executorDeserializeTime.get(time) == null) {
            executorDeserializeTime.put(time, 1);
        } else {
            executorDeserializeTime.put(time, executorDeserializeTime.get(time) + 1);
        }
    }

    public void addExecutorRunTime(Integer time) {
        if (executorRunTime.get(time) == null) {
            executorRunTime.put(time, 1);
        } else {
            executorRunTime.put(time, executorRunTime.get(time) + 1);
        }
    }

    public void addResultSerializationTime(Integer time) {
        if (resultSerializationTime.get(time) == null) {
            resultSerializationTime.put(time, 1);
        } else {
            resultSerializationTime.put(time, resultSerializationTime.get(time) + 1);
        }
    }

    public void addGettingResultTime(Long time) {
        if (gettingResultTime.get(time) == null) {
            gettingResultTime.put(time, 1);
        } else {
            gettingResultTime.put(time, gettingResultTime.get(time) + 1);
        }
    }

    public void addJvmGCTime(Integer time) {
        if (jvmGCTime.get(time) == null) {
            jvmGCTime.put(time, 1);
        } else {
            jvmGCTime.put(time, jvmGCTime.get(time) + 1);
        }
    }

    public void addResultSize(Integer size) {
        resultSize.CNT++;
        resultSize.SUM += size;
        resultSize.AVG = resultSize.SUM / resultSize.CNT;

        if (size > resultSize.MAX) {
            resultSize.MAX = size;
        }

        if (resultSize.MIN > size || resultSize.MIN == 0 ) {
            resultSize.MIN = size;
        }
    }

    public void addMemoryBytesSpilled(Long spill) {
        memoryBytesSpilled.CNT++;
        memoryBytesSpilled.SUM += spill;
        memoryBytesSpilled.AVG = memoryBytesSpilled.SUM / memoryBytesSpilled.CNT;

        if (spill > memoryBytesSpilled.MAX) {
            memoryBytesSpilled.MAX = spill;
        }

        if (memoryBytesSpilled.MIN == 0 || memoryBytesSpilled.MIN > spill ) {
            memoryBytesSpilled.MIN = spill;
        }
    }

    public void addDiskBytesSpilled(Long spill) {
        diskBytesSpilled.CNT++;
        diskBytesSpilled.SUM += spill;
        diskBytesSpilled.AVG = diskBytesSpilled.SUM / diskBytesSpilled.CNT;

        if (spill > diskBytesSpilled.MAX) {
            diskBytesSpilled.MAX = spill;
        }

        if (diskBytesSpilled.MIN == 0 || diskBytesSpilled.MIN > spill ) {
            diskBytesSpilled.MIN = spill;
        }
    }
}
