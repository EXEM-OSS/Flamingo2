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

@Repository
public class MenuRepositoryImpl extends PersistentRepositoryImpl<String, Object> implements MenuRepository {

    @Override
    public String getNamespace() {
        return this.NAMESPACE;
    }

    @Autowired
    public MenuRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public List<Map<String, Object>> select(Map<String, Object> params) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".select", params);
    }

    @Override
    public List<Map<String, Object>> selectNode(Map<String, Object> params) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectNode", params);
    }

    @Override
    public void insert(Map<String, Object> params) {
        this.getSqlSessionTemplate().insert(this.getNamespace() + ".insert", params);
    }

    @Override
    public void update(Map<String, Object> params) {
        this.getSqlSessionTemplate().update(this.getNamespace() + ".update", params);
    }

    @Override
    public void delete(Map<String, Object> params) {
        this.getSqlSessionTemplate().delete(this.getNamespace() + ".delete", params);
    }
}
