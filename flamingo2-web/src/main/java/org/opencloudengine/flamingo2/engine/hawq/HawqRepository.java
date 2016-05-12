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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Pivotal HAWQ Repository.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
public interface HawqRepository {

    HawqConnection getHawqConnection(Map<String, Object> params) throws ClassNotFoundException, SQLException;

    HawqConnection getPostgresqlConnection(Map<String, Object> params) throws ClassNotFoundException, SQLException;

    int executeUpdateQueryById(String queryId, Map<String, Object> params) throws SQLException;

    int executeUpdateQuery(String query, Map<String, Object> params) throws SQLException;

    List<Object> executeSelectListById(String queryId, Map<String, Object> params) throws SQLException;

    <T> List<T> executeSelectListById(String queryId, Map<String, Object> params, Class<T> cls) throws SQLException;

    List<Object> executeSelectList(String query, Map<String, Object> params) throws SQLException;

    Object executeSelectOne(String queryId, Map<String, Object> params) throws SQLException;

    <T> T executeSelectOneForFirstColumn(String queryId, Map<String, Object> params, Class<T> cls) throws SQLException;

    List<Object> getResultSetObjects(ResultSet rs) throws SQLException;
}