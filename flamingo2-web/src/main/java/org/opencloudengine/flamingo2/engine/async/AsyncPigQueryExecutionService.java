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
import org.opencloudengine.flamingo2.engine.pig.PigRemoteService;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.event.Event;
import reactor.function.Consumer;

import java.util.Map;

@Service
public class AsyncPigQueryExecutionService implements ConsumerNameAware, Consumer<Event<Map>> {

    @Autowired
    PigRemoteService service;

    @Override
    public String getName() {
        return "pigQuery";
    }

    @Override
    public void accept(Event<Map> event) {
        Map params = event.getData();
        EngineConfig engineConfig = (EngineConfig) params.get("engineConfig");
        service.execute(engineConfig, params);
    }
}
