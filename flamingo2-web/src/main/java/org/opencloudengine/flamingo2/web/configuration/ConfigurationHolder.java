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
package org.opencloudengine.flamingo2.web.configuration;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Hadoop 환경 설정 정보를 로딩하고 유지하는 환경설정 지정자.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class ConfigurationHolder {

    private static Map<String, EngineConfig> engines;

    static {
        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        Resource resource = applicationContext.getResource("/WEB-INF/hadoop.properties");
        Properties props = new Properties();
        try {
            // UTF-8 처리를 위해서 다음과 같이 처리함 (Java 7)
            BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            props.load(in);
        } catch (Exception ex) {
            throw new ServiceException("/WEB-INF/hadoop.properties file can not be loaded.", ex);
        }

        engines = new HashMap();

        String[] names = props.getProperty("cluster.names").split(",");
        String[] qualifiers = props.getProperty("cluster.qualifiers").split(",");

        for (int i = 0; i < qualifiers.length; i++) {
            String qualifier = qualifiers[i];
            String name = names[i];

            EngineConfig engine = new EngineConfig();
            engines.put(qualifier, engine);

            engine.setId(qualifier);
            engine.setName(name);

            engine.setHsAddress(props.getProperty(qualifier + ".hs.address"));
            engine.setHsPort(Integer.parseInt(props.getProperty(qualifier + ".hs.port")));

            engine.setWapAddress(props.getProperty(qualifier + ".wap.address"));
            engine.setWapPort(Integer.parseInt(props.getProperty(qualifier + ".wap.port")));

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
            engine.setHiveLegacy(Boolean.parseBoolean(props.getProperty(qualifier + ".hive.legacy")));

            // FIXME 여기 경로 변경하도록 함
            // ConfigurationHelper.getHelper().get("pig.home");
            // ConfigurationHelper.getHelper().get("visual.rdata.tmp");
            engine.setPigHome(props.getProperty(qualifier + ".pig.home"));
            engine.setPigTemp(props.getProperty(qualifier + ".pig.temp"));

            engine.setHawqJdbcType(props.getProperty(qualifier + ".hawq.jdbc.type"));
            engine.setHawqGreenplumConnectionUrl(props.getProperty(qualifier + ".hawq.greenplum.connectionUrl"));
            engine.setHawqPostgresqlConnectionUrl(props.getProperty(qualifier + ".hawq.postgresql.connectionUrl"));
            engine.setHawqHost(props.getProperty(qualifier + ".hawq.host"));
            engine.setHawqPort(props.getProperty(qualifier + ".hawq.port"));
            engine.setHawqDefaultDatabaseName(props.getProperty(qualifier + ".hawq.databaseName"));
            engine.setHawqUser(props.getProperty(qualifier + ".hawq.user"));
            engine.setHawqPassword(props.getProperty(qualifier + ".hawq.password"));
            engine.setHawqAutoCommit(props.getProperty(qualifier + ".hawq.autoCommit"));
            engine.setHawqDriver(props.getProperty(qualifier + ".hawq.driver"));
            engine.setHawqPostgresqlDriver(props.getProperty(qualifier + ".hawq.postgresql.driver"));
        }
    }

    private static Map<String, Integer> getZkAgents(String[] zkAgentIps, String[] zkAgentPorts) {
        Assert.isTrue(zkAgentIps.length == zkAgentPorts.length, "ZooKeeper Agent Mapping Error");
        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < zkAgentIps.length; i++) {
            String ip = zkAgentIps[i];
            Integer port = Integer.parseInt(zkAgentPorts[i]);
            map.put(ip, port);
        }
        return map;
    }

    /**
     * 지정한 클러스터의 ZooKeeper Agent를 임의로 선택한다.
     *
     * @param clusterName 클러스터 구분자(예; <tt>default</tt>)
     * @return ZooKeeper Agent 정보
     */
    public static String getZkAgentByRandom(String clusterName) {
        EngineConfig engineConfig = engines.get(clusterName);
        Map<String, Integer> agents = engineConfig.getZookeeperAgents();
        int selected = clusterName.hashCode() % agents.size();
        Object key = agents.keySet().toArray()[selected];
        Integer integer = agents.get(key);
        return key + ":" + integer;
    }

    /**
     * 지정한 클러스터의 ZooKeeper Client를 임의로 선택한다.
     *
     * @param clusterName 클러스터 구분자(예; <tt>default</tt>)
     * @return ZooKeeper Client 정보
     */
    public static String getZkClientByRandom(String clusterName) {
        EngineConfig engineConfig = engines.get(clusterName);
        List<String> zkClients = engineConfig.getZookeepers();
        int selected = clusterName.hashCode() % zkClients.size();
        return zkClients.get(selected);
    }

    public static EngineConfig getEngine(String clusterName) {
        return engines.get(clusterName);
    }

    public static Map<String, EngineConfig> getEngines() {
        return engines;
    }
}
