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
        jdbcTemplate.update("INSERT INTO FL_CL_MR_DUMP (system, jobId, name, queue, state, mapsTotal, mapsCompleted, reducesTotal, reducesCompleted, submitTime, startTime, finishTime, counters, configuration, tasks, type, user, username) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                systemId,
                jobId,
                job.get("name"),
                job.get("queue"),
                job.get("state"),
                job.get("mapsTotal"),
                job.get("mapsCompleted"),
                job.get("reducesTotal"),
                job.get("reducesCompleted"),
                new Date((Long) job.get("submitTime")),
                new Date((Long) job.get("startTime")),
                new Date((Long) job.get("finishTime")),
                counters,
                jobConf,
                attempts,
                jobType,
                job.get("user"),
                username
        });
    }


}
