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
package org.opencloudengine.flamingo2.core.security;

import org.opencloudengine.flamingo2.model.rest.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 사용자의 요청 발생시 사용자의 세션 정보를 해당 Thread에만 유지시키고 요청이 완료되었을 때 삭제하는 세션 유틸리티.
 * 이 유틸리티는 Session Filter의 도움없이 자체적으로 동작할 수 없음.
 *
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class SessionUtils {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(SessionUtils.class);

    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public static void put(User user) {
        users.set(user);
    }

    public static User get() {
        return users.get();
    }

    public static void remove() {
        users.remove();
    }

    public static Long getId() {
        User user = users.get();
        return user.getId();
    }

    public static String getUsername() {
        User user = users.get();
        return user.getUsername();
    }

    public static String getName() {
        User user = users.get();
        return user.getName();
    }

    public static String getEmail() {
        User user = users.get();
        return user.getEmail();
    }

    public static int getLevel() {
        User user = users.get();
        return user.getLevel();
    }

    public static String getUserGroup() {
        User user = users.get();
        return user.getUserGroup();
    }

    public static String getWebsocketKey() {
        User user = users.get();
        return user.getWebsocketKey();
    }
}