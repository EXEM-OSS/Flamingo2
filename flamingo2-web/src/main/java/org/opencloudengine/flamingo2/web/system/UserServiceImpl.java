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

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.model.rest.Organization;
import org.opencloudengine.flamingo2.model.rest.User;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 로컬 DB에 저장된 플라밍고 사용자 정보를 관리하기 위한 User Service Implements
 *
 * @author Myeongha KIM
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Override
    public boolean acknowledge(String username, String password) {
        Map userMap = new HashMap();
        userMap.put("username", username);
        userMap.put("encodedPassword", password);

        return userRepository.updateByAck(userMap);
    }

    @Override
    public boolean createUser(Map userMap) {
        String username = (String) userMap.get("username");
        Long userId = null;

        if (userRepository.exist(username)) {
            throw new ServiceException("Already exists.");
        }

        if (userRepository.insertByUser(userMap)) {
            userId = userRepository.selectUserIdByUsername(username);
        }

        return userRepository.insertByAuth(userId);
    }

    @Override
    public boolean createUserByManager(Map userMap) {
        String username = (String) userMap.get("username");
        Long userId = null;

        if (userRepository.exist(username)) {
            throw new ServiceException("Already exist.");
        }

        if (userRepository.insertByManager(userMap)) {
            userId = userRepository.selectUserIdByUsername(username);
        }

        return userRepository.insertByAuth(userId);
    }

    @Override
    public boolean createWorkflowDesignerUser(String username) {
        return userRepository.insertWorkflowDesignerUser(username);
    }

    @Override
    public boolean updatePassword(Map userMap) {
        return userRepository.updatePassword(userMap);
    }

    @Override
    public boolean deleteUser(String username) {
        return userRepository.exist(username) && userRepository.deleteByUsername(username);
    }

    @Override
    public boolean deleteWorkflowDesignerUser(String username) {
        return userRepository.deleteWorkflowDesignerUser(username);
    }

    @Override
    public boolean updateUserInfo(Map userMap) {
        /**
         * 사용자 정보 변경 프로세서
         *
         * Case 1. 사용자 정보 변경, 등급: 일반사용자 -> 관리자
         * Case 2. 사용자 정보 변경, 등급: 관리자 -> 일반사용자
         * Case 3. 사용자 정보 변경
         */
        if (userMap.containsKey("level") && userMap.get("level") == 1) {
            return userRepository.insertUserAuth(userMap)
                    && userRepository.updateUserInfo(userMap);
        } else if (userMap.containsKey("level") && userMap.get("level") == 2) {
            return userRepository.deleteUserAuth(userMap) && userRepository.updateUserInfo(userMap);
        } else {
            return userRepository.updateUserInfo(userMap);
        }
    }

    @Override
    public User getUser(String username) {
        return userRepository.selectByUsername(username);
    }

    @Override
    public Organization getOrganization(long orgId) {
        Map organizationMap;
        organizationMap = userRepository.selectByOrgId(orgId);

        Organization organization = new Organization();
        organization.setId((Long) organizationMap.get("ID"));
        organization.setOrgCD(organizationMap.get("ORG_CD").toString());
        organization.setOrgNM(organizationMap.get("ORG_NM").toString());
        organization.setDescription(organizationMap.get("DESCRIPTION").toString());
        organization.setRegisterDate((Timestamp) organizationMap.get("REG_DT"));
        organization.setUpdateDate((Timestamp) organizationMap.get("UPD_DT"));

        return organization;
    }

    @Override
    public List<Map> getUserAll(Map conditionMap) {
        return userRepository.selectAll(conditionMap);
    }

    @Override
    public String getUserPassword(String username) {
        return userRepository.selectPasswordByUsername(username);
    }

    @Override
    public List<Map> getOrganizationAll() {
        return organizationRepository.selectAllOrganization();
    }

    @Override
    public boolean createOrganization(Map organizationMap) {
        return organizationRepository.insert(organizationMap);
    }

    @Override
    public boolean deleteOrganization(long orgId) {
        Map organizationMap = new HashMap();
        organizationMap.put("id", orgId);
        boolean deleted = false;

        /**
         * 삭제할 소속 정보를 다수의 유저가 사용하고 있을 경우
         * 관련된 모든 유저의 소속 정보를 기본 소속 코드 정보(OCE)로 초기화 한 후 소속 정보를 삭제한다.
         */
        if (userRepository.selectUserByOrgId(orgId)) {
            if (userRepository.updateById(organizationMap)) {
                deleted = organizationRepository.delete(organizationMap);
            }
        } else {
            deleted = organizationRepository.delete(organizationMap);
        }

        return deleted;
    }

    @Override
    public boolean updateOrganizationInfo(Map organizationMap) {
        return organizationRepository.update(organizationMap);
    }

    @Override
    public boolean updateUserHomeInfo(Map<String, String> userMap) {
        return userRepository.updateUserHomeInfo(userMap);
    }

    @Override
    public String selectPasswordByUsername(String username) {
        return userRepository.selectPasswordByUsername(username);
    }
}
