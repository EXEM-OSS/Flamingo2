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
package org.opencloudengine.flamingo2.collector.repository;

import org.opencloudengine.flamingo2.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
@Repository
public class SystemMetricsRepositoryImpl implements SystemMetricsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insert(String system, String name, String type, Map map) {
        Date date = new Date();
        jdbcTemplate.update("INSERT INTO FL_CL_SYS_METRICS (system, name, type, hostname, ip, heapMax, heapUsed, heapTotal, heapFree, cpuSys, cpuIdle, cpuUser, procCpuUser, procCpuSys, procCpuTotal, procCpuPer, reg_dt, yyyy, mm, dd) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                system,
                name,
                type,
                map.get("hostname"),
                map.get("ip"),
                map.get("heap-max"),
                map.get("heap-used"),
                map.get("heap-total"),
                map.get("heap-free"),
                map.get("cpu-sys"),
                map.get("cpu-idle"),
                map.get("cpu-user"),
                map.get("proc-cpu-user"),
                map.get("proc-cpu-sys"),
                map.get("proc-cpu-total"),
                map.get("proc-cpu-per"),
                date,
                DateUtils.parseDate(date, "yyyy"),
                DateUtils.parseDate(date, "MM"),
                DateUtils.parseDate(date, "dd")
        });
    }
}
