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
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 8..
 */
public class ExecutorInfo implements Serializable {
    @SerializedName ("Host") private String host;
    @SerializedName ("Total Cores") private Integer totalCores;
    @SerializedName ("Log Urls") private Map logUrls;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getTotalCores() {
        return totalCores;
    }

    public void setTotalCores(Integer totalCores) {
        this.totalCores = totalCores;
    }

    public Map getLogUrls() {
        return logUrls;
    }

    public void setLogUrls(Map logUrls) {
        this.logUrls = logUrls;
    }
}