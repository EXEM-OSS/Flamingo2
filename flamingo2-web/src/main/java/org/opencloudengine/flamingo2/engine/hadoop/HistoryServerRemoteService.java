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

import java.util.List;
import java.util.Map;

public interface HistoryServerRemoteService {

    Map<String, Object> getHistoryServerInfo(String historyServerUrl);

    Map<String, Object> getJobInfo(String historyServerUrl, String jobId);

    Map<String, Object> getJobs(String historyServerUrl);

    Map<String, Object> getJob(String historyServerUrl, String jobId);

    Map<String, Object> getJobToList(String historyServerUrl, String jobId);

    Map<String, Object> getAttempts(String historyServerUrl, String jobId);

    Map<String, Object> getCounters(String historyServerUrl, String jobId);

    Map<String, Object> getJobConf(String historyServerUrl, String jobId);

    Map<String, Object> getTasks(String historyServerUrl, String jobId);

    Map<String, Object> getTaskCounters(String historyServerUrl, String jobId, String taskId);

    Map<String, Object> getTaskAttempts(String historyServerUrl, String jobId, String taskId);

    Map<String, Object> getTaskAttemptsCounters(String historyServerUrl, String jobId, String taskId, String attemptId);

    Map<String, Object> getJobConfByProxy(String proxyServerUrl, String jobId);

    Map<String, Object> getJobByProxy(String proxyServerUrl, String jobId);

    Map<String, Object> getAttemptsByProxy(String proxyServerUrl, String jobId);

    Map<String, Object> getCountersByProxy(String proxyServerUrl, String jobId);

    Map<String, Object> getTasksByProxy(String proxyServerUrl, String jobId);

    Map<String, Object> getTaskCountersByProxy(String proxyServerUrl, String jobId, String taskId);

    Map<String, Object> getTaskAttemptsByProxy(String proxyServerUrl, String jobId, String taskId);

    Map<String, Object> getTaskAttemptsCountersByProxy(String proxyServerUrl, String jobId, String taskId, String attemptId);

    List getJobsByApplication(String applicationId, String proxyServerUrl);
}
