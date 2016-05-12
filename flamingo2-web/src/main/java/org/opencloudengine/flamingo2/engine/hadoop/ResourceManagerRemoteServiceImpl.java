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
import org.opencloudengine.flamingo2.agent.resourcemanager.ResourceManagerAgentService;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Resource Manager에 배포되어 있는 Resource Manager Agent와 호출하여 Resource Manager 관련 기능을 제공하는 Resource Manager 서비스 구현체.
 * Resource Manager Agent와 Remote Invocation을 수행하며 관련 기능을 제공한다.
 *
 * @See <a href="http://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-site/WritingYarnApplications.html">Writing Yarn Applications</a>
 */
public class ResourceManagerRemoteServiceImpl extends RemoteInvocation implements ResourceManagerRemoteService {

    public static final String RESOURCE_MANAGER_SERVICE = "resourcemanager";

    @Override
    public void killApplication(String applicationId, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            rmAgentService.killApplication(applicationId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to terminate a application.", ex);
        }
    }

    @Override
    public Map<String, Object> getAppStatInfo(String applicationId, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getAppStatInfo(applicationId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the status information of Application Id in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getResourceManagerInfo(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getResourceManagerInfo();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the information of resource manager in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getJVMHeap(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getJVMHeap();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the heap of JVM in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getClusterMetrics(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getClusterMetrics();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the information of Cluster in Resource Manager Agent.", ex);
        }
    }

    @Override
    public List getAllQueues(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getAllQueues();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the queues in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getApplicationAttemptReport(String applicationAttemptId, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getApplicationAttemptReport(applicationAttemptId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the attempt id of application in Resource Manager Agent.", ex);
        }
    }

    @Override
    public String getApplicationLog(String applicationId, EngineConfig engineConfig) throws IOException {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getApplicationLog(applicationId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the log of application in Resource Manager Agent.", ex);
        }
    }

    @Override
    public String getApplicationLog(String applicationId, String appOwner, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getApplicationLog(applicationId, appOwner);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the log of application in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getApplicationReport(String applicationId, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getApplicationReport(applicationId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the status of application in Resource Manager Agent.", ex);
        }
    }

    @Override
    public List getApplicationsOfQueue(String queueName, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getApplicationsOfQueue(queueName);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the running applications of queue in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, String> getBlockers(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getBlockers();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the blocked nodes in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getConfiguration(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getConfiguration();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the configuration of resource manager in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getContainerReport(String containerId, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getContainerReport(containerId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the log of container in Resource Manager Agent.", ex);
        }
    }

    @Override
    public String getContainersLogs(String appId, String containerId, String nodeId, String jobOwner, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getContainersLogs(appId, containerId, nodeId, jobOwner);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the container's log of application in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getNodeStatus(String nodeIdStr, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getNodeStatus(nodeIdStr);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the status of node in Resource Manager Agent.", ex);
        }
    }

    @Override
    public List getNodes(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getNodes();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the nodes of cluster in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map<String, Object> getQueueInfo(String queueName, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getQueueInfo(queueName);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the information of queue in Resource Manager Agent.", ex);
        }
    }

    @Override
    public Map getSystemMetrics(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.getSystemMetrics();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the information of system in Resource Manager Agent.", ex);
        }
    }

    @Override
    public List listApplicationAttempts(String applicationId, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.listApplicationAttempts(applicationId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the attempt ids of application in Resource Manager Agent.", ex);
        }
    }

    @Override
    public List listApplications(Set<String> appTypes, List<String> appStates, boolean allAppStates, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.listApplications(appTypes, appStates, allAppStates);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the applications by status in Resource Manager Agent.", ex);
        }
    }

    public List listMapReduceJobsOfApplication(String applicationId, EngineConfig engineConfig) {
        HistoryServerRemoteService historyServerService = ApplicationContextRegistry.getApplicationContext().getBean(HistoryServerRemoteService.class);
        try {
            return historyServerService.getJobsByApplication(applicationId, engineConfig.getWebApplicationServerUrl());
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the MapReduce Jobs of application in Resource Manager Agent.", ex);
        }
    }

    @Override
    public List listClusterNodes(Set<NodeState> nodeStates, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.listClusterNodes(nodeStates);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the nodes by status in Resource Manager Agent.", ex);
        }
    }

    @Override
    public List listContainers(String appAttemptId, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.listContainers(appAttemptId);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the containers of attempt id in Resource Manager Agent.", ex);
        }
    }

    @Override
    public void moveApplicationAcrossQueues(String applicationId, String queue, EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            rmAgentService.moveApplicationAcrossQueues(applicationId, queue);
        } catch (Exception ex) {
            throw new ServiceException("Unable to move an application to another specified Queue.", ex);
        }
    }

    @Override
    public List getRunningApplications(EngineConfig engineConfig) {
        ResourceManagerAgentService rmAgentService = getResourceManagerAgentService(engineConfig);
        try {
            return rmAgentService.listApplications(null, new ArrayList(), false);
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the running applications.", ex);
        }
    }

    /**
     * Resource Manager의 JVM에 배포되어 있는 Resource Manager Agent의 서비스를 획득한다.
     *
     * @param engineConfig Resource Manager Agent 정보
     * @return {@link org.opencloudengine.flamingo2.agent.resourcemanager.ResourceManagerAgentService}
     */
    private ResourceManagerAgentService getResourceManagerAgentService(EngineConfig engineConfig) {
        String agentIp = engineConfig.getRmAgentAddress();
        int agentPort = engineConfig.getRmAgentPort();
        String remoteServiceUrl = this.getRemoteServiceUrl(agentIp, agentPort, RESOURCE_MANAGER_SERVICE);
        return this.getRemoteService(remoteServiceUrl, ResourceManagerAgentService.class);
    }
}