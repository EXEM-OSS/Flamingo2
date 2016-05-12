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
package org.opencloudengine.flamingo2.engine.fs;

import org.opencloudengine.flamingo2.engine.fs.hdfs.HdfsFileInfo;
import org.opencloudengine.flamingo2.model.rest.FileInfo;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.util.List;
import java.util.Map;

/**
 * Apache Hadoop HDFS File System Service Interface.
 *
 * @author Byoung Gon, Kim
 * @author Myeong Ha, KIM
 * @since 0.4
 */

/**
 * AS-IS : org.openflamingo.fs.hdfs.HdfsFileSystemServiceImpl
 */
public interface FileSystemRemoteService {

    /**
     * 디렉토리 목록을 반환한다.
     *
     * @param engineConfig Engine Config
     * @param path         HDFS directory path to check
     * @return 디렉토리 목록
     */
    List<FileInfo> getDirectories(EngineConfig engineConfig, String path, boolean directoryOnly);

    /**
     * 파일 목록을 반환한다.
     *
     * @param engineConfig Engine Config
     * @param path         HDFS file path to check
     * @return 파일 목록
     */
    List<FileInfo> getFiles(EngineConfig engineConfig, String path);

    /**
     * 디렉토리를 생성한다.
     *
     * @param engineConfig Engine Config
     * @param path         HDFS directory path to create
     * @param username     Username
     * @return 디렉토리 생성 여부
     */
    boolean createDirectory(EngineConfig engineConfig, String path, String username);

    /**
     * 디렉토리를 복사한다.
     *
     * @param engineConfig Engine Config
     * @param currentPath  HDFS directory source path to copy
     * @param dstPath      HDFS directory target path to be copied
     * @param username     Username
     * @return 디렉토리 복사 여부
     */
    boolean copyDirectory(EngineConfig engineConfig, String currentPath, String dstPath, String username);

    /**
     * 디렉토리를 이동한다.
     *
     * @param engineConfig Engine Config
     * @param currentPath  HDFS directory source path to move
     * @param dstPath      HDFS directory target path to be moved
     * @param username     Username
     * @return 디렉토리 이동 여부
     */
    boolean moveDirectory(EngineConfig engineConfig, String currentPath, String dstPath, String username);

    /**
     * 디렉토리명을 변경한다.
     *
     * @param engineConfig  Engine Config
     * @param srcPath       HDFS directory source path to rename
     * @param directoryName HDFS directory new name to be renamed
     * @param username      Username
     * @return 디렉토리명 변경 여부
     */
    boolean renameDirectory(EngineConfig engineConfig, String srcPath, String directoryName, String username);

    /**
     * 디렉토리를 삭제한다.
     *
     * @param engineConfig Engine Config
     * @param currentPath  HDFS directory path to delete
     * @param username     Username
     * @return 디렉토리 삭제 여부
     */
    boolean deleteDirectory(EngineConfig engineConfig, String currentPath, String username);

    /**
     * @param engineConfig Engine Config
     * @param currentPath  HDFS directory source path to merge files
     * @param dstPath      HDFS directory new name to be saved merged files
     * @param username     Username
     * @return true or false
     */
    boolean mergeFiles(EngineConfig engineConfig, String currentPath, String dstPath, String username);

    /**
     * 디렉토리 및 파일 정보를 확인한다.
     *
     * @param engineConfig Engine Config
     * @param path         HDFS directory or file information
     * @return 디렉토리 또는 파일 정보
     */
    HdfsFileInfo getFileInfo(EngineConfig engineConfig, String path);

    /**
     * @param engineConfig  Engine Config
     * @param permissionMap HDFS permission information
     * @param username      Username
     * @return true or false
     */
    boolean setPermission(EngineConfig engineConfig, Map permissionMap, String username);

    /**
     * 파일을 복사한다.
     *
     * @param engineConfig  Engine Config
     * @param srcFileList   HDFS file(s) fullyQualified path to copy
     * @param dstPath       HDFS file(s) destination path to be copied
     * @return 복사된 파일 목록
     */
    List<String> copyFiles(EngineConfig engineConfig, List<String> srcFileList, String dstPath, String username);

    /**
     * 파일을 이동한다.
     *
     * @param engineConfig Engine Config
     * @param srcFileList  HDFS file(s) fullyQualified path to move
     * @param dstPath      HDFS file(s) destination path to be moved
     * @param username     Username
     * @return 이동된 파일 목록
     */
    List<String> moveFiles(EngineConfig engineConfig, List<String> srcFileList, String dstPath, String username);

    /**
     * 파일명을 변경한다.
     *
     * @param engineConfig Engine Config
     * @param srcPath      HDFS file Source path to rename
     * @param filename     HDFS filename to be renamed
     * @param username     Username
     * @return 변경 여부
     */
    boolean renameFile(EngineConfig engineConfig, String srcPath, String filename, String username);

    /**
     * 파일을 삭제한다.
     *
     * @param engineConfig Engine Config
     * @param files        HDFS file(s) path to delete
     * @param srcPath      HDFS file source path to delete
     * @param username     Username
     * @param userLevel    User Level
     * @return 삭제된 파일 목록
     */
    List<String> deleteFiles(EngineConfig engineConfig, String files, String srcPath, String username, int userLevel);

    /**
     * 업로드한 파일을 저장한다.
     *
     * @param engineConfig       Engine Config
     * @param pathToUpload       HDFS file source path to upload
     * @param fullyQualifiedPath HDFS file source path to upload
     * @param content            HDFS file byte array to save
     * @param username           Username
     * @return 파일 업로드 여부
     */
    boolean save(EngineConfig engineConfig, String pathToUpload, String fullyQualifiedPath, byte[] content, String username);

    /**
     * Namenode Agent를 통해 직접 업로드하기 전 패턴 검사 및 Audit 로그를 업데이트 한다.
     *
     * @param engineConfig       Engine Config
     * @param pathToUpload       HDFS file source path to upload
     * @param fullyQualifiedPath HDFS file source path to upload
     * @param content            HDFS file byte array to save
     * @param username           Username
     */
    void validateBeforeUpload(EngineConfig engineConfig, String pathToUpload, String fullyQualifiedPath, byte[] content, String username);

    /**
     * 파일을 로딩한다.
     *
     * @param engineConfig        Engine Config
     * @param srcFilePath         HDFS file source path to download
     * @param fullyQualifiedPath  HDFS fully qualified path to download
     * @param username            Username
     * @return 다운로드할 파일의 내용
     */
    byte[] load(EngineConfig engineConfig, String srcFilePath, String fullyQualifiedPath, String username);

    /**
     * 선택한 파일의 내용을 가져온다.
     *
     * @param engineConfig    Engine Config
     * @param fileContestsMap File contents information to view
     * @param username        Username
     * @return Contents of File
     */
    Map viewFileContents(EngineConfig engineConfig, Map fileContestsMap, String username);

    /**
     * HDFS 경로에 사용자 홈 디렉토리를 생성한다.
     *
     * @param engineConfig Engine Config
     * @param hdfsUserHome HDFS User's home directory
     * @param username     Username
     * @return true or false
     */
    boolean createHdfsUserHome(EngineConfig engineConfig, String hdfsUserHome, String username);

    /**
     * HDFS 경로에 존재하는 해당 사용자의 홈 디렉토리를 삭제한다.
     *
     * @param engineConfig Engine Config
     * @param hdfsUserHome HDFS user home directory
     * @return true or false
     */
    boolean deleteHdfsUserHome(EngineConfig engineConfig, String hdfsUserHome);

    /**
     * 쓰기 금지 목록 패턴을 검증한다.
     *
     * @param path HDFS Path
     */
    void validatePath(String path);

    /**
     * Namenode Agent를 통해 직접 다운로드 한 후 Audit 로그를 저장한다.
     *
     * @param engineConfig          Engine Config
     * @param srcFilePath           HDFS file source path to download
     * @param fullyQualifiedPath    HDFS file fully qualified path to download
     * @param username              Username
     */
    void validateBeforeDownload(EngineConfig engineConfig, String srcFilePath, String fullyQualifiedPath, String username);
}
