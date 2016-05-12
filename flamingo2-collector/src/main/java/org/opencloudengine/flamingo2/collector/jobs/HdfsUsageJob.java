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
package org.opencloudengine.flamingo2.collector.jobs;

import org.opencloudengine.flamingo2.agent.namenode.Namenode2AgentService;
import org.opencloudengine.flamingo2.agent.resourcemanager.ResourceManagerAgentService;
import org.opencloudengine.flamingo2.collector.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.collector.repository.HdfsService;
import org.opencloudengine.flamingo2.collector.repository.MapReduceJobService;
import org.opencloudengine.flamingo2.engine.hadoop.HistoryServerRemoteService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class HdfsUsageJob extends RemoteInvocation {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(HdfsUsageJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("HDFS 사용량 정보 수집을 시작합니다.");

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        Map<String, SystemConfig> configs = ConfigurationHolder.getConfigs();
        HdfsService hdfsService = applicationContext.getBean(HdfsService.class);

        Collection<SystemConfig> values = configs.values();
        for (SystemConfig config : values) {
            String address = config.getNnAgentAddress();
            int port = config.getNnAgentPort();

            Namenode2AgentService namenode2AgentService = getNamenode2AgentService(address, port);
            try {
                Map<String, Object> namenodeInfo = namenode2AgentService.getNamenodeInfo();
                hdfsService.collect(config.getId(), config.getName(), namenodeInfo);
            } catch (Exception e) {
                logger.warn("HDFS 사용량 정보를 수집할 수 없습니다.", e);
            }
        }
    }

    private Namenode2AgentService getNamenode2AgentService(String agentAddress, int agentPort) {
        String remoteServiceUrl = this.getRemoteServiceUrl(agentAddress, agentPort, "namenode2");
        return this.getRemoteService(remoteServiceUrl, Namenode2AgentService.class);
    }
}
