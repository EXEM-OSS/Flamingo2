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

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.hadoop.RemoteInvocation;
import org.opencloudengine.flamingo2.engine.monitoring.AlarmRemoteService;
import org.opencloudengine.flamingo2.engine.remote.EngineService;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHolder;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.web.remote.EngineLookupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmServiceImpl extends RemoteInvocation implements AlarmService {

    @Override
    public Map getAlarm(EngineConfig engineConfig) {
        try {
            Object engines[] = ConfigurationHolder.getEngines().keySet().toArray();
            Map messageMap = new HashMap();

            EngineService engineService = getEngineService(engineConfig);
            List<Map> messageList = new ArrayList<>();

            AlarmRemoteService service = engineService.getAlarmRemoteService();
            Map nodeMap = service.getDatanodes(engineConfig);
            Map rmMap = service.getNodemanagers(engineConfig);

            messageList.add(nodeMap);
            messageList.add(rmMap);
            messageMap.put(engineConfig.getId(), messageList);

            return messageMap;
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private EngineService getEngineService(EngineConfig engineConfig) {
        return EngineLookupService.lookup(engineConfig);
    }
}
