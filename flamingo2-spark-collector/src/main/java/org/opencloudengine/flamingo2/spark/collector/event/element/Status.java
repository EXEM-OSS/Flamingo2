package org.opencloudengine.flamingo2.spark.collector.event.element;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 18..
 */
public class Status implements Serializable {
    @SerializedName("Storage Level") private StorageLevel storageLevel;
    @SerializedName("Memory Size") private Long memorySize;
    @SerializedName("ExternalBlockStore Size") private Long externalBlockStoreSize;
    @SerializedName("Tachyon Size") private Long tachyonSize;
    @SerializedName("Disk Size") private Long diskSize;

    public StorageLevel getStorageLevel() {
        return storageLevel;
    }

    public void setStorageLevel(StorageLevel storageLevel) {
        this.storageLevel = storageLevel;
    }

    public Long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Long memorySize) {
        this.memorySize = memorySize;
    }

    public Long getExternalBlockStoreSize() {
        return externalBlockStoreSize;
    }

    public void setExternalBlockStoreSize(Long externalBlockStoreSize) {
        this.externalBlockStoreSize = externalBlockStoreSize;
    }

    public Long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Long diskSize) {
        this.diskSize = diskSize;
    }

    public Long getTachyonSize() {
        return tachyonSize;
    }

    public void setTachyonSize(Long tachyonSize) {
        this.tachyonSize = tachyonSize;
    }
}
