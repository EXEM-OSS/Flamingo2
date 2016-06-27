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
import java.util.List;

/**
 * Created by Hyokun Park on 15. 8. 8..
 */
public class RDDInfo implements Serializable {
    @SerializedName("RDD ID") private int rDDID;
    @SerializedName("Name") private String name;
    @SerializedName("Scope") private String scope;
    @SerializedName("Parent IDs") private List<Integer> parentIDs;
    @SerializedName("Storage Level") private StorageLevel storageLevel;
    @SerializedName("Number of Partitions") private Integer numberofPartitions;
    @SerializedName("Number of Cached Partitions") private Integer numberofCachedPartitions;
    @SerializedName("Memory Size") private Long memorySize;
    @SerializedName("ExternalBlockStore Size") private Integer externalBlockStoreSize;
    @SerializedName("Tachyon Size") private Integer tachyonSize;
    @SerializedName("Disk Size") private Long diskSize;

    public Integer getrDDID() {
        return rDDID;
    }

    public void setrDDID(Integer rDDID) {
        this.rDDID = rDDID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Integer> getParentIDs() {
        return parentIDs;
    }

    public void setParentIDs(List<Integer> parentIDs) {
        this.parentIDs = parentIDs;
    }

    public StorageLevel getStorageLevel() {
        return storageLevel;
    }

    public void setStorageLevel(StorageLevel storageLevel) {
        this.storageLevel = storageLevel;
    }

    public int getNumberofPartitions() {
        return numberofPartitions;
    }

    public void setNumberofPartitions(Integer numberofPartitions) {
        this.numberofPartitions = numberofPartitions;
    }

    public int getNumberofCachedPartitions() {
        return numberofCachedPartitions;
    }

    public void setNumberofCachedPartitions(Integer numberofCachedPartitions) {
        this.numberofCachedPartitions = numberofCachedPartitions;
    }

    public Long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Long memorySize) {
        this.memorySize = memorySize;
    }

    public Integer getExternalBlockStoreSize() {
        return externalBlockStoreSize;
    }

    public void setExternalBlockStoreSize(Integer externalBlockStoreSize) {
        this.externalBlockStoreSize = externalBlockStoreSize;
    }

    public Integer getTachyonSize() {
        return tachyonSize;
    }

    public void setTachyonSize(Integer tachyonSize) {
        this.tachyonSize = tachyonSize;
    }

    public Long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Long diskSize) {
        this.diskSize = diskSize;
    }
}

