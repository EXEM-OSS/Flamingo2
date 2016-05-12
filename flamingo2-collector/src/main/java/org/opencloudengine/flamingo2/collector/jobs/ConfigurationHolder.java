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
package org.opencloudengine.flamingo2.collector.jobs;

import org.opencloudengine.flamingo2.collector.ApplicationContextRegistry;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationHolder {

    private static Map<String, SystemConfig> configs;

    static {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        Properties props = (Properties) applicationContext.getBean("hadoop");

        configs = new HashMap();

        String[] names = props.getProperty("cluster.names").split(",");
        String[] qualifiers = props.getProperty("cluster.qualifiers").split(",");

        for (int i = 0; i < qualifiers.length; i++) {
            String qualifier = qualifiers[i];
            String name = names[i];

            SystemConfig engine = new SystemConfig();
            configs.put(qualifier, engine);

            engine.setId(qualifier);
            engine.setWebIp(props.getProperty(qualifier + ".web.address"));
            engine.setWebPort(Integer.parseInt(props.getProperty(qualifier + ".web.port")));
            engine.setName(name);

            engine.setHsAddress(props.getProperty(qualifier + ".hs.address"));
            engine.setHsPort(Integer.parseInt(props.getProperty(qualifier + ".hs.port")));

            engine.setRmAddress(props.getProperty(qualifier + ".rm.address"));
            engine.setRmPort(Integer.parseInt(props.getProperty(qualifier + ".rm.port")));

            engine.setNnAddress(props.getProperty(qualifier + ".nn.address"));
            engine.setNnPort(Integer.parseInt(props.getProperty(qualifier + ".nn.port")));

            engine.setRmAgentAddress(props.getProperty(qualifier + ".rm.agent.address"));
            engine.setRmAgentPort(Integer.parseInt(props.getProperty(qualifier + ".rm.agent.port")));

            engine.setNnAgentAddress(props.getProperty(qualifier + ".nn.agent.address"));
            engine.setNnAgentPort(Integer.parseInt(props.getProperty(qualifier + ".nn.agent.port")));

            engine.setHiveServerUrl(props.getProperty(qualifier + ".hive.server2.url"));
            engine.setHiveServerUsername(props.getProperty(qualifier + ".hive.server2.username"));

            engine.setHiveMetastoreAddress(props.getProperty(qualifier + ".hive.metastore.address"));
            engine.setHiveMetastorePort(Integer.parseInt(props.getProperty(qualifier + ".hive.metastore.port")));
        }

    }

    public static SystemConfig getConfig(String configName) {
        return configs.get(configName);
    }

    public static Map<String, SystemConfig> getConfigs() {
        return configs;
    }
}
