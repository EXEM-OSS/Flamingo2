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
package org.opencloudengine.flamingo2.agent.system;

/**
 * System User Service Remote Interface.
 * SystemUserServiceDelegator를 통해 System User Service 통신한다.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public interface SystemUserAgentService {

    /**
     * 시스템 사용자가 존재하는지 확인한다.
     *
     * @param username 사용자명
     * @return	true or false
     */
    boolean existUser(String username);

    /**
     * 시스템 사용자를 생성한다.
     *
     * @param username	        사용자명
     * @param name	            이름
     * @param password	        비밀번호
     * @param linuxUserHome	    리눅스 사용자 홈 디렉토리
     * @return	true or false
     */
    boolean createUser(String username, String name, String password, String linuxUserHome);

    /**
     * 시스템 사용자의 비밀번호를 변경한다.
     *
     * @param username	사용자명
     * @param password	비밀번호
     * @return true or false
     */
    boolean changeUser(String username, String password);

    /**
     * 시스템 사용자를 삭제한다.
     *
     * @param username	사용자명
     * @return	true or false
     */
    boolean deleteUser(String username);
}