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
package org.opencloudengine.flamingo2.spark.collector.event;

import com.google.gson.annotations.SerializedName;
import org.opencloudengine.flamingo2.spark.collector.event.element.StageInfo;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 8..
 */
public class StageCompleted implements Serializable {
    @SerializedName("Stage Info") private StageInfo stageInfo;

    public StageInfo getStageInfo() {
        return stageInfo;
    }

    public void setStageInfo(StageInfo stageInfo) {
        this.stageInfo = stageInfo;
    }
}
