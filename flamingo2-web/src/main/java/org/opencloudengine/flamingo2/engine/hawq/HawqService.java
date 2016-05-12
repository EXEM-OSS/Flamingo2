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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Pivotal HAWQ Service.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
public interface HawqService {

    Map<String, Object> connect(Map<String, Object> params) throws SQLException;

    List<Object> getDatabases(Map<String, Object> params) throws SQLException;

    List<Object> getSchemas(Map<String, Object> params) throws SQLException;

    List<Object> getTables(Map<String, Object> params) throws SQLException;

    List<Object> getViews(Map<String, Object> params) throws SQLException;

    List<Object> getFunctions(Map<String, Object> params) throws SQLException;

    List<Object> getExternalTables(Map<String, Object> params) throws SQLException;

    List<Object> getColumns(Map<String, Object> params) throws SQLException;

    List<Object> getObjectMetadatas(Map<String, Object> params) throws SQLException;

    Object getPartitionDetail(Map<String, Object> params) throws SQLException;

    List<Object> getPartitionsTree(Map<String, Object> params) throws SQLException;

    String getObjectDef(Map<String, Object> params) throws SQLException;

    void getQueryResultAsync(Map<String, Object> params) throws SQLException, ClassNotFoundException;

    String dropDatabase(Map<String, Object> params) throws SQLException;

    int createDatabase(Map<String, Object> params) throws SQLException;

    int dropSchema(Map<String, Object> params) throws SQLException;

    int createSchema(Map<String, Object> params) throws SQLException;

    List<Object> getTablespaces(Map<String, Object> params) throws SQLException;

    List<Object> getUsers(Map<String, Object> params) throws SQLException;

    int dropTable(Map<String, Object> params) throws SQLException;

    int createTable(Map<String, Object> params) throws SQLException;

    int dropExternalTable(Map<String, Object> params) throws SQLException;

    List<Object> getCustomFormatter(Map<String, Object> params) throws SQLException;

    int createExternalTable(Map<String, Object> params) throws SQLException, IOException;

    int dropView(Map<String, Object> params) throws SQLException;

    String getDefaultSchema(Map<String, Object> params) throws SQLException;

    List<Object> getQueryPlan(Map<String, Object> params) throws SQLException;

    int dropFunction(Map<String, Object> params) throws SQLException;

    Map<String, Object> tableDetail(Map<String, Object> params) throws SQLException;

    int alterTable(Map<String, Object> params) throws SQLException;

    List<Object> alterColumn(Map<String, Object> params) throws SQLException;

    List<Object> getConstraints(Map<String, Object> params) throws SQLException;

    List<Object> alterConstraint(Map<String, Object> params) throws SQLException;

    boolean killSession(Map<String, Object> params) throws SQLException;

    List<Object> getRQueues(Map<String, Object> params) throws SQLException;

    Object getRQueue(Map<String, Object> params) throws SQLException;

    int createResourceQueue(Map<String, Object> params) throws SQLException;

    int dropResourceQueue(Map<String, Object> params) throws SQLException;

    List<Object> getGroupRoles(Map<String, Object> params) throws SQLException;

    List<Object> getLoginRoles(Map<String, Object> params) throws SQLException;

    Object role(Map<String, Object> params) throws SQLException;

    int createRole(Map<String, Object> params) throws SQLException;

    int alterRole(Map<String, Object> params) throws SQLException;

    int dropRole(Map<String, Object> params) throws SQLException;

    List<Object> getSessions(Map<String, Object> params) throws SQLException;

    long getSessionsTotal(Map<String, Object> params) throws SQLException;

    List<Object> getLockTables(Map<String, Object> params) throws SQLException;

    long getLockTablesTotal(Map<String, Object> params) throws SQLException;

    byte[] getResultToCsv(Map<String, Object> params) throws IOException;
}