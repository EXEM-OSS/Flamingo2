package org.opencloudengine.flamingo2.engine.monitoring;

import org.opencloudengine.flamingo2.agent.namenode.Namenode2AgentService;
import org.opencloudengine.flamingo2.agent.resourcemanager.ResourceManagerAgentService;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.hadoop.RemoteInvocation;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Park on 15. 6. 29..
 */
public class AlarmRemoteServiceImpl extends RemoteInvocation implements AlarmRemoteService {
    @Override
    public Map getDatanodes(EngineConfig engineConfig) {

        try {
            Namenode2AgentService nnService = getNamenode2AgentService(engineConfig.getNnAgentAddress(), engineConfig.getNnAgentPort());
            ResourceManagerAgentService rmService = getResourceManagerAgentService(engineConfig.getRmAgentAddress(), engineConfig.getRmAgentPort());
            List<Map<String, Object>> deadList = nnService.getDeadNodes();
            Map<String, Object> rmMap = rmService.getResourceManagerInfo();

            Map returnMap = new HashMap();

            returnMap.put("isAlarm", false);
            returnMap.put("cnt", 0);

            if (deadList.size() > 0) {
                returnMap.put("isAlarm", true);
                returnMap.put("cnt", deadList.size());
            }

            returnMap.put("type", "DATANODE");
            return returnMap;
        } catch (Exception e) {
            throw new ServiceException("Could not get datanode information");
        }
    }

    @Override
    public Map getNodemanagers(EngineConfig engineConfig) {
        try {
            ResourceManagerAgentService rmService = getResourceManagerAgentService(engineConfig.getRmAgentAddress(), engineConfig.getRmAgentPort());
            Map<String, Object> rmMap = rmService.getResourceManagerInfo();

            Map returnMap = new HashMap();

            returnMap.put("isAlarm", false);
            returnMap.put("cnt", 0);

            Map<String, Object> cluster = (Map<String, Object>) rmMap.get("cluster");

            int lost = (int) cluster.get("lostNodes");
            if (lost > 0) {
                returnMap.put("isAlarm", true);
                returnMap.put("cnt", lost);
            }

            returnMap.put("type", "NODEMANAGER");
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Could not get nodemanager information");
        }
    }

    private Namenode2AgentService getNamenode2AgentService(String agentAddress, int agentPort) {
        String remoteServiceUrl = this.getRemoteServiceUrl(agentAddress, agentPort, "namenode2");
        return this.getRemoteService(remoteServiceUrl, Namenode2AgentService.class);
    }

    private ResourceManagerAgentService getResourceManagerAgentService(String agentAddress, int agentPort) {
        String remoteServiceUrl = this.getRemoteServiceUrl(agentAddress, agentPort, "resourcemanager");
        return this.getRemoteService(remoteServiceUrl, ResourceManagerAgentService.class);
    }
}
