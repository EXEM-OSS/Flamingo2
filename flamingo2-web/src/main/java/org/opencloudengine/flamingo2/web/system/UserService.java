/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.flamingo2.web.system;

import org.opencloudengine.flamingo2.model.rest.Organization;
import org.opencloudengine.flamingo2.model.rest.User;

import java.util.List;
import java.util.Map;

/**
 * 로컬 DB에 저장된 플라밍고 사용자 정보를 관리하기 위한 User Service Interface
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public interface UserService {

    /**
     * 관리자의 승인 후 사용자를 추가한다.
     *
     * @param username 사용자명
     * @param password 비밀번호
     * @return true or false
     */
    boolean acknowledge(String username, String password);

    /**
     * 사용자의 가입 신청을 처리한다.
     *
     * @param userMap User Map
     * @return true or false
     */
    boolean createUser(Map userMap);

    /**
     * 관리자의 사용자 가입 신청을 처리한다.
     *
     * @param userMap User Map
     * @return true or false
     */
    boolean createUserByManager(Map userMap);

    /**
     * 사용자의 비밀번호를 변경한다.
     *
     * @param userMap User Map
     * @return true or false
     */
    boolean updatePassword(Map userMap);

    /**
     * 사용자를 삭제한다.
     *
     * @param username 사용자명
     * @return true or false
     */
    boolean deleteUser(String username);

    /**
     * 사용자 정보를 수정한다.
     *
     * @param userMap User Map
     * @return true or false
     */
    boolean updateUserInfo(Map userMap);

    /**
     * 사용자 정보를 가져온다.
     *
     * @param username 사용자명
     * @return User Information
     */
    User getUser(String username);

    /**
     * Organization 정보를 가져온다.
     *
     * @param orgId 소속 ID
     * @return Organization Information
     */
    Organization getOrganization(long orgId);

    /**
     * 등록된 모든 사용자 정보를 가져온다.
     *
     * @param conditionMap 조회 조건
     * @return User List
     */
    List<Map> getUserAll(Map conditionMap);

    /**
     * 소속 목록을 가져온다.
     *
     * @return Organization List
     */
    List<Map> getOrganizationAll();

    /**
     * 사용자의 비밀번호를 가져온다.
     *
     * @param username 사용자명
     * @return 비밀번호
     */
    String getUserPassword(String username);

    /**
     * 소속 정보를 추가한다.
     *
     * @param organizationMap Organization Map
     * @return true or false
     */
    boolean createOrganization(Map organizationMap);

    /**
     * 소속 정보를 삭제한다.
     * 소속 정보의 경우는 여러 사용자가 소속 정보를 공유하고 있기에 삭제 조건이 반드시 필요.
     *
     * @param orgId 소속 ID
     * @return true for false
     */
    boolean deleteOrganization(long orgId);

    /**
     * 소속 정보를 갱신한다.
     *
     * @param organizationMap Organization Map
     * @return true or false
     */
    boolean updateOrganizationInfo(Map organizationMap);

    /**
     * 리눅스 사용자 및 HDFS 사용자의 홈 디렉토리 정보를 업데이트 한다.
     *
     * @param userMap 사용자 정보
     * @return true or false
     */
    boolean updateUserHomeInfo(Map<String, String> userMap);
}
