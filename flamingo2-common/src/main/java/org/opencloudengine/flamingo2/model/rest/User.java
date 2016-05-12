/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public class User implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String name;

    private Short level;

    private String description;

    private String linuxUserHome;

    private String hdfsUserHome;

    private String userGroup;

    private Timestamp registerDate;

    private Timestamp updateDate;

    private Boolean enabled;

    private Long orgId;

    private Long authId;

    private String websocketKey;

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinuxUserHome() {
        return linuxUserHome;
    }

    public void setLinuxUserHome(String linuxUserHome) {
        this.linuxUserHome = linuxUserHome;
    }

    public String getHdfsUserHome() {
        return hdfsUserHome;
    }

    public void setHdfsUserHome(String hdfsUserHome) {
        this.hdfsUserHome = hdfsUserHome;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public Timestamp getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Timestamp registerDate) {
        this.registerDate = registerDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public String getWebsocketKey() {
        return websocketKey;
    }

    public void setWebsocketKey(String websocketKey) {
        this.websocketKey = websocketKey;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", description='" + description + '\'' +
                ", linuxUserHome='" + linuxUserHome + '\'' +
                ", hdfsUserHome='" + hdfsUserHome + '\'' +
                ", userGroup='" + userGroup + '\'' +
                ", registerDate=" + registerDate +
                ", updateDate=" + updateDate +
                ", enabled=" + enabled +
                ", orgId=" + orgId +
                ", authId=" + authId +
                ", websocketKey='" + websocketKey + '\'' +
                '}';
    }
}
