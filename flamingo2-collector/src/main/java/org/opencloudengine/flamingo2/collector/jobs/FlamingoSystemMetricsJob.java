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
import org.opencloudengine.flamingo2.collector.repository.SystemMetricsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class FlamingoSystemMetricsJob extends QuartzJobBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(FlamingoSystemMetricsJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Flamingo의 시스템 메트릭스 정보를 수집합니다.");

        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        SystemMetricsService service = applicationContext.getBean(SystemMetricsService.class);
        Map<String, SystemConfig> configs = ConfigurationHolder.getConfigs();

        Collection<SystemConfig> values = configs.values();
        for (SystemConfig config : values) {
            String address = config.getWebIp();
            int port = config.getWebPort();

            String url = MessageFormatter.arrayFormat("http://{}:{}/monitoring/engine/metrics.json", new Object[]{address, port}).getMessage().toString();
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                Map map = response.getBody();
                service.collect(config.getId(), config.getName(), "engine", map);
            } catch (Exception e) {
                throw new RuntimeException("Flamingo에서 시스템 메트릭스 정보를 확인할 수 없습니다.", e);
            }
        }
    }
}
