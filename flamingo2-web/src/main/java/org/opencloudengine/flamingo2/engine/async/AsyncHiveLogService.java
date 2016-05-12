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
package org.opencloudengine.flamingo2.engine.async;

import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.backend.ConsumerNameAware;
import org.opencloudengine.flamingo2.engine.hive.HiveQueryRemoteService;
import org.opencloudengine.flamingo2.util.EscapeUtils;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.event.Event;
import reactor.function.Consumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AsyncHiveLogService implements ConsumerNameAware, Consumer<Event<Map>> {
    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(AsyncHiveLogService.class);

    /**
     * WebSocket Util Send to Client
     */
    @Autowired
    private WebSocketUtil webSocketUtil;

    @Autowired
    private HiveQueryRemoteService hiveQueryRemoteService;

    @Override
    public String getName() {
        return "hiveLog";
    }

    @Override
    public void accept(Event<Map> event) {

        Map messageMap = new HashMap();
        Map params = event.getData();
        String uuid = EscapeUtils.unescape((String) params.get("uuid"));
        //TODO WebSocket 연결이 안되있는 경우 예외처리.
        String websocketKey = EscapeUtils.unescape((String) params.get("websocketKey"));
        String log, message;
        EngineConfig engineConfig = (EngineConfig) params.get("engineConfig");

        try {

            logger.info(hiveQueryRemoteService.getStatus(uuid));

            while (!hiveQueryRemoteService.isEnd(uuid)) {
                messageMap = new HashMap();
                String error = hiveQueryRemoteService.getErrorLog(uuid);
                if (StringUtils.isEmpty(error)) {
                    log = hiveQueryRemoteService.getLog(uuid);
                    log = log.replaceAll("\n$", "");

                    if (!log.isEmpty()) {
                        messageMap.put("isError", false);
                        messageMap.put("isEnd", false);
                        messageMap.put("message", log);
                        messageMap.put("uuid", uuid);

                        message = objectMapper.writeValueAsString(messageMap);
                        webSocketUtil.PushNotification(websocketKey, "/topic/hive", message);
                    }
                } else {
                    messageMap.put("isError", true);
                    messageMap.put("isEnd", true);
                    messageMap.put("message", error);
                    messageMap.put("uuid", uuid);

                    message = objectMapper.writeValueAsString(messageMap);
                    webSocketUtil.PushNotification(websocketKey, "/topic/hive", message);
                    break;
                }

                //Thread.sleep(1000);
            }

            log = hiveQueryRemoteService.getLog(uuid);
            log = log.replaceAll("\n$", "");

            messageMap.put("isError", false);
            messageMap.put("isEnd", true);
            messageMap.put("message", log);
            messageMap.put("uuid", uuid);
            messageMap.put("status", hiveQueryRemoteService.getStatus(uuid));
        } catch (Exception e) {
            messageMap.put("isError", true);
            messageMap.put("isEnd", false);
            messageMap.put("message", e.getCause());
            messageMap.put("uuid", uuid);
            messageMap.put("status", hiveQueryRemoteService.getStatus(uuid));
        } finally {
            try {
                message = objectMapper.writeValueAsString(messageMap);
                webSocketUtil.PushNotification(websocketKey, "/topic/hive", message);
            } catch (IOException e) {
            }
        }

    }
}
