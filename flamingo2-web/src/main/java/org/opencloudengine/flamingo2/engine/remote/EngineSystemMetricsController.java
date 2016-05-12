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
package org.opencloudengine.flamingo2.engine.remote;

import org.hyperic.sigar.SigarProxy;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.util.SystemUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/monitoring/engine")
public class EngineSystemMetricsController extends DefaultController {

    @Autowired
    private SigarProxy sigar;

    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map metrics() {
        try {
            Map map = SystemUtils.getSystemMetrics(sigar);
            map.put("type", "engine");
            return map;
        } catch (Exception ex) {
            throw new ServiceException("Unable to collect the system metrics of Flamingo Engine.", ex);
        }
    }

}