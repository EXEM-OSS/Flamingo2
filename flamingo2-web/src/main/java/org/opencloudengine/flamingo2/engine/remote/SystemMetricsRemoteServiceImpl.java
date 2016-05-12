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

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.opencloudengine.flamingo2.util.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;
import java.util.Map;

public class SystemMetricsRemoteServiceImpl implements SystemMetricsRemoteService {

    @Autowired
    private SigarProxy sigar;

    @Override
    public Map getSystemMetrics() throws SigarException, UnknownHostException {
        return SystemUtils.getSystemMetrics(sigar);
    }

}
