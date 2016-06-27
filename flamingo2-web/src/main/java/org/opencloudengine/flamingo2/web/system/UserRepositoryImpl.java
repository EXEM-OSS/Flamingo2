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

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.opencloudengine.flamingo2.model.rest.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 사용자 정보에 대한 CRUD 기능을 처리하는 User Repository Implements
 *
 * @author Myeongha KIM
 */
@Repository
public class UserRepositoryImpl extends PersistentRepositoryImpl<String, Object> implements UserRepository {

    @Override
    public String getNamespace() {
        return this.NAMESPACE;
    }

    @Autowired
    public UserRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public User selectByUsername(String username) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectByUsername", username);
    }

    @Override
    public Map selectByOrgId(long orgId) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectByOrgId", orgId);
    }

    @Override
    public Long selectUserIdByUsername(String username) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectUserIdByUsername", username);
    }

    @Override
    public boolean insertByUser(Map userMap) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertByUser", userMap) > 0;
    }

    @Override
    public boolean insertByManager(Map userMap) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertByManager", userMap) > 0;
    }

    @Override
    public boolean insertByAuth(Long userId) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertByAuth", userId) > 0;
    }

    @Override
    public boolean insertWorkflowDesignerUser(String username) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertWorkflowDesignerUser", username) > 0;
    }

    @Override
    public boolean updateByAck(Map userMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateByAck", userMap) > 0;
    }

    @Override
    public List<Map> selectAll(Map conditionMap) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectAll", conditionMap);
    }

    @Override
    public String selectPasswordByUsername(String username) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectPasswordByUsername", username);
    }

    @Override
    public boolean exist(String username) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".exist", username).size() > 0;
    }

    @Override
    public boolean updatePassword(Map userMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updatePassword", userMap) > 0;
    }

    @Override
    public boolean deleteByUsername(String username) {
        return this.getSqlSessionTemplate().delete(this.getNamespace() + ".deleteByUsername", username) > 0;
    }

    @Override
    public boolean deleteWorkflowDesignerUser(String username) {
        return this.getSqlSessionTemplate().delete(this.getNamespace() + ".deleteWorkflowDesigner", username) > 0;
    }

    @Override
    public boolean updateUserInfo(Map userMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateUserInfo", userMap) > 0;
    }

    @Override
    public boolean updateById(Map orgMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateById", orgMap) > 0;
    }

    @Override
    public boolean updateUserHomeInfo(Map userMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateUserHomeInfo", userMap) > 0;
    }

    @Override
    public boolean selectUserByOrgId(long orgId) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectUserByOrgId", orgId).size() > 0;
    }

    @Override
    public boolean insertUserAuth(Map userMap) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertUserAuth", userMap) > 0;
    }

    @Override
    public boolean deleteUserAuth(Map userMap) {
        return this.getSqlSessionTemplate().delete(this.getNamespace() + ".deleteUserAuth", userMap) > 0;
    }
}