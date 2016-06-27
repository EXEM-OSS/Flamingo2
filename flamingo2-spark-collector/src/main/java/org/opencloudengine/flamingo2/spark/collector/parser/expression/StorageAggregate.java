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
package org.opencloudengine.flamingo2.spark.collector.parser.expression;

import org.opencloudengine.flamingo2.spark.collector.event.JobStart;
import org.opencloudengine.flamingo2.spark.collector.event.TaskEnd;
import org.opencloudengine.flamingo2.spark.collector.event.element.RDDInfo;
import org.opencloudengine.flamingo2.spark.collector.event.element.StageInfo;
import org.opencloudengine.flamingo2.spark.collector.event.element.Status;
import org.opencloudengine.flamingo2.spark.collector.event.element.UpdateBlock;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 18..
 */
public class StorageAggregate implements Serializable {
    List<Integer> rddIDs;

    Map<Integer, Storage> storageMap;

    Map<String, String> partitionMap;

    public StorageAggregate() {
        rddIDs = new ArrayList<>();
        storageMap = new HashMap<>();
        partitionMap = new HashMap<>();
    }

    public void add(JobStart jobStart) {
        List<StageInfo> stageInfoList = jobStart.getStageInfos();

        for (StageInfo stageInfo : stageInfoList) {
            List<RDDInfo> rddInfoList = stageInfo.getrDDInfo();

            if (rddInfoList != null) {
                for (RDDInfo rddInfo : rddInfoList) {
                    if (storageMap.get(rddInfo.getrDDID()) != null) {
                        continue;
                    }
                    rddIDs.add(rddInfo.getrDDID());
                    Storage storage = new Storage();

                    storage.setRddID(rddInfo.getrDDID());
                    storage.setRddName(rddInfo.getName());
                    storage.setPartitions(rddInfo.getNumberofPartitions());
                    storage.setReplication(rddInfo.getStorageLevel().getReplication());
                    storage.setUseDisk(rddInfo.getStorageLevel().isUseDisk());
                    storage.setUseMemory(rddInfo.getStorageLevel().isUseMemory());
                    storage.setDeserialized(rddInfo.getStorageLevel().isDeserialized());

                    if (rddInfo.getStorageLevel().isUseExternalBlockStore() || rddInfo.getStorageLevel().isUseTachyon()) {
                        storage.setUseExternalBlockStore(true);
                    } else {
                        storage.setUseExternalBlockStore(false);
                    }

                    storageMap.put(rddInfo.getrDDID(), storage);
                }
            }
        }

    }

    public void add(TaskEnd taskEnd) {
        if (taskEnd.getTaskMetrics() == null)
            return;

        List<UpdateBlock> updateBlockList = taskEnd.getTaskMetrics().getUpdatedBlocks();

        if (updateBlockList != null) {
            for (UpdateBlock updateBlock : updateBlockList) {

                if (partitionMap.get(updateBlock.getBlockID()) == null) {
                    String split[] = updateBlock.getBlockID().split("_");
                    Status status = updateBlock.getStatus();
                    Integer rddID = Integer.valueOf(split[1]);

                    Storage storage = storageMap.get(rddID);

                    storage.setMemory(storage.getMemory() + status.getMemorySize());
                    storage.setDisk(storage.getDisk() + status.getDiskSize());
                    storage.setCachedPartitions(storage.getCachedPartitions() + 1);

                    if (status.getExternalBlockStoreSize() != null) {
                        storage.setExternalBlockStore(storage.getExternalBlockStore() + status.getExternalBlockStoreSize());
                    } else if (status.getTachyonSize() != null) {
                        storage.setExternalBlockStore(storage.getExternalBlockStore() + status.getTachyonSize());
                    }

                    storage.isUpdated = true;
                    storageMap.put(rddID, storage);
                    partitionMap.put(updateBlock.getBlockID(), updateBlock.getBlockID());
                }
            }
        }
    }

    public List<Integer> getRddIDs() {
        return rddIDs;
    }

    public void setRddIDs(List<Integer> rddIDs) {
        this.rddIDs = rddIDs;
    }

    public Map<Integer, Storage> getStorageMap() {
        return storageMap;
    }

    public void setStorageMap(Map<Integer, Storage> storageMap) {
        this.storageMap = storageMap;
    }
}
