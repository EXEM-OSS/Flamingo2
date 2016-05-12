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
 * 조직 정보에 대한 CRUD 기능을 처리하는 User Repository Implements
 *
 * @author Myeongha KIM
 */
@Repository
public class OrganizationRepositoryImpl extends PersistentRepositoryImpl<String, Object> implements OrganizationRepository {

    @Override
    public String getNamespace() {
        return this.NAMESPACE;
    }

    @Autowired
    public OrganizationRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<Map> selectAllOrganization() {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectAll");
    }

    @Override
    public int insert(Map organizationMap) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insert", organizationMap);
    }

    @Override
    public int delete(Map organizationMap) {
        return this.getSqlSessionTemplate().delete(this.getNamespace() + ".delete", organizationMap);
    }

    @Override
    public int update(Map organizationMap) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".update", organizationMap);
    }

}
