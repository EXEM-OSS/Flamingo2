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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.opencloudengine.flamingo2.agent.namenode.Namenode2AgentService;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.fs.FileSystemRemoteService;
import org.opencloudengine.flamingo2.engine.fs.audit.FileSystemAuditRemoteService;
import org.opencloudengine.flamingo2.engine.hadoop.RemoteInvocation;
import org.opencloudengine.flamingo2.model.rest.*;
import org.opencloudengine.flamingo2.util.FileUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import java.util.*;

public class HdfsFileSystemRemoteServiceImpl extends RemoteInvocation implements FileSystemRemoteService {

    public static final String NAMENODE_SERVICE = "namenode2";

    @Autowired
    ConfigurationHelper configurationHelper;

    private Namenode2AgentService namenode2AgentService;

    private FileSystemAuditRemoteService fileSystemAuditRemoteService;

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(HdfsFileSystemRemoteServiceImpl.class);

    @Override
    public List<FileInfo> getDirectories(EngineConfig engineConfig, String path, boolean directoryOnly) {
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.list(path, true);
    }

    @Override
    public List<FileInfo> getFiles(EngineConfig engineConfig, String path) {
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.list(path, false);
    }

    @Override
    public boolean createDirectory(EngineConfig engineConfig, String path, String username) {
        validateForbiddenPath(path);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.CREATE, FileType.DIRECTORY, RequestType.UI, path, "", 0);
        return namenode2AgentService.mkdir(path, username);
    }

    @Override
    public boolean copyDirectory(EngineConfig engineConfig, String currentPath, String dstPath, String username) {
        validateForbiddenPath(currentPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.COPY, FileType.DIRECTORY, RequestType.UI, currentPath, dstPath, 0);
        return namenode2AgentService.copy(currentPath, dstPath, username);
    }

    @Override
    public boolean moveDirectory(EngineConfig engineConfig, String currentPath, String dstPath, String username) {
        validateForbiddenPath(currentPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.MOVE, FileType.DIRECTORY, RequestType.UI, currentPath, dstPath, 0);
        return namenode2AgentService.move(currentPath, dstPath);
    }

    @Override
    public boolean renameDirectory(EngineConfig engineConfig, String currentPath, String directoryName, String username) {
        validateForbiddenPath(currentPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.RENAME, FileType.DIRECTORY, RequestType.UI, currentPath, directoryName, 0);
        return namenode2AgentService.rename(currentPath, directoryName);
    }


    @Override
    public boolean deleteDirectory(EngineConfig engineConfig, String currentPath, String username) {
        validateForbiddenPath(currentPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.DELETE, FileType.DIRECTORY, RequestType.UI, currentPath, "", 0);
        return namenode2AgentService.delete(currentPath);
    }

    @Override
    public boolean mergeFiles(EngineConfig engineConfig, String currentPath, String dstPath, String username) {
        validateForbiddenPath(currentPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        long length = getFileInfo(engineConfig, currentPath).getLength();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.MERGE, FileType.FILE, RequestType.UI, currentPath, dstPath, length);
        return namenode2AgentService.merge(currentPath, dstPath, username);
    }

    @Override
    public HdfsFileInfo getFileInfo(EngineConfig engineConfig, String srcPath) {
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        return namenode2AgentService.getFileInfo(srcPath);
    }

    @Override
    public boolean setPermission(EngineConfig engineConfig, Map permissionMap, String username) {
        String srcPath = (String) permissionMap.get("currentPath");
        validateForbiddenPath(srcPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        String fileStatus = (String) permissionMap.get("fileStatus");
        String files = (String) permissionMap.get("files");
        String[] fileArray = files.split(",");
        String names = (String) permissionMap.get("fileNames");
        String[] fileNames = names.split(",");
        List<String> fileList = new ArrayList<>();
        String multiFiles;
        short logCount = 0;
        boolean changed = false;

        if (fileStatus.equalsIgnoreCase("DIRECTORY")) {
            fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.PERMISSION, FileType.DIRECTORY, RequestType.UI, srcPath, "", 0);
            changed = namenode2AgentService.setPermission(permissionMap);
        } else {
            long length;

            if (fileArray.length > 1) {
                permissionMap.put("fileListSize", String.valueOf(fileArray.length));

                for (String file : fileArray) {
                    permissionMap.put("file", file);
                    changed = namenode2AgentService.setPermission(permissionMap);
                }

                for (String fileName : fileNames) {
                    logCount++;
                    fileList.add(fileName);

                    if (logCount == 10) {
                        multiFiles = srcPath + SystemUtils.FILE_SEPARATOR + StringUtils.join(fileList, ",");
                        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.PERMISSION, FileType.FILE, RequestType.UI, multiFiles, "", 0);
                        fileList.clear();
                        logCount = 0;
                    }
                }

                multiFiles = srcPath + SystemUtils.FILE_SEPARATOR + StringUtils.join(fileList, ",");
                fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.PERMISSION, FileType.FILE, RequestType.UI, multiFiles, "", 0);
            } else {
                permissionMap.put("file", fileArray[0]);
                length = getFileInfo(engineConfig, fileArray[0]).getLength();
                fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.PERMISSION, FileType.FILE, RequestType.UI, fileArray[0], "", length);
                changed = namenode2AgentService.setPermission(permissionMap);
            }
        }

        return changed;
    }

    @Override
    public List<String> copyFiles(EngineConfig engineConfig, List<String> srcFileList, String dstPath, String username) {
        validateForbiddenPath(dstPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        List<String> copiedFiles = new ArrayList<>();
        String dstFilePath;
        long length;
        boolean copied;

        // TODO > Bulk insert 시 executeBatch 쿼리 체크
        if (srcFileList.size() > 2) {
            for (String srcFilePath : srcFileList) {

                if (dstPath.equalsIgnoreCase("/")) {
                    dstFilePath = dstPath + FileUtils.getFilename(srcFilePath);
                } else {
                    dstFilePath = dstPath + SystemUtils.FILE_SEPARATOR + FileUtils.getFilename(srcFilePath);
                }

                copied = namenode2AgentService.copy(srcFilePath, dstFilePath, username);

                if (copied) {
                    copiedFiles.add(srcFilePath);
                }

                length = getFileInfo(engineConfig, srcFilePath).getLength();
                fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.COPY, FileType.FILE, RequestType.UI, srcFilePath, dstFilePath, length);
            }
        } else {
            String srcPath = srcFileList.get(0);

            if (dstPath.equalsIgnoreCase("/")) {
                dstFilePath = dstPath + FileUtils.getFilename(srcPath);
            } else {
                dstFilePath = dstPath + SystemUtils.FILE_SEPARATOR + FileUtils.getFilename(srcPath);
            }

            copied = namenode2AgentService.copy(srcPath, dstFilePath, username);

            if (copied) {
                copiedFiles.add(srcPath);
            }

            length = getFileInfo(engineConfig, srcPath).getLength();
            fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.COPY, FileType.FILE, RequestType.UI, srcPath, dstPath, length);
        }

        return copiedFiles;
    }

    @Override
    public List<String> moveFiles(EngineConfig engineConfig, List<String> srcFileList, String dstPath, String username) {
        validateForbiddenPath(dstPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        List<String> movedFiles = new ArrayList<>();
        String dstFilePath;
        long length;
        boolean moved;

        if (srcFileList.size() > 2) {
            for (String srcFilePath : srcFileList) {

                if (dstPath.equalsIgnoreCase("/")) {
                    dstFilePath = dstPath + FileUtils.getFilename(srcFilePath);
                } else {
                    dstFilePath = dstPath + SystemUtils.FILE_SEPARATOR + FileUtils.getFilename(srcFilePath);
                }

                length = getFileInfo(engineConfig, srcFilePath).getLength();
                moved = namenode2AgentService.move(srcFilePath, dstPath);

                if (moved) {
                    movedFiles.add(srcFilePath);
                }

                fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.MOVE, FileType.FILE, RequestType.UI, srcFilePath, dstFilePath, length);
            }
        } else {
            String srcPath = srcFileList.get(0);

            if (dstPath.equalsIgnoreCase("/")) {
                dstFilePath = dstPath + FileUtils.getFilename(srcPath);
            } else {
                dstFilePath = dstPath + SystemUtils.FILE_SEPARATOR + FileUtils.getFilename(srcPath);
            }

            length = getFileInfo(engineConfig, srcPath).getLength();
            moved = namenode2AgentService.move(srcPath, dstFilePath);

            if (moved) {
                movedFiles.add(srcPath);
            }

            fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.MOVE, FileType.FILE, RequestType.UI, srcPath, dstFilePath, length);
        }

        return movedFiles;
    }

    @Override
    public boolean renameFile(EngineConfig engineConfig, String srcPath, String filename, String username) {
        validateForbiddenPath(srcPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        long length = getFileInfo(engineConfig, srcPath).getLength();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.RENAME, FileType.FILE, RequestType.UI, srcPath, "", length);
        return namenode2AgentService.rename(srcPath, filename);
    }

    @Override
    public List<String> deleteFiles(EngineConfig engineConfig, String srcPath, String files, String username, int userLevel) {
        validateForbiddenPath(srcPath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();

        String[] fromItems = files.split(",");
        List<String> fileList = new java.util.ArrayList<>();
        List<String> deletedFiles = new ArrayList<>();
        boolean deleted;

        Collections.addAll(fileList, fromItems);

        for (String filePath : fileList) {
            long length = getFileInfo(engineConfig, filePath).getLength();
            fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.DELETE, FileType.FILE, RequestType.UI, filePath, "", length);
            deleted = namenode2AgentService.delete(filePath);

            if (deleted) {
                deletedFiles.add(filePath);
            }
        }

        return deletedFiles;
    }

    @Override
    public boolean save(EngineConfig engineConfig, String pathToUpload, String fullyQualifiedPath, byte[] content, String username) {
        validateForbiddenPath(pathToUpload);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        long length = content.length;

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.UPLOAD, FileType.FILE, RequestType.UI, fullyQualifiedPath, "", length);
        return namenode2AgentService.save(pathToUpload, fullyQualifiedPath, content, username);
    }

    @Override
    public void validateBeforeUpload(EngineConfig engineConfig, String pathToUpload, String fullyQualifiedPath, byte[] content, String username) {
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        long length = content.length;

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.UPLOAD, FileType.FILE, RequestType.UI, fullyQualifiedPath, "", length);
    }

    @Override
    public byte[] load(EngineConfig engineConfig, String srcFilePath, String fullyQualifiedPath, String username) {
        validateForbiddenPath(srcFilePath);
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        long length = getFileInfo(engineConfig, fullyQualifiedPath).getLength();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.DOWNLOAD, FileType.FILE, RequestType.UI, fullyQualifiedPath, "", length);
        return namenode2AgentService.load(fullyQualifiedPath);
    }

    @Override
    public Map viewFileContents(EngineConfig engineConfig, Map fileContestsMap, String username) {
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        String filePath = (String) fileContestsMap.get("filePath");
        long length = getFileInfo(engineConfig, filePath).getLength();
        boolean auditLogKey = (boolean) fileContestsMap.get("auditLogKey");

        if (auditLogKey) {
            fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.VIEW, FileType.FILE, RequestType.UI, filePath, "", length);
        }

        return namenode2AgentService.view(fileContestsMap);
    }

    @Override
    public boolean createHdfsUserHome(EngineConfig engineConfig, String hdfsUserHome, String username) {
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        Map hdfsUserMap = new HashMap();
        hdfsUserMap.put("hdfsUserHome", hdfsUserHome + "/" + username);
        hdfsUserMap.put("username", username);
        hdfsUserMap.put("group", username);

        return namenode2AgentService.createUserHome(hdfsUserMap);
    }

    @Override
    public boolean deleteHdfsUserHome(EngineConfig engineConfig, String hdfsUserHome) {
        namenode2AgentService = getNamenode2AgentService(engineConfig);
        validateForbiddenPath(hdfsUserHome);

        return namenode2AgentService.deleteUserHome(hdfsUserHome);
    }

    @Override
    public void validatePath(String path) {
        validateForbiddenPath(path);
    }

    @Override
    public void validateBeforeDownload(EngineConfig engineConfig, String srcFilePath, String fullyQualifiedPath, String username) {
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        long length = getFileInfo(engineConfig, fullyQualifiedPath).getLength();

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.DOWNLOAD, FileType.FILE, RequestType.UI, fullyQualifiedPath, "", length);
    }

    /**
     * 선택한 경로가 쓰기 금지 목록에 포함되는지 검증한다.
     *
     * @param path 디렉토리 경로
     */
    private void validateForbiddenPath(String path) {
        String[] paths = StringUtils.splitPreserveAllTokens(configurationHelper.get("hdfs.delete.forbidden.paths"), ",");
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        for (String pathToValid : paths) {
            boolean isMatch = antPathMatcher.match(path, pathToValid);
            if (isMatch) {
                throw new ServiceException("A directory is contained in the banned directory list.");
            }
        }
    }

    /**
     * Namenode Agent의 JVM에 배포되어 있는 Namenode Agent의 서비스를 획득한다.
     *
     * @param engineConfig Namenode Agent Agent 정보
     * @return {@link org.opencloudengine.flamingo2.agent.namenode.Namenode2AgentService}
     */
    private Namenode2AgentService getNamenode2AgentService(EngineConfig engineConfig) {
        String agentIp = engineConfig.getNnAgentAddress();
        int agentPort = engineConfig.getNnAgentPort();
        String remoteServiceUrl = this.getRemoteServiceUrl(agentIp, agentPort, NAMENODE_SERVICE);

        return this.getRemoteService(remoteServiceUrl, Namenode2AgentService.class);
    }

    public void setFileSystemAuditRemoteService(FileSystemAuditRemoteService fileSystemAuditRemoteService) {
        this.fileSystemAuditRemoteService = fileSystemAuditRemoteService;
    }
}
