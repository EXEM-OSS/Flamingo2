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
package org.opencloudengine.flamingo2.collector.repository;

import org.opencloudengine.flamingo2.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
@Repository
public class HdfsRepositoryImpl implements HdfsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void insert(String system, String name, Map<String, Object> map) {
        Date date = new Date();
        jdbcTemplate.update("DELETE FROM FL_CL_HDFS WHERE reg_dt < ?", DateUtils.addDays(date, -7));
        jdbcTemplate.update("INSERT INTO FL_CL_HDFS (system, name, type, nodeAll, nodeDead, nodeLive, nodeDecommisioning, blocksTotal, corrupt, underReplicatedBlocks, totalFiles, totalBlocks, totalLoad, capacityRemaining, capacityRemainingPercent, capacityTotal, capacityUsed, capacityUsedNonDFS, capacityUsedPercent, editLogSize, free, used, total, threads, jvmMaxMemory, jvmTotalMemory, jvmFreeMemory, jvmUsedMemory, reg_dt, yyyy, mm, dd) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                system,
                name,
                "hdfs",
                map.get("all"),
                map.get("dead"),
                map.get("live"),
                map.get("decommisioning"),
                map.get("blocksTotal"),
                map.get("corrupt"),
                map.get("underReplicatedBlocks"),
                map.get("totalFiles"),
                map.get("totalBlocks"),
                map.get("totalLoad"),
                map.get("capacityRemaining"),
                map.get("capacityRemainingPercent"),
                map.get("capacityTotal"),
                map.get("capacityUsed"),
                map.get("capacityUsedNonDFS"),
                map.get("capacityUsedPercent"),
                map.get("editLogSize"),
                map.get("free"),
                map.get("used"),
                map.get("total"),
                map.get("threads"),
                map.get("jvmMaxMemory"),
                map.get("jvmTotalMemory"),
                map.get("jvmFreeMemory"),
                map.get("jvmUsedMemory"),
                date,
                DateUtils.parseDate(date, "yyyy"),
                DateUtils.parseDate(date, "MM"),
                DateUtils.parseDate(date, "dd")
        });
    }
}
