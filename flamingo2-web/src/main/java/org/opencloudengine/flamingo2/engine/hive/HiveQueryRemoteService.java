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

import org.apache.hadoop.hive.ql.parse.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HiveQueryRemoteService {

    /**
     * Hive Query의 유효성을 검증한다.
     *
     * @param query Hive Query
     * @throws ParseException Hive Query 문법이 유효하지 않은 경우
     */
    void validateQuery(String query) throws ParseException;

    /**
     * Hive Query를 실행한다.
     */
    void execute(Map params);

    void executeAsync(Map params);

    String getLog(String uuid);

    void getLogAsync(Map params);

    Map[] getResults(String uuid);

    void removeAll(List hiveIds);

    boolean isEnd(String uuid);

    boolean isExist(String uuid);

    String getErrorLog(String uuid);

    void remove(String uuid);

    String getStatus(String uuid);

    Map[] getPage(Map params);

    byte[] getResultToCsv(String uuid) throws IOException;
}
