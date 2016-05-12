/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.engine.hadoop;

import java.util.Map;

public interface MapReduceRemoteService {

    public Map<String, Object> getAllJobs();

    public Map<String, Object> getJobInfo(String jobId);

    public Map<String, Object> getJobConf(String JobID);

    public Map<String, Object> getJob(String jobId);

    public Map<String, Object> getAttempts(String jobId);

    public Map<String, Object> getCounters(String jobId);

    public Map<String, Object> getTasks(String jobId);

    public Map<String, Object> getTaskCounters(String jobId, String taskId);

    public Map<String, Object> getTaskAttempts(String jobId, String taskId);

    public Map<String, Object> getTaskAttemptsCounters(String jobId, String taskId, String attemptId);
}
