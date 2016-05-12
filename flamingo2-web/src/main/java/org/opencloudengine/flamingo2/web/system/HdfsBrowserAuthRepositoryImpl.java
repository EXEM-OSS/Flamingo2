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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * HDFS Browser에 대한 CRUD 기능을 처리하는 HDFS Browser Authority Repository Implements
 *
 * @author Myeongha KIM
 */
@Repository
public class HdfsBrowserAuthRepositoryImpl extends PersistentRepositoryImpl<String, Object> implements HdfsBrowserAuthRepository {

    @Override
    public String getNamespace() {
        return this.NAMESPACE;
    }

    @Autowired
    public HdfsBrowserAuthRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<Map> selectHdfsAuthAll() {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectHdfsAuthAll");
    }

    @Override
    public Map selectHdfsAuthDetail(Map hdfsAuthMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectHdfsAuthDetail", hdfsAuthMap);
    }

    @Override
    public List<Map> selectUserAuth() {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectUserAuthAll");
    }

    @Override
    public List<Map> selectUserLevel() {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectUserLevelAll");
    }

    @Override
    public List<String> selectHdfsBrowserPatternAll(String username) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectHdfsBrowserPatternAll", username);
    }

    @Override
    public int selectHdfsBrowserUserDirAuth(Map<String, String> dirMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectHdfsBrowserUserDirAuth", dirMap);
    }

    @Override
    public int selectHdfsBrowserUserFileAuth(Map<String, String> fileMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectHdfsBrowserUserFileAuth", fileMap);
    }

    @Override
    public int insertHdfsBrowserAuth(Map hdfsBrowserAuthMap) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insertHdfsBrowserAuth", hdfsBrowserAuthMap);
    }

    @Override
    public int deleteHdfsBrowserAuth(Map hdfsBrowserAuthMap) {
        return this.getSqlSessionTemplate().delete(this.getNamespace() + ".deleteHdfsBrowserAuth", hdfsBrowserAuthMap);
    }

    @Override
    public int exist(Map hdfsBrowserAuthMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".exist", hdfsBrowserAuthMap);
    }

    @Override
    public int updatedHdfsBrowserAuth(Map hdfsBrowserAuthMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".updatedHdfsBrowserAuth", hdfsBrowserAuthMap.get("hdfsAuthModFormValues"));
    }
}