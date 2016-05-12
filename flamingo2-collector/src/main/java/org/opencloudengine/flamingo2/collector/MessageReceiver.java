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
package org.opencloudengine.flamingo2.collector;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class MessageReceiver extends ReceiverAdapter implements InitializingBean {

    private String dbName;
    private String username;
    private String password;
    private String influxdb;
    private InfluxDB influxDB;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.influxDB = InfluxDBFactory.connect(influxdb, username, password);
    }

    @Override
    public void receive(Message msg) {
        try {
            Map map = (Map) msg.getObject();
            String type = (String) map.get("type");
            Serie series = null;
            switch (type) {
                case "hiveserver2":
                    series = new Serie.Builder("hiveserver")
                            .columns("name", "hostname", "ip", "heap_max", "heap_used", "heap_total", "heap_free", "cpu_sys",
                                    "cpu_idle", "cpu_user", "proc_cpu_user", "proc_cpu_sys", "proc_cpu_total", "proc_cpu_per")
                            .values(
                                    map.get("name"),
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
                                    map.get("proc-cpu-per")
                            )
                            .build();
                    break;
            }
            if (series != null) {
                influxDB.write(dbName, TimeUnit.SECONDS, series);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setInfluxdb(String influxdb) {
        this.influxdb = influxdb;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
