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
    public int insertByUser(Map userMap) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertByUser", userMap);
    }

    @Override
    public int insertByManager(Map userMap) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertByManager", userMap);
    }

    @Override
    public int insertByAuth(Long userId) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertByAuth", userId);
    }

    @Override
    public int updateByAck(Map userMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateByAck", userMap);
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
    public int exist(String username) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".exist", username);
    }

    @Override
    public int updatePassword(Map userMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updatePassword", userMap);
    }

    @Override
    public int deleteByUsername(String username) {
        return this.getSqlSessionTemplate().delete(this.getNamespace() + ".deleteByUsername", username);
    }

    @Override
    public int updateUserInfo(Map userMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateUserInfo", userMap);
    }

    @Override
    public int updateById(Map orgMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateById", orgMap);
    }

    @Override
    public int updateUserHomeInfo(Map userMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updateUserHomeInfo", userMap);
    }

    @Override
    public int selectUserByOrgId(long orgId) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectUserByOrgId", orgId);
    }
}