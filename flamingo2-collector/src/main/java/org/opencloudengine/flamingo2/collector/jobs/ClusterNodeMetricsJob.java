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

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;
import org.opencloudengine.flamingo2.agent.resourcemanager.ResourceManagerAgentService;
import org.opencloudengine.flamingo2.collector.ApplicationContextRegistry;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Resource Manager Agent를 통해 클러스터 노드 정보를 수집하는 Job.
 *
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class ClusterNodeMetricsJob extends RemoteInvocation {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(ClusterNodeMetricsJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Resource Manager의 클러스터 노드 정보를 수집합니다.");

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        Map<String, SystemConfig> configs = ConfigurationHolder.getConfigs();
        Properties configProps = (Properties) applicationContext.getBean("config");

        InfluxDB influxDB = InfluxDBFactory.connect(configProps.getProperty("influxdb.url"), configProps.getProperty("influxdb.username"), configProps.getProperty("influxdb.password"));

        Collection<SystemConfig> values = configs.values();
        for (SystemConfig config : values) {
            String address = config.getRmAgentAddress();
            int port = config.getRmAgentPort();

            ResourceManagerAgentService resourceManagerAgentService = getResourceManagerAgentService(address, port);

            try {
                List nodes = resourceManagerAgentService.getNodes();
                int totalMemorySum = 0;
                int usedMemorySum = 0;
                int nodeSum = nodes.size();
                int containerSum = 0;
                int totalVCoreSum = 0;
                int usedVCoreSum = 0;

                int NEW = 0;
                int RUNNING = 0;
                int UNHEALTHY = 0;
                int DECOMMISSIONED = 0;
                int LOST = 0;
                int REBOOTED = 0;

                for (Object object : nodes) {
                    Map node = (Map) object;
                    Integer usedMemory = (Integer) node.get("usedMemory");
                    usedMemorySum += usedMemory;

                    Integer capacityMemory = (Integer) node.get("capacityMemory");
                    totalMemorySum += capacityMemory;

                    String nodeState = (String) node.get("nodeState");
                    switch (nodeState) {
                        case "NEW":
                            NEW++;
                            break;
                        case "RUNNING":
                            RUNNING++;
                            break;
                        case "UNHEALTHY":
                            UNHEALTHY++;
                            break;
                        case "DECOMMISSIONED":
                            DECOMMISSIONED++;
                            break;
                        case "LOST":
                            LOST++;
                            break;
                        case "REBOOTED":
                            REBOOTED++;
                            break;
                    }

                    Integer numContainers = (Integer) node.get("numContainers");
                    containerSum += numContainers;

                    Integer capacityVcores = (Integer) node.get("capacityVcores");
                    totalVCoreSum += capacityVcores;

                    Integer usedVcores = (Integer) node.get("usedVcores");
                    usedVCoreSum += usedVcores;
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                Serie series = new Serie.Builder("clusternode")
                        .columns(
                                "id", "name", "timestamp", "totalMemorySum", "usedMemorySum", "nodeSum", "containerSum", "totalVCoreSum", "usedVCoreSum", "NEW", "RUNNING", "UNHEALTHY", "DECOMMISSIONED", "LOST", "REBOOTED"
                        )
                        .values(
                                config.getId(),
                                config.getName(),
                                formatter.format(new Date()),
                                totalMemorySum,
                                usedMemorySum,
                                nodeSum,
                                containerSum,
                                totalVCoreSum,
                                usedVCoreSum,
                                NEW,
                                RUNNING,
                                UNHEALTHY,
                                DECOMMISSIONED,
                                LOST,
                                REBOOTED
                        )
                        .build();
                influxDB.write(configProps.getProperty("influxdb.dbname"), TimeUnit.SECONDS, series);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private ResourceManagerAgentService getResourceManagerAgentService(String agentAddress, int agentPort) {
        String remoteServiceUrl = this.getRemoteServiceUrl(agentAddress, agentPort, "resourcemanager");
        return this.getRemoteService(remoteServiceUrl, ResourceManagerAgentService.class);
    }
}
