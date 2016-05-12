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

import org.apache.hadoop.yarn.api.records.NodeState;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResourceManagerRemoteService {

    void killApplication(String applicationId, EngineConfig engineConfig);

    Map<String, Object> getAppStatInfo(String applicationId, EngineConfig engineConfig);

    Map<String, Object> getResourceManagerInfo(EngineConfig engineConfig);

    Map<String, Object> getJVMHeap(EngineConfig engineConfig);

    Map<String, Object> getClusterMetrics(EngineConfig engineConfig);

    List getAllQueues(EngineConfig engineConfig);

    Map<String, Object> getApplicationAttemptReport(String applicationAttemptId, EngineConfig engineConfig);

    String getApplicationLog(String applicationId, EngineConfig engineConfig) throws IOException;

    String getApplicationLog(String applicationId, String appOwner, EngineConfig engineConfig) throws IOException;

    Map<String, Object> getApplicationReport(String applicationId, EngineConfig engineConfig);

    List getApplicationsOfQueue(String queueName, EngineConfig engineConfig);

    Map<String, String> getBlockers(EngineConfig engineConfig);

    Map<String, Object> getConfiguration(EngineConfig engineConfig);

    Map<String, Object> getContainerReport(String containerId, EngineConfig engineConfig);

    String getContainersLogs(String appId, String containerId, String nodeId, String jobOwner, EngineConfig engineConfig);

    Map<String, Object> getNodeStatus(String nodeIdStr, EngineConfig engineConfig);

    List getNodes(EngineConfig engineConfig);

    Map<String, Object> getQueueInfo(String queueName, EngineConfig engineConfig);

    Map getSystemMetrics(EngineConfig engineConfig);

    List listApplicationAttempts(String applicationId, EngineConfig engineConfig);

    List listApplications(Set<String> appTypes, List<String> appStates, boolean allAppStates, EngineConfig engineConfig);

    List listClusterNodes(Set<NodeState> nodeStates, EngineConfig engineConfig);

    List listContainers(String appAttemptId, EngineConfig engineConfig);

    void moveApplicationAcrossQueues(String applicationId, String queue, EngineConfig engineConfig);

    List getRunningApplications(EngineConfig engineConfig);
}
