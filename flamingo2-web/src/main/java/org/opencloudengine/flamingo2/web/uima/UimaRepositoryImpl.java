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
package org.opencloudengine.flamingo2.web.uima;

import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.flamingo2.core.repository.PersistentRepositoryImpl;
import org.opencloudengine.flamingo2.model.rest.Uima;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * UIMA Log 정보에 대한 CRUD 기능을 처리하는 UIMA Repository Implements
 *
 * @author Myeongha KIM
 * @since 2.1.0
 */
@Repository
public class UimaRepositoryImpl extends PersistentRepositoryImpl<String, Object> implements UimaRepository {

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Autowired
    public UimaRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public int selectTotalCount(Map uimaMap) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectTotalCount", uimaMap);
    }

    @Override
    public List<Uima> selectAllUimaLogs(Map uimaMap) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectAllUimaLogs", uimaMap);
    }

    @Override
    public List<Map<String, Object>> selectUimaLogSummary(Map uimaMap) {
        return this.getSqlSessionTemplate().selectList(this.getNamespace() + ".selectUimaLogSummary", uimaMap);
    }
}