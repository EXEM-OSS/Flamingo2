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

import org.apache.thrift.TException;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HiveMetastoreService {
    public List<Map> getAllDatabases(EngineConfig engineConfig) throws TException;

    public List<Map> getTables(EngineConfig engineConfig, String dbname) throws TException;

    public List<Map> getColumns(EngineConfig engineConfig, String dbname, String tablename) throws TException;

    public List<Map> getPartitions(EngineConfig engineConfig, String dbname, String tablename) throws TException;

    public void createDatabase(EngineConfig engineConfig, Map params) throws TException, IOException;

    public void dropDatabase(EngineConfig engineConfig, String dbname) throws TException;

    public void createTable(EngineConfig engineConfig, Map params) throws IOException, TException;

    public void dropTable(EngineConfig engineConfig, String dbname, String tablename) throws TException;

    public Map getTableInfo(EngineConfig engineConfig, String dbname, String tablename) throws TException;

    public void alterTable(EngineConfig engineConfig, Map params) throws TException, IOException;
}
