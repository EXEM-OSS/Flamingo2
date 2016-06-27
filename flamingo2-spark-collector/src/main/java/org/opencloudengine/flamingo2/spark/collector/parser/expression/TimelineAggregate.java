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
import org.opencloudengine.flamingo2.spark.collector.parser.value.Stage;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Timeline;
import org.opencloudengine.flamingo2.spark.collector.parser.value.TimelineNode;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Hyokun Park on 15. 8. 20..
 */
public class TimelineAggregate implements Serializable {
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<TimelineNode> executor;

    private List<Integer> jobList;

    private Map<Integer, TimelineNode> job;

    private Map<Integer, JobStart> jobStartMap;

    private List<String> stageList;

    private Map<String, TimelineNode> stage;

    private Map<Integer, Integer> stageJobMap;

    private Map<String, List<TimelineNode>> taskMap;

    public TimelineAggregate() {
        executor = new ArrayList<>();
        jobList = new ArrayList<>();
        job = new HashMap<>();
        stageJobMap = new HashMap<>();
        jobStartMap = new HashMap<>();
        stageList = new ArrayList<>();
        stage = new HashMap<>();
        taskMap = new HashMap<>();
    }

    public void add(ExecutorAdded executorAdded) {
        TimelineNode timelineNode = new TimelineNode();
        timelineNode.setId("Added" + executorAdded.getExecutorID());
        timelineNode.setStart(getDate(executorAdded.getTimestamp()));
        timelineNode.setClassName("executor added");
        timelineNode.setGroup("executors");

        String content = org.slf4j.helpers.MessageFormatter.arrayFormat("<div class=\"executor-event-content\" data-toggle=\"tooltip\" data-placement=\"bottom\" data-title=\"Executor {}<br>Added at {}\" data-html=\"true\">Executor {} added</div>", new String[]{
                executorAdded.getExecutorID(),
                getDate(executorAdded.getTimestamp()),
                executorAdded.getExecutorID()
        }).getMessage();

        timelineNode.setContent(content);

        executor.add(timelineNode);
    }

    public void add(ExecutorRemoved executorRemoved) {
        TimelineNode timelineNode = new TimelineNode();

        timelineNode.setId("Removed" + executorRemoved.getExecutorID());
        timelineNode.setStart(getDate(executorRemoved.getTimestamp()));
        timelineNode.setClassName("executor removed");
        timelineNode.setGroup("executors");

        String content = org.slf4j.helpers.MessageFormatter.arrayFormat("<div class=\"executor-event-content\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Executor {}<br>Removed at {}<br>Reason: {}\" data-html=\"true\">Executor {} removed</div>", new String[]{
                executorRemoved.getExecutorID(),
                getDate(executorRemoved.getTimestamp()),
                executorRemoved.getRemovedReason(),
                executorRemoved.getExecutorID()
        }).getMessage();

        timelineNode.setContent(content);

        executor.add(timelineNode);
    }

    public void add(JobStart jobStart) {
        jobList.add(jobStart.getJobID());

        TimelineNode timelineNode = new TimelineNode();

        timelineNode.setId("jobs" + jobStart.getJobID());
        timelineNode.setGroup("jobs");
        timelineNode.setStart(getDate(jobStart.getSubmissionTime()));

        jobStartMap.put(jobStart.getJobID(), jobStart);
        job.put(jobStart.getJobID(), timelineNode);

        for (StageInfo stageInfo : jobStart.getStageInfos()) {
            stageJobMap.put(stageInfo.getStageID(), jobStart.getJobID());
        }
    }

    public void add(JobEnd jobEnd) {
        TimelineNode timelineNode = job.get(jobEnd.getJobID());
        String status = "";

        timelineNode.setEnd(getDate(jobEnd.getCompletionTime()));

        if (jobEnd.getJobResult().get("Result").equals("JobSucceeded")) {
            status = "succeeded";
        } else {
            status = "failed";
        }

        JobStart jobStart = jobStartMap.get(jobEnd.getJobID());

        timelineNode.setClassName("job application-timeline-object " + status);

        String content = org.slf4j.helpers.MessageFormatter.arrayFormat("<div class=\"application-timeline-content\" data-html=\"true\" data-placement=\"top\" data-toggle=\"tooltip\" title=\"{} (Job {})<br>Status: {}<br>Submitted: {}<br>Completed: {}\">{} (Job {})</div>", new String[]{
                jobStart.getStageInfos().get(jobStart.getStageInfos().size() - 1).getStageName(),
                jobEnd.getJobID().toString(),
                status,
                timelineNode.getStart(),
                timelineNode.getEnd(),
                jobStart.getStageInfos().get(jobStart.getStageInfos().size() - 1).getStageName(),
                jobEnd.getJobID().toString()
        }).getMessage();

        timelineNode.setContent(content);

        job.put(jobStart.getJobID(), timelineNode);
    }

    public void add(StageCompleted stageCompleted) {
        TimelineNode timelineNode = new TimelineNode();

        timelineNode.setGroup("stages");
        timelineNode.setStart(getDate(stageCompleted.getStageInfo().getSubmissionTime()));
        timelineNode.setEnd(getDate(stageCompleted.getStageInfo().getCompletionTime()));

        String status = "";
        if (stageCompleted.getStageInfo().isFailure()) {
            status = "failed";
        } else {
            status = "succeeded";
        }

        timelineNode.setClassName("stage job-timeline-object " + status);

        String content = org.slf4j.helpers.MessageFormatter.arrayFormat("<div class=\"job-timeline-content\" data-toggle=\"tooltip\" data-placement=\"top\" data-html=\"true\" data-title=\"{} (Stage {}.{})<br>Status: {}<br>Submitted: {}<br>Completed: {}\">{} (Stage {}.{})</div>", new String[]{
                stageCompleted.getStageInfo().getStageName(),
                stageCompleted.getStageInfo().getStageID().toString(),
                stageCompleted.getStageInfo().getStageAttemptID().toString(),
                status.toUpperCase(),
                timelineNode.getStart(),
                timelineNode.getEnd(),
                stageCompleted.getStageInfo().getStageName(),
                stageCompleted.getStageInfo().getStageID().toString(),
                stageCompleted.getStageInfo().getStageAttemptID().toString()
        }).getMessage();

        timelineNode.setContent(content);

        String stageID = stageCompleted.getStageInfo().getStageID() + "-" + stageCompleted.getStageInfo().getStageAttemptID();
        stageList.add(stageID);
        stage.put(stageID, timelineNode);
    }

    public void add(TaskEnd taskEnd) {
        TimelineNode timelineNode = new TimelineNode();

        String status = "";
        if (taskEnd.getTaskEndReason().get("Reason").equals("Success")) {
            status = "SUCCESS";
        } else {
            return;
        }

        timelineNode.setStart(getDate(taskEnd.getTaskInfo().getLaunchTime()));
        timelineNode.setEnd(getDate(taskEnd.getTaskInfo().getFinishTime()));
        timelineNode.setClassName("task task-assignment-timeline-object");
        timelineNode.setGroup(taskEnd.getTaskInfo().getExecutorID());

        Long totalExecutionTime = taskEnd.getTaskInfo().getFinishTime() - taskEnd.getTaskInfo().getLaunchTime();
        Long shuffleReadTime = (taskEnd.getTaskMetrics().getShuffleReadMetrics()==null)?0:taskEnd.getTaskMetrics().getShuffleReadMetrics().getFetchWaitTime();
        Long shuffleWriteTime = (taskEnd.getTaskMetrics().getShuffleWriteMetrics()==null)?0:taskEnd.getTaskMetrics().getShuffleWriteMetrics().getShuffleWriteTime();
        Long executorComputingTime = taskEnd.getTaskMetrics().getExecutorRunTime() - shuffleReadTime - shuffleWriteTime;
        Long serializationTime = Long.valueOf(taskEnd.getTaskMetrics().getResultSerializationTime());
        Long deserializationTime = Long.valueOf(taskEnd.getTaskMetrics().getExecutorDeserializeTime());
        Long gettingResultTime = taskEnd.getTaskInfo().getGettingResultTime();

        Long shuffleReadTimeProportion = toProportion(totalExecutionTime, shuffleReadTime);
        Long shuffleWriteTimeProportion = toProportion(totalExecutionTime, shuffleWriteTime);
        Long executorComputingTimeProportion = toProportion(totalExecutionTime, executorComputingTime);
        Long serializationTimeProportion = toProportion(totalExecutionTime, serializationTime);
        Long deserializationTimeProportion = toProportion(totalExecutionTime, deserializationTime);
        Long gettingResultTimeProportion = toProportion(totalExecutionTime, gettingResultTime);

        Long schedulerDelay = totalExecutionTime - (executorComputingTime + shuffleReadTime + shuffleWriteTime + serializationTime + deserializationTime + gettingResultTime);
        Long schedulerDelayProportion = (100 - executorComputingTimeProportion - shuffleReadTimeProportion - shuffleWriteTimeProportion - serializationTimeProportion - deserializationTimeProportion - gettingResultTimeProportion);

        Long schedulerDelayProportionPos = Long.valueOf(0);
        Long deserializationTimeProportionPos = schedulerDelayProportionPos + schedulerDelayProportion;
        Long shuffleReadTimeProportionPos = deserializationTimeProportionPos + deserializationTimeProportion;
        Long executorRuntimeProportionPos = shuffleReadTimeProportionPos + shuffleReadTimeProportion;
        Long shuffleWriteTimeProportionPos = executorRuntimeProportionPos + executorComputingTimeProportion;
        Long serializationTimeProportionPos = shuffleWriteTimeProportionPos + shuffleWriteTimeProportion;
        Long gettingResultTimeProportionPos = serializationTimeProportionPos + serializationTimeProportion;

        String messge = "<div class=\"task-assignment-timeline-content\" data-toggle=\"tooltip\" data-placement=\"top\" data-html=\"true\" data-container=\"body\" data-title=\"Task {} (attempt {})<br>Status: {}<br>Launch Time: {}<br>Finish Time: {}<br>Scheduler Delay: {} ms<br>Task Deserialization Time: {} <br>Shuffle Read Time: {}<br>Executor Computing Time: {}<br>Shuffle Write Time: {}<br>Result Serialization Time: {}<br>Getting Result Time: {}\"> " +
                "<svg class=\"task-assignment-timeline-duration-bar\">" +
                "<rect class=\"scheduler-delay-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                "<rect class=\"deserialization-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                "<rect class=\"shuffle-read-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                "<rect class=\"executor-runtime-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                "<rect class=\"shuffle-write-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                "<rect class=\"serialization-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                "<rect class=\"getting-result-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect></svg>";

        String content = org.slf4j.helpers.MessageFormatter.arrayFormat(messge, new String[]{
                taskEnd.getTaskInfo().getTaskID().toString(),
                taskEnd.getTaskInfo().getAttempt().toString(),
                status,
                timelineNode.getStart(),
                timelineNode.getEnd(),
                schedulerDelay.toString(),
                deserializationTime.toString(),
                shuffleReadTime.toString(),
                executorComputingTime.toString(),
                shuffleWriteTime.toString(),
                serializationTime.toString(),
                gettingResultTime.toString(),
                schedulerDelayProportionPos.toString(),
                schedulerDelayProportion.toString(),
                deserializationTimeProportionPos.toString(),
                deserializationTimeProportion.toString(),
                shuffleReadTimeProportionPos.toString(),
                shuffleReadTimeProportion.toString(),
                executorRuntimeProportionPos.toString(),
                executorComputingTimeProportion.toString(),
                shuffleWriteTimeProportionPos.toString(),
                shuffleWriteTimeProportion.toString(),
                serializationTimeProportionPos.toString(),
                serializationTimeProportion.toString(),
                gettingResultTimeProportionPos.toString(),
                gettingResultTimeProportion.toString()
        }).getMessage();

        timelineNode.setContent(content);

        Integer stageID = taskEnd.getStageID();
        Integer stageAttemtID = taskEnd.getStageAttemptID();

        String stageGroupID = stageID+"-"+stageAttemtID;
        List<TimelineNode> taskList = null;
        if (taskMap.get(stageGroupID) == null) {
            taskList = new ArrayList<>();
        } else {
            taskList = taskMap.get(stageGroupID);
        }

        taskList.add(timelineNode);

        taskMap.put(stageGroupID, taskList);
    }

    public List<TimelineNode> getExecutor() {
        return executor;
    }

    public void setExecutor(List<TimelineNode> executor) {
        this.executor = executor;
    }

    public Map<Integer, JobStart> getJobStartMap() {
        return jobStartMap;
    }

    public void setJobStartMap(Map<Integer, JobStart> jobStartMap) {
        this.jobStartMap = jobStartMap;
    }

    public List<String> getStageList() {
        return stageList;
    }

    public void setStageList(List<String> stageList) {
        this.stageList = stageList;
    }

    public Map<String, TimelineNode> getStage() {
        return stage;
    }

    public void setStage(Map<String, TimelineNode> stage) {
        this.stage = stage;
    }

    public List<Integer> getJobList() {
        return jobList;
    }

    public void setJobList(List<Integer> jobList) {
        this.jobList = jobList;
    }

    public Map<Integer, TimelineNode> getJob() {
        return job;
    }

    public void setJob(Map<Integer, TimelineNode> job) {
        this.job = job;
    }

    private String getDate(Long time) {
        Date date = new Date(time);
        return format.format(date);
    }

    public List<Timeline> getTimelineList() {
        List<Timeline> returnList = new ArrayList<>();
        Gson gson = new Gson();

        Timeline executorTimeline = new Timeline();

        executorTimeline.setAttemptID(-1);
        executorTimeline.setJobID(-1);
        executorTimeline.setStageID(-1);
        executorTimeline.setType("Executor");
        executorTimeline.setJson(gson.toJson(executor));
        returnList.add(executorTimeline);

        Timeline jobsTimeline = new Timeline();
        jobsTimeline.setAttemptID(-1);
        jobsTimeline.setJobID(-1);
        jobsTimeline.setStageID(-1);
        jobsTimeline.setType("Jobs");
        List<TimelineNode> jobsTimelineList = new ArrayList<>();
        for (Integer jobID : jobList) {
            jobsTimelineList.add(job.get(jobID));
        }

        jobsTimeline.setJson(gson.toJson(jobsTimelineList));
        returnList.add(jobsTimeline);

        Map<Integer, List<TimelineNode>> jobTimelineMap = new HashMap<>();
        for (String stageID : stageList) {
            String stageIDs[] = stageID.split("-");

            Integer jobID = stageJobMap.get(Integer.valueOf(stageIDs[0]));

            List<TimelineNode> jobTimelineList = null;
            if (jobTimelineMap.get(jobID) == null) {
                jobTimelineList = new ArrayList<>();
            }
            else {
                jobTimelineList = jobTimelineMap.get(jobID);
            }
            jobTimelineList.add(stage.get(stageID));

            jobTimelineMap.put(jobID, jobTimelineList);
        }

        Object keys[] = jobTimelineMap.keySet().toArray();

        for (Object keyObject : keys) {
            Integer jobID = (Integer) keyObject;

            List<TimelineNode> timelineNodeList = jobTimelineMap.get(jobID);

            Timeline stageTimeline = new Timeline();

            stageTimeline.setJobID(jobID);
            stageTimeline.setStageID(-1);
            stageTimeline.setAttemptID(-1);
            stageTimeline.setType("Stages");
            stageTimeline.setJson(gson.toJson(timelineNodeList));

            returnList.add(stageTimeline);
        }

        keys = taskMap.keySet().toArray();

        for (Object keyObject : keys) {
            String stageGroup = (String) keyObject;
            String split[] = stageGroup.split("-");

            List<TimelineNode> taskList = taskMap.get(stageGroup);

            Timeline taskTimeline = new Timeline();
            Integer jobID = stageJobMap.get(Integer.valueOf(split[0]));

            taskTimeline.setJobID(jobID);
            taskTimeline.setStageID(Integer.valueOf(split[0]));
            taskTimeline.setAttemptID(Integer.valueOf(split[1]));
            taskTimeline.setType("Tasks");
            taskTimeline.setJson(gson.toJson(taskList));

            returnList.add(taskTimeline);
        }

        return returnList;
    }

    private Long toProportion(Long total, Long time) {
        return time / total * 100;
    }
}
