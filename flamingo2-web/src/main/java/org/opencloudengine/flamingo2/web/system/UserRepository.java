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

import org.opencloudengine.flamingo2.model.rest.User;

import java.util.List;
import java.util.Map;

/**
 * 사용자 정보에 대한 CRUD 기능을 처리하는 User Repository
 *
 * @author Myeongha KIM
 */
public interface UserRepository {

    String NAMESPACE = UserRepository.class.getName();

    User selectByUsername(String username);

    Map selectByOrgId(long orgId);

    Long selectUserIdByUsername(String username);

    boolean insertByUser(Map params);

    boolean insertByManager(Map params);

    boolean insertByAuth(Long userId);

    boolean insertWorkflowDesignerUser(String username);

    boolean updateByAck(Map userMap);

    List<Map> selectAll(Map conditionMap);

    String selectPasswordByUsername(String username);

    boolean exist(String username);

    boolean updatePassword(Map params);

    boolean deleteByUsername(String username);

    boolean deleteWorkflowDesignerUser(String username);

    boolean updateUserInfo(Map userMap);

    boolean updateById(Map orgMap);

    boolean updateUserHomeInfo(Map userMap);

    boolean selectUserByOrgId(long orgId);

    boolean insertUserAuth(Map userMap);

    boolean deleteUserAuth(Map userMap);
}
