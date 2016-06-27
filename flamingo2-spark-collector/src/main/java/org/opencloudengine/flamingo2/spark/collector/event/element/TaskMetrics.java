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
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 8..
 */
public class TaskMetrics implements Serializable {
    @SerializedName ("Host Name") private String hostName;
    @SerializedName ("Executor Deserialize Time") private Integer executorDeserializeTime;
    @SerializedName ("Executor Run Time") private Integer executorRunTime;
    @SerializedName ("Result Size") private Integer resultSize;
    @SerializedName ("JVM GC Time") private Integer jVMGCTime;
    @SerializedName ("Result Serialization Time") private Integer resultSerializationTime;
    @SerializedName ("Memory Bytes Spilled") private Long memoryBytesSpilled;
    @SerializedName ("Disk Bytes Spilled") private Long diskBytesSpilled;
    @SerializedName ("Shuffle Read Metrics") private ShuffleReadMetrics shuffleReadMetrics;
    @SerializedName ("Shuffle Write Metrics") private ShuffleWriteMetrics shuffleWriteMetrics;
    @SerializedName ("Input Metrics") private InputMetrics inputMetrics;
    @SerializedName ("Output Metrics") private OutputMetrics outputMetrics;
    @SerializedName ("Updated Blocks") private List<UpdateBlock> updatedBlocks;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getExecutorDeserializeTime() {
        return executorDeserializeTime;
    }

    public void setExecutorDeserializeTime(Integer executorDeserializeTime) {
        this.executorDeserializeTime = executorDeserializeTime;
    }

    public Integer getExecutorRunTime() {
        return executorRunTime;
    }

    public void setExecutorRunTime(Integer executorRunTime) {
        this.executorRunTime = executorRunTime;
    }

    public Integer getResultSize() {
        return resultSize;
    }

    public void setResultSize(Integer resultSize) {
        this.resultSize = resultSize;
    }

    public Integer getjVMGCTime() {
        return jVMGCTime;
    }

    public void setjVMGCTime(Integer jVMGCTime) {
        this.jVMGCTime = jVMGCTime;
    }

    public Integer getResultSerializationTime() {
        return resultSerializationTime;
    }

    public void setResultSerializationTime(Integer resultSerializationTime) {
        this.resultSerializationTime = resultSerializationTime;
    }

    public Long getMemoryBytesSpilled() {
        return memoryBytesSpilled;
    }

    public void setMemoryBytesSpilled(Long memoryBytesSpilled) {
        this.memoryBytesSpilled = memoryBytesSpilled;
    }

    public Long getDiskBytesSpilled() {
        return diskBytesSpilled;
    }

    public void setDiskBytesSpilled(Long diskBytesSpilled) {
        this.diskBytesSpilled = diskBytesSpilled;
    }

    public ShuffleReadMetrics getShuffleReadMetrics() {
        return shuffleReadMetrics;
    }

    public void setShuffleReadMetrics(ShuffleReadMetrics shuffleReadMetrics) {
        this.shuffleReadMetrics = shuffleReadMetrics;
    }

    public ShuffleWriteMetrics getShuffleWriteMetrics() {
        return shuffleWriteMetrics;
    }

    public void setShuffleWriteMetrics(ShuffleWriteMetrics shuffleWriteMetrics) {
        this.shuffleWriteMetrics = shuffleWriteMetrics;
    }

    public InputMetrics getInputMetrics() {
        return inputMetrics;
    }

    public void setInputMetrics(InputMetrics inputMetrics) {
        this.inputMetrics = inputMetrics;
    }

    public OutputMetrics getOutputMetrics() {
        return outputMetrics;
    }

    public void setOutputMetrics(OutputMetrics outputMetrics) {
        this.outputMetrics = outputMetrics;
    }

    public List<UpdateBlock> getUpdatedBlocks() {
        return updatedBlocks;
    }

    public void setUpdatedBlocks(List<UpdateBlock> updatedBlocks) {
        this.updatedBlocks = updatedBlocks;
    }
}


