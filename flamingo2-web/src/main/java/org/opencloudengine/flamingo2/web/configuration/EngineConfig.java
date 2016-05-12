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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EngineConfig implements Serializable {

    public String id;
    public String name;

    public String hsAddress;
    public int hsPort;

    public String rmAddress;
    public int rmPort;

    public String wapAddress;
    public int wapPort;

    public String nnAddress;
    public int nnPort;

    public String rmAgentAddress;
    public int rmAgentPort;

    public String nnAgentAddress;
    public int nnAgentPort;

    public String hiveServerUrl;
    public String hiveServerUsername;

    public String hiveMetastoreAddress;
    public int hiveMetastorePort;
    public boolean hiveLegacy;

    public String pigHome;
    public String pigTemp;

    public String hawqJdbcType;
    public String hawqGreenplumConnectionUrl;
    public String hawqPostgresqlConnectionUrl;
    public String hawqHost;
    public String hawqPort;
    public String hawqDefaultDatabaseName;
    public String hawqUser;
    public String hawqPassword;
    public String hawqAutoCommit;
    public String hawqDriver;
    public String hawqPostgresqlDriver;

    public List<String> zookeepers;

    public Map<String, Integer> zookeeperAgents;

    public String getHistoryServerUrl() {
        return hsAddress + ":" + hsPort;
    }

    public String getWebApplicationServerUrl() {
        return wapAddress + ":" + wapPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHsAddress() {
        return hsAddress;
    }

    public void setHsAddress(String hsAddress) {
        this.hsAddress = hsAddress;
    }

    public int getHsPort() {
        return hsPort;
    }

    public void setHsPort(int hsPort) {
        this.hsPort = hsPort;
    }

    public String getRmAddress() {
        return rmAddress;
    }

    public void setRmAddress(String rmAddress) {
        this.rmAddress = rmAddress;
    }

    public int getRmPort() {
        return rmPort;
    }

    public void setRmPort(int rmPort) {
        this.rmPort = rmPort;
    }

    public String getNnAddress() {
        return nnAddress;
    }

    public void setNnAddress(String nnAddress) {
        this.nnAddress = nnAddress;
    }

    public int getNnPort() {
        return nnPort;
    }

    public void setNnPort(int nnPort) {
        this.nnPort = nnPort;
    }

    public String getRmAgentAddress() {
        return rmAgentAddress;
    }

    public void setRmAgentAddress(String rmAgentAddress) {
        this.rmAgentAddress = rmAgentAddress;
    }

    public int getRmAgentPort() {
        return rmAgentPort;
    }

    public void setRmAgentPort(int rmAgentPort) {
        this.rmAgentPort = rmAgentPort;
    }

    public String getNnAgentAddress() {
        return nnAgentAddress;
    }

    public void setNnAgentAddress(String nnAgentAddress) {
        this.nnAgentAddress = nnAgentAddress;
    }

    public int getNnAgentPort() {
        return nnAgentPort;
    }

    public void setNnAgentPort(int nnAgentPort) {
        this.nnAgentPort = nnAgentPort;
    }

    public String getHiveMetastoreAddress() {
        return hiveMetastoreAddress;
    }

    public void setHiveMetastoreAddress(String hiveMetastoreAddress) {
        this.hiveMetastoreAddress = hiveMetastoreAddress;
    }

    public int getHiveMetastorePort() {
        return hiveMetastorePort;
    }

    public void setHiveMetastorePort(int hiveMetastorePort) {
        this.hiveMetastorePort = hiveMetastorePort;
    }

    public String getHiveServerUrl() {
        return hiveServerUrl;
    }

    public void setHiveServerUrl(String hiveServerUrl) {
        this.hiveServerUrl = hiveServerUrl;
    }

    public String getHiveServerUsername() {
        return hiveServerUsername;
    }

    public void setHiveServerUsername(String hiveServerUsername) {
        this.hiveServerUsername = hiveServerUsername;
    }

    public boolean isHiveLegacy() {
        return hiveLegacy;
    }

    public void setHiveLegacy(boolean hiveLagacy) {
        this.hiveLegacy = hiveLagacy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getZookeepers() {
        return zookeepers;
    }

    public void setZookeepers(List<String> zookeepers) {
        this.zookeepers = zookeepers;
    }

    public Map<String, Integer> getZookeeperAgents() {
        return zookeeperAgents;
    }

    public void setZookeeperAgents(Map<String, Integer> zookeeperAgents) {
        this.zookeeperAgents = zookeeperAgents;
    }

    public String getWapAddress() {
        return wapAddress;
    }

    public void setWapAddress(String wapAddress) {
        this.wapAddress = wapAddress;
    }

    public int getWapPort() {
        return wapPort;
    }

    public void setWapPort(int wapPort) {
        this.wapPort = wapPort;
    }

    public String getPigHome() {
        return pigHome;
    }

    public void setPigHome(String pigHome) {
        this.pigHome = pigHome;
    }

    public String getPigTemp() {
        return pigTemp;
    }

    public void setPigTemp(String pigTemp) {
        this.pigTemp = pigTemp;
    }

    public String getHawqJdbcType() {
        return hawqJdbcType;
    }

    public void setHawqJdbcType(String hawqJdbcType) {
        this.hawqJdbcType = hawqJdbcType;
    }

    public String getHawqGreenplumConnectionUrl() {
        return hawqGreenplumConnectionUrl;
    }

    public void setHawqGreenplumConnectionUrl(String hawqGreenplumConnectionUrl) {
        this.hawqGreenplumConnectionUrl = hawqGreenplumConnectionUrl;
    }

    public String getHawqPostgresqlConnectionUrl() {
        return hawqPostgresqlConnectionUrl;
    }

    public void setHawqPostgresqlConnectionUrl(String hawqPostgresqlConnectionUrl) {
        this.hawqPostgresqlConnectionUrl = hawqPostgresqlConnectionUrl;
    }

    public String getHawqHost() {
        return hawqHost;
    }

    public void setHawqHost(String hawqHost) {
        this.hawqHost = hawqHost;
    }

    public String getHawqPort() {
        return hawqPort;
    }

    public void setHawqPort(String hawqPort) {
        this.hawqPort = hawqPort;
    }

    public String getHawqDefaultDatabaseName() {
        return hawqDefaultDatabaseName;
    }

    public void setHawqDefaultDatabaseName(String hawqDefaultDatabaseName) {
        this.hawqDefaultDatabaseName = hawqDefaultDatabaseName;
    }

    public String getHawqUser() {
        return hawqUser;
    }

    public void setHawqUser(String hawqUser) {
        this.hawqUser = hawqUser;
    }

    public String getHawqPassword() {
        return hawqPassword;
    }

    public void setHawqPassword(String hawqPassword) {
        this.hawqPassword = hawqPassword;
    }

    public String getHawqAutoCommit() {
        return hawqAutoCommit;
    }

    public void setHawqAutoCommit(String hawqAutoCommit) {
        this.hawqAutoCommit = hawqAutoCommit;
    }

    public String getHawqDriver() {
        return hawqDriver;
    }

    public void setHawqDriver(String hawqDriver) {
        this.hawqDriver = hawqDriver;
    }

    public String getHawqPostgresqlDriver() {
        return hawqPostgresqlDriver;
    }

    public void setHawqPostgresqlDriver(String hawqPostgresqlDriver) {
        this.hawqPostgresqlDriver = hawqPostgresqlDriver;
    }
}
