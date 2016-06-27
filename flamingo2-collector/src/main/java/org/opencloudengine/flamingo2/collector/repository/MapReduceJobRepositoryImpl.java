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
public class MapReduceJobRepositoryImpl implements MapReduceJobRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map selectByJobId(String systemId, String jobId) {
        try {
            return jdbcTemplate.queryForMap("SELECT ID, SYSTEM, JOB_ID FROM FL_CL_MR WHERE JOB_ID = ? AND SYSTEM = ?", new Object[]{jobId, systemId});
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void insertMapReduceJobConfirm(String systemId, String jobId) {
        jdbcTemplate.update("INSERT INTO FL_CL_MR (SYSTEM, JOB_ID) VALUES (?,?)", new Object[]{systemId, jobId});
    }

    @Override
    public void insertMapReduceJobInfo(String systemId, String jobId, Map job, String jobConf, String attempts, String counters, String jobType, String username) {
        jdbcTemplate.update("INSERT INTO FL_CL_MR_DUMP (SYSTEM, JOB_ID, NAME, QUEUE, USER, STATE, USERNAME, TYPE, MAPS_TOTAL, MAPS_COMPLETED, REDUCES_TOTAL, REDUCES_COMPLETED, FAILED_MAP_ATTEMPTS, KILLED_MAP_ATTEMPTS, FAILED_REDUCE_ATTEMPTS, KILLED_REDUCE_ATTEMPTS, AVG_MAP_TIME, AVG_SHUFFLE_TIME, AVG_MERGE_TIME, AVG_REDUCE_TIME, SUBMIT_TIME, START_TIME, FINISH_TIME, COUNTERS, CONFIGURATION, TASKS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                systemId,
                jobId,
                job.get("name"),
                job.get("queue"),
                job.get("user"),
                job.get("state"),
                username,
                jobType,
                job.get("mapsTotal"),
                job.get("mapsCompleted"),
                job.get("reducesTotal"),
                job.get("reducesCompleted"),
                job.get("failedMapAttempts"),
                job.get("killedMapAttempts"),
                job.get("failedReduceAttempts"),
                job.get("killedReduceAttempts"),
                job.get("avgMapTime"),
                job.get("avgShuffleTime"),
                job.get("avgMergeTime"),
                job.get("avgReduceTime"),
                new Date((Long) job.get("submitTime")),
                new Date((Long) job.get("startTime")),
                new Date((Long) job.get("finishTime")),
                counters,
                jobConf,
                attempts
        });
    }
}


