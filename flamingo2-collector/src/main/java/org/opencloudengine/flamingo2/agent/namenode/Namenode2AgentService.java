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
package org.opencloudengine.flamingo2.agent.namenode;

import org.opencloudengine.flamingo2.model.rest.FileInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public interface Namenode2AgentService {

    public Map getNamenodeInfo();

    public Map<String, Object> getConfiguration();

    public Map<String, Object> getMetrics();

    public List<Map<String, Object>> getDataNodes();

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
     * @param path 디렉토리 경로
     * @return 정상적으로 생성한 경우 <tt>true</tt>
     */
    boolean mkdir(String path);

    /**
     * 지정한 디렉토리를 선택한 경로로 복사한다.
     *
     * @param srcPath 복사할 디렉토리
     * @param dstPath 복사될 경로
     * @return 정상적으로 복사한 경우 <tt>true</tt>
     */
    boolean copy(String srcPath, String dstPath);

    /**
     * 지정한 디렉토리를 선택한 경로로 복사한다.
     *
     * @param srcPath 이동할 디렉토리
     * @param dstPath 이동될 경로
     * @return 정상적으로 복사한 경우 <tt>true</tt>
     */
    boolean move(String srcPath, String dstPath);

    /**
     * 지정한 디렉토리명을 변경한다.
     *
     * @param oldName 원본 디렉토리 경로
     * @param newName 변경될 디렉토리명
     * @return 정상적으로 변경된 경우 <tt>true</tt>
     */
    boolean rename(String oldName, String newName);

    /**
     * 지정한 경로를 삭제한다.
     *
     * @param path 삭제할 디렉토리 경로
     * @return 정상 삭제 여부
     */
    boolean delete(String path);

    /**
     * 선택한 디렉토리 또는 파일의 정보를 조회한다.
     *
     * @param path 조회할 디렉토리 또는 파일 경로
     * @return 디렉토리 및 파일 정보
     */
    FileInfo getFileInfo(String path);
}