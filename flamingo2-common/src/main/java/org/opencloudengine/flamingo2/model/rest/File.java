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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Hadoop FileSystem의 File JAXB Object.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"id",
	"path",
	"name",
	"text",
	"length",
	"isdir",
	"leaf",
	"replication",
	"blocksize",
	"modificationTime",
	"accessTime",
	"permission",
	"owner",
	"group",
	"cls"
})
@XmlRootElement(name = "file")
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.ANY,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.ANY
)
public class File implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

	String id;

	String path;

	String name;

	String text;

	String length;

	boolean isdir;

	boolean leaf;

	short replication;

	String blocksize;

	String modificationTime;

	String accessTime;

	String permission;

	String owner;

	String group;

	String cls;

	public File() {
	}

	public String getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		this.id = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.text = name;
	}

	public String getText() {
		return text;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public boolean isIsdir() {
		return isdir;
	}

	public void setIsdir(boolean isdir) {
		this.isdir = isdir;
		this.leaf = !isdir;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public short getReplication() {
		return replication;
	}

	public void setReplication(short replication) {
		this.replication = replication;
	}

	public String getBlocksize() {
		return blocksize;
	}

	public void setBlocksize(String blocksize) {
		this.blocksize = blocksize;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    @Override
	public String toString() {
		return "File{" +
			"id='" + id + '\'' +
			", path='" + path + '\'' +
			", name='" + name + '\'' +
			", text='" + text + '\'' +
			", length=" + length +
			", isdir=" + isdir +
			", leaf=" + leaf +
			", replication=" + replication +
			", blocksize=" + blocksize +
			", modificationTime=" + modificationTime +
			", accessTime=" + accessTime +
			", permission='" + permission + '\'' +
			", owner='" + owner + '\'' +
			", group='" + group + '\'' +
			", cls='" + cls + '\'' +
			'}';
	}
}
