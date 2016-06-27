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
public class OutputMetrics implements Serializable {
    @SerializedName ("Data Write Method") private String dataWriteMethod;
    @SerializedName ("Bytes Written") private Long bytesWritten;
    @SerializedName ("Records Written") private Long recordsWritten;

    public String getDataWriteMethod() {
        return dataWriteMethod;
    }

    public void setDataWriteMethod(String dataWriteMethod) {
        this.dataWriteMethod = dataWriteMethod;
    }

    public Long getBytesWritten() {
        return bytesWritten;
    }

    public void setBytesWritten(Long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

    public Long getRecordsWritten() {
        return recordsWritten;
    }

    public void setRecordsWritten(Long recordsWritten) {
        this.recordsWritten = recordsWritten;
    }
}
