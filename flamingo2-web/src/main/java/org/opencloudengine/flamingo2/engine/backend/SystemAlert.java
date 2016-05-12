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
public class SystemAlert implements InitializingBean {

    @Autowired
    Reactor reactor;

    private static SystemAlert instance;

    public void send(SystemEvent event) {
        reactor.notify("alert", Event.wrap(event));
    }

    public void send(String category, String service, String action, String status, String message) {
        SystemEvent event = SystemEvent.create(category, service, action, status, message);
        reactor.notify("alert", Event.wrap(event));
    }

    public void send(String category, String service, String action, String status, String message, Exception e) {
        SystemEvent event = SystemEvent.create(category, service, action, status, message, e);
        reactor.notify("alert", Event.wrap(event));
    }

    public static Event empty() {
        return Event.wrap(null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SystemAlert.instance = this;
    }

    public static SystemAlert get() {
        return SystemAlert.instance;
    }

    public Reactor getReactor() {
        return reactor;
    }
}
