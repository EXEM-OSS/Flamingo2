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

import org.opencloudengine.flamingo2.backend.ConsumerNameAware;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.util.JsonUtils;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.event.Event;
import reactor.function.Consumer;

import java.io.IOException;
import java.util.Map;

/**
 * HAWQ query 실행시 비동기로 HAWQ 의 Message 를 실행중인 tabpanel 에 보내주는 Service.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
@Service
public class AsyncHawqMessageService implements ConsumerNameAware, Consumer<Event<Map>> {

    @Autowired
    private WebSocketUtil webSocketUtil;

    @Override
    public String getName() {
        return "hawqMessage";
    }

    @Override
    public void accept(Event<Map> event) {
        Map params = event.getData();
        String websocketKey = params.get("websocketKey").toString();
        String destination = "/topic/hawqPid";

        Response response = new Response();
        response.getMap().put("uuid", params.get("uuid"));
        response.getMap().put("pid", params.get("pid"));

        try {
            response.setSuccess(true);
            webSocketUtil.PushNotification(websocketKey, destination, JsonUtils.marshal(response));
        } catch (Exception e) {
            response.setSuccess(false);
            response.getError().setMessage(e.getMessage());
            if (e.getCause() != null) response.getError().setCause(e.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(e));
            try {
                webSocketUtil.PushNotification(websocketKey, destination, JsonUtils.marshal(response));
            } catch (IOException e1) {
            }
        }
    }
}
