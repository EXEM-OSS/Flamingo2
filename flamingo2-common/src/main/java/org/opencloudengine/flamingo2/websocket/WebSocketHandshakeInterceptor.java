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

import org.opencloudengine.flamingo2.model.rest.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

    @Autowired
    WebSocketUtil webSocketUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
        HttpSession session = serverRequest.getServletRequest().getSession(false);

        try {
            String uri[] = request.getURI().toString().split("/");
            String websocketKey = uri[uri.length - 2];

            session.setAttribute("websocketKey", websocketKey);
            User user = (User) session.getAttribute("user");
            user.setWebsocketKey(websocketKey);
            session.setAttribute("user", user);

            logger.info("HandShake: " + websocketKey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
