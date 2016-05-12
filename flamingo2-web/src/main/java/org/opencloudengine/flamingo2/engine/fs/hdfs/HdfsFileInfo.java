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
package org.opencloudengine.flamingo2.engine.fs.hdfs;

import org.apache.commons.lang.SystemUtils;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.hdfs.server.blockmanagement.BlockInfo;
import org.opencloudengine.flamingo2.model.rest.ExtJSTreeNode;
import org.opencloudengine.flamingo2.model.rest.FileInfo;

/**
 * HDFS File Info.
 *
 * @author Byoung Gon, Kim
 * @since 0.3
 */
public class HdfsFileInfo extends ExtJSTreeNode implements FileInfo {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private String filename;

    private String fullyQualifiedPath;

    private String path;

    private long length;

    private boolean directory;

    private boolean file;

    private String owner;

    private String group;

    private long blockSize;

    private int replication;

    private long modificationTime;

    private long accessTime;

    private String permission;

    private long spaceQuota;

    private long spaceConsumed;

    private long quota;

    private long fileCount;

    private long directoryCount;

    public HdfsFileInfo(FileStatus fileStatus, ContentSummary contentSummary, BlockInfo blockInfo) {
        this.fullyQualifiedPath = fileStatus.getPath().toUri().getPath();
        this.filename = isEmpty(getFilename(fullyQualifiedPath)) ? getDirectoryName(fullyQualifiedPath) : getFilename(fullyQualifiedPath);
        this.length = fileStatus.getLen();
        this.path = getPath(fullyQualifiedPath);
        this.directory = fileStatus.isDirectory();
        this.file = !fileStatus.isDirectory();
        this.owner = fileStatus.getOwner();
        this.group = fileStatus.getGroup();
        this.blockSize = fileStatus.getBlockSize();
        this.replication = fileStatus.getReplication();
        this.modificationTime = fileStatus.getModificationTime();
        if (contentSummary != null) {
            this.spaceConsumed = contentSummary.getSpaceConsumed();
            this.quota = contentSummary.getQuota();
            this.spaceQuota = contentSummary.getSpaceQuota();
            this.directoryCount = contentSummary.getDirectoryCount();
            this.fileCount = contentSummary.getFileCount();
        }
        this.accessTime = fileStatus.getAccessTime();
        this.setText(this.filename);
        this.setLeaf(file);
        this.setIconCls(directory ? "folder" : "file");
        this.setQtip(this.fullyQualifiedPath);
        this.setId(fullyQualifiedPath);
        this.permission = fileStatus.getPermission().toString();
    }

    public static String getPath(String fullyQualifiedPath) {
        int sep = fullyQualifiedPath.lastIndexOf(SystemUtils.FILE_SEPARATOR);
        if (sep != 0) {
            return fullyQualifiedPath.substring(0, sep);
        }
        return SystemUtils.FILE_SEPARATOR;
    }

    public static String getDirectoryName(String fullyQualifiedPath) {
        int sep = fullyQualifiedPath.lastIndexOf(SystemUtils.FILE_SEPARATOR);
        int length = fullyQualifiedPath.getBytes().length;
        return fullyQualifiedPath.substring(sep + 1, length);
    }

    public static String getFilename(String path) {
        if (path == null) {
            return null;
        } else {
            int separatorIndex = path.lastIndexOf("/");
            return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
        }
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() < 1);
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public String getFullyQualifiedPath() {
        return fullyQualifiedPath;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean isFile() {
        return file;
    }

    @Override
    public boolean isDirectory() {
        return directory;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public long getBlockSize() {
        return blockSize;
    }

    @Override
    public int getReplication() {
        return replication;
    }

    @Override
    public long getModificationTime() {
        return modificationTime;
    }

    @Override
    public long getAccessTime() {
        return accessTime;
    }

    public String getPermission() {
        return permission;
    }

    public void setSpaceQuota(long spaceQuota) {
        this.spaceQuota = spaceQuota;
    }

    public long getSpaceQuota() {
        return spaceQuota;
    }

    public void setSpaceConsumed(long spaceConsumed) {
        this.spaceConsumed = spaceConsumed;
    }

    public long getSpaceConsumed() {
        return spaceConsumed;
    }

    public void setQuota(long quota) {
        this.quota = quota;
    }

    public long getQuota() {
        return quota;
    }

    public void setFileCount(long fileCount) {
        this.fileCount = fileCount;
    }

    public long getFileCount() {
        return fileCount;
    }

    public void setDirectoryCount(long directoryCount) {
        this.directoryCount = directoryCount;
    }

    public long getDirectoryCount() {
        return directoryCount;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFullyQualifiedPath(String fullyQualifiedPath) {
        this.fullyQualifiedPath = fullyQualifiedPath;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setBlockSize(long blockSize) {
        this.blockSize = blockSize;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }

    public void setModificationTime(long modificationTime) {
        this.modificationTime = modificationTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
