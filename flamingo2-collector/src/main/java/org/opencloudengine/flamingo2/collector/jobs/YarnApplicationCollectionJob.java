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

import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.opencloudengine.flamingo2.agent.resourcemanager.ResourceManagerAgentService;
import org.opencloudengine.flamingo2.collector.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.collector.repository.YarnApplicationService;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class YarnApplicationCollectionJob extends RemoteInvocation {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(YarnApplicationCollectionJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("YARN Application의 수집을 시작합니다.");

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        Map<String, SystemConfig> configs = ConfigurationHolder.getConfigs();
        YarnApplicationService yarnApplicationService = applicationContext.getBean(YarnApplicationService.class);

        Collection<SystemConfig> values = configs.values();
        for (SystemConfig config : values) {
            String address = config.getRmAgentAddress();
            int port = config.getRmAgentPort();

            ResourceManagerAgentService resourceManagerAgentService = getResourceManagerAgentService(address, port);
            try {
                List applications = resourceManagerAgentService.listApplications(null, null, true);
                int count = 1;
                for (Object obj : applications) {
                    int cnt = 5;
                    if (System.getProperty("yarn.app.count") != null) {
                        try {
                            cnt = Integer.parseInt(System.getProperty("yarn.app.count"));
                        } catch (Exception ex) {
                            cnt = 5;
                        }
                    }
                    if (count > cnt) {
                        logger.info("YARN Application의 수집을 완료했습니다.");
                        return;
                    }
                    Map application = (Map) obj;

                    String applicationId = (String) application.get("applicationId");
                    String applicationType = (String) application.get("applicationType");

                    try {
                        if (!yarnApplicationService.exist(config.getId(), applicationId)) {
                            Map<String, Object> applicationReport = resourceManagerAgentService.getApplicationReport(applicationId);
                            FinalApplicationStatus finalApplicationStatus = (FinalApplicationStatus) applicationReport.get("finalApplicationStatus");
                            switch (finalApplicationStatus) {
                                case FAILED:
                                case KILLED:
                                case SUCCEEDED:
                                    String log = resourceManagerAgentService.getApplicationLog(applicationId, (String) applicationReport.get("user"));
                                    logger.info("YARN Application :: {} at {}", applicationId, DateUtils.parseDate(new Date((Long) application.get("startTime")), "yyyy-MM-dd HH:mm:ss"));
                                    yarnApplicationService.confirm(config.getId(), applicationId, applicationType, applicationReport, log);
                                    count++;
                                    break;
                                default:
                                    break;
                            }
                        }
                    } catch (Exception ex) {
                        logger.warn("YARN Application 정보를 수집하고 저장할 수 없습니다.", ex);
                    }
                }
            } catch (Exception e) {
                logger.warn("YARN Application 목록을 얻을 수 없습니다.", e);
            }
        }
    }

    private ResourceManagerAgentService getResourceManagerAgentService(String agentAddress, int agentPort) {
        String remoteServiceUrl = this.getRemoteServiceUrl(agentAddress, agentPort, "resourcemanager");
        return this.getRemoteService(remoteServiceUrl, ResourceManagerAgentService.class);
    }
}
