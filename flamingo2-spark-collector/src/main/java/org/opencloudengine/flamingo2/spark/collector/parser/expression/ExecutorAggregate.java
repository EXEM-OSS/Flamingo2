package org.opencloudengine.flamingo2.spark.collector.parser.expression;

import org.opencloudengine.flamingo2.spark.collector.event.BlockManagerAdded;
import org.opencloudengine.flamingo2.spark.collector.event.ExecutorAdded;
import org.opencloudengine.flamingo2.spark.collector.event.TaskEnd;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Executor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 17..
 */
public class ExecutorAggregate implements Serializable {
    List<String> executorList;

    Map<String, Executor> executorMap;

    public void add(ExecutorAdded executorAdded) {
        String executorID = executorAdded.getExecutorID();
        Executor executor = null;

        if (executorMap.get(executorID) == null) {
            executorList.add(executorID);
            executor = new Executor();
        } else {
            executor = executorMap.get(executorID);
        }

        executor.setExecutorID(executorID);
        executor.setTotal_cores(executorAdded.getExecutorInfo().getTotalCores());
        executor.setStdout_url((String) executorAdded.getExecutorInfo().getLogUrls().get("stdout"));
        executor.setStderr_url((String) executorAdded.getExecutorInfo().getLogUrls().get("stderr"));

        executorMap.put(executorID, executor);
    }

    public void add(BlockManagerAdded blockManagerAdded) {
        String executorID = blockManagerAdded.getBlockManagerID().get("Executor ID").toString();
        Executor executor = null;

        if (executorMap.get(executorID) == null) {
            executorList.add(executorID);
            executor = new Executor();
        } else {
            executor = executorMap.get(executorID);
        }

        executor.setExecutorID(executorID);
        executor.setAddress(blockManagerAdded.getBlockManagerID().get("Host").toString());
        executor.setPort(blockManagerAdded.getBlockManagerID().get("Port").toString());
        executor.setMax_memory(blockManagerAdded.getMaximumMemory());

        executorMap.put(executorID, executor);
    }

    public void add(TaskEnd taskEnd) {
        Executor executor = executorMap.get(taskEnd.getTaskInfo().getExecutorID());

        executor.setTotal_tasks(executor.getTotal_tasks() + 1);

        executor.setTotal_duration(executor.getTotal_duration() + (taskEnd.getTaskInfo().getFinishTime() - taskEnd.getTaskInfo().getLaunchTime()));

        if (taskEnd.getTaskInfo().getFailed()) {
            executor.setFailed_tasks(executor.getFailed_tasks() + 1);
        } else {
            executor.setCompleted_tasks(executor.getCompleted_tasks() + 1);
        }


        if (taskEnd.getTaskMetrics() == null) {
            return;
        }

        if (taskEnd.getTaskMetrics().getInputMetrics() != null) {
            executor.setTotal_input_bytes(executor.getTotal_input_bytes() + taskEnd.getTaskMetrics().getInputMetrics().getBytesRead());
        }

        if (taskEnd.getTaskMetrics().getShuffleReadMetrics() != null) {
            executor.setTotal_shuffle_read(executor.getTotal_shuffle_read() + taskEnd.getTaskMetrics().getShuffleReadMetrics().getLocalBytesRead() + taskEnd.getTaskMetrics().getShuffleReadMetrics().getRemoteBytesRead());
        }

        if (taskEnd.getTaskMetrics().getShuffleWriteMetrics() != null) {
            executor.setTotal_shuffle_write(executor.getTotal_shuffle_write() + taskEnd.getTaskMetrics().getShuffleWriteMetrics().getShuffleBytesWritten());
        }

    }

    public ExecutorAggregate() {
        executorList = new ArrayList<>();
        executorMap = new HashMap<>();
    }

    public List<String> getExecutorList() {
        return executorList;
    }

    public void setExecutorList(List<String> executorList) {
        this.executorList = executorList;
    }

    public Map<String, Executor> getExecutorMap() {
        return executorMap;
    }

    public void setExecutorMap(Map<String, Executor> executorMap) {
        this.executorMap = executorMap;
    }
}