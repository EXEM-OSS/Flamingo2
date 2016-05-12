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
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.postgresql.util.PSQLState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

/**
 * Pivotal HAWQ Repository Implements.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
@Repository
public class HawqRepositoryImpl implements HawqRepository {

    private Logger logger = LoggerFactory.getLogger(HawqRepositoryImpl.class);

    @Autowired
    private HawqQueryMapper hawqQueryMapper;

    @Autowired
    private Properties config;

    @Override
    public HawqConnection getHawqConnection(Map<String, Object> params) throws ClassNotFoundException, SQLException {
        return getConnection(params, true);
    }

    @Override
    public HawqConnection getPostgresqlConnection(Map<String, Object> params) throws ClassNotFoundException, SQLException {
        return getConnection(params, false);
    }

    @Override
    public int executeUpdateQueryById(String queryId, Map<String, Object> params) throws SQLException {
        int result = 0;

        Connection connection = null;
        Statement statement = null;
        String query = "";
        try {
            connection = getHawqConnection(params).getConnection();
            statement = connection.createStatement();

            query = hawqQueryMapper.getQuery(queryId, params);
            logger.debug("\nid = " + queryId + "\n" + query);
            result = statement.executeUpdate(query);
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            return result;
        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public int executeUpdateQuery(String query, Map<String, Object> params) throws SQLException {
        logger.debug("\n" + query);
        int result = 0;

        Connection connection = null;
        Statement statement = null;
        try {
            connection = getHawqConnection(params).getConnection();
            statement = connection.createStatement();

            result = statement.executeUpdate(query);
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            return result;
        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public List<Object> executeSelectListById(String queryId, Map<String, Object> params) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "";
        try {
            connection = getHawqConnection(params).getConnection();
            statement = connection.createStatement();

            query = hawqQueryMapper.getQuery(queryId, params);
            logger.debug("\nid = " + queryId + "\n" + query);

            resultSet = statement.executeQuery(query);
            return getResultSetObjects(resultSet);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public <T> List<T> executeSelectListById(String queryId, Map<String, Object> params, Class<T> cls) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "";
        try {
            connection = getHawqConnection(params).getConnection();
            statement = connection.createStatement();

            query = hawqQueryMapper.getQuery(queryId, params);
            logger.debug("\nid = " + queryId + "\n" + query);

            resultSet = statement.executeQuery(query);
            return getResultSetList(resultSet);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public List<Object> executeSelectList(String query, Map<String, Object> params) throws SQLException {
        logger.debug("\n" + query);

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getHawqConnection(params).getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            return getResultSetObjects(resultSet);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public Object executeSelectOne(String queryId, Map<String, Object> params) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "";
        try {
            connection = getHawqConnection(params).getConnection();
            statement = connection.createStatement();
            statement.setMaxRows(1);
            query = hawqQueryMapper.getQuery(queryId, params);
            logger.debug("\nid = " + queryId + "\n" + query);
            resultSet = statement.executeQuery(query);

            List<Object> rows = getResultSetObjects(resultSet);
            return rows.isEmpty() ? null : rows.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public <T> T executeSelectOneForFirstColumn(String queryId, Map<String, Object> params, Class<T> cls) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "";
        try {
            connection = getHawqConnection(params).getConnection();
            statement = connection.createStatement();
            statement.setMaxRows(1);
            query = hawqQueryMapper.getQuery(queryId, params);
            logger.debug("\nid = " + queryId + "\n" + query);
            resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            String columnName = metaData.getColumnName(1);
            return (T) ((Map<String, Object>) getResultSetObjects(resultSet).get(0)).get(columnName);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public List<Object> getResultSetObjects(ResultSet rs) throws SQLException {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        Map<String, Object> record;
        List<Object> records = new ArrayList<>();
        while (rs.next()) {
            record = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                record.put(rsMetaData.getColumnName(i), rs.getObject(i));
            }
            records.add(record);
        }
        return records;
    }

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("org.postgresql.Driver");
//            Class.forName("com.pivotal.jdbc.GreenplumDriver");

            connection = DriverManager.getConnection("jdbc:postgresql://10.211.55.5:5432/gpadmin", "gpadmin", "");
//            connection = DriverManager.getConnection("jdbc:pivotal:greenplum://172.16.213.140:5432;DatabaseName=gpadmin", "gpadmin", "");

            HawqRepositoryImpl repository = new HawqRepositoryImpl();

            statement = connection.createStatement();
            /*resultSet = statement.executeQuery("SELECT get_last_name('John');");
            List<Object> objects = repository.getResultSetObjects(resultSet);
            System.err.println("objects = " + objects);*/
            statement.executeUpdate("create table table_space_test (number int) tablespace pg_default");
            for (SQLWarning warn = statement.getWarnings(); warn != null; warn = warn.getNextWarning()) {
                System.err.println("warn.getLocalizedMessage() = " + warn.getLocalizedMessage());
                System.err.println("warn.getSQLState() = " + warn.getSQLState());
                System.err.println("PSQLState = " + new PSQLState(warn.getSQLState()).getState());
                System.err.println("warn.getMessage() = " + warn.getMessage());
                System.err.println("warn.getErrorCode() = " + warn.getErrorCode());
            }

        } catch (SQLException e) {
            System.err.println("catch");
            System.err.println("e.getLocalizedMessage() = " + e.getLocalizedMessage());
            System.err.println("e.getSQLState() = " + e.getSQLState());
            System.err.println("PSQLState = " + new PSQLState(e.getSQLState()).getState());
            System.err.println("e.getMessage() = " + e.getMessage());
            System.err.println("e.getErrorCode() = " + e.getErrorCode());
            try {
                System.err.println("connection.getWarnings()");
                for (SQLWarning warn = connection.getWarnings(); warn != null; warn = warn.getNextWarning()) {
                    System.err.println("warn = " + warn);
                    System.err.println("warn.getLocalizedMessage() = " + warn.getLocalizedMessage());
                    System.err.println("warn.getSQLState() = " + warn.getSQLState());
                    System.err.println("PSQLState = " + new PSQLState(warn.getSQLState()).getState());
                    System.err.println("warn.getMessage() = " + warn.getMessage());
                    System.err.println("warn.getErrorCode() = " + warn.getErrorCode());
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                System.err.println("statement.getWarnings()");
                for (SQLWarning warn = statement.getWarnings(); warn != null; warn = warn.getNextWarning()) {
                    System.err.println("warn = " + warn);
                    System.err.println("warn.getLocalizedMessage() = " + warn.getLocalizedMessage());
                    System.err.println("warn.getSQLState() = " + warn.getSQLState());
                    System.err.println("PSQLState = " + new PSQLState(warn.getSQLState()).getState());
                    System.err.println("warn.getMessage() = " + warn.getMessage());
                    System.err.println("warn.getErrorCode() = " + warn.getErrorCode());
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ======================================================================================
    // private methods
    // ======================================================================================
    private HawqConnection getConnection(Map<String, Object> params, boolean checkJdbcType) throws SQLException, ClassNotFoundException {
        EngineConfig engineConfig = (EngineConfig) params.get("engineConfig");
        String jdbcType = engineConfig.getHawqJdbcType();
        String connectionUrl = "";
        if (checkJdbcType) {
            if (jdbcType.endsWith("greenplum")) {
                connectionUrl = engineConfig.getHawqGreenplumConnectionUrl();
            } else {
                connectionUrl = engineConfig.getHawqPostgresqlConnectionUrl();
            }
        } else {// default postgresql
            connectionUrl = engineConfig.getHawqPostgresqlConnectionUrl();
        }

        String url = org.slf4j.helpers.MessageFormatter.arrayFormat("{}{}:{}", new String[]{connectionUrl, engineConfig.getHawqHost(), engineConfig.getHawqPort()}).getMessage();
        String databaseName = engineConfig.getHawqDefaultDatabaseName();
        String user = engineConfig.getHawqUser();
        String password = engineConfig.getHawqPassword();
        boolean autoCommit = Boolean.parseBoolean(engineConfig.getHawqAutoCommit());
        String driver = "";

        if (params != null) {
            databaseName = params.get("databaseName") == null ? databaseName : params.get("databaseName").toString();
            user = params.get("user") == null ? user : params.get("user").toString();
            password = params.get("password") == null ? password : params.get("password").toString();
            autoCommit = params.get("autoCommit") == null ? autoCommit : Boolean.parseBoolean(params.get("autoCommit").toString());
        }
        if (checkJdbcType) {
            if ("greenplum".equals(jdbcType)) {
                url += ";DatabaseName=" + databaseName + ";";
                driver = engineConfig.getHawqDriver();
            } else {// postgresql
                url += "/" + databaseName;
                driver = engineConfig.getHawqPostgresqlDriver();
            }
        } else {// default postgresql
            url += "/" + databaseName;
            driver = engineConfig.getHawqPostgresqlDriver();
        }

        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        if (connection == null) {
            throw new ServiceException("Connection information doesn't exist.");
        }
        connection.setAutoCommit(autoCommit);

        params.put("appName", config.getProperty("application.title"));
        setAppName(connection, params);
        return new HawqConnection(connection, getPid(connection, params));
    }

    private int getPid(Connection connection, Map<String, Object> params) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "";
        try {
            statement = connection.createStatement();
            statement.setMaxRows(1);

            query = hawqQueryMapper.getQuery("SHOW_PID_QUERY", params);
            resultSet = statement.executeQuery(query);

            return (int) ((Map<String, Object>) getResultSetObjects(resultSet).get(0)).get(resultSet.getMetaData().getColumnName(1));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            resultSet.close();
            statement.close();
        }
    }

    private void setAppName(Connection connection, Map<String, Object> params) throws SQLException {
        Statement statement = null;
        String query = "";
        try {
            statement = connection.createStatement();
            query = hawqQueryMapper.getQuery("SET_APP_NAME_QUERY", params);
            statement.executeUpdate(query);
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw new ServiceException(e.getMessage() + "\nExecuted HAWQ Query: " + query, e.getCause());
        } finally {
            if (statement != null) statement.close();
        }
    }

    private List getResultSetList(ResultSet rs) throws SQLException {
        List records = new ArrayList<>();
        while (rs.next()) {
            records.add(rs.getObject(1));
        }
        return records;
    }

}