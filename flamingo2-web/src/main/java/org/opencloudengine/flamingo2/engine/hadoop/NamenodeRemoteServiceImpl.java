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

import org.opencloudengine.flamingo2.agent.namenode.Namenode2AgentService;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.monitoring.HdfsRepository;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamenodeRemoteServiceImpl extends RemoteInvocation implements NamenodeRemoteService {

    public static final String NAMENODE_SERVICE = "namenode2";

    @Autowired
    private HdfsRepository hdfsRepository;

    @Override
    public Map getNamenodeInfo(EngineConfig engineConfig) throws IOException {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getNamenodeInfo();
    }

    @Override
    public Map getConfiguration(EngineConfig engineConfig) throws IOException {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getConfiguration();
    }

    @Override
    public Map getMetrics(EngineConfig engineConfig) throws IOException {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getJVMHeap();
    }

    @Override
    public List getDatanodes(EngineConfig engineConfig) throws IOException {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getDatanodes();
    }

    @Override
    public String getFileContents(EngineConfig engineConfig, String path, int chunkSize, long startOffset) throws IOException {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getFileContents(path, chunkSize, startOffset);
    }

    @Override
    public Map<String, Long> getJVMHeap(EngineConfig engineConfig) throws IOException {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getJVMHeap();
    }

    @Override
    public List getDataNodes(EngineConfig engineConfig) {
        try {
            Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
            return namenode2AgentService.getDatanodes();
        } catch (Exception ex) {
            throw new ServiceException("Unable to retrieve the information of datanodes. ", ex);
        }
    }

    @Override
    public List getLiveNodes(EngineConfig engineConfig) {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getLiveNodes();
    }

    @Override
    public List getDeadNodes(EngineConfig engineConfig) {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getDeadNodes();
    }

    @Override
    public List getDecommissioningNodes(EngineConfig engineConfig) {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getDecommissioningNodes();
    }

    @Override
    public List getTop5(EngineConfig engineConfig) throws IOException {
        Namenode2AgentService namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getTop5();
    }

    @Override
    public List getRecentTrend(EngineConfig engineConfig) {
        Map params = new HashMap();
        params.put("system", engineConfig.getId());
        return hdfsRepository.selectNamenodeMetrics(params);
    }

    private Namenode2AgentService getNamenode2AgentService(EngineConfig engineConfig) {
        String agentIp = engineConfig.getNnAgentAddress();
        int agentPort = engineConfig.getNnAgentPort();
        String remoteServiceUrl = this.getRemoteServiceUrl(agentIp, agentPort, NAMENODE_SERVICE);
        return this.getRemoteService(remoteServiceUrl, Namenode2AgentService.class);
    }
}
