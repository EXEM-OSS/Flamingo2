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
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.hive.HiveQueryRemoteService;
import org.opencloudengine.flamingo2.util.EscapeUtils;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

import java.util.HashMap;
import java.util.Map;

@Service
public class AsyncHiveQueryExecutionService implements ConsumerNameAware, Consumer<Event<Map>> {

    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * WebSocket Util Send to Client
     */
    @Autowired
    private WebSocketUtil webSocketUtil;

    @Autowired
    private Reactor reactor;

    @Autowired
    private HiveQueryRemoteService hiveQueryRemoteService;

    @Override
    public String getName() {
        return "query";
    }

    @Override
    public void accept(Event<Map> event) {
        Map params = event.getData();
        Map messageMap = new HashMap();
        String uuid = EscapeUtils.unescape((String) params.get("uuid"));
        String websocketKey = EscapeUtils.unescape((String) params.get("websocketKey"));

        try {
            hiveQueryRemoteService.execute(params);
        } catch (Exception ex) {
            messageMap.put("isError", true);
            messageMap.put("isEnd", true);
            messageMap.put("message", ex.getCause().getMessage());
            messageMap.put("uuid", uuid);
            try {
                String message = objectMapper.writeValueAsString(messageMap);
                webSocketUtil.PushNotification(websocketKey, "/topic/hive", message);
            } catch (Exception e) {
                throw new ServiceException(e);
            }

        }

    }
}
