package org.opencloudengine.flamingo2.spark.collector.parser.value;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 18..
 */
public class Storage implements Serializable {
    private String appID;

    private Integer rddID;

    private String rddName;

    private Integer partitions = 0;

    private Integer cachedPartitions = 0;

    private Long memory = Long.valueOf(0);

    private Long disk = Long.valueOf(0);

    private Long externalBlockStore = Long.valueOf(0);

    private Boolean useDisk;

    private Boolean useMemory;

    private Boolean useExternalBlockStore;

    private Boolean deserialized;

    private Integer replication;

    public Boolean isUpdated;

    public Storage() {
        isUpdated = false;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public Integer getRddID() {
        return rddID;
    }

    public void setRddID(Integer rddID) {
        this.rddID = rddID;
    }

    public String getRddName() {
        return rddName;
    }

    public void setRddName(String rddName) {
        this.rddName = rddName;
    }

    public Integer getPartitions() {
        return partitions;
    }

    public void setPartitions(Integer partitions) {
        this.partitions = partitions;
    }

    public Integer getCachedPartitions() {
        return cachedPartitions;
    }

    public void setCachedPartitions(Integer cachedPartitions) {
        this.cachedPartitions = cachedPartitions;
    }

    public Long getMemory() {
        return memory;
    }

    public void setMemory(Long memory) {
        this.memory = memory;
    }

    public Long getDisk() {
        return disk;
    }

    public void setDisk(Long disk) {
        this.disk = disk;
    }

    public Boolean getUseDisk() {
        return useDisk;
    }

    public void setUseDisk(Boolean useDisk) {
        this.useDisk = useDisk;
    }

    public Boolean getUseMemory() {
        return useMemory;
    }

    public void setUseMemory(Boolean useMemory) {
        this.useMemory = useMemory;
    }

    public Boolean getDeserialized() {
        return deserialized;
    }

    public void setDeserialized(Boolean deserialized) {
        this.deserialized = deserialized;
    }

    public Integer getReplication() {
        return replication;
    }

    public void setReplication(Integer replication) {
        this.replication = replication;
    }

    public Long getExternalBlockStore() {
        return externalBlockStore;
    }

    public void setExternalBlockStore(Long externalBlockStore) {
        this.externalBlockStore = externalBlockStore;
    }

    public Boolean getUseExternalBlockStore() {
        return useExternalBlockStore;
    }

    public void setUseExternalBlockStore(Boolean useExternalBlockStore) {
        this.useExternalBlockStore = useExternalBlockStore;
    }

    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
