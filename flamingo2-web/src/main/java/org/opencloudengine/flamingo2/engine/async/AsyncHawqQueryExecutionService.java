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
package org.opencloudengine.flamingo2.engine.async;

import org.opencloudengine.flamingo2.backend.ConsumerNameAware;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.util.JsonUtils;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.event.Event;
import reactor.function.Consumer;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HAWQ query 실행시 ResultSet 하나씩 비동기로 query 결과를 보내주는 Service.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
@Service
public class AsyncHawqQueryExecutionService implements ConsumerNameAware, Consumer<Event<Map<String, Object>>> {

    private static final String QUERY_DESTINATION = "/topic/hawqQuery";

    private static final String MESSAGE_DESTINATION = "/topic/hawqMessage";

    @Autowired
    private WebSocketUtil webSocketUtil;

    private Logger logger = LoggerFactory.getLogger(AsyncHawqQueryExecutionService.class);

    /**
     * 조회시 컬럼명이 중복될 경우 사용하는 값.
     * 컬럼명은 63바이트까지 지원되므로 64글자 이상으로 설정함.
     */
    private String DUPLICATED_COLUMN_KEY = "_pivotal_hawq_management_editor_executed_query_by_hawq_query_editor_";

    @Override
    public String getName() {
        return "hawqQuery";
    }

    @Override
    public void accept(Event<Map<String, Object>> event) {
        Map<String, Object> params = event.getData();
        String websocketKey = params.get("websocketKey").toString();

        Response response = new Response();
        response.getMap().put("uuid", params.get("uuid"));
        response.getMap().put("pid", params.get("pid"));

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        String query = params.get("query").toString();
        try {
            long beforeRunningTime = System.currentTimeMillis();
            connection = (Connection) params.get("connection");

            Map<String, Object> queryResult = new HashMap<>();
            statement = connection.createStatement();

            try {
                Object maxRowsObject = params.get("maxRows");
                int maxRows = maxRowsObject == null ? 1000 : Integer.parseInt(params.get("maxRows").toString());
                if (maxRows > 0) {
                    statement.setMaxRows(maxRows);
                }

                String replaceQuery = query.replaceAll("(?m)(--.*)|(/\\*(.|\\s)*\\*/)", "").trim();// 주석 제거

                if (replaceQuery.toUpperCase().startsWith("SELECT") ||
                        replaceQuery.toUpperCase().startsWith("SHOW") ||
                        replaceQuery.toUpperCase().startsWith("EXPLAIN")
                        ) {
                    String aliasRemovedQuery = replaceQuery.replaceAll("\".*\"", "").trim();
                    String aliasNstringRemovedQuery = aliasRemovedQuery.replaceAll("'.*'", "");

                    Pattern pattern = Pattern.compile("\\sinto\\s", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(aliasNstringRemovedQuery);
                    boolean existInto = false;
                    if (matcher.find()) {
                        existInto = true;
                    }

                    pattern = Pattern.compile("\\s*select\\s", Pattern.CASE_INSENSITIVE);
                    matcher = pattern.matcher(aliasNstringRemovedQuery);
                    boolean existSelect = false;
                    if (matcher.find()) {
                        existSelect = true;
                    }

                    if (existInto && existSelect) {
                        logger.debug("select into");
                        statement.executeUpdate(query);
                        response.setSuccess(true);
                        sendMessage(websocketKey, response, statement);

                        if (!connection.getAutoCommit()) {
                            connection.commit();
                        }

                        queryResult.put("queryType", "NOT_SELECT_QUERY");
                    } else {
                        rs = statement.executeQuery(query);
                        response.setSuccess(true);
                        sendMessage(websocketKey, response, statement);

                        ResultSetMetaData rsMetaData = rs.getMetaData();
                        int columnCount = rsMetaData.getColumnCount();
                        Map<String, Object> record;
                        List<Object> records = new ArrayList<>();
                        while (rs.next()) {
                            record = new LinkedHashMap<>();
                            for (int i = 1; i <= columnCount; i++) {
                                if (record.get(rsMetaData.getColumnName(i)) == null) {
                                    record.put(rsMetaData.getColumnName(i), rs.getString(i));
                                } else {
                                    record.put(rsMetaData.getColumnName(i) + DUPLICATED_COLUMN_KEY + i, rs.getString(i));
                                }
                            }
                            records.add(record);

                            response.setSuccess(true);
                            queryResult.put("isEnd", false);
                            queryResult.put("record", record);
                            response.getMap().putAll(queryResult);
                            webSocketUtil.PushNotification(websocketKey, QUERY_DESTINATION, JsonUtils.marshal(response));
                            Thread.sleep(0, 1);
                        }

                        queryResult.put("queryType", "SELECT_QUERY");
                        queryResult.put("recordsSize", records.size());
                    }
                } else {
                    logger.debug("not select");
                    statement.executeUpdate(query);
                    response.setSuccess(true);
                    sendMessage(websocketKey, response, statement);

                    if (!connection.getAutoCommit()) {
                        connection.commit();
                    }

                    queryResult.put("queryType", "NOT_SELECT_QUERY");
                }

            } catch (Exception e) {
                throw new SQLException(e.getMessage() + "\nExecuted HAWQ Query : " + query, e);
            }

            logger.debug("[Query] {}" + query);
            queryResult.put("isEnd", true);
            queryResult.put("runningTime", (getRunningTime(beforeRunningTime, System.currentTimeMillis())));

            response.setSuccess(true);
            response.getMap().putAll(queryResult);

            webSocketUtil.PushNotification(websocketKey, QUERY_DESTINATION, JsonUtils.marshal(response));
        } catch (Exception e) {
            response.setSuccess(false);
            try {
                sendMessage(websocketKey, response, statement);
                response.setObject(e.getMessage());
                webSocketUtil.PushNotification(websocketKey, MESSAGE_DESTINATION, JsonUtils.marshal(response));
            } catch (Exception e1) {
                logger.warn("Unable to send a log", e1);
            }
            response.getError().setMessage(e.getMessage());
            if (e.getCause() != null) response.getError().setCause(e.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(e));
            try {
                webSocketUtil.PushNotification(websocketKey, QUERY_DESTINATION, JsonUtils.marshal(response));
            } catch (IOException e1) {
                logger.warn("Unable to send a result of HAWQ Query", e1);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.warn("Unable to close Result Set", e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.warn("Unable to close Statement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warn("Unable to close Connection", e);
                }
            }
        }
    }

    private void sendMessage(String websocketKey, Response response, Statement statement) throws SQLException, IOException, InterruptedException {
        for (SQLWarning warn = statement.getWarnings(); warn != null; warn = warn.getNextWarning()) {
            response.setObject(warn.toString());

            webSocketUtil.PushNotification(websocketKey, MESSAGE_DESTINATION, JsonUtils.marshal(response));
            Thread.sleep(0, 1);
        }
        response.setObject(null);// 마지막 줄에 log 추가되지 않도록 object null 로 set
        response.getMap().put("isLogEnd", true);
        webSocketUtil.PushNotification(websocketKey, MESSAGE_DESTINATION, JsonUtils.marshal(response));
        Thread.sleep(0, 1);
    }

    private double getRunningTime(long before, long after) {
        return (double) (after - before) / 1000;
    }

}
