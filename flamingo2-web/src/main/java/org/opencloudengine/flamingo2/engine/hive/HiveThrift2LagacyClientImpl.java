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

import org.apache.hive.jdbc.Utils;
import org.apache.hive.service.auth.HiveAuthFactory;
import org.apache.hive.service.auth.KerberosSaslHelper;
import org.apache.hive.service.auth.PlainSaslHelper;
import org.apache.hive.service.cli.FetchOrientation;
import org.apache.hive.service.cli.FetchType;
import org.apache.hive.service.cli.RowSet;
import org.apache.hive.service.cli.RowSetFactory;
import org.apache.hive.service.cli.thrift.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.model.hive.Schema;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.resource.NotSupportedException;
import javax.security.sasl.SaslException;
import java.sql.SQLException;
import java.util.*;

import static org.apache.hive.service.cli.thrift.TCLIServiceConstants.TYPE_NAMES;

/**
 * Thrift API를 이용한 Hive Server 2 Client.
 */
public class HiveThrift2LagacyClientImpl implements HiveThrift2Client {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(HiveThrift2LagacyClientImpl.class);

    private static final String HIVE_AUTH_TYPE = "auth";
    private static final String HIVE_AUTH_SIMPLE = "noSasl";
    private static final String HIVE_AUTH_USER = "user";
    private static final String HIVE_AUTH_PRINCIPAL = "principal";
    private static final String HIVE_AUTH_PASSWD = "password";
    private static final String HIVE_ANONYMOUS_USER = "anonymous";
    private static final String HIVE_ANONYMOUS_PASSWD = "anonymous";

    private final List<TProtocolVersion> supportedProtocols = new LinkedList<>();

    private String jdbcUri;

    private Utils.JdbcConnectionParams connectionParams;

    private Map<String, String> sessConf;

    private Map<String, String> hiveConf;

    private Map<String, String> hiveVar;

    private TTransport transport;

    private TCLIService.Iface client;

    private TOpenSessionResp currentSession;

    private TSessionHandle sessHandle = null;

    private TOperationHandle currentOperation;

    private TProtocolVersion protocol;

    private String error;

    protected HiveThrift2LagacyClientImpl(String uri) {
        try {
            this.jdbcUri = uri;
            this.connectionParams = Utils.parseURL(jdbcUri);
            this.sessConf = connectionParams.getSessionVars();
            this.hiveConf = connectionParams.getHiveConfs();
            this.hiveVar = connectionParams.getHiveVars();
        } catch (Exception ex) {
            throw new ServiceException("Failed to init Hive Thrift Client. Invalid Connection URI. value:" + uri, ex);
        }
    }

    public void addHiveVariable(String key, String value) {
        hiveVar.put(key, value);
    }

    public void addHiveConfiguration(String key, String value) {
        hiveConf.put(key, value);
    }

    public String getUsername() {
        String userName = sessConf.get(HIVE_AUTH_USER);
        if (StringUtils.isEmpty(userName)) {
            return HIVE_ANONYMOUS_USER;
        }
        return userName;
    }

    public String getPassword() {
        String password = sessConf.get(HIVE_AUTH_PASSWD);
        if (StringUtils.isEmpty(password)) {
            return HIVE_ANONYMOUS_PASSWD;
        }
        return password;
    }

    private void createTransport() throws SQLException {
        transport = new TSocket(connectionParams.getHost(), connectionParams.getPort());
        if (!sessConf.containsKey(HIVE_AUTH_TYPE) || !sessConf.get(HIVE_AUTH_TYPE).equals(HIVE_AUTH_SIMPLE)) {
            try {
                if (sessConf.containsKey(HIVE_AUTH_PRINCIPAL))
                    transport = KerberosSaslHelper.getKerberosTransport(sessConf.get(HIVE_AUTH_PRINCIPAL), connectionParams.getHost(), transport, new HashMap<String, String>(), true);
                else
                    transport = PlainSaslHelper.getPlainTransport(getUsername(), getPassword(), transport);
            } catch (SaslException e) {
                throw new SQLException("Could not establish secure connection to " + jdbcUri + ": " + e.getMessage(), " 08S01");
            }
        }
    }

    /**
     * Hive Thrift 세션을 오픈한다.
     *
     * @throws SQLException
     */
    public void openSession() throws TException {
        TOpenSessionReq openReq = new TOpenSessionReq();

        if (currentSession != null) {
            throw new TException("Session is already opened.");
        }

        Map<String, String> openConf = new HashMap<String, String>();
        openConf.put(HiveAuthFactory.HS2_PROXY_USER, getUsername());
        openReq.setConfiguration(openConf);
        currentSession = client.OpenSession(openReq);
        sessHandle = currentSession.getSessionHandle();
        protocol = currentSession.getServerProtocolVersion();
    }

    public void closeSession() throws TException {
        if (currentOperation != null) {
            closeOperation();
        }

        TCloseSessionReq closeReq = new TCloseSessionReq(sessHandle);

        client.CloseSession(closeReq);
        currentSession = null;
    }

    public TTransport getTransport() {
        return transport;
    }

    protected TCLIService.Iface getClient() {
        return client;
    }

    protected TOpenSessionResp getCurrentSession() {
        return currentSession;
    }

    protected TOperationHandle getCurrentOperation() {
        return currentOperation;
    }

    private void closeOperation() throws TException {
        TCloseOperationReq closeReq = new TCloseOperationReq();
        closeReq.setOperationHandle(currentOperation);
        client.CloseOperation(closeReq);
        currentOperation = null;
    }

    public void connect() throws SQLException {
        createTransport();
        client = new TCLIService.Client(new TBinaryProtocol(transport));
        try {
            transport.open();
        } catch (TTransportException e) {
            throw new SQLException("Could not establish connection to " + jdbcUri + ": " + e.getMessage(), " 08S01");
        }
    }

    public void execute(String query) throws TException {
        try {
            execute(query, false);
        } catch (Exception ex) {
            error = ex.getMessage();
            throw ex;
        }
    }

    public void executeAsync(String query) throws TException {
        execute(query, true);
    }

    public void execute(String query, boolean async) throws TException {
        if (currentSession == null) {
            openSession();
        }
        if (currentOperation != null) {
            closeOperation();
        }

        TExecuteStatementReq execReq = new TExecuteStatementReq(sessHandle, query);

        execReq.setConfOverlay(sessConf);
        if (async) {
            execReq.setRunAsync(true);
        } else {
            execReq.setRunAsync(false);
        }
        TExecuteStatementResp execResp = client.ExecuteStatement(execReq);

        currentOperation = execResp.getOperationHandle();
    }

    public void execute(List<String> queries) throws TException {
        for (String query : queries) {
            if (!"".equals(query.trim()))
                execute(query.trim());
        }
    }

    public List<Schema> getResultSchema() throws TException {
        if (currentOperation == null) {
            throw new TException("Least one operation is executed.");
        }

        if (!currentOperation.isHasResultSet()) {
            return new ArrayList<>();
        }

        TGetResultSetMetadataReq metadataReq = new TGetResultSetMetadataReq(currentOperation);
        TGetResultSetMetadataResp metadataResp = client.GetResultSetMetadata(metadataReq);

        TTableSchema schema = metadataResp.getSchema();

        List<TColumnDesc> columns = schema.getColumns();

        List<Schema> schemas = new ArrayList<>(schema.getColumnsSize());

        for (int pos = 0; pos < schema.getColumnsSize(); pos++) {

            String columnName = columns.get(pos).getColumnName();
            String comment = columns.get(pos).getComment();
            TPrimitiveTypeEntry primitiveTypeEntry = columns.get(pos).getTypeDesc().getTypes().get(0).getPrimitiveEntry();
            String columnTypeName = TYPE_NAMES.get(primitiveTypeEntry.getType());

            Schema column = new Schema(columnName, columnTypeName, comment);
            schemas.add(column);
        }

        return schemas;
    }

    public Map[] getResults() throws TException {
        return getResults(TFetchOrientation.FETCH_NEXT, Integer.MAX_VALUE, FetchType.QUERY_OUTPUT);
    }

    public String getStatus() throws TException {
        TGetOperationStatusReq statusReq = new TGetOperationStatusReq(currentOperation);
        TGetOperationStatusResp statusResp = client.GetOperationStatus(statusReq);
        return statusResp.getOperationState().toString();
    }

    public String getLog() throws TException {
        logger.info("Not support getting hive logs in hive legacy mode");
        return "";
    }

    @Override
    public Map[] getResults(FetchOrientation orientation, int maxRows, FetchType fetchType) throws Exception {
        throw new NotSupportedException("Not Supported");
    }

    public Map[] getResults(TFetchOrientation orientation, int maxRows, FetchType fetchType) throws TException {
        if (currentOperation == null)
            throw new TException("Least one operation is executed.");

        if (!currentOperation.isHasResultSet()) {
            return null;
        }

        // 쿼리 수행 결과의 스키마 정보를 가져온다.
        List<Schema> schemas = getResultSchema();
        Schema[] scs = schemas.toArray(new Schema[schemas.size()]);

        // 쿼리의 실행 결과를 가져온다.
        TFetchResultsReq resultsReq = new TFetchResultsReq(currentOperation, orientation, maxRows);
        TFetchResultsResp fetchResp = client.FetchResults(resultsReq);
        //RowSet objects = client.fetchResults(currentOperation, orientation, maxRows, fetchType);

        TRowSet tRowSet = fetchResp.getResults();
        RowSet fetchedRows = RowSetFactory.create(tRowSet, protocol);

        List<TColumn> columns = tRowSet.getColumns();
        TColumn[] cols = columns.toArray(new TColumn[columns.size()]);
        Map[] vs = new Map[fetchedRows.numRows()];
        for (int i = 0; i < cols.length; i++) {
            Schema schema = scs[i];
            TColumn col = cols[i];
            List values = FieldReader.readToString(schema.type, col);
            bindColumns(schema.name, vs, values);
        }

        return vs;
    }

    private static void bindColumns(String columnName, Map[] vs, List values) {
        Object[] objects = values.toArray(new Object[values.size()]);
        for (int i = 0; i < values.size(); i++) {
            Object value = objects[i];
            if (vs[i] == null) {
                vs[i] = new HashMap();
            }

            Map column = vs[i];
            column.put(columnName, value);
        }
    }

    public Map[] getResults(int maxRows) throws TException {
        return getResults(TFetchOrientation.FETCH_NEXT, maxRows, FetchType.QUERY_OUTPUT);
    }

    public String getError() {
        return error;
    }

    private static class FieldReader {
        public static List readToString(String type, TColumn column) {
            Object fieldValue = column.getFieldValue();
            switch (type) {
                case "BINARY":
                    return ((TBinaryColumn) fieldValue).getValues();
                case "BOOL":
                    return ((TBoolColumn) fieldValue).getValues();
                case "SMALLINT":
                    return ((TI16Column) fieldValue).getValues();
                case "INT":
                    return ((TI32Column) fieldValue).getValues();
                case "BIGINT":
                    return ((TI64Column) fieldValue).getValues();
                case "TINYINT":
                    return ((TByteColumn) fieldValue).getValues();
                case "FLOAT":
                case "DOUBLE":
                    return ((TDoubleColumn) fieldValue).getValues();
                case "CHAR":
                case "VARCHAR":
                case "TIMESTAMP":
                case "DATE":
                case "DECIMAL":
                case "ARRAY":
                case "MAP":
                case "STRUCT":
                case "STRING":
                    return ((TStringColumn) fieldValue).getValues();
                default:
                    return new ArrayList();
            }
        }
    }

    public static class Factory {
        public static HiveThrift2LagacyClientImpl getClient(String uri) {
            return new HiveThrift2LagacyClientImpl(uri);
        }
    }
}
