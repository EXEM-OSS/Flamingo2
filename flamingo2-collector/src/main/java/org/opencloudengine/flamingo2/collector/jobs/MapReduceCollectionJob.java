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

import org.opencloudengine.flamingo2.collector.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.collector.repository.MapReduceJobService;
import org.opencloudengine.flamingo2.engine.hadoop.HistoryServerRemoteService;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class MapReduceCollectionJob extends RemoteInvocation {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(MapReduceCollectionJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("MapReduce Job의 수집을 시작합니다.");

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        Map<String, SystemConfig> configs = ConfigurationHolder.getConfigs();
        MapReduceJobService mapReduceJobService = applicationContext.getBean(MapReduceJobService.class);

        Collection<SystemConfig> values = configs.values();
        for (SystemConfig config : values) {
            String address = config.getWebIp();
            int port = config.getWebPort();

            HistoryServerRemoteService historyServerRemoteService = getHistoryServerRemoteService(address, port);
            try {
                Map<String, Object> res = historyServerRemoteService.getJobs(config.getHistoryServerUrl());
                if (res.get("jobs") != null) {
                    List jobs = (List) ((Map) res.get("jobs")).get("job");
                    if (jobs.size() > 0) {
                        int count = 1;
                        for (Object obj : jobs) {
                            int cnt = 5;
                            if (System.getProperty("mapreduce.job.count") != null) {
                                try {
                                    cnt = Integer.parseInt(System.getProperty("mapreduce.job.count"));
                                } catch (Exception ex) {
                                    cnt = 5;
                                }
                            }
                            if (count > cnt) {
                                return;
                            }

                            try {
                                Map job = (Map) obj;
                                String jobId = (String) job.get("id");
                                Map<String, Object> attempts = historyServerRemoteService.getAttempts(config.getHistoryServerUrl(), jobId);
                                Map<String, Object> counters = historyServerRemoteService.getCounters(config.getHistoryServerUrl(), jobId);
                                Map<String, Object> jobConf = historyServerRemoteService.getJobConf(config.getHistoryServerUrl(), jobId);

                                if (!mapReduceJobService.exist(config.getId(), jobId)) {
                                    count++;
                                    logger.info("MapReduce Job :: {} at {}", jobId, DateUtils.parseDate(new Date((Long) job.get("submitTime")), "yyyy-MM-dd HH:mm:ss"));
                                    mapReduceJobService.confirm(config.getId(), jobId, job, jobConf, attempts, counters);
                                }
                            } catch (Exception ex) {
                                logger.warn("MapReduce Job 정보를 수집하고 저장할 수 없습니다.", ex);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("MapReduce Job 목록을 얻을 수 없습니다.", e);
            }
        }
    }

    private HistoryServerRemoteService getHistoryServerRemoteService(String address, int port) {
        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceUrl(getRemoteServiceUrl(address, port));
        factoryBean.setServiceInterface(HistoryServerRemoteService.class);
        factoryBean.afterPropertiesSet();
        return (HistoryServerRemoteService) factoryBean.getObject();
    }

    private String getRemoteServiceUrl(String address, int port) {
        return arrayFormat("http://{}:{}/remote/history", new Object[]{address, port}).getMessage();
    }
}
