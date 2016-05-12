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
package org.opencloudengine.flamingo2.agent.namenode;

import org.opencloudengine.flamingo2.engine.fs.hdfs.HdfsFileInfo;
import org.opencloudengine.flamingo2.model.rest.FileInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Namenode2AgentService {

    public Map getNamenodeInfo();

    public Map<String, Object> getConfiguration();

    public Map<String, Object> getMetrics();

    List getDatanodes() throws IOException;

    public List<Map<String, Object>> getLiveNodes();

    public List<Map<String, Object>> getDeadNodes();

    public List<Map<String, Object>> getDecommissioningNodes();

    public String getFileContents(String path, int chunkSize, long startOffset);

    Map<String, Long> getJVMHeap();

    List<Map> getTop5() throws IOException;

    List<FileInfo> list(String path, boolean directoryOnly);

    /**
     * 선택한 경로에 디렉토리를 생성한다.
     *
     * @param srcPath   디렉토리 경로
     * @param username  Username
     * @return 정상적으로 생성한 경우 <tt>true</tt>
     */
    boolean mkdir(String srcPath, String username);

    /**
     * 지정한 디렉토리 또는 파일을 선택한 경로로 복사한다.
     *
     * @param srcPath   복사할 디렉토리 또는 파일(멀티파일 포함)
     * @param dstPath   복사될 경로
     * @param username  Username
     * @return 정상적으로 복사한 경우 <tt>true</tt>
     */
    boolean copy(String srcPath, String dstPath, String username);

    /**
     * 지정한 디렉토리 또는 파일을 선택한 경로로 복사한다.
     *
     * @param srcPath 이동할 디렉토리 또는 파일(멀티파일 포함)
     * @param dstPath 이동될 경로
     * @return 정상적으로 복사한 경우 <tt>true</tt>
     */
    boolean move(String srcPath, String dstPath);

    /**
     * 지정한 디렉토리 또는 파일명을 변경한다.
     *
     * @param srcPath  원본 디렉토리 또는 파일의 전체 경로
     * @param filename 변경될 디렉토리명 또는 파일명
     * @return 정상적으로 변경된 경우 <tt>true</tt>
     */
    boolean rename(String srcPath, String filename);

    /**
     * 지정한 디렉토리 또는 파일(멀티파일 포함)을 삭제한다.
     *
     * @param srcPath 삭제할 디렉토리 또는 파일 경로
     * @return 정상 삭제 여부
     */
    boolean delete(String srcPath);

    /**
     * 지정한 디렉토리에 존재하는 모든 파일들을 병합한다.
     *
     * @param srcPath   병합할 파일이 존재하는 경로
     * @param dstPath   병합할 파일이 저장될 경로
     * @param username  Username
     * @return 정상적으로 병합된 경우 <tt>true</tt>
     */
    boolean merge(String srcPath, String dstPath, String username);

    /**
     * 선택한 디렉토리 또는 파일의 정보를 조회한다.
     *
     * @param srcPath 조회할 디렉토리 또는 파일 경로
     * @return 디렉토리 및 파일 정보
     */
    HdfsFileInfo getFileInfo(String srcPath);

    /**
     * 선택한 디렉토리 또는 파일의 권한을 변경한다.
     *
     * @param permissionMap 선택한 경로의 사용자 및 그룹, 권한을 변경한다.
     * @return 정상적으로 병합한 경우 <tt>true</tt>
     */
    boolean setPermission(Map permissionMap);

    /**
     * 지정한 경로에 업로드한 파일을 저장한다.
     *
     * @param pathToUpload       업로드할 파일의 경로
     * @param fullyQualifiedPath 업로드할 파일의 전체 경로
     * @param content            업로드한 파일의 바이트 배열
     * @param username           Username
     * @return 정상적으로 업로드한 경우 <tt>true</tt>
     */
    boolean save(String pathToUpload, String fullyQualifiedPath, byte[] content, String username);

    /**
     * 지정한 경로의 파일을 로딩한다. 이 메소드는 큰 파일 또는 동시에 다수의 사용자가 호출하는 경우 많는 양의 Heap을 소비할 수 있다.
     *
     * @param fullyQualifiedPath  다운로드할 파일의 전체 경로
     * @return 파일의 바이트 배열
     */
    byte[] load(String fullyQualifiedPath);

    /**
     * 선택한 파일의 내용을 지정한 사이즈 단위로 읽어온다.
     *
     * @param fileContentsMap 파일 내용보기에 필요한 정보
     * @return 선택한 페이지 범위에 해당하는 파일 내용
     */
    Map view(Map fileContentsMap);

    /**
     * HDFS 경로에 사용자 홈 디렉토리를 생성한다.
     *
     * @param hdfsUserMap HDFS 사용자 홈 디렉토리 생성 및 권한 설정에 필요한 정보
     * @return true or false
     */
    boolean createUserHome(Map hdfsUserMap);

    /**
     * HDFS 경로에 사용자 홈 디렉토리를 삭제한다.
     *
     * @param hdfsUserHomePath HDFS 사용자 홈 디렉토리 경로
     * @return true or false
     */
    boolean deleteUserHome(String hdfsUserHomePath);
}
