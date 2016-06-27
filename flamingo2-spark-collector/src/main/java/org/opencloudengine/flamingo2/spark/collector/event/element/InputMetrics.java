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
 * Created by Hyokun Park on 15. 8. 11..
 */
public class InputMetrics implements Serializable {

    @SerializedName ("Data Read Method") private String dataReadMethod;
    @SerializedName ("Bytes Read") private Long bytesRead;
    @SerializedName ("Records Read") private Long recordsRead;

    public String getDataReadMethod() {
        return dataReadMethod;
    }

    public void setDataReadMethod(String dataReadMethod) {
        this.dataReadMethod = dataReadMethod;
    }

    public Long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(Long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public Long getRecordsRead() {
        return recordsRead;
    }

    public void setRecordsRead(Long recordsRead) {
        this.recordsRead = recordsRead;
    }
}
