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
package org.opencloudengine.flamingo2.spark.collector.parser;

import org.opencloudengine.flamingo2.spark.collector.parser.expression.*;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Apps;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Executor;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 13..
 */
public class ApplicationAggregate implements Serializable {
    private Apps apps;

    private JobAggregate jobAggregate;

    private StageAggregate stageAggregate;

    private StageDetailAggregate stageDetailAggregate;

    private ExecutorAggregate executorAggregate;

    private StorageAggregate storageAggregate;

    private TimelineAggregate timelineAggregate;

    public ApplicationAggregate() {
        apps = new Apps();
        jobAggregate = new JobAggregate();
        stageAggregate = new StageAggregate();
        stageDetailAggregate = new StageDetailAggregate();
        timelineAggregate = new TimelineAggregate();
    }

    public Apps getApps() {
        return apps;
    }

    public void setApps(Apps apps) {
        this.apps = apps;
    }

    public JobAggregate getJobAggregate() {
        return jobAggregate;
    }

    public void setJobAggregate(JobAggregate jobAggregate) {
        this.jobAggregate = jobAggregate;
    }

    public StageAggregate getStageAggregate() {
        return stageAggregate;
    }

    public void setStageAggregate(StageAggregate stageAggregate) {
        this.stageAggregate = stageAggregate;
    }

    public StageDetailAggregate getStageDetailAggregate() {
        return stageDetailAggregate;
    }

    public void setStageDetailAggregate(StageDetailAggregate stageDetailAggregate) {
        this.stageDetailAggregate = stageDetailAggregate;
    }

    public ExecutorAggregate getExecutorAggregate() {
        return executorAggregate;
    }

    public void setExecutorAggregate(ExecutorAggregate executorAggregate) {
        this.executorAggregate = executorAggregate;
    }

    public StorageAggregate getStorageAggregate() {
        return storageAggregate;
    }

    public void setStorageAggregate(StorageAggregate storageAggregate) {
        this.storageAggregate = storageAggregate;
    }

    public TimelineAggregate getTimelineAggregate() {
        return timelineAggregate;
    }

    public void setTimelineAggregate(TimelineAggregate timelineAggregate) {
        this.timelineAggregate = timelineAggregate;
    }
}
