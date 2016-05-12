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
package org.opencloudengine.flamingo2.web.system;

import java.util.List;
import java.util.Map;

/**
 * HDFS Browser의 사용 권한을 관리하기 위한 HDFS Browser Authority Service Interface
 *
 * @author Myeongha KIM
 */
public interface HdfsBrowserAuthService {

    /**
     * HDFS 파일시스템 브라우저에 설정된 모든 Path Pattern의 기본 정보를 가져온다.
     *
     * @return HDFS Path Pattern List
     */
    List<Map> getHdfsAuthAll();

    /**
     * 선택한 Path Pattern에 설정된 상세 정보를 가져온다.
     *
     * @param hdfsAuthMap HDFS Authority Map
     * @return HDFS Path Pattern Detail List
     */
    Map getHdfsBrowserAuthDetail(Map hdfsAuthMap);

    /**
     * 사용자 권한 목록을 가져온다.
     *
     * @return User Authority List
     */
    List<Map> getUserAuthAll();

    /**
     * 사용자 등급 목록을 가져온다.
     *
     * @return User Level List
     */
    List<Map> getUserLevelAll();

    /**
     * 선택한 경로에 사용자 권한 및 등급에 맞는 Path Pattern 정보를 생성한다.
     *
     * @param hdfsBrowserAuthMap HDFS Browser Authority Map
     * @return True or False
     */
    boolean createHdfsBrowserAuth(Map hdfsBrowserAuthMap);

    /**
     * 선택한 경로에 사용자 권한 및 등급에 맞는 Path Pattern 정보를 삭제한다.
     *
     * @param hdfsBrowserAuthMap HDFS Browser Authority Map
     * @return True or False
     */
    boolean deleteHdfsBrowserAuth(Map hdfsBrowserAuthMap);

    /**
     * 선택한 경로에 사용자 권한 및 등급에 맞는 Path Pattern 정보를 업데이트한다.
     *
     * @param hdfsBrowserAuthMap HDFS Browser Authority Map
     * @return True or False
     */
    boolean updateHdfsBrowserAuth(Map hdfsBrowserAuthMap);

    /**
     * 로그인한 사용자의 등급에 설정된 Path Pattern 정보를 가져온다.
     *
     * @param username HDFS Browser Authority Map
     * @return True or False
     */
    List<String> getHdfsBrowserPatternAll(String username);

    /**
     * HDFS 파일시스템 브라우저에서 요청한 기능에 해당하는 현재 경로에 대해
     * 로그인한 사용자의 등급에 따라 조회된 패턴 목록을 비교한다.
     *
     * @param dirPath HDFS Browser에서 선택한 현재 경로
     * @param paths   사용자 등급에 할당된 사용 금지 목록
     * @return 사용자의 패턴 정보
     */
    String validateHdfsPathPattern(String dirPath, List<String> paths);

    /**
     * 로그인한 사용자의 등급에 설정된 Path Pattern 정보 중 디렉토리 기능 사용 권한이 있는지 체크한다.
     *
     * @param dirMap 디렉토리 권한 체크에 필요한 정보
     */
    void getHdfsBrowserUserDirAuth(Map<String, String> dirMap);

    /**
     * 로그인한 사용자의 등급에 설정된 Path Pattern 정보 중 파일 기능 사용 권한이 있는지 체크한다.
     *
     * @param fileMap 파일 권한 체크에 필요한 정보
     */
    void getHdfsBrowserUserFileAuth(Map<String, String> fileMap);

    /**
     * HDFS 브라우저의 사용자 홈 디렉토리에 대한 Write 권한을 체크한다.
     *
     * @param currentPath   현재 경로
     * @param filter        로그인한 사용자의 HDFS 사용자 홈 디렉토리
     * @param userLevel     사용자 등급
     */
    void validateHdfsHomeWritePermission(String currentPath, String filter, int userLevel);
}
