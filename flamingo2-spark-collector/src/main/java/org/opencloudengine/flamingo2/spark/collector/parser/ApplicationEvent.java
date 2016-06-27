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

import org.opencloudengine.flamingo2.spark.collector.event.ApplicationEnd;
import org.opencloudengine.flamingo2.spark.collector.event.ApplicationStart;
import org.opencloudengine.flamingo2.spark.collector.event.BlockManagerAdded;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 11..
 */
public class ApplicationEvent implements Serializable {
    private List<BlockManagerAdded> blockManagerAddedList;
    private Map jobStartMap;
    private Map jobEndMap;
    private Map stageSubmitMap;
    private Map stageCompleteMap;
    private Map tastStartMap;
    private Map taskEndMap;
    private ApplicationStart applicationStart;
    private ApplicationEnd applicationEnd;

    public List<BlockManagerAdded> getBlockManagerAddedList() {
        return blockManagerAddedList;
    }

    public void setBlockManagerAddedList(List<BlockManagerAdded> blockManagerAddedList) {
        this.blockManagerAddedList = blockManagerAddedList;
    }

    public Map getJobStartMap() {
        return jobStartMap;
    }

    public void setJobStartMap(Map jobStartMap) {
        this.jobStartMap = jobStartMap;
    }

    public Map getJobEndMap() {
        return jobEndMap;
    }

    public void setJobEndMap(Map jobEndMap) {
        this.jobEndMap = jobEndMap;
    }

    public Map getStageSubmitMap() {
        return stageSubmitMap;
    }

    public void setStageSubmitMap(Map stageSubmitMap) {
        this.stageSubmitMap = stageSubmitMap;
    }

    public Map getStageCompleteMap() {
        return stageCompleteMap;
    }

    public void setStageCompleteMap(Map stageCompleteMap) {
        this.stageCompleteMap = stageCompleteMap;
    }

    public Map getTastStartMap() {
        return tastStartMap;
    }

    public void setTastStartMap(Map tastStartMap) {
        this.tastStartMap = tastStartMap;
    }

    public Map getTaskEndMap() {
        return taskEndMap;
    }

    public void setTaskEndMap(Map taskEndMap) {
        this.taskEndMap = taskEndMap;
    }

    public ApplicationStart getApplicationStart() {
        return applicationStart;
    }

    public void setApplicationStart(ApplicationStart applicationStart) {
        this.applicationStart = applicationStart;
    }

    public ApplicationEnd getApplicationEnd() {
        return applicationEnd;
    }

    public void setApplicationEnd(ApplicationEnd applicationEnd) {
        this.applicationEnd = applicationEnd;
    }
}
