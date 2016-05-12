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
 * HDFS Browser Authority Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public class HdfsBrowserAuth implements Serializable {

    private long id;

    private String hdfsPathPattern;

    private boolean createDir;

    private boolean copyDir;

    private boolean moveDir;

    private boolean renameDir;

    private boolean deleteDir;

    private boolean mergeDir;
    
    private boolean permissionDir;
    
    private boolean createDbDir;
    
    private boolean createTableDir;
    
    private boolean copyFile;
    
    private boolean moveFile;
    
    private boolean renameFile;
    
    private boolean deleteFile;
    
    private boolean uploadFile;
    
    private boolean downloadFile;
    
    private boolean viewFile;

    private boolean permissionFile;

    private Timestamp registerDate;

    private Timestamp updateDate;

    private Long authId;

    private short level;

    public HdfsBrowserAuth() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHdfsPathPattern() {
        return hdfsPathPattern;
    }

    public void setHdfsPathPattern(String hdfsPathPattern) {
        this.hdfsPathPattern = hdfsPathPattern;
    }

    public boolean isCreateDir() {
        return createDir;
    }

    public void setCreateDir(boolean createDir) {
        this.createDir = createDir;
    }

    public boolean isCopyDir() {
        return copyDir;
    }

    public void setCopyDir(boolean copyDir) {
        this.copyDir = copyDir;
    }

    public boolean isMoveDir() {
        return moveDir;
    }

    public void setMoveDir(boolean moveDir) {
        this.moveDir = moveDir;
    }

    public boolean isRenameDir() {
        return renameDir;
    }

    public void setRenameDir(boolean renameDir) {
        this.renameDir = renameDir;
    }

    public boolean isDeleteDir() {
        return deleteDir;
    }

    public void setDeleteDir(boolean deleteDir) {
        this.deleteDir = deleteDir;
    }

    public boolean isMergeDir() {
        return mergeDir;
    }

    public void setMergeDir(boolean mergeDir) {
        this.mergeDir = mergeDir;
    }

    public boolean isPermissionDir() {
        return permissionDir;
    }

    public void setPermissionDir(boolean permissionDir) {
        this.permissionDir = permissionDir;
    }

    public boolean isCreateDbDir() {
        return createDbDir;
    }

    public void setCreateDbDir(boolean createDbDir) {
        this.createDbDir = createDbDir;
    }

    public boolean isCreateTableDir() {
        return createTableDir;
    }

    public void setCreateTableDir(boolean createTableDir) {
        this.createTableDir = createTableDir;
    }

    public boolean isCopyFile() {
        return copyFile;
    }

    public void setCopyFile(boolean copyFile) {
        this.copyFile = copyFile;
    }

    public boolean isMoveFile() {
        return moveFile;
    }

    public void setMoveFile(boolean moveFile) {
        this.moveFile = moveFile;
    }

    public boolean isRenameFile() {
        return renameFile;
    }

    public void setRenameFile(boolean renameFile) {
        this.renameFile = renameFile;
    }

    public boolean isDeleteFile() {
        return deleteFile;
    }

    public void setDeleteFile(boolean deleteFile) {
        this.deleteFile = deleteFile;
    }

    public boolean isUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(boolean uploadFile) {
        this.uploadFile = uploadFile;
    }

    public boolean isDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(boolean downloadFile) {
        this.downloadFile = downloadFile;
    }

    public boolean isViewFile() {
        return viewFile;
    }

    public void setViewFile(boolean viewFile) {
        this.viewFile = viewFile;
    }

    public boolean isPermissionFile() {
        return permissionFile;
    }

    public void setPermissionFile(boolean permissionFile) {
        this.permissionFile = permissionFile;
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

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "HdfsBrowserAuth{" +
                "id=" + id +
                ", hdfsPathPattern='" + hdfsPathPattern + '\'' +
                ", createDir=" + createDir +
                ", copyDir=" + copyDir +
                ", moveDir=" + moveDir +
                ", renameDir=" + renameDir +
                ", deleteDir=" + deleteDir +
                ", mergeDir=" + mergeDir +
                ", permissionDir=" + permissionDir +
                ", createDbDir=" + createDbDir +
                ", createTableDir=" + createTableDir +
                ", copyFile=" + copyFile +
                ", moveFile=" + moveFile +
                ", renameFile=" + renameFile +
                ", deleteFile=" + deleteFile +
                ", uploadFile=" + uploadFile +
                ", downloadFile=" + downloadFile +
                ", viewFile=" + viewFile +
                ", permissionFile=" + permissionFile +
                ", registerDate=" + registerDate +
                ", updateDate=" + updateDate +
                ", authId=" + authId +
                ", level=" + level +
                '}';
    }
}
