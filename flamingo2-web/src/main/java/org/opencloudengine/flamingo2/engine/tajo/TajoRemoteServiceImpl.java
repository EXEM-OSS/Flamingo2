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

import org.apache.tajo.QueryId;
import org.apache.tajo.catalog.Column;
import org.apache.tajo.catalog.Schema;
import org.apache.tajo.catalog.TableDesc;
import org.apache.tajo.catalog.TableMeta;
import org.apache.tajo.catalog.partition.PartitionMethodDesc;
import org.apache.tajo.catalog.proto.CatalogProtos;
import org.apache.tajo.client.*;
import org.apache.tajo.common.TajoDataTypes;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.rpc.RpcUtils;
import org.apache.tajo.util.KeyValueSet;
import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.logging.StringUtils;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Reactor;
import reactor.event.Event;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

/**
 * Created by Park on 15. 7. 14..
 */
public class TajoRemoteServiceImpl implements TajoRemoteService, InitializingBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(TajoRemoteServiceImpl.class);

    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Object> clientMap;

    private final String defaultDatabase = "default";

    @Autowired
    private Reactor reactor;

    @Override
    public void afterPropertiesSet() throws Exception {
        clientMap = Collections.synchronizedMap(new HashMap());
    }

    @Override
    public List getAllDatabases(EngineConfig engineConfig) {
        TajoClient client = connect(engineConfig, defaultDatabase);

        try {
            List<String> dbList = client.getAllDatabaseNames();
            List<Map> returnList = new ArrayList<>();

            for (String db : dbList) {
                Map dbMap = new HashMap();

                dbMap.put("database", db);

                returnList.add(dbMap);
            }
            return returnList;
        } catch (Exception e) {
            throw new ServiceException("Unable to get Tajo databases", e);
        } finally {
            client.close();
        }
    }


    @Override
    public List getTables(EngineConfig engineConfig, String dbname) {
        TajoClient client = connect(engineConfig, dbname);

        try {
            List<String> tableList = client.getTableList(dbname);
            List<Map> returnList = new ArrayList<>();

            for (String table : tableList) {
                Map tableMap = new HashMap();
                TableDesc tableDesc = client.getTableDesc(table);

                /*System.out.println(tableDesc.toJson());
                System.out.println(tableDesc.getMeta().getStoreType().toString());
                System.out.println(tableDesc.getMeta().getOptions().toJson());*/

                tableMap.put("tableName", table);

                if (tableDesc.isExternal()) {
                    tableMap.put("tableType", "EXTERNAL");
                }
                else {
                    tableMap.put("tableType", "BASE");
                }

                returnList.add(tableMap);
            }
            return returnList;
        } catch (Exception e) {
            throw new ServiceException("Unable to get Tajo tables", e);
        } finally {
            client.close();
        }
    }

    @Override
    public List<Map> getColumns(EngineConfig engineConfig, String dbname, String tablename) {
        TajoClient client = connect(engineConfig, dbname);

        try {
            TableDesc desc = client.getTableDesc(tablename);
            Schema schema = desc.getSchema();
            List<Column> columnList = schema.getColumns();
            List<Map> returnList = new ArrayList<>();

            for (Column column : columnList) {
                Map columnMap = new HashMap();
                columnMap.put("name", column.getSimpleName());
                columnMap.put("type", column.getDataType().getType().name());

                returnList.add(columnMap);
            }
            return returnList;
        } catch (Exception e) {
            throw new ServiceException("Unable to get Tajo columns", e);
        } finally {
            client.close();
        }
    }

    @Override
    public List<Map> getPartitions(EngineConfig engineConfig, String dbname, String tablename) {
        TajoClient client = connect(engineConfig, dbname);

        try {
            TableDesc desc = client.getTableDesc(tablename);
            PartitionMethodDesc partitionMethodDesc = desc.getPartitionMethod();
            List<Map> returnList = new ArrayList<>();

            if (desc.hasPartition()) {
                Schema partitonSchema = partitionMethodDesc.getExpressionSchema();
                List<Column> columnList = partitonSchema.getColumns();


                for (Column column : columnList) {
                    Map columnMap = new HashMap();
                    columnMap.put("name", column.getSimpleName());
                    columnMap.put("type", column.getDataType().getType().name());

                    returnList.add(columnMap);
                }
            }
            return returnList;
        } catch (Exception e) {
            throw new ServiceException("Unable to get Tajo partitions", e);
        } finally {
            client.close();
        }
    }

    @Override
    public void createDatabase(EngineConfig engineConfig, Map params) {
        TajoClient client = connect(engineConfig, defaultDatabase);

        try {
            client.createDatabase(params.get("database").toString());
        } catch (Exception e) {
            throw new ServiceException("Unable to Create Tajo database", e);
        } finally {
            client.close();
        }
    }

    @Override
    public void dropDatabase(EngineConfig engineConfig, String dbname) {
        TajoClient client = connect(engineConfig, defaultDatabase);

        try {
            client.dropDatabase(dbname);
        } catch (Exception e) {
            throw new ServiceException("Unable to Drop Tajo database", e);
        } finally {
            client.close();
        }
    }

    @Override
    public void createTable(EngineConfig engineConfig, Map params) {
        TajoClient client = connect(engineConfig, params.get("database").toString());

        try {
            ArrayList columnList = objectMapper.readValue(params.get("columns").toString(), ArrayList.class);
            ArrayList partitonList = objectMapper.readValue(params.get("partitions").toString(), ArrayList.class);
            Schema schema = createSchema(columnList);
            CatalogProtos.StoreType storeType = null;
            Map kvMap = new HashMap();
            KeyValueSet options = new KeyValueSet();

            switch (params.get("storeType").toString()) {
                case "TEXTFILE":
                    storeType = CatalogProtos.StoreType.TEXTFILE;
                    if (!StringUtils.isEmpty(params.get("serde").toString())) {
                        kvMap.put("text.serde", params.get("serde"));
                    }
                    if (!StringUtils.isEmpty(params.get("nulltext").toString())) {
                        String nulltext;
                        switch (params.get("nulltext").toString()) {
                            case "empty":
                                nulltext = "";
                                break;
                            case "null":
                                nulltext = "\\\\N";
                                break;
                            default:
                                throw new ServiceException("Unsupported text.null");
                        }
                        kvMap.put("text.null", nulltext);
                    }
                    if (!StringUtils.isEmpty(params.get("errorTolerance").toString())) {
                        kvMap.put("text.error-tolerance.max-num", params.get("errorTolerance"));
                    }
                    if (!StringUtils.isEmpty(params.get("compress").toString())) {
                        kvMap.put("compression.codec", params.get("compress"));
                    }
                    if (!StringUtils.isEmpty(params.get("delimiter").toString())) {
                        kvMap.put("text.delimiter", params.get("delimiter"));
                    }

                    break;
                case "RCFILE":
                    storeType = CatalogProtos.StoreType.RCFILE;
                    if (!StringUtils.isEmpty(params.get("serde").toString())) {
                        kvMap.put("text.serde", params.get("serde"));
                    }
                    if (!StringUtils.isEmpty(params.get("compress").toString())) {
                        kvMap.put("compression.codec", params.get("compress"));
                    }
                    break;
                case "PARQUET":
                    storeType = CatalogProtos.StoreType.PARQUET;
                    break;
                case "SEQUENCEFILE":
                    storeType = CatalogProtos.StoreType.SEQUENCEFILE;
                    break;
                default:
                    throw new ServiceException("Unsupported Storetype");
            }

            options.putAll(kvMap);
            TableMeta tableMeta = new TableMeta(storeType, options);

            URI uri = getURI(params.get("hostName").toString(), params.get("port").toString(), params.get("location").toString());

            if (partitonList.size() > 0) {
                Schema partitionSchema = createSchema(partitonList);
                String expression = createPartitionExpression(partitonList);
                PartitionMethodDesc partitionMethodDesc = new PartitionMethodDesc(params.get("database").toString(), params.get("tableName").toString(), CatalogProtos.PartitionType.COLUMN, expression, partitionSchema);

                client.createExternalTable(params.get("tableName").toString(), schema, uri, tableMeta, partitionMethodDesc);
            }
            else {
                client.createExternalTable(params.get("tableName").toString(), schema, uri, tableMeta);
            }


        } catch (Exception e) {
            throw new ServiceException("Unable to Create Tajo Table", e);
        } finally {
            client.close();
        }
    }

    @Override
    public void dropTable(EngineConfig engineConfig, String dbname, String tablename) {
        TajoClient client = connect(engineConfig, dbname);

        try {
            client.dropTable(tablename);
        } catch (Exception e) {
            throw new ServiceException("Unable to Drop Tajo table", e);
        } finally {
            client.close();
        }
    }

    @Override
    public Map execute(EngineConfig engineConfig, Map params) {
        TajoClient client = connect(engineConfig, params.get("database").toString());
        Map returnMap = new HashMap();
        try {
            ClientProtos.SubmitQueryResponse response = client.executeQuery(java.net.URLDecoder.decode(params.get("query").toString(), "UTF-8"));

            if (response.hasErrorMessage()) {
                returnMap.put("error", true);
                returnMap.put("cause", response.getErrorMessage());
                return returnMap;
            }

            QueryId queryId = new QueryId(response.getQueryId());
            QueryStatus queryStatus = client.getQueryStatus(queryId);

            returnMap.put("startTime", queryStatus.getSubmitTime());
            returnMap.put("queryId", response.getQueryId().getId() + "_" + response.getQueryId().getSeq());
            returnMap.put("error", false);

            if (response.hasResultSet() || response.hasTableDesc() || queryStatus.getState().toString().equals("QUERY_SUCCEEDED")) {

                List<Map> resultList = new ArrayList<>();
                returnMap.put("isFinish", true);
                if (response.hasResultSet() || response.hasTableDesc()) {
                    returnMap.put("isResult", true);
                    List<String> columnList = new ArrayList<>();
                    ResultSet resultSet = TajoClientUtil.createResultSet(client, response, Integer.parseInt(params.get("maxRows").toString()));
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int i, rows = 0, columnCount = metaData.getColumnCount();

                    for (i = 1; i <= columnCount; i++) {
                        columnList.add(metaData.getColumnName(i));
                    }

                    returnMap.put("columns", columnList);

                    while(resultSet.next()) {
                        Map columns = new HashMap();
                        for (i = 1; i <= columnCount; i++) {
                            columns.put(metaData.getColumnName(i), resultSet.getObject(i));
                        }

                        resultList.add(columns);
                        rows++;

                        if (rows >= Integer.parseInt(params.get("maxRows").toString())) {
                            break;
                        }
                    }
                }
                else {
                    returnMap.put("isResult", false);
                }

                returnMap.put("finish", queryStatus.getFinishTime());
                returnMap.put("result", resultList);
            }
            else {
                clientMap.put(params.get("uuid").toString(), queryId);

                params.put("SubmitQueryResponse", response);
                params.put("engineConfig", engineConfig);

                reactor.notify("tajoStatus", Event.wrap(params));
                logger.info("Sent to async");
                returnMap.put("isResult", false);
                returnMap.put("isFinish", false);
            }

            return returnMap;
        } catch (Exception e) {
            throw new ServiceException("Unable to Execute Tajo Query", e);
        } finally {
            client.close();
        }
    }

    @Override
    public Map getResults(EngineConfig engineConfig, Map params) {
        TajoClient client = connect(engineConfig, defaultDatabase);
        Map returnMap = new HashMap();
        try {
            QueryId queryId = (QueryId) clientMap.get(params.get("uuid"));

            List<Map> resultList = new ArrayList<>();
            List<String> columnList = new ArrayList<>();
            ResultSet resultSet = client.getQueryResult(queryId);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int i, rows = 0, columnCount = metaData.getColumnCount();

            for (i = 1; i <= columnCount; i++) {
                columnList.add(metaData.getColumnName(i));
            }

            returnMap.put("columns", columnList);

            while(resultSet.next()) {
                Map columns = new HashMap();
                for (i = 1; i <= columnCount; i++) {
                    columns.put(metaData.getColumnName(i), resultSet.getObject(i));
                }

                resultList.add(columns);
                rows++;

                if (rows >= Integer.parseInt(params.get("maxRows").toString())) {
                    break;
                }
            }

            returnMap.put("result", resultList);

            return returnMap;
        } catch (Exception e) {
            throw new ServiceException("Unable to get Tajo Results", e);
        } finally {
            client.close();
        }
    }

    @Override
    public void removeQueryId(String uuid) {
        clientMap.remove(uuid);
    }

    @Override
    public void closeQuery(EngineConfig engineConfig, Map params) {
        TajoClient client = connect(engineConfig, defaultDatabase);

        try {
            QueryId queryId = (QueryId) clientMap.get(params.get("uuid"));

            logger.info("Close Query Id: " + queryId.getId() + "_" + queryId.getSeq());

            client.killQuery(queryId);
        } catch (Exception e) {
            throw new ServiceException("Unable to Close Tajo Query", e);
        } finally {
            client.close();
        }
    }

    private TajoClient connect(EngineConfig engineConfig, String database) {
        try {
            KeyValueSet clientProperties = new KeyValueSet();
            return new TajoClientImpl(RpcUtils.createSocketAddr(engineConfig.getTajoAddress(), Integer.parseInt(engineConfig.getTajoPort())), database, clientProperties);
        } catch (Exception e) {
            throw new ServiceException("Unable to connect Tajo Master", e);
        }
    }

    private URI getURI(String hostname, String port, String location) throws URISyntaxException {
        return new URI(arrayFormat("hdfs://{}:{}/{}", new Object[]{hostname, port, location}).getMessage());
    }

    private Schema createSchema(ArrayList schemaList) {
        Schema schema = new Schema();

        for (Object column : schemaList) {
            Map col = (Map) column;
            TajoDataTypes.Type type = null;
            switch (col.get("type").toString()) {
                case "BOOLEAN":
                    type = TajoDataTypes.Type.BOOLEAN;
                    break;
                case "INT1":
                    type = TajoDataTypes.Type.INT1;
                    break;
                case "INT2":
                    type = TajoDataTypes.Type.INT2;
                    break;
                case "INT4":
                    type = TajoDataTypes.Type.INT4;
                    break;
                case "INT8":
                    type = TajoDataTypes.Type.INT8;
                    break;
                case "FLOAT4":
                    type = TajoDataTypes.Type.FLOAT4;
                    break;
                case "FLOAT8":
                    type = TajoDataTypes.Type.FLOAT8;
                    break;
                case "TEXT":
                    type = TajoDataTypes.Type.TEXT;
                    break;
                case "BLOB":
                    type = TajoDataTypes.Type.BLOB;
                    break;
                case "DATE":
                    type = TajoDataTypes.Type.DATE;
                    break;
                case "TIME":
                    type = TajoDataTypes.Type.TIME;
                    break;
                case "TIMESTAMP":
                    type = TajoDataTypes.Type.TIMESTAMP;
                    break;
                case "INET4":
                    type = TajoDataTypes.Type.INET4;
                    break;
                default:
                    throw new ServiceException("Not Supported Datatype");
            }

            Column tajoColumn = new Column(col.get("name").toString(), type);

            schema.addColumn(tajoColumn);

        }
        return schema;
    }

    private String createPartitionExpression(ArrayList schemaList) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < schemaList.size(); i++) {
            Map exprMap = new HashMap();
            Map col = (Map) schemaList.get(i);

            exprMap.put("ColumnDefName", col.get("name"));
            exprMap.put("DataTypeName", col.get("type"));
            exprMap.put("OpType", "DataType");
            if (i > 0) {
                sb.append(",");
            }

            sb.append(objectMapper.writeValueAsString(exprMap));
        }

        return sb.toString();
    }
}

