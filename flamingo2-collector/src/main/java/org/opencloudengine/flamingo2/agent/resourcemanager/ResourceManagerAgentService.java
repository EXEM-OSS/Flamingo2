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
package org.opencloudengine.flamingo2.agent.resourcemanager;

import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.NodeState;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public interface ResourceManagerAgentService {

    Map<String, Object> getConfiguration();

    Map getSystemMetrics() throws Exception;

    Map<String, Object> getResourceManagerInfo();

    Map<String, Object> getJVMHeap();

    Map<String, Object> getClusterMetrics();

    Map<String, Object> getAppStatInfo(String applicationId);

    void killApplication(String applicationId) throws IOException, YarnException;

    void moveApplicationAcrossQueues(String applicationId, String queue) throws YarnException, IOException;

    Map<String, Object> getApplicationAttemptReport(String applicationAttemptId) throws YarnException, IOException;

    Map<String, Object> getContainerReport(String containerId) throws YarnException, IOException;

    List listApplications(Set<String> appTypes, List<String> appStates, boolean allAppStates) throws YarnException, IOException;

    Map<String, Object> getApplicationReport(String applicationId) throws YarnException, IOException;

    List listApplicationAttempts(String applicationId) throws YarnException, IOException;

    List listContainers(String appAttemptId) throws YarnException, IOException;

    Map<String, String> getBlockers();

    List getAllQueues() throws IOException, YarnException;

    Map<String, Object> getQueueInfo(String queueName) throws IOException, YarnException;

    List getApplicationsOfQueue(String queueName) throws IOException, YarnException;

    Map getApplication(ApplicationReport application);

    List listClusterNodes(Set<NodeState> nodeStates) throws YarnException, IOException;

    Map<String, Object> getNodeStatus(String nodeIdStr) throws YarnException, IOException;

    List getNodes() throws YarnException, IOException;

    String getApplicationLog(String applicationId) throws IOException;

    String getApplicationLog(String applicationId, String appOwner) throws IOException;

    String getContainersLogs(String appId, String containerId, String nodeId, String jobOwner) throws IOException;
}
