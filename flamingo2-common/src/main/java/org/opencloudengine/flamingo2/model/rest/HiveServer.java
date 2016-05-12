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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.opencloudengine.flamingo2.util.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"id",
	"name",
	"name",
	"connectionDriverName",
	"connectionUrl",
	"username",
	"password",
	"metastoreUris",
	"jdbcUrl",
	"user",
})
@XmlRootElement(name = "hiveServer")
@JsonAutoDetect(
	getterVisibility = JsonAutoDetect.Visibility.ANY,
	fieldVisibility = JsonAutoDetect.Visibility.NONE,
	setterVisibility = JsonAutoDetect.Visibility.ANY
)
public class HiveServer implements Serializable {

	/**
	 * Serialization UID
	 */
	private static final long serialVersionUID = 1;

	private long id;

	private String name;

	private String connectionDriverName;

	private String connectionUrl;

	private String username;

	private String password;

	private String metastoreUris;

	private String jdbcUrl;

	private String user;

	public HiveServer() {
	}

	public HiveServer(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name.trim();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return StringUtils.isEmpty(user) ? user : user.trim();
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getJdbcUrl() {
		return StringUtils.isEmpty(jdbcUrl) ? jdbcUrl : jdbcUrl.trim();
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getMetastoreUris() {
		return StringUtils.isEmpty(metastoreUris) ? metastoreUris : metastoreUris.trim();
	}

	public void setMetastoreUris(String metastoreUris) {
		this.metastoreUris = metastoreUris;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return StringUtils.isEmpty(username) ? username : username.trim();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getConnectionUrl() {
		return StringUtils.isEmpty(connectionUrl) ? connectionUrl : connectionUrl.trim();
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getConnectionDriverName() {
		return StringUtils.isEmpty(connectionDriverName) ? connectionDriverName : connectionDriverName.trim();
	}

	public void setConnectionDriverName(String connectionDriverName) {
		this.connectionDriverName = connectionDriverName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		HiveServer server = (HiveServer) o;

		if (id != server.id) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "HiveServer{" +
			"id=" + id +
			", name='" + name.trim() + '\'' +
			", connectionDriverName='" + connectionDriverName.trim() + '\'' +
			", connectionUrl='" + connectionUrl.trim() + '\'' +
			", username='" + username.trim() + '\'' +
			", password='" + password + '\'' +
			", metastoreUris='" + metastoreUris.trim() + '\'' +
			", jdbcUrl='" + jdbcUrl.trim() + '\'' +
			", user='" + user.trim() + '\'' +
			'}';
	}
}
