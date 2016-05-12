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

import java.io.Serializable;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class SystemConfig implements Serializable {

    public String id;
    public String name;

    public String webIp;
    public int webPort;

    public String hsAddress;
    public int hsPort;

    public String rmAddress;
    public int rmPort;

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

    public String getHistoryServerUrl() {
        return hsAddress + ":" + hsPort;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebIp() {
        return webIp;
    }

    public void setWebIp(String webIp) {
        this.webIp = webIp;
    }

    public int getWebPort() {
        return webPort;
    }

    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }
}
