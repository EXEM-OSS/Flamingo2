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
package org.opencloudengine.flamingo2.web.monitoring;

import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHolder;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.Map;

public class AlarmJob extends QuartzJobBean {

    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(AlarmJob.class);

    private WebSocketUtil webSocketUtil = ApplicationContextRegistry.getApplicationContext().getBean(WebSocketUtil.class);

    private AlarmService alarmService = ApplicationContextRegistry.getApplicationContext().getBean(AlarmService.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("Now checking the nodes information of Hadoop.");

        try {
            Object engines[] = ConfigurationHolder.getEngines().keySet().toArray();
            Map messageMap = new HashMap();

            for (Object engine : engines) {
                EngineConfig engineConfig = ConfigurationHolder.getEngine((String) engine);

                messageMap.putAll(alarmService.getAlarm(engineConfig));
            }

            String message = objectMapper.writeValueAsString(messageMap);
            webSocketUtil.pushNotification("/topic/alarm", message);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }

    }
}
