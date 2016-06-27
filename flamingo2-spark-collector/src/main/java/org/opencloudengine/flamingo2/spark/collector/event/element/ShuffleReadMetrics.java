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

/**
 * Created by Hyokun Park on 15. 8. 9..
 */
public class ShuffleReadMetrics implements Serializable {
    @SerializedName ("Remote Blocks Fetched") private Integer remoteBlocksFetched = 0;
    @SerializedName ("Local Blocks Fetched") private Integer localBlocksFetched = 0;
    @SerializedName ("Fetch Wait Time") private Long fetchWaitTime = 0L;
    @SerializedName ("Remote Bytes Read") private Long remoteBytesRead = 0L;
    @SerializedName ("Local Bytes Read") private Long localBytesRead = 0L;
    @SerializedName ("Total Records Read") private Long totalRecordsRead = 0L;

    public Integer getRemoteBlocksFetched() {
        return remoteBlocksFetched;
    }

    public void setRemoteBlocksFetched(Integer remoteBlocksFetched) {
        this.remoteBlocksFetched = remoteBlocksFetched;
    }

    public Integer getLocalBlocksFetched() {
        return localBlocksFetched;
    }

    public void setLocalBlocksFetched(Integer localBlocksFetched) {
        this.localBlocksFetched = localBlocksFetched;
    }

    public Long getFetchWaitTime() {
        return fetchWaitTime;
    }

    public void setFetchWaitTime(Long fetchWaitTime) {
        this.fetchWaitTime = fetchWaitTime;
    }

    public Long getRemoteBytesRead() {
        return remoteBytesRead;
    }

    public void setRemoteBytesRead(Long remoteBytesRead) {
        this.remoteBytesRead = remoteBytesRead;
    }

    public Long getLocalBytesRead() {
        return localBytesRead;
    }

    public void setLocalBytesRead(Long localBytesRead) {
        this.localBytesRead = localBytesRead;
    }

    public Long getTotalRecordsRead() {
        return totalRecordsRead;
    }

    public void setTotalRecordsRead(Long totalRecordsRead) {
        this.totalRecordsRead = totalRecordsRead;
    }

    public Long getTotalBytesRead() {
        return this.localBytesRead + this.remoteBytesRead;
    }
}