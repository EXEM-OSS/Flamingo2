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
package org.opencloudengine.flamingo2.engine.tajo;

import org.apache.thrift.TException;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Park on 15. 7. 14..
 */
public interface TajoRemoteService {
    List getAllDatabases(EngineConfig engineConfig);

    List getTables(EngineConfig engineConfig, String dbname);

    List<Map> getColumns(EngineConfig engineConfig, String dbname, String tablename);

    List<Map> getPartitions(EngineConfig engineConfig, String dbname, String tablename);

    void createDatabase(EngineConfig engineConfig, Map params);

    void dropDatabase(EngineConfig engineConfig, String dbname);

    void createTable(EngineConfig engineConfig, Map params);

    void dropTable(EngineConfig engineConfig, String dbname, String tablename);

    Map execute(EngineConfig engineConfig, Map params);

    Map getResults(EngineConfig engineConfig, Map params);

    void removeQueryId(String uuid);

    void closeQuery(EngineConfig engineConfig, Map params);
}
