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
package org.opencloudengine.flamingo2.engine.hive;

import org.apache.hive.service.cli.FetchOrientation;
import org.apache.hive.service.cli.FetchType;
import org.apache.hive.service.cli.HiveSQLException;
import org.apache.hive.service.cli.thrift.TFetchOrientation;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.opencloudengine.flamingo2.model.hive.Schema;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Thrift API를 이용한 Hive Server 2 Client.
 */
public interface HiveThrift2Client {

    public void addHiveVariable(String key, String value);

    public void addHiveConfiguration(String key, String value);

    public String getUsername();

    public String getPassword();

    /**
     * Hive Thrift 세션을 오픈한다.
     *
     * @throws SQLException
     */
    public void openSession() throws SQLException, TException;

    public void closeSession() throws HiveSQLException, TException;

    public TTransport getTransport();

    public void connect() throws SQLException;

    public void execute(String query) throws SQLException, TException;

    public void executeAsync(String query) throws SQLException, TException;

    public void execute(String query, boolean async) throws SQLException, TException;

    public void execute(List<String> queries) throws SQLException, TException;

    public List<Schema> getResultSchema() throws SQLException, TException;

    public Map[] getResults() throws SQLException, TException;

    public String getStatus() throws SQLException, TException;

    public String getLog() throws HiveSQLException, TException;

    public Map[] getResults(FetchOrientation orientation, int maxRows, FetchType fetchType) throws Exception;

    public Map[] getResults(TFetchOrientation orientation, int maxRows, FetchType fetchType) throws Exception;

    public Map[] getResults(int maxRows) throws SQLException, TException;

    public String getError();
}
