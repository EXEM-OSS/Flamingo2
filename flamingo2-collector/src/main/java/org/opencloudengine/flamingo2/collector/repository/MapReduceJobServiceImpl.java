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

import org.opencloudengine.flamingo2.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
@Service
public class MapReduceJobServiceImpl implements MapReduceJobService {

    @Autowired
    private MapReduceJobRepository repository;


    @Override
    public boolean exist(String systemId, String jobId) {
        return repository.selectByJobId(systemId, jobId) != null;
    }

    @Override
    public void confirm(String systemId, String jobId, Map job, Map jobConf, Map attempts, Map counters) throws IOException {
        String jobType = getJobType(jobConf);
        String username = getUsername(jobConf);
        repository.insertMapReduceJobInfo(
                systemId,
                jobId,
                job,
                jobConf != null ? JsonUtils.format(jobConf) : "",
                attempts != null ? JsonUtils.format(attempts) : "",
                counters != null ? JsonUtils.format(counters) : "",
                jobType, username
        );
        repository.insertMapReduceJobConfirm(systemId, jobId);
    }

    private String getUsername(Map jobConf) {
        if (jobConf.get("flamingo.username") != null) {
            return (String) jobConf.get("flamingo.username");
        }
        return "Anonymous";
    }

    private String getJobType(Map jobConf) {
        Map conf = (Map) jobConf.get("conf");
        List properties = (List) conf.get("property");
        for (Object obj : properties) {
            Map entry = (Map) obj;
            if (entry.get("name").equals("pig.hadoop.version")) {
                return "PIG";
            } else if (entry.get("name").equals("hive.exec.plan")) {
                return "HIVE";
            }
        }
        return "MAPREDUCE";
    }
}