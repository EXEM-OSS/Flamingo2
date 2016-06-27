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
import org.opencloudengine.flamingo2.spark.collector.parser.value.Job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class JobAggregate implements Serializable {
    private List<Integer> jobIDs;

    private Map<Integer, Job> jobMap;

    private Map<Integer, Integer> stageJobMap;

    private Map<Integer, Integer> maxAttemptMap;

    public JobAggregate() {
        jobIDs = new ArrayList<>();
        jobMap = new HashMap<>();
        stageJobMap = new HashMap<>();
        maxAttemptMap = new HashMap<>();
    }

    public List<Integer> getJobIDs() {
        return jobIDs;
    }

    public void setJobIDs(List<Integer> jobIDs) {
        this.jobIDs = jobIDs;
    }

    public Map<Integer, Job> getJobMap() {
        return jobMap;
    }

    public void setJobMap(Map<Integer, Job> jobMap) {
        this.jobMap = jobMap;
    }

    public Map<Integer, Integer> getStageJobMap() {
        return stageJobMap;
    }

    public Map<Integer, Integer> getMaxAttemptMap() {
        return maxAttemptMap;
    }

    public void add(JobStart jobStart) {
        Job job = new Job();
        jobIDs.add(jobStart.getJobID());
        job.setJobID(jobStart.getJobID());
        job.setSubmitted(jobStart.getSubmissionTime());
        job.setStages(job.getStages() + jobStart.getStageIDs().size());
        job.setStageSkipped(job.getStageSkipped() + jobStart.getStageIDs().size());

        if (jobStart.getProperties() != null && jobStart.getProperties().get("spark.job.description") != null) {
            job.setDescription(jobStart.getProperties().get("spark.job.description").toString());
        }

        if (jobStart.getStageInfos().size() > 0) {
            job.setJobName(jobStart.getStageInfos().get(0).getStageName());
        }

        for (StageInfo stageInfo : jobStart.getStageInfos()) {
            stageJobMap.put(stageInfo.getStageID(), jobStart.getJobID());
        }

        this.jobMap.put(jobStart.getJobID(), job);
    }

    public void add(JobEnd jobEnd) {
        Job job = getJobMap().get(jobEnd.getJobID());

        job.setCompleted(jobEnd.getCompletionTime());
        job.setStatus(jobEnd.getJobResult().get("Result").toString());

        this.jobMap.put(jobEnd.getJobID(), job);
    }

    public void add(StageSubmitted stageSubmitted) {
        Integer jobID = stageJobMap.get(stageSubmitted.getStageInfo().getStageID());
        Job job = getJobMap().get(jobID);

        if (maxAttemptMap.get(stageSubmitted.getStageInfo().getStageID()) == null) {
            job.setStageSkipped(job.getStageSkipped() - 1);
        }

        maxAttemptMap.put(stageSubmitted.getStageInfo().getStageID(), stageSubmitted.getStageInfo().getStageAttemptID());
    }

    public void add(StageCompleted stageCompleted) {
        Integer jobID = stageJobMap.get(stageCompleted.getStageInfo().getStageID());
        Job job = getJobMap().get(jobID);

        if (stageCompleted.getStageInfo().isFailure()) {
            job.setStageFailed(job.getStageFailed() + 1);
        }
    }

    public void add(TaskEnd taskEnd) {
        Integer jobID = stageJobMap.get(taskEnd.getStageID());
        Job job = getJobMap().get(jobID);

        job.setTasks(job.getTasks() + 1);
        if (taskEnd.getTaskEndReason().get("Reason").equals("Success")) {
            job.setTaskCompleted(job.getTaskCompleted() + 1);
        } else {
            job.setTaskFailed(job.getTaskFailed() + 1);
        }
    }

    public String getJson() {
        Gson gson = new Gson();

        return gson.toJson(jobMap);
    }
}
