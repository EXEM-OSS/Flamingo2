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
package org.opencloudengine.flamingo2.engine.backend;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Reactor;
import reactor.event.Event;

@Component
public class UserAlert implements InitializingBean {

    @Autowired
    Reactor reactor;

    private static UserAlert instance;

    public void send(UserEvent event) {
        reactor.notify("user-event", Event.wrap(event));
    }

    public UserEvent send(String name, String status) {
        UserEvent event = UserEvent.create(name, status);
        reactor.notify("user-event", Event.wrap(event));
        return event;
    }

    public UserEvent start(String name) {
        UserEvent event = UserEvent.create(name, "RUNNING");
        reactor.notify("user-event", Event.wrap(event));
        return event;
    }

    public void end(UserEvent event, String status) {
        event.setStatus(status);
        reactor.notify("user-event", Event.wrap(event));
    }

    public void send(String name, String status, String message, String identifier, Long referenceId) {
        UserEvent event = UserEvent.create(name, status, message, identifier, referenceId);
        reactor.notify("user-event", Event.wrap(event));
    }

    public void send(String name, String status, String message, SystemEvent systemEvent) {
        UserEvent event = UserEvent.create(name, status, message, systemEvent.getIdentifier(), systemEvent.getId());
        reactor.notify("user-event", Event.wrap(event));
    }

    public static Event empty() {
        return Event.wrap(null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UserAlert.instance = this;
    }

    public static UserAlert get() {
        return UserAlert.instance;
    }

    public Reactor getReactor() {
        return reactor;
    }
}
