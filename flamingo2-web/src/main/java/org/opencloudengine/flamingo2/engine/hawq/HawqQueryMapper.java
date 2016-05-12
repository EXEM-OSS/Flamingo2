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
package org.opencloudengine.flamingo2.engine.hawq;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.hawq.xml.QueriesType;
import org.opencloudengine.flamingo2.engine.hawq.xml.QueryType;
import org.opencloudengine.flamingo2.util.JaxbUtils;
import org.opencloudengine.flamingo2.util.ResourceUtils;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pivotal HAWQ Query Registry Mapper.
 * classpath:hawq-query.xml 에 저장된 query 를 불러오는 Component.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
@Component
public class HawqQueryMapper implements InitializingBean {

    private Map<String, String> queryMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.queryMap = new HashMap<>();

        String xml = ResourceUtils.getResourceTextContents("classpath:hawq-query.xml");
        JAXBElement element = (JAXBElement) JaxbUtils.unmarshal("org.opencloudengine.flamingo2.engine.hawq.xml", xml);

        QueriesType queries = (QueriesType) element.getValue();
        List<QueryType> queryTypes = queries.getQuery();
        for (QueryType queryType : queryTypes) {
            String query = this.queryMap.get(queryType.getId());
            if (query != null) {
                throw new ServiceException("The HAWQ Query id \"" + queryType.getId() + "\" is duplicated.");
            }
            this.queryMap.put(queryType.getId(), queryType.getValue());
        }
    }

    /**
     * queryId 에 해당하는 query 에 params 를 매핑하여 가져온다.
     * query 에는 MyBatis 와 같이 #{} 와 ${} 를 사용 가능한데
     * #{} 는 MyBatis 와 같이 class 가 String 일 경우 ' 가 붙고,
     * String 이 아닌 primitive type 일 경우 ' 가 붙지 않고 query 가 생성된다.
     * ${} 는 MyBatis 와 같이 class 에 상관없이 ' 가 붙지 않고 query 가 생성된다.
     *
     * @param queryId query 태그의 id
     * @param params  query 에 들어가는 parameter
     * @return 생성된 query
     */
    public String getQuery(String queryId, Map<String, Object> params) {
        String query = getQuery(queryId);

        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    query = StringUtils.replace(query, "${" + key + "}", params.get(key).toString());
                    if (params.get(key) instanceof String) {
                        query = StringUtils.replace(query, "#{" + key + "}", "'" + params.get(key) + "'");
                    } else {
                        query = StringUtils.replace(query, "#{" + key + "}", params.get(key).toString());
                    }
                }
            }
        }

        return query;
    }

    // ========================================================================================
    // private methods
    // ========================================================================================
    private String getQuery(String queryId) {
        if (queryId == null || "".equals(queryId.trim())) {
            throw new ServiceException("HAWQ Query id is blank. id = " + queryId);
        }
        String query = this.queryMap.get(queryId);
        if (query == null) {
            throw new ServiceException("HAWQ Query id \"" + queryId + "\" doesn't exist.");
        }
        if (StringUtils.isEmpty(query.trim())) {
            throw new ServiceException("The query corresponding to the HAWQ Query id \"" + queryId + "\" is empty.");
        }
        return query.trim();
    }

}