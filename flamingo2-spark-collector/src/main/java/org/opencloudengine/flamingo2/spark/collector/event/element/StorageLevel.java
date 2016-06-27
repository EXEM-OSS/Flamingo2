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
 * Created by Hyokun Park on 15. 8. 8..
 */
public class StorageLevel implements Serializable {
    @SerializedName ("Use Disk") private boolean useDisk;
    @SerializedName ("Use Memory") private boolean useMemory;
    @SerializedName ("Use ExternalBlockStore") private boolean useExternalBlockStore;
    @SerializedName ("Use Tachyon") private boolean useTachyon;
    @SerializedName ("Deserialized") private boolean deserialized;
    @SerializedName ("Replication") private int replication;

    public boolean isUseDisk() {
        return useDisk;
    }

    public void setUseDisk(boolean useDisk) {
        this.useDisk = useDisk;
    }

    public boolean isUseMemory() {
        return useMemory;
    }

    public void setUseMemory(boolean useMemory) {
        this.useMemory = useMemory;
    }

    public boolean isUseExternalBlockStore() {
        return useExternalBlockStore;
    }

    public void setUseExternalBlockStore(boolean useExternalBlockStore) {
        this.useExternalBlockStore = useExternalBlockStore;
    }

    public boolean isUseTachyon() {
        return useTachyon;
    }

    public void setUseTachyon(boolean useTachyon) {
        this.useTachyon = useTachyon;
    }

    public boolean isDeserialized() {
        return deserialized;
    }

    public void setDeserialized(boolean deserialized) {
        this.deserialized = deserialized;
    }

    public int getReplication() {
        return replication;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }
}





