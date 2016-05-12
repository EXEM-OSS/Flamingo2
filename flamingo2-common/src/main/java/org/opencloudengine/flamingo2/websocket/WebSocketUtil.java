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
package org.opencloudengine.flamingo2.websocket;

import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.IdTimestampMessageHeaderInitializer;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUtil implements InitializingBean {

	/**
	 * SLF4J Logging
	 */
	private Logger logger = LoggerFactory.getLogger(WebSocketUtil.class);

	/**
	 * The strategy for ID and TIMESTAMP message header generation.
	 */
	private IdTimestampMessageHeaderInitializer idTimestampMessageHeaderInitializer;

	@Autowired
	private SimpMessagingTemplate template;

	public void PushNotification(String websocketKey, String destination, String message) {
		//logger.debug("WebsocketPush : {} {} \n{}", new Object[]{websocketKey, destination, message});
		this.template.convertAndSendToUser(websocketKey, destination, message, createHeaders(websocketKey));
	}

    public void pushNotification(String destination, String message) {
        this.template.convertAndSend(destination, message);
    }

	private MessageHeaders createHeaders(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

		idTimestampMessageHeaderInitializer.initHeaders(headerAccessor);

		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.idTimestampMessageHeaderInitializer = new IdTimestampMessageHeaderInitializer();
	}
}

