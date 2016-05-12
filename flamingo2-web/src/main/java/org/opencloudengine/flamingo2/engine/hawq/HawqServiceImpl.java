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

import au.com.bytecode.opencsv.CSVWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.opencloudengine.flamingo2.core.cmd.ByteArrayOutputStream;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.hawq.externaltable.Csv;
import org.opencloudengine.flamingo2.engine.hawq.externaltable.Custom;
import org.opencloudengine.flamingo2.engine.hawq.externaltable.Format;
import org.opencloudengine.flamingo2.engine.hawq.externaltable.Text;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.Reactor;
import reactor.event.Event;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.*;

/**
 * Pivotal HAWQ Service Implements.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
@Service
public class HawqServiceImpl implements HawqService {

    @Autowired
    private Reactor reactor;

    @Autowired
    private HawqRepository repository;

    private Logger logger = LoggerFactory.getLogger(HawqServiceImpl.class);

    @Override
    public Map<String, Object> connect(Map<String, Object> params) throws SQLException {
        EngineConfig engineConfig = (EngineConfig) params.get("engineConfig");
        String userName = engineConfig.getHawqUser();
        params.put("userName", userName);// for SHOW_DEFAULT_DATABASES_QUERY

        Map<String, Object> hawqMetadata = (Map<String, Object>) repository.executeSelectOne("SHOW_DEFAULT_DATABASES_QUERY", params);

        hawqMetadata.put("autoCommit", repository.executeSelectOneForFirstColumn("SHOW_HAWQ_AUTO_COMMIT_QUERY", params, String.class));
        hawqMetadata.put("databaseProductVersion", repository.executeSelectOneForFirstColumn("SHOW_HAWQ_VERSION_QUERY", params, String.class));
        hawqMetadata.put("userName", userName);
        hawqMetadata.put("defaultSchema", getDefaultSchema(params));
        hawqMetadata.put("databaseName", engineConfig.getHawqDefaultDatabaseName());

        return hawqMetadata;
    }

    @Override
    public List<Object> getDatabases(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_ALL_DATABASES_QUERY", params);
    }

    @Override
    public List<Object> getSchemas(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");

        return repository.executeSelectListById("SHOW_SCHEMAS_QUERY", params);
    }

    @Override
    public List<Object> getTables(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");

        return repository.executeSelectListById("SHOW_TABLES_QUERY", params);
    }

    @Override
    public List<Object> getViews(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");

        return repository.executeSelectListById("SHOW_VIEWS_QUERY", params);
    }

    @Override
    public List<Object> getFunctions(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");

        return repository.executeSelectListById("SHOW_FUNCTIONS_QUERY", params);
    }

    @Override
    public List<Object> getExternalTables(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");

        return repository.executeSelectListById("SHOW_EXTERNAL_TABLES_QUERY", params);
    }

    @Override
    public List<Object> getColumns(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "tableName");

        params.put("dbSchemaTable", getDBSchemaTable(params));

        List<Object> columns = repository.executeSelectListById("SHOW_COLUMNS_QUERY", params);
        Map<String, Object> distributedMap = (Map<String, Object>) repository.executeSelectOne("SHOW_DISTRIBUTED_QUERY", params);
        if (distributedMap != null && distributedMap.get("attrnums") != null) {
            setColumnDistributed(columns, distributedMap.get("attrnums").toString());
        }

        return columns;
    }

    @Override
    public List<Object> getObjectMetadatas(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "objectType");

        String objectType = params.get("objectType").toString();

        Map<String, Object> metadataMap = null;
        if ("FUNCTION".equals(objectType)) {
            assertParam(params, "oid");

            metadataMap = (Map<String, Object>) repository.executeSelectOne("SHOW_FUNCTION_INFORMATION_QUERY", params);
        } else {
            assertParam(params, "tableName");

            params.put("dbSchemaTable", getDBSchemaTable(params));
            metadataMap = (Map<String, Object>) repository.executeSelectOne("SHOW_TABLE_INFORMATION_QUERY", params);
            metadataMap.put("Inherits", StringUtils.join(repository.executeSelectListById("SHOW_INHERITS_QUERY", params, String.class), ", "));
            try {
                metadataMap.put("Row Count", repository.executeSelectOneForFirstColumn("SHOW_TABLE_ROW_COUNT_QUERY", params, int.class));
            } catch (Exception e) {
            }
        }

        List<Object> metadatas = new ArrayList<>();

        Map<String, Object> metadata;
        for (String key : metadataMap.keySet()) {
            Object value = metadataMap.get(key);
            metadata = new HashMap<>();
            if ("reloptions".equals(key) && value != null) {
                String optionsString = value.toString();
                List<String> optionsList = Arrays.asList(optionsString.substring(1, optionsString.length() - 1).split(","));

                Map<String, Object> optionMap;
                for (String option : optionsList) {
                    optionMap = new HashMap<>();
                    optionMap.put("key", option.split("=")[0]);
                    optionMap.put("value", option.split("=")[1]);

                    metadatas.add(optionMap);
                }

                optionMap = new HashMap<>();
                optionMap.put("key", "reloptions");
                optionMap.put("value", value);

                metadatas.add(optionMap);
            } else {
                metadata.put("key", key);
                metadata.put("value", value);

                metadatas.add(metadata);
            }
        }

        return metadatas;
    }

    @Override
    public Object getPartitionDetail(Map<String, Object> params) throws SQLException {
        assertParam(params, "schemaName");
        assertParam(params, "tableName");

        return repository.executeSelectOne("SHOW_PARTITION_DETAIL_QUERY", params);
    }

    @Override
    public List<Object> getPartitionsTree(Map<String, Object> params) throws SQLException {
        assertParam(params, "oid");

        return "root".equals(params.get("oid").toString()) ? new ArrayList<>() : repository.executeSelectListById("SHOW_PARTITION_QUERY", params);
    }

    @Override
    public String getObjectDef(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "objectType");
        assertParam(params, "oid");

        String objectType = params.get("objectType").toString();

        String objectDef = "";
        if ("FUNCTION".equals(objectType)) {
            Map<String, Object> functionDetail = (Map<String, Object>) repository.executeSelectOne("SHOW_FUNCTION_DETAIL_QUERY", params);

            String schemaName = functionDetail.get("nspname").toString();
            String rolname = functionDetail.get("rolname").toString();
            String proname = functionDetail.get("proname").toString();
            String lanname = functionDetail.get("lanname").toString();
            boolean proisstrict = (boolean) functionDetail.get("proisstrict");
            boolean proretset = (boolean) functionDetail.get("proretset");
            String provolatile = functionDetail.get("provolatile").toString();
            String prorettype = functionDetail.get("prorettype").toString();
            List<String> proargtypes = functionDetail.get("proargtypes") == null ? new ArrayList<String>() : Arrays.asList(functionDetail.get("proargtypes").toString().split(", "));
            List<String> proallargtypes = functionDetail.get("proallargtypes") == null ? new ArrayList<String>() : Arrays.asList(functionDetail.get("proallargtypes").toString().split(","));
            List<String> proargmodes = functionDetail.get("proargmodes") == null ? new ArrayList<String>() : Arrays.asList(functionDetail.get("proargmodes").toString().split(","));
            List<String> proargnames = functionDetail.get("proargnames") == null ? new ArrayList<String>() : Arrays.asList(functionDetail.get("proargnames").toString().split(","));
            String prosrc = functionDetail.get("prosrc").toString();
            String probin = functionDetail.get("probin").toString();
            String procdesc = functionDetail.get("procdesc").toString();
            String comment = functionDetail.get("comment") == null ? "" : functionDetail.get("comment").toString();

            List<String> argumentList = new ArrayList<>();
            if (proargmodes.isEmpty()) {
                for (int i = 0, typesSize = proargtypes.size(); i < typesSize; i++) {
                    String proargname = proargnames.isEmpty() ? "" : proargnames.get(i) + " ";
                    String argument = proargname + proargtypes.get(i);
                    if (!StringUtils.isEmpty(argument)) {
                        argumentList.add(argument);
                    }
                }
            } else {
                for (int i = 0, modesSize = proargmodes.size(); i < modesSize; i++) {
                    String proargmode = proargmodes.get(i) + " ";
                    String proargname = proargnames.isEmpty() ? "" : proargnames.get(i) + " ";
                    String argument = proargmode + proargname + proallargtypes.get(i);
                    if (!StringUtils.isEmpty(argument)) {
                        argumentList.add(argument);
                    }
                }
            }

            String arguments = argumentList.isEmpty() ? "" : "\n    " + StringUtils.join(argumentList, ",\n    ") + "\n";
            String setOf = proretset ? "SETOF " : "";
            String probody = "";
            if (StringUtils.isEmpty(probin)) {
                if ("internal".equals(lanname)) {
                    probody = "    '" + prosrc + "'";
                } else {
                    probody = "$BODY$" + prosrc + "$BODY$";
                }
            } else {
                probody = "    '" + probin + "', '" + prosrc + "'";
            }
            String prostrict = proisstrict ? " STRICT" : "";
            String commentQuery = StringUtils.isEmpty(comment) ? "" : "\nCOMMENT ON FUNCTION " + schemaName + "." + procdesc + " IS '" + comment + "';";

            objectDef = "CREATE OR REPLACE FUNCTION " + schemaName + "." + proname + "(" +
                    arguments +
                    ")\n" +
                    "RETURNS " + setOf + prorettype + " AS\n" +
                    probody + "\n" +
                    "LANGUAGE " + lanname + " " + provolatile + prostrict + ";\n" +
                    "ALTER FUNCTION " + schemaName + "." + procdesc + " OWNER TO " + rolname + ";" +
                    commentQuery;
        } else {
            assertParam(params, "tableName");
            assertParam(params, "storage");

            // get datas
            List<Object> columns = getColumns(params);
            Map<String, Object> tableMetadata = new HashMap<>();
            for (Object object : getObjectMetadatas(params)) {
                Map<String, Object> tableMeta = (Map) object;
                tableMetadata.put(tableMeta.get("key").toString(), tableMeta.get("value"));
            }
            List<String> constraintDefinitions = new ArrayList<>();
            for (Object object : getConstraints(params)) {
                Map<String, Object> constraint = (Map) object;
                constraintDefinitions.add("CONSTRAINT " + constraint.get("conname") + " CHECK (" + constraint.get("consrc") + ")");
            }
            Object partitionDefinition = repository.executeSelectOne("SHOW_PARTITION_DEF_QUERY", params);
            String partitionDef = "";
            if (partitionDefinition != null && ((Map) partitionDefinition).get("partitiondef") != null) {
                partitionDef += "\n" + ((Map) partitionDefinition).get("partitiondef").toString();
            }
            String storage = params.get("storage").toString();
            Map<String, Object> externalTableInfo = null;
            if ("x".equals(storage)) {// external table
                externalTableInfo = (Map<String, Object>) repository.executeSelectOne("SHOW_EXTERNAL_TABLE_INFORMATION_QUERY", params);
                String locationsString = externalTableInfo.get("locations").toString();

                if (locationsString.startsWith("ALL_SEGMENTS") ||
                        locationsString.startsWith("MASTER_ONLY") ||
                        locationsString.startsWith("TOTAL_SEGS") ||
                        locationsString.startsWith("PER_HOST") ||
                        locationsString.startsWith("HOST") ||
                        locationsString.startsWith("SEGMENT_ID")) {

                    String on = "ON ";
                    if (locationsString.startsWith("ALL_SEGMENTS")) {
                        on += "ALL";
                    } else if (locationsString.startsWith("MASTER_ONLY")) {
                        on += "MASTER";
                    } else if (locationsString.startsWith("TOTAL_SEGS")) {
                        on += locationsString.split(":")[1];
                    } else if (locationsString.startsWith("PER_HOST")) {
                        on += "HOST";
                    } else if (locationsString.startsWith("HOST")) {
                        on += "HOST '" + locationsString.split(":")[1] + "'";
                    } else if (locationsString.startsWith("SEGMENT_ID")) {
                        on += "SEGMENT " + locationsString.split(":")[1];
                    }

                    String execute = "EXECUTE '" + externalTableInfo.get("command") + "' " + on;
                    externalTableInfo.put("execute", execute);
                } else {
                    String[] locationsArray = locationsString.split("\\uF8FF");
                    List<String> locations = new ArrayList<>();
                    for (String location : locationsArray) {
                        locations.add("'" + location + "'");
                    }
                    externalTableInfo.put("location", locations);
                }
            }

            String dbSchemaTable = getDBSchemaTable(params);

            // generate column definition, column comments, distributed
            List<String> columnDefinitions = new ArrayList<>();
            List<String> columnComments = new ArrayList<>();
            List<String> distributes = new ArrayList<>();
            for (Object object : columns) {
                String columnDefinition = "";
                Map<String, Object> column = (Map) object;
                String columnName = column.get("column_name").toString();

                columnDefinition += "\"" + columnName + "\" ";
                columnDefinition += column.get("data_type").toString() + " ";
                if (column.get("character_maximum_length") != null) {
                    columnDefinition += "(" + column.get("character_maximum_length").toString() + ") ";
                }
                if (column.get("column_default") != null) {
                    columnDefinition += "DEFAULT " + column.get("column_default").toString() + " ";
                }
                if ("NO".equals(column.get("is_nullable").toString())) {
                    columnDefinition += "NOT NULL ";
                }
                if (column.get("distributed") instanceof Boolean && (boolean) column.get("distributed")) {
                    distributes.add("\"" + columnName + "\"");
                }

                if (column.get("column_comment") != null) {
                    columnComments.add("COMMENT ON COLUMN " + dbSchemaTable + "." + columnName + " IS '" + column.get("column_comment") + "';");
                }

                columnDefinitions.add(columnDefinition.trim());
            }

            // generate table type
            String tableType = "";
            if ("external".equals(tableMetadata.get("Storage").toString())) {
                if (externalTableInfo != null && (boolean) externalTableInfo.get("writable")) {
                    tableType += "WRITABLE ";
                }
                tableType += "EXTERNAL ";
                if (externalTableInfo != null && (boolean) externalTableInfo.get("web")) {
                    tableType += "WEB ";
                }
            }
            tableType += "TABLE ";

            // generate external table location
            String location = "";
            if (externalTableInfo != null && externalTableInfo.get("location") != null) {
                location += "\nLOCATION (\n    " + StringUtils.join((List) externalTableInfo.get("location"), ",\n    ") + "\n)";
            }

            // generate external table execute
            String execute = "";
            if (externalTableInfo != null && externalTableInfo.get("execute") != null) {
                execute += "\n" + externalTableInfo.get("execute");
            }

            // generate external table format
            String format = "";
            if (externalTableInfo != null) {
                format += "\nFORMAT '";
                String fmttype = externalTableInfo.get("fmttype").toString();
                String fmtopts = externalTableInfo.get("fmtopts").toString();
                if ("t".equals(fmttype)) {
                    format += "TEXT";
                } else if ("c".equals(fmttype)) {
                    format += "CSV";
                } else if ("b".equals(fmttype)) {
                    format += "CUSTOM";
                    fmtopts = fmtopts.replace(" ", " = ");
                }
                format += "' (\n    " + StringUtils.replace(fmtopts, "\\", "\\\\") + "\n)";// \ -> \\
            }

            // generate external table error
            String error = "";
            if (externalTableInfo != null) {
                error += externalTableInfo.get("errrelname") == null ?
                        "\n" :
                        "\nLOG ERRORS INTO " + externalTableInfo.get("errrelname") + " ";
                error += externalTableInfo.get("rejectlimit") == null ?
                        "" :
                        "SEGMENT REJECT LIMIT " + externalTableInfo.get("rejectlimit");
            }

            // generate inherit
            String inherit = "";
            if (!StringUtils.isEmpty(tableMetadata.get("Inherits").toString())) {
                inherit = "\nINHERITS (" + tableMetadata.get("Inherits").toString() + ")";
            }

            // generate with
            String with = "";
            if (tableMetadata.get("reloptions") != null) {
                String reloptions = tableMetadata.get("reloptions").toString().toUpperCase();
                String withOptions = StringUtils.replace(reloptions.substring(1, reloptions.length() - 1), ",", ",\n    ");
                with = "\nWITH (" +
                        "\n    " + withOptions +
                        "\n)";
            }

            // generate distributed
            String distributed = "";
            if ("TABLE ".equals(tableType) || tableType.contains("WRITABLE")) {
                distributed += "\nDISTRIBUTED ";
                distributed += distributes.isEmpty() ? "RANDOMLY" : "BY (" + StringUtils.join(distributes, ", ") + ")";
            }

            // generate alter owner
            String alterOwner = "ALTER TABLE " + dbSchemaTable + " OWNER TO " + tableMetadata.get("Owner") + ";";

            // generate table comment
            String tableComment = "";
            if (tableMetadata.get("Comment") != null) {
                tableComment = "COMMENT ON TABLE " + dbSchemaTable + " IS '" + tableMetadata.get("Comment") + "';";
            }

            objectDef = "CREATE " + tableType + dbSchemaTable + " (" +
                    "\n    " + StringUtils.join(columnDefinitions, ",\n    ") +
                    (constraintDefinitions.isEmpty() ? "" : ",\n    " + StringUtils.join(constraintDefinitions, ",\n    ")) +
                    "\n)" +
                    location +
                    execute +
                    format +
                    error +
                    inherit +
                    with +
                    distributed +
                    partitionDef + ";" +
                    "\n" + alterOwner +
                    (StringUtils.isEmpty(tableComment) ? "" : "\n" + tableComment) +
                    "\n" + StringUtils.join(columnComments, "\n");
        }

        return objectDef;
    }

    @Override
    public void getQueryResultAsync(Map<String, Object> params) throws SQLException, ClassNotFoundException {
        assertParam(params, "databaseName");
        assertParam(params, "query");
        assertParam(params, "uuid");

        HawqConnection hawqConnection = repository.getPostgresqlConnection(params);// greenplum jdbc 일 경우 create function ... raise ... 시 Unexpected message type:83
        int pid = hawqConnection.getPid();

        params.put("pid", pid);
        params.put("connection", hawqConnection.getConnection());

        reactor.notify("hawqPid", Event.wrap(params));
        reactor.notify("hawqQuery", Event.wrap(params));
    }

    /**
     * Drop 후 기본 데이터베이스를 선택하도록 함.
     *
     * @param params Drop 위한 데이터베이스명을 담은 Map
     * @return 기본 데이터데이스명
     */
    @Override
    public String dropDatabase(Map<String, Object> params) throws SQLException {
        EngineConfig engineConfig = (EngineConfig) params.get("engineConfig");
        assertParam(params, "dropDatabaseName");

        repository.executeUpdateQueryById("DROP_DATABASE_QUERY", params);
        return engineConfig.getHawqDefaultDatabaseName();
    }

    @Override
    public int createDatabase(Map<String, Object> params) throws SQLException {
        assertParam(params, "newDatabaseName");

        if (repository.executeSelectOneForFirstColumn("SHOW_EXIST_DATABASE_QUERY", params, boolean.class)) {
            throw new ServiceException("\"" + params.get("newDatabaseName") + "\" is already exist.");
        }

        return repository.executeUpdateQuery(getCreateDatabaseQuery(params), params);
    }


    @Override
    public int dropSchema(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");

        params.put("schemaName", params.get("schemaName"));
        return repository.executeUpdateQueryById("DROP_SCHEMA_QUERY", params);
    }

    @Override
    public int createSchema(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");

        return repository.executeUpdateQuery(getCreateSchemaQuery(params), params);
    }

    @Override
    public List<Object> getTablespaces(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_TABLESPACES_QUERY", params);
    }

    @Override
    public List<Object> getUsers(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_USERS_QUERY", params);
    }

    @Override
    public int dropTable(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "tableName");

        params.put("dbSchemaTable", getDBSchemaTable(params));
        return repository.executeUpdateQueryById("DROP_TABLE_QUERY", params);
    }

    @Override
    public int createTable(Map<String, Object> params) throws SQLException {
        assertParam(params, "tableName");
        assertParam(params, "columns");

        return repository.executeUpdateQuery(getCreateTableQuery(params), params);
    }

    @Override
    public int dropExternalTable(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "externalTableName");

        params.put("dbSchemaTable", getDBSchemaTable(params, "databaseName", "schemaName", "externalTableName"));
        return repository.executeUpdateQueryById("DROP_EXTERNAL_TABLE_QUERY", params);
    }

    @Override
    public int createExternalTable(Map<String, Object> params) throws SQLException, IOException {
        assertParam(params, "tableName");

        return repository.executeUpdateQuery(getCreateExternalTableQuery(params), params);
    }

    @Override
    public int dropView(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "viewName");

        params.put("dbSchemaView", getDBSchemaTable(params, "databaseName", "schemaName", "viewName"));
        return repository.executeUpdateQueryById("DROP_VIEW_QUERY", params);
    }

    @Override
    public List<Object> getCustomFormatter(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_CUSTOM_FORMATTER_QUERY", params);
    }

    @Override
    public String getDefaultSchema(Map<String, Object> params) throws SQLException {
        return repository.executeSelectOneForFirstColumn("SHOW_DEFAULT_SCHEMA_QUERY", params, String.class).split(",")[1];
    }

    @Override
    public List<Object> getQueryPlan(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "query");

        return repository.executeSelectList("EXPLAIN " + params.get("query").toString(), params);
    }

    @Override
    public int dropFunction(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "functionName");

        params.put("dbSchemaFunction", getDBSchemaTable(params, "databaseName", "schemaName", "functionName"));
        params.put("proargtypes", params.get("proargtypes"));
        return repository.executeUpdateQueryById("DROP_FUNCTION_QUERY", params);
    }

    @Override
    public Map<String, Object> tableDetail(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "tableName");

        params.put("dbSchemaTable", getDBSchemaTable(params));

        Map<String, Object> tableInformation = (Map<String, Object>) repository.executeSelectOne("SHOW_TABLE_DETAIL_QUERY", params);

        tableInformation.put("columns", getColumns(params));
        tableInformation.put("constraints", repository.executeSelectListById("SHOW_CONSTRAINTS_QUERY", params));
        tableInformation.put("partition", repository.executeSelectOne("SHOW_PARTITION_DEF_QUERY", params));

        return tableInformation;
    }

    @Override
    public int alterTable(Map<String, Object> params) throws SQLException {
        assertParam(params, "tableName");
        assertParam(params, "original");

        String alterTableQuery = getAlterTableQuery(params);
        if ("".equals(alterTableQuery)) {
            throw new ServiceException("This item is not modified.");
        }

        return repository.executeUpdateQuery(alterTableQuery, params);
    }

    @Override
    public List<Object> alterColumn(Map<String, Object> params) throws SQLException {
        String alterColumnQuery = getAlterColumnQuery(params);
        if ("".equals(alterColumnQuery)) {
            throw new ServiceException("There is no fixed Column.");
        }

        repository.executeUpdateQuery(alterColumnQuery, params);

        return getColumns(params);
    }

    @Override
    public List<Object> getConstraints(Map<String, Object> params) throws SQLException {
        assertParam(params, "databaseName");
        assertParam(params, "schemaName");
        assertParam(params, "tableName");

        params.put("dbSchemaTable", getDBSchemaTable(params));

        // constraint key 를 알아내기 위해 column 정보들을 가져옴
        Map<Integer, String> columnMap = new HashMap<>();
        List<Object> columns = repository.executeSelectListById("SHOW_COLUMNS_QUERY", params);
        for (Object object : columns) {
            Map<String, Object> column = (Map<String, Object>) object;
            columnMap.put((int) column.get("ordinal_position"), column.get("column_name").toString());
        }

        List<Object> constraints = repository.executeSelectListById("SHOW_CONSTRAINTS_QUERY", params);

        // constraint key 를 ordinal_position 에서 column name 으로 변환함
        for (Object object : constraints) {
            Map<String, Object> constraint = (Map<String, Object>) object;
            List<String> convertKeys = new ArrayList<>();
            String[] conkeys = constraint.get("conkeys").toString().split(",");
            for (String conkey : conkeys) {
                String columnName = columnMap.get(Integer.parseInt(conkey));
                convertKeys.add(columnName);
            }
            constraint.put("conkeys", StringUtils.join(convertKeys, ", "));
        }

        return constraints;
    }

    @Override
    public List<Object> alterConstraint(Map<String, Object> params) throws SQLException {
        String alterConstraintQuery = getAlterConstraintQuery(params);
        if ("".equals(alterConstraintQuery)) {
            throw new ServiceException("There is no fixed Constraint.");
        }

        repository.executeUpdateQuery(alterConstraintQuery, params);

        return getConstraints(params);
    }

    @Override
    public boolean killSession(Map<String, Object> params) throws SQLException {
        assertParam(params, "pid");
        return repository.executeSelectOneForFirstColumn("KILL_SESSION_QUERY", params, boolean.class);
    }

    @Override
    public List<Object> getRQueues(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_RESOURCE_QUEUES_QUERY", params);
    }

    @Override
    public Object getRQueue(Map<String, Object> params) throws SQLException {
        return repository.executeSelectOne("SHOW_RESOURCE_QUEUE_QUERY", params);
    }

    @Override
    public int createResourceQueue(Map<String, Object> params) throws SQLException {
        assertParam(params, "queueName");

        if (repository.executeSelectOneForFirstColumn("SHOW_EXIST_RESOURCE_QUEUE_QUERY", params, boolean.class)) {
            throw new ServiceException("\"" + params.get("queueName") + "\" is already exist.");
        }

        return repository.executeUpdateQuery(getCreateResourceQueueQuery(params), params);
    }

    @Override
    public int dropResourceQueue(Map<String, Object> params) throws SQLException {
        assertParam(params, "queueName");
        return repository.executeUpdateQueryById("DROP_RESOURCE_QUEUE_QUERY", params);
    }

    @Override
    public List<Object> getGroupRoles(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_GROUP_ROLES_QUERY", params);
    }

    @Override
    public List<Object> getLoginRoles(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_LOGIN_ROLES_QUERY", params);
    }

    @Override
    public Object role(Map<String, Object> params) throws SQLException {
        return repository.executeSelectOne("SHOW_ROLE_QUERY", params);
    }

    @Override
    public int createRole(Map<String, Object> params) throws SQLException {
        assertParam(params, "roleName");
        return repository.executeUpdateQuery(getCreateRoleQuery(params), params);
    }

    @Override
    public int alterRole(Map<String, Object> params) throws SQLException {
        assertParam(params, "newRoleName");
        return repository.executeUpdateQuery(getAlterRoleQuery(params), params);
    }

    @Override
    public int dropRole(Map<String, Object> params) throws SQLException {
        assertParam(params, "rolname");
        return repository.executeUpdateQueryById("DROP_ROLE_QUERY", params);
    }

    @Override
    public List<Object> getSessions(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_SESSIONS_QUERY", params);
    }

    @Override
    public long getSessionsTotal(Map<String, Object> params) throws SQLException {
        return repository.executeSelectOneForFirstColumn("SHOW_SESSIONS_TOTAL_QUERY", params, long.class);
    }

    @Override
    public List<Object> getLockTables(Map<String, Object> params) throws SQLException {
        return repository.executeSelectListById("SHOW_LOCK_TABLES_QUERY", params);
    }

    @Override
    public long getLockTablesTotal(Map<String, Object> params) throws SQLException {
        return repository.executeSelectOneForFirstColumn("SHOW_LOCK_TABLES_TOTAL_QUERY", params, long.class);
    }

    @Override
    public byte[] getResultToCsv(Map<String, Object> params) throws IOException {
        assertParam(params, "fields");
        assertParam(params, "datas");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        CSVWriter writer = new CSVWriter(outputStreamWriter);

        ObjectMapper objectMapper = new ObjectMapper();

        List<String> fields = objectMapper.readValue(StringUtils.unescape(params.get("fields").toString()), List.class);
        List<List<String>> escapedDatas = objectMapper.readValue(StringUtils.unescape(params.get("datas").toString()), List.class);

        List<String[]> datasStringArray = new ArrayList<>();
        String[] stringArray = null;
        for (List<String> escapedDatasList : escapedDatas) {
            int size = escapedDatasList.size();
            stringArray = new String[size];
            for (int i = 0; i < size; i++) {
                stringArray[i] = new String(StringEscapeUtils.unescapeHtml(escapedDatasList.get(i)));
            }
            datasStringArray.add(stringArray);
        }
        writer.writeNext(fields.toArray(new String[fields.size()]));
        writer.writeAll(datasStringArray);

        writer.close();
        outputStreamWriter.close();
        outputStream.close();

        return outputStream.toByteArray();
    }

    private void assertParam(Map<String, Object> params, String parameterName) {
        Assert.notNull(params.get(parameterName), "Invalid parameters. \"" + parameterName + "\" must not be null.");
        Assert.hasText(params.get(parameterName).toString(), "Invalid parameters. \"" + parameterName + "\" must not be empty, or blank.");
    }

    private String getDBSchemaTable(Map<String, Object> params) {
        return getDBSchemaTable(params, "databaseName", "schemaName", "tableName");
    }

    private String getDBSchemaTable(Map<String, Object> params, String databaseName, String schemaName, String tableName) {
        return params.get(databaseName) + "." + params.get(schemaName) + "." + params.get(tableName);
    }

    private String getCreateDatabaseQuery(Map<String, Object> params) {
        String query = "END; CREATE DATABASE " + params.get("newDatabaseName");
        if (params.get("owner") != null && !"".equals(params.get("owner").toString())) {
            query += " OWNER " + params.get("owner");
        }
        if (params.get("template") != null && !"".equals(params.get("template").toString())) {
            query += " TEMPLATE " + params.get("template");
        }
        if (params.get("encoding") != null && !"".equals(params.get("encoding").toString())) {
            query += " ENCODING '" + params.get("encoding") + "'";
        }
        if (params.get("tablespace") != null && !"".equals(params.get("tablespace").toString())) {
            query += " TABLESPACE " + params.get("tablespace");
        }
        if (params.get("connlimit") != null && Integer.parseInt(params.get("connlimit").toString()) >= -1) {
            query += " CONNECTION LIMIT " + params.get("connlimit");
        }
        query += "; BEGIN;";

        logger.debug("HAWQ Query = {}", query);
        return query;
    }


    private String getCreateSchemaQuery(Map<String, Object> params) {
        String query = "CREATE SCHEMA " + params.get("schemaName");
        if (params.get("username") != null && !"".equals(params.get("username").toString())) {
            query += " AUTHORIZATION " + params.get("username");
        }
        if (params.get("element") != null && !"".equals(params.get("element").toString())) {
            query += " " + params.get("element");
        }
        query += ";";

        logger.debug("HAWQ Query = {}", query);
        return query;
    }

    private String getCreateTableQuery(Map<String, Object> params) throws SQLException {
        EngineConfig engineConfig = (EngineConfig) params.get("engineConfig");
        // General
        String temporary = params.get("temporary") == null ? "" : params.get("temporary").toString();
        String databaseName = params.get("databaseName") == null ? engineConfig.getHawqDefaultDatabaseName() : params.get("databaseName").toString();
        String schemaName = params.get("schemaName") == null ? getDefaultSchema(params) : params.get("schemaName").toString();
        String tableName = params.get("tableName").toString();
        String dbSchemaTable = databaseName + "." + schemaName + "." + tableName;
        String createTableQuery = "CREATE TABLE " + temporary + " " + dbSchemaTable + " (";

        // Columns
        List<String> distributes = new ArrayList<>();
        List<String> commentColumnQueries = new ArrayList<>();
        Object otherTable = params.get("otherTable");
        if (otherTable != null && !StringUtils.isEmpty(otherTable.toString())) {
            createTableQuery += "LIKE " + otherTable;
        } else {
            List<Object> columns = (List<Object>) params.get("columns");
            for (int i = 0, size = columns.size(); i < size; i++) {
                Map<String, Object> column = (Map<String, Object>) columns.get(i);

                String columnName = column.get("columnName").toString();
                createTableQuery += columnName;

                createTableQuery += " " + column.get("dataType").toString();

                if (column.get("length") != null && !StringUtils.isEmpty(column.get("length").toString())) {
                    createTableQuery += " (" + column.get("length").toString() + ")";
                }

                if (column.get("default") != null && !StringUtils.isEmpty(column.get("default").toString())) {
                    createTableQuery += " DEFAULT '" + column.get("default").toString() + "'";
                }

                if (column.get("isNull") != null) {
                    createTableQuery += (Boolean.parseBoolean(column.get("isNull").toString()) ? "" : " NOT NULL");
                }

                if (column.get("check") != null) {
                    createTableQuery += " CHECK (" + column.get("check").toString() + ")";
                }

                if (column.get("comment") != null) {
                    commentColumnQueries.add("COMMENT ON COLUMN " + dbSchemaTable + "." + columnName + " IS '" + column.get("comment").toString() + "';");
                }

                if (column.get("distributed") != null && (boolean) column.get("distributed")) {
                    distributes.add(columnName);
                }

                if (i + 1 != size) {
                    createTableQuery += ", ";
                }
            }
        }
        createTableQuery += ")";

        // With
        String tableWithQuery = "";
        if (params.get("with") != null) {
            Map<String, Object> with = (Map<String, Object>) params.get("with");
            for (String key : with.keySet()) {
                Object value = with.get(key);
                if (value != null) {
                    tableWithQuery += key + "=" + value + ",";
                }
            }
        }
        if (tableWithQuery.endsWith(",")) {
            createTableQuery += " WITH (" + tableWithQuery.substring(0, tableWithQuery.length() - 1) + ")";// 마지막 , 제거
        }

        // Distributed
        createTableQuery += " DISTRIBUTED ";
        if (distributes.size() > 0) {
            createTableQuery += "BY (";
            for (int i = 0, size = distributes.size(); i < size; i++) {
                String columnName = distributes.get(i);
                createTableQuery += columnName;
                if (i + 1 != size) {
                    createTableQuery += ", ";
                }
            }
            createTableQuery += ")";
        } else {
            createTableQuery += "RANDOMLY";
        }

        // Partition
        Map<String, Object> partitionMap = (Map<String, Object>) params.get("partition");

        if (partitionMap.get("partitionColumnName") != null) {
            assertParam(partitionMap, "type");

            String partitionColumnName = partitionMap.get("partitionColumnName").toString();
            String partitionType = partitionMap.get("type").toString();

            createTableQuery += " PARTITION BY " + partitionType + " (" + partitionColumnName + ") (";

            if ("RANGE".equals(partitionType)) {
                assertParam(partitionMap, "ranges");

                List<Map<String, Object>> partitionRanges = (List<Map<String, Object>>) partitionMap.get("ranges");
                for (int i = 0, size = partitionRanges.size(); i < size; i++) {
                    Map<String, Object> partitionRange = partitionRanges.get(i);

                    String partitionName = partitionRange.get("name").toString();
                    createTableQuery += "PARTITION " + partitionName;

                    if (partitionRange.get("startValue") != null) {
                        String startType = partitionRange.get("startType").toString();
                        String startValue = partitionRange.get("startValue").toString();
                        createTableQuery += " START ('" + startValue + "'::" + startType + ")";
                    }

                    if (partitionRange.get("endValue") != null) {
                        String endType = partitionRange.get("endType").toString();
                        String endValue = partitionRange.get("endValue").toString();
                        createTableQuery += " END ('" + endValue + "'::" + endType + ")";
                    }

                    if (partitionRange.get("partitionWith") != null) {
                        String partitionWithQuery = "";
                        Map<String, Object> partitionWith = (Map<String, Object>) partitionRange.get("partitionWith");
                        for (String key : partitionWith.keySet()) {
                            Object value = partitionWith.get(key);
                            if (value != null) {
                                partitionWithQuery += key + "=" + value + ",";
                            }
                        }
                        if (partitionWithQuery.endsWith(",")) {
                            createTableQuery += " WITH (" + partitionWithQuery.substring(0, partitionWithQuery.length() - 1) + ")";// 마지막 , 제거
                        }
                    }

                    if (i + 1 != size) {
                        createTableQuery += ", ";
                    }
                }
            } else {// LIST
                assertParam(partitionMap, "lists");

                List<Map<String, Object>> partitionLists = (List<Map<String, Object>>) partitionMap.get("lists");
                for (int i = 0, size = partitionLists.size(); i < size; i++) {
                    Map<String, Object> partitionListMap = partitionLists.get(i);

                    String name = partitionListMap.get("name").toString();
                    String[] valueArray = partitionListMap.get("value").toString().split(",");

                    createTableQuery += "PARTITION " + name + " VALUES (";
                    String valueArrayString = "";
                    for (String value : valueArray) {
                        if (!StringUtils.isEmpty(value.trim())) {
                            valueArrayString += "'" + value.trim() + "',";
                        }
                    }
                    if (valueArrayString.endsWith(",")) {
                        valueArrayString = valueArrayString.substring(0, valueArrayString.length() - 1);// 마지막 , 제거
                    }
                    createTableQuery += valueArrayString + ")";

                    if (i + 1 != size) {
                        createTableQuery += ", ";
                    }
                }
            }

            if (partitionMap.get("partitionDefault") != null && !StringUtils.isEmpty(partitionMap.get("partitionDefault").toString())) {
                createTableQuery += ", DEFAULT PARTITION " + partitionMap.get("partitionDefault").toString();
            }

            createTableQuery += ")";
        }

        createTableQuery += ";";
        // End making Create Table Query

        String alterTableOwnerQuery = "";
        if (params.get("owner") != null && !StringUtils.isEmpty(params.get("owner").toString())) {
            alterTableOwnerQuery = "ALTER TABLE " + dbSchemaTable + " OWNER TO " + params.get("owner").toString() + ";";
        }

        String commentTableQuery = "";
        if (params.get("tableComment") != null && !StringUtils.isEmpty(params.get("tableComment").toString())) {
            commentTableQuery = "COMMENT ON TABLE " + dbSchemaTable + " IS '" + params.get("tableComment").toString() + "';";
        }

        String query = createTableQuery + alterTableOwnerQuery + commentTableQuery;
        for (String commentColumnQuery : commentColumnQueries) {
            query += commentColumnQuery;
        }

        logger.debug("HAWQ Query = {}", query);
        return query;
    }

    private String getCreateExternalTableQuery(Map<String, Object> params) throws SQLException, IOException {
        EngineConfig engineConfig = (EngineConfig) params.get("engineConfig");
        List<String> commentColumnQueries = new ArrayList<>();
        List<String> distributes = new ArrayList<>();

        // General
        String databaseName = params.get("databaseName") == null ? engineConfig.getHawqDefaultDatabaseName() : params.get("databaseName").toString();
        String schemaName = params.get("schemaName") == null ? getDefaultSchema(params) : params.get("schemaName").toString();
        String tableName = params.get("tableName").toString();
        String dbSchemaTable = databaseName + "." + schemaName + "." + tableName;

        String writable = Boolean.parseBoolean(params.get("writable").toString()) ? "WRITABLE " : "";
        String webTable = Boolean.parseBoolean(params.get("webTable").toString()) ? "WEB " : "";

        String createExternalTableQuery = "CREATE " + writable + "EXTERNAL " + webTable + "TABLE " + dbSchemaTable + " (";

        // Columns
        Object otherTable = params.get("otherTable");
        if (otherTable != null && !StringUtils.isEmpty(otherTable.toString())) {
            createExternalTableQuery += "LIKE " + otherTable;
        } else {
            List<Object> columns = (List<Object>) params.get("columns");
            for (int i = 0, size = columns.size(); i < size; i++) {
                Map<String, Object> column = (Map<String, Object>) columns.get(i);

                String columnName = column.get("columnName").toString();
                createExternalTableQuery += "\"" + columnName + "\"";

                createExternalTableQuery += " " + column.get("dataType").toString();

                if (column.get("length") != null && !StringUtils.isEmpty(column.get("length").toString())) {
                    createExternalTableQuery += " (" + column.get("length").toString() + ")";
                }

                if (column.get("comment") != null && !StringUtils.isEmpty(column.get("comment").toString())) {
                    commentColumnQueries.add("COMMENT ON COLUMN " + dbSchemaTable + "." + columnName + " IS '" + column.get("comment").toString() + "';");
                }

                if (column.get("distributed") != null && (boolean) column.get("distributed")) {
                    distributes.add(columnName);
                }

                if (i + 1 != size) {
                    createExternalTableQuery += ", ";
                }
            }
        }

        // Locations
        createExternalTableQuery += ") LOCATION (";
        List<String> locations = (List<String>) params.get("locations");
        for (int i = 0, locationsSize = locations.size(); i < locationsSize; i++) {
            createExternalTableQuery += "'" + locations.get(i) + "'";
            if (i + 1 != locationsSize) {
                createExternalTableQuery += ", ";
            }
        }
        createExternalTableQuery += ")";

        // Format
        Map<String, Object> formatMap = ((Map<String, Object>) params.get("format"));
        ObjectMapper objectMapper = new ObjectMapper();
        String typeString = formatMap.get("type").toString();

        Format format;
        if ("TEXT".equals(typeString)) {
            format = objectMapper.readValue(objectMapper.writeValueAsString(formatMap), Text.class);
        } else if ("CSV".equals(typeString)) {
            format = objectMapper.readValue(objectMapper.writeValueAsString(formatMap), Csv.class);
        } else {// CUSTOM
            format = objectMapper.readValue(objectMapper.writeValueAsString(formatMap), Custom.class);
        }

        createExternalTableQuery += " FORMAT '" + format.getType() + "'";

        if (!format.isEmptyOptions()) {
            createExternalTableQuery += " (" + format.getFormatString() + ")";
        }

        // Distributed
        if (!StringUtils.isEmpty(writable)) {
            createExternalTableQuery += " DISTRIBUTED ";
            if (distributes.size() > 0) {
                createExternalTableQuery += "BY (";
                for (int i = 0, size = distributes.size(); i < size; i++) {
                    String columnName = distributes.get(i);
                    createExternalTableQuery += columnName;
                    if (i + 1 != size) {
                        createExternalTableQuery += ", ";
                    }
                }
                createExternalTableQuery += ")";
            } else {
                createExternalTableQuery += "RANDOMLY";
            }
        }

        createExternalTableQuery += ";";

        String alterTableOwnerQuery = params.get("owner") == null ? "" : "ALTER TABLE " + dbSchemaTable + " OWNER TO " + params.get("owner").toString() + ";";
        String commentTableQuery = params.get("tableComment") == null ? "" : "COMMENT ON TABLE " + dbSchemaTable + " IS '" + params.get("tableComment").toString() + "';";

        String query = createExternalTableQuery + alterTableOwnerQuery + commentTableQuery;
        for (String commentColumnQuery : commentColumnQueries) {
            query += commentColumnQuery;
        }

        logger.debug("HAWQ Query = {}", query);
        return query;
    }

    private void setColumnDistributed(List<Object> columns, String attrnums) {
        List<String> positions = Arrays.asList(attrnums.split(","));

        for (int i = 0, length = columns.size(); i < length; i++) {
            Map<String, Object> column = (Map<String, Object>) columns.get(i);
            String position = column.get("ordinal_position").toString();
            if (positions.contains(position)) {
                column.put("distributed", true);
            } else {
                column.put("distributed", "");
            }
            columns.set(i, column);
        }
    }

    private String getAlterTableQuery(Map<String, Object> newTable) {
        String alterTableQuery = "";
        Map<String, Object> oriTable = (Map<String, Object>) newTable.get("original");

        boolean isChangedTableName = isChanged(newTable, oriTable, "tableName");
        boolean isChangedOwner = isChanged(newTable, oriTable, "owner");
        boolean isChangedSchemaName = isChanged(newTable, oriTable, "schemaName");
        boolean isChangedDistributed = isChangedDistributed(newTable, oriTable);
        boolean isChangedTableComment = isChanged(newTable, oriTable, "tableComment", "table_comment");

        String oriDbSchemaTable = getDBSchemaTable(oriTable);

        // schema 와 table 을 동시에 수정했을 경우를 처리하기 위한 현재 table 정보를 담고있는 Map
        Map<String, Object> currentDBSchemaTableMap = new HashMap<>();
        currentDBSchemaTableMap.put("databaseName", oriTable.get("databaseName").toString());
        currentDBSchemaTableMap.put("schemaName", oriTable.get("schemaName").toString());
        currentDBSchemaTableMap.put("tableName", oriTable.get("tableName").toString());

        if (isChangedTableName) {
            alterTableQuery += "ALTER TABLE " + oriDbSchemaTable + " RENAME TO " + newTable.get("tableName") + ";";
            currentDBSchemaTableMap.put("tableName", newTable.get("tableName").toString());
        }

        if (isChangedOwner) {
            alterTableQuery += "ALTER TABLE " + getDBSchemaTable(currentDBSchemaTableMap) + " OWNER TO " + newTable.get("owner") + ";";
        }

        if (isChangedSchemaName) {
            alterTableQuery += "ALTER TABLE " + getDBSchemaTable(currentDBSchemaTableMap) + " SET SCHEMA " + newTable.get("schemaName") + ";";
            currentDBSchemaTableMap.put("schemaName", newTable.get("schemaName").toString());
        }

        if (isChangedDistributed) {
            List<Object> newDistributed = (List<Object>) newTable.get("distributed");
            String distributionKey = "";
            if (newDistributed.isEmpty()) {
                distributionKey = "RANDOMLY";
            } else {
                distributionKey = "BY (" + StringUtils.join(newDistributed, ",") + ")";
            }
            alterTableQuery += "ALTER TABLE " + getDBSchemaTable(currentDBSchemaTableMap) + " SET DISTRIBUTED " + distributionKey + ";";
        }

        if (isChangedTableComment) {
            alterTableQuery += "COMMENT ON TABLE " + getDBSchemaTable(currentDBSchemaTableMap) + " IS '" + newTable.get("tableComment").toString() + "';";
        }

        logger.debug("Alter Table Query = {}", alterTableQuery);
        return alterTableQuery;
    }

    private boolean isChanged(Map<String, Object> a, Map<String, Object> b, String key) {
        return isChanged(a, b, key, key);
    }

    private boolean isChanged(Map<String, Object> a, Map<String, Object> b, String aKey, String bKey) {
        Object v1 = a.get(aKey);
        Object v2 = b.get(bKey);

        if (v1 == null) {
            if (v2 == null) {
                return false;
            } else if (v2 instanceof String) {
                return !"".equals(v2);
            }
        }
        if (v2 == null && v1 instanceof String) {
            return !"".equals(v1);
        }
        if (v1 instanceof String && v2 instanceof String) {
            return !v1.toString().trim().equals(v2.toString().trim());
        }

        return v1 != v2 || !Objects.equals(v1, v2);
    }

    private boolean isChangedDistributed(Map<String, Object> newTable, Map<String, Object> oriTable) {
        List<Object> newDistributed = (List<Object>) newTable.get("distributed");
        List<Object> oriColumns = (List<Object>) oriTable.get("columns");
        List<String> oriDistributed = new ArrayList<>();
        for (Object object : oriColumns) {
            Map<String, Object> oriColumn = (Map<String, Object>) object;
            if (oriColumn.get("distributed") != null &&
                    !StringUtils.isEmpty(oriColumn.get("distributed").toString()) &&
                    Boolean.parseBoolean(oriColumn.get("distributed").toString())) {
                oriDistributed.add(oriColumn.get("column_name").toString());
            }
        }

        return newDistributed.size() != oriDistributed.size() ||
                !newDistributed.containsAll(oriDistributed);
    }

    private String getAlterColumnQuery(Map<String, Object> params) {
        String dbSchemaTable = getDBSchemaTable(params);

        List<Object> newRecords = (List<Object>) params.get("newRecords");
        List<Object> removedRecords = (List<Object>) params.get("removedRecords");
        List<Object> updatedRecords = (List<Object>) params.get("updatedRecords");

        String columnAddQuery = getColumnAddQuery(dbSchemaTable, newRecords);
        String columnDropQuery = getColumnDropQuery(dbSchemaTable, removedRecords);
        String columnAlterQuery = getColumnAlterQuery(dbSchemaTable, updatedRecords);

        String alterColumnQuery = columnAddQuery + columnDropQuery + columnAlterQuery;
        logger.debug("columnAddQuery = {}", columnAddQuery);
        logger.debug("columnDropQuery = {}", columnDropQuery);
        logger.debug("columnAlterQuery = {}", columnAlterQuery);
        logger.debug("alterColumnQuery = {}", alterColumnQuery);
        return alterColumnQuery;
    }

    private String getColumnAddQuery(String dbSchemaTable, List<Object> newRecords) {
        String columnAddQuery = "";
        String commentColumnQuery = "";
        if (!newRecords.isEmpty()) {
            String query;
            for (Object object : newRecords) {
                query = "ALTER TABLE " + dbSchemaTable + " ADD COLUMN ";

                Map<String, Object> column = (Map<String, Object>) object;

                String columnName = column.get("column_name").toString();
                query += columnName + " " + column.get("data_type").toString();

                if (column.get("character_maximum_length") != null &&
                        !StringUtils.isEmpty(column.get("character_maximum_length").toString())) {
                    query += " (" + column.get("character_maximum_length").toString() + ")";
                }

                if (column.get("column_default") != null &&
                        !StringUtils.isEmpty(column.get("column_default").toString())) {
                    query += " DEFAULT '" + column.get("column_default").toString() + "'";
                }

                if (column.get("is_nullable") != null &&
                        !"NO".equals(column.get("is_nullable").toString())) {
                    query += (Boolean.parseBoolean(column.get("is_nullable").toString()) ? "" : " NOT NULL");
                }

                if (column.get("column_comment") != null) {
                    commentColumnQuery += "COMMENT ON COLUMN " + dbSchemaTable + "." + columnName + " IS '" + column.get("column_comment").toString() + "';";
                }

                columnAddQuery += query + ";";
            }
        }

        columnAddQuery += commentColumnQuery;
        return columnAddQuery;
    }

    private String getColumnDropQuery(String dbSchemaTable, List<Object> removedRecords) {
        String columnDropQuery = "";
        if (!removedRecords.isEmpty()) {
            for (Object object : removedRecords) {
                Map<String, Object> column = (Map<String, Object>) object;
                columnDropQuery += "ALTER TABLE " + dbSchemaTable + " DROP COLUMN " + column.get("column_name") + ";";
            }
        }
        return columnDropQuery;
    }

    private String getColumnAlterQuery(String dbSchemaTable, List<Object> updatedRecords) {
        Set<String> columnAlterQueries = new LinkedHashSet<>();
        if (!updatedRecords.isEmpty()) {
            String query;
            for (Object object : updatedRecords) {
                query = "ALTER TABLE " + dbSchemaTable + " ";
                Map<String, Object> column = (Map<String, Object>) object;
                Map<String, Object> newColumn = (Map<String, Object>) column.get("newColumn");
                Map<String, Object> oriColumn = (Map<String, Object>) column.get("oriColumn");

                Object newColumnName = newColumn.get("column_name");
                Object oriColumnName = oriColumn.get("column_name");
                String columnName = newColumnName == null ? oriColumnName.toString() : newColumnName.toString();

                for (String key : oriColumn.keySet()) {
                    if (isChanged(newColumn, oriColumn, key)) {
                        Object newValue = newColumn.get(key);
                        Object oriValue = oriColumn.get(key);

                        switch (key) {
                            case "column_name":
                                query += "RENAME COLUMN " + oriValue + " TO " + newValue + ";";
                                break;
                            case "data_type":
                                query += "ALTER COLUMN " + columnName + " TYPE " + newColumn.get("data_type");
                                if (newColumn.get("character_maximum_length") != null &&
                                        !StringUtils.isEmpty(newColumn.get("character_maximum_length").toString())) {
                                    query += " (" + newColumn.get("character_maximum_length") + ")";
                                }
                                query += ";";
                                break;
                            case "column_default":
                                query += "ALTER COLUMN " + columnName + " SET DEFAULT '" + newValue + "';";
                                break;
                            case "is_nullable":
                                query += "ALTER COLUMN " + columnName + " " + ((boolean) newValue ? "DROP" : "SET") + " NOT NULL;";
                                break;
                            case "column_comment":
                                query = "COMMENT ON COLUMN " + dbSchemaTable + "." + columnName + " IS '" + newValue + "';";
                                break;
                            default:
                                query = "";
                                break;
                        }

                        columnAlterQueries.add(query);
                    }
                }
            }
        }

        return StringUtils.join(columnAlterQueries, " ");
    }

    private String getAlterConstraintQuery(Map<String, Object> params) {
        String dbSchemaTable = getDBSchemaTable(params);

        List<Object> newRecords = (List<Object>) params.get("newRecords");
        List<Object> removedRecords = (List<Object>) params.get("removedRecords");

        String constraintAddQuery = getConstraintAddQuery(dbSchemaTable, newRecords);
        String constraintDropQuery = getConstraintDropQuery(dbSchemaTable, removedRecords);

        String alterConstraintQuery = constraintAddQuery + constraintDropQuery;

        logger.debug("Alter Constraint Query = {}", alterConstraintQuery);
        return alterConstraintQuery;
    }

    private String getConstraintAddQuery(String dbSchemaTable, List<Object> newRecords) {
        String constraintAddQuery = "";
        if (!newRecords.isEmpty()) {
            String query;
            for (Object object : newRecords) {
                Map<String, Object> constraint = (Map<String, Object>) object;
                query = "ALTER TABLE " + dbSchemaTable + " ADD CONSTRAINT " +
                        constraint.get("conname") + " CHECK (" + constraint.get("consrc") + ");";
                constraintAddQuery += query;
            }
        }

        return constraintAddQuery;
    }

    private String getConstraintDropQuery(String dbSchemaTable, List<Object> removedRecords) {
        String constraintDropQuery = "";
        if (!removedRecords.isEmpty()) {
            String query;
            for (Object object : removedRecords) {
                Map<String, Object> constraint = (Map<String, Object>) object;
                query = "ALTER TABLE " + dbSchemaTable + " DROP CONSTRAINT " + constraint.get("conname") + ";";
                constraintDropQuery += query;
            }
        }

        return constraintDropQuery;
    }

    private String getCreateResourceQueueQuery(Map<String, Object> params) {
        String createResourceQueueQuery = "END; CREATE RESOURCE QUEUE " + params.get("queueName") + " WITH (";

        if (params.get("activeStatements") != null) {
            createResourceQueueQuery += " ACTIVE_STATEMENTS=" + params.get("activeStatements") + ",";
        }

        if (params.get("maxCost") != null) {
            createResourceQueueQuery += " MAX_COST=" + params.get("maxCost") + ",";
        }

        if (params.get("costOvercommit") != null) {
            createResourceQueueQuery += " COST_OVERCOMMIT=" + params.get("costOvercommit") + ",";
        }

        if (params.get("minCost") != null) {
            createResourceQueueQuery += " MIN_COST=" + params.get("minCost") + ",";
        }

        if (params.get("priority") != null) {
            createResourceQueueQuery += " PRIORITY=" + params.get("priority") + ",";
        }

        if (params.get("memoryLimit") != null) {
            if (params.get("memoryUnit") != null) {
                createResourceQueueQuery += "'" + params.get("memoryLimit") + params.get("memoryUnit") + "'";
            } else {
                createResourceQueueQuery += params.get("memoryLimit");
            }
        }

        // , 제거
        if (createResourceQueueQuery.endsWith(",")) {
            createResourceQueueQuery = createResourceQueueQuery.substring(0, createResourceQueueQuery.length() - 1);
        }

        createResourceQueueQuery += "); BEGIN;";

        logger.debug("HAWQ QUery = {}", createResourceQueueQuery);
        return createResourceQueueQuery;
    }

    private String getCreateRoleQuery(Map<String, Object> params) {
        String createRoleQuery = "CREATE ROLE " + params.get("roleName") + " ";

        if (params.get("superUser") != null) {
            boolean isSuperUser = Boolean.parseBoolean(params.get("superUser").toString());
            createRoleQuery += isSuperUser ? "SUPERUSER " : "NOSUPERUSER ";
        }

        if (params.get("createDB") != null) {
            boolean isCreateDB = Boolean.parseBoolean(params.get("createDB").toString());
            createRoleQuery += isCreateDB ? "CREATEDB " : "NOCREATEDB ";
        }

        if (params.get("createRole") != null) {
            boolean isCreateRole = Boolean.parseBoolean(params.get("createRole").toString());
            createRoleQuery += isCreateRole ? "CREATEROLE " : "NOCREATEROLE ";
        }

        boolean isCreateExtTable = (boolean) params.get("createExtTable");
        boolean checkedRgpfdist = (boolean) params.get("rgpfdist");
        boolean checkedWgpfdist = (boolean) params.get("wgpfdist");
        boolean checkedRhttp = (boolean) params.get("rhttp");
        String createExtTable = isCreateExtTable ? "CREATEEXTTABLE " : "NOCREATEEXTTABLE ";
        if (!checkedRgpfdist && !checkedWgpfdist && !checkedRhttp) {
            createRoleQuery += createExtTable;
        } else {
            if (checkedRgpfdist) {
                createRoleQuery += createExtTable + "(TYPE='READABLE', PROTOCOL='GPFDIST') ";
            }
            if (checkedWgpfdist) {
                createRoleQuery += createExtTable + "(TYPE='WRITABLE', PROTOCOL='GPFDIST') ";
            }
            if (checkedRhttp) {
                createRoleQuery += createExtTable + "(TYPE='READABLE', PROTOCOL='HTTP') ";
            }
        }

        if (params.get("inherit") != null) {
            boolean isInherit = Boolean.parseBoolean(params.get("inherit").toString());
            createRoleQuery += isInherit ? "INHERIT " : "NOINHERIT ";
        }

        if (params.get("canLogin") != null) {
            boolean isCanLogin = Boolean.parseBoolean(params.get("canLogin").toString());
            createRoleQuery += isCanLogin ? "LOGIN " : "NOLOGIN ";
        }

        if (params.get("connLimit") != null) {
            createRoleQuery += "CONNECTION LIMIT " + Integer.parseInt(params.get("connLimit").toString()) + " ";
        }

        if (params.get("password") != null) {
            String password = params.get("password").toString();
            if (params.get("encrypted") != null) {
                boolean isEncrypted = Boolean.parseBoolean(params.get("encrypted").toString());
                if (isEncrypted) {
                    createRoleQuery += "ENCRYPTED ";
                } else {
                    createRoleQuery += "UNENCRYPTED ";
                }
                createRoleQuery += "PASSWORD '" + password + "' ";
            }
        }

        if (params.get("validUntil") != null) {
            createRoleQuery += "VALID UNTIL '" + params.get("validUntil") + "' ";
        }

        List<Object> inRoleList = (List<Object>) params.get("inRole");
        if (inRoleList != null && !inRoleList.isEmpty()) {
            createRoleQuery += "IN ROLE " + StringUtils.join(inRoleList, ", ") + " ";
        }

        List<Object> roleList = (List<Object>) params.get("role");
        if (roleList != null && !roleList.isEmpty()) {
            createRoleQuery += "ROLE " + StringUtils.join(roleList, ", ") + " ";
        }

        List<Object> adminList = (List<Object>) params.get("admin");
        if (adminList != null && !adminList.isEmpty()) {
            createRoleQuery += "ADMIN " + StringUtils.join(adminList, ", ") + " ";
        }

        if (params.get("resourceQueue") != null) {
            createRoleQuery += "RESOURCE QUEUE " + params.get("resourceQueue") + " ";
        }

        createRoleQuery += getDeny(params) + ";";

        logger.debug("HAWQ Query = {}", createRoleQuery);
        return createRoleQuery;
    }

    private String getDeny(Map<String, Object> params) {
        String deny = "";
        Object denyToDayObject = params.get("denyToDay");
        Object denyToTimeObject = params.get("denyToTime");
        Object denyFromDayObject = params.get("denyFromDay");
        Object denyFromTimeObject = params.get("denyFromTime");
        String denyTo = "";
        String denyFrom = "";
        if (denyToDayObject != null && !StringUtils.isEmpty(denyToDayObject.toString())) {
            denyTo += "DAY '" + denyToDayObject + "' ";
            if (denyToTimeObject != null && !StringUtils.isEmpty(denyToTimeObject.toString())) {
                denyTo += "TIME '" + denyToTimeObject + "' ";
            }
        }
        if (denyFromDayObject != null && !StringUtils.isEmpty(denyFromDayObject.toString())) {
            denyFrom += "DAY '" + denyFromDayObject + "' ";
            if (denyFromTimeObject != null && !StringUtils.isEmpty(denyFromTimeObject.toString())) {
                denyFrom += "TIME '" + denyFromTimeObject + "' ";
            }
        }

        if (!StringUtils.isEmpty(denyFrom)) {
            if (!StringUtils.isEmpty(denyTo)) {
                deny = "DENY BETWEEN " + denyFrom + " AND " + denyTo;
            } else {
                deny = "DENY " + denyFrom;
            }
        }
        return deny;
    }

    private String getAlterRoleQuery(Map<String, Object> params) {
        Map<String, Object> oriRole = (Map<String, Object>) params.get("originalRole");
        Map<String, Object> newRole = (Map<String, Object>) params.get("updatedRole");

        Map<String, Boolean> changedMap = getChangedMap(oriRole, newRole);
        if (!changedMap.get("changed")) {
            throw new ServiceException("This item is not modified.");
        }

        String alterRoleQuery = "";
        String alterRoleRenameQuery = "";
        String alterRoleConfigQuery = "";
        String alterRoleRQueueQuery = "";

        String oriRoleName = oriRole.get("rolname").toString();
        String newRoleName = newRole.get("rolname").toString();
        String currentRolName = oriRoleName;

        if (changedMap.get("rolname")) {
            alterRoleRenameQuery = "ALTER ROLE " + oriRoleName + " RENAME TO " + newRoleName + ";";
            currentRolName = newRoleName;
        }

        if (changedMap.get("rolconfig")) {
            String newConfig = newRole.get("rolconfig") == null ? "" : newRole.get("rolconfig").toString();
            alterRoleConfigQuery = "ALTER ROLE " + currentRolName + " RESET " + newConfig + ";";
        }

        if (changedMap.get("rsqname")) {
            String newRsqname = newRole.get("rsqname") == null ? "NONE" : newRole.get("rsqname").toString();
            alterRoleRQueueQuery = "ALTER ROLE " + currentRolName + " RESOURCE QUEUE " + newRsqname;
        }

        alterRoleQuery = "ALTER ROLE " + currentRolName + " ";

        if (changedMap.get("rolsuper")) {
            boolean changedRolsuper = (boolean) newRole.get("rolsuper");
            alterRoleQuery += changedRolsuper ? "SUPERUSER " : "NOSUPERUSER ";
        }

        if (changedMap.get("rolcreatedb")) {
            boolean changedRolcreatedb = (boolean) newRole.get("rolcreatedb");
            alterRoleQuery += changedRolcreatedb ? "CREATEDB " : "NOCREATEDB ";
        }

        if (changedMap.get("rolcreaterole")) {
            boolean changedRolcreaterole = (boolean) params.get("rolcreaterole");
            alterRoleQuery += changedRolcreaterole ? "CREATEROLE " : "NOCREATEROLE ";
        }

        boolean changedRolcreateRextgpfd = changedMap.get("rolcreaterextgpfd");
        boolean changedRolcreateWextgpfd = changedMap.get("rolcreatewextgpfd");
        boolean changedRolcreateRexthttp = changedMap.get("rolcreaterexthttp");
        if (changedRolcreateRextgpfd) {
            boolean checkedRolcreateRextgpfd = (boolean) newRole.get("rolcreaterextgpfd");
            alterRoleQuery += (checkedRolcreateRextgpfd ? "" : "NO") + "CREATEEXTTABLE (TYPE='READABLE', PROTOCOL='GPFDIST') ";
        }
        if (changedRolcreateWextgpfd) {
            boolean checkedRolcreateWextgpfd = (boolean) newRole.get("rolcreatewextgpfd");
            alterRoleQuery += (checkedRolcreateWextgpfd ? "" : "NO") + "CREATEEXTTABLE (TYPE='WRITABLE', PROTOCOL='GPFDIST') ";
        }
        if (changedRolcreateRexthttp) {
            boolean checkedRolcreateRexthttp = (boolean) newRole.get("rolcreaterexthttp");
            alterRoleQuery += (checkedRolcreateRexthttp ? "" : "NO") + "CREATEEXTTABLE (TYPE='READABLE', PROTOCOL='HTTP') ";
        }

        if (changedMap.get("rolinherit")) {
            boolean checkedRolinherit = (boolean) newRole.get("rolinherit");
            alterRoleQuery += checkedRolinherit ? "INHERIT " : "NOINHERIT ";
        }

        if (changedMap.get("rolcanlogin")) {
            boolean checkedRolcanlogin = (boolean) newRole.get("rolcanlogin");
            alterRoleQuery += checkedRolcanlogin ? "LOGIN " : "NOLOGIN ";
        }

        if (changedMap.get("rolconnlimit")) {
            alterRoleQuery += "CONNECTION LIMIT " + newRole.get("rolconnlimit") + " ";
        }

        if (newRole.get("password") != null && !StringUtils.isEmpty(newRole.get("password").toString())) {
            String password = newRole.get("password").toString();
            if (newRole.get("encrypted") != null) {
                boolean isEncrypted = (boolean) newRole.get("encrypted");
                if (isEncrypted) {
                    alterRoleQuery += "ENCRYPTED ";
                } else {
                    alterRoleQuery += "UNENCRYPTED ";
                }
                alterRoleQuery += "PASSWORD '" + password + "' ";
            }
        }

        if (changedMap.get("rolvaliduntil")) {
            alterRoleQuery += "VALID UNTIL '" + newRole.get("rolvaliduntil") + "' ";
        }

        alterRoleQuery += getDeny(newRole) + ";";

        alterRoleQuery = alterRoleRenameQuery + alterRoleConfigQuery + alterRoleRQueueQuery + alterRoleQuery;

        logger.debug("HAWQ Query = {}", alterRoleQuery);
        return alterRoleQuery;
    }

    private Map<String, Boolean> getChangedMap(Map<String, Object> oriRole, Map<String, Object> newRole) {
        Map<String, Boolean> changedMap = new HashMap<>();

        changedMap.put("rolname", isChanged(oriRole, newRole, "rolname"));
        changedMap.put("rolconfig", isChanged(oriRole, newRole, "rolconfig"));
        changedMap.put("rsqname", isChanged(oriRole, newRole, "rsqname"));
        changedMap.put("rolsuper", isChanged(oriRole, newRole, "rolsuper"));
        changedMap.put("rolcreatedb", isChanged(oriRole, newRole, "rolcreatedb"));
        changedMap.put("rolcreaterole", isChanged(oriRole, newRole, "rolcreaterole"));
        changedMap.put("rolcreaterextgpfd", isChanged(oriRole, newRole, "rolcreaterextgpfd"));
        changedMap.put("rolcreatewextgpfd", isChanged(oriRole, newRole, "rolcreatewextgpfd"));
        changedMap.put("rolcreaterexthttp", isChanged(oriRole, newRole, "rolcreaterexthttp"));
        changedMap.put("rolcreaterexthdfs", isChanged(oriRole, newRole, "rolcreaterexthdfs"));
        changedMap.put("rolcreatewexthdfs", isChanged(oriRole, newRole, "rolcreatewexthdfs"));
        changedMap.put("rolinherit", isChanged(oriRole, newRole, "rolinherit"));
        changedMap.put("rolcanlogin", isChanged(oriRole, newRole, "rolcanlogin"));
        changedMap.put("rolconnlimit", isChanged(oriRole, newRole, "rolconnlimit"));
        changedMap.put("rolpassword", !StringUtils.isEmpty(newRole.get("rolpassword").toString()));
        changedMap.put("encrypted", (boolean) newRole.get("encrypted"));
        changedMap.put("rolvaliduntil", isChanged(oriRole, newRole, "rolvaliduntil"));
        changedMap.put("deny", !StringUtils.isEmpty(getDeny(newRole)));

        // 값들이 바뀌었는지 체크하기 위한 값 세팅
        changedMap.put("changed", false);
        Collection<Boolean> changedValues = changedMap.values();
        for (boolean changed : changedValues) {
            if (changed) {
                changedMap.put("changed", true);
                break;
            }
        }

        return changedMap;
    }

}