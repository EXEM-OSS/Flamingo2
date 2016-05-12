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

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.*;
import org.apache.thrift.TException;
import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.security.SessionUtils;
import org.opencloudengine.flamingo2.engine.fs.audit.FileSystemAuditRemoteService;
import org.opencloudengine.flamingo2.model.rest.AuditType;
import org.opencloudengine.flamingo2.model.rest.FileSystemType;
import org.opencloudengine.flamingo2.model.rest.FileType;
import org.opencloudengine.flamingo2.model.rest.RequestType;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HiveMetastoreServiceImpl implements HiveMetastoreService {

    @Autowired
    private FileSystemAuditRemoteService fileSystemAuditRemoteService;

    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private Logger exceptionLogger = LoggerFactory.getLogger("flamingo.exception");

    @Override
    public List<Map> getAllDatabases(EngineConfig engineConfig) throws TException {
        try {
            HiveMetaStoreClient client = getMetaStoreClient(engineConfig);

            List<Map> returnList = new ArrayList<>();
            List<String> dbList = client.getAllDatabases();

            for (String db : dbList) {
                Map dbMap = new HashMap();
                dbMap.put("database", db);
                returnList.add(dbMap);
            }
            return returnList;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public List<Map> getTables(EngineConfig engineConfig, String dbname) throws TException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);

        List<Map> returnList = new ArrayList<>();
        List<String> tableList = client.getAllTables(dbname);

        for (String tablename : tableList) {
            Map tableMap = new HashMap();
            Table table = client.getTable(dbname, tablename);

            tableMap.put("tableName", table.getTableName());
            tableMap.put("createTime", table.getCreateTime());
            tableMap.put("owner", table.getOwner());
            tableMap.put("lastAccessTime", table.getLastAccessTime());
            tableMap.put("retention", table.getRetention());
            tableMap.put("viewOriginalText", table.getViewOriginalText());
            tableMap.put("viewExpandedText", table.getViewExpandedText());
            tableMap.put("tableType", table.getTableType());

            returnList.add(tableMap);
        }

        return returnList;
    }

    @Override
    public List<Map> getColumns(EngineConfig engineConfig, String dbname, String tablename) throws TException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);
        Table table = client.getTable(dbname, tablename);
        StorageDescriptor storageDesc = table.getSd();
        List<Map> returnList = new ArrayList<>();
        List<FieldSchema> fieldSchemas = storageDesc.getCols();
        for (FieldSchema fieldSchema : fieldSchemas) {
            Map<String, String> map = new HashMap<>();

            map.put("name", fieldSchema.getName());
            map.put("type", fieldSchema.getType());
            map.put("comment", fieldSchema.getComment());

            returnList.add(map);
        }
        return returnList;
    }

    @Override
    public List<Map> getPartitions(EngineConfig engineConfig, String dbname, String tablename) throws TException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);
        Table table = client.getTable(dbname, tablename);
        List<Map> returnList = new ArrayList<>();
        List<FieldSchema> fieldSchemas = table.getPartitionKeys();

        for (FieldSchema fieldSchema : fieldSchemas) {
            Map<String, String> map = new HashMap<>();

            map.put("name", fieldSchema.getName());
            map.put("type", fieldSchema.getType());
            map.put("comment", fieldSchema.getComment());

            returnList.add(map);
        }
        return returnList;
    }

    @Override
    public void createDatabase(EngineConfig engineConfig, Map params) throws TException, IOException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);

        Database database = new Database();

        database.setName(params.get("database").toString());
        database.setDescription(params.get("comment").toString());

        List<Object> propList = objectMapper.readValue(params.get("properties").toString(), List.class);
        Map paramMap = new HashMap();

        String location = null;
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        String username = (String) params.get("username");

        for (Object prop : propList) {
            Map propMap = (Map) prop;

            paramMap.put(propMap.get("key"), propMap.get("value"));
        }

        database.setParameters(paramMap);

        if (Boolean.valueOf(params.get("external").toString())) {
            location = params.get("location").toString();
            database.setLocationUri(location);
        } else {
            location = "/user/" + SessionUtils.getUsername();
        }

        client.createDatabase(database);

        fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.HIVE_DB, FileType.DIRECTORY, RequestType.UI, location, "", 0);
    }

    @Override
    public void dropDatabase(EngineConfig engineConfig, String dbname) throws TException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);

        client.dropDatabase(dbname);
    }

    @Override
    public void createTable(EngineConfig engineConfig, Map params) throws IOException, TException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);

        Table tbl = new Table();

        //Column Setting
        StorageDescriptor storageDesc = new StorageDescriptor();
        List<FieldSchema> columns = new ArrayList<>();
        ArrayList columnList = objectMapper.readValue(params.get("columns").toString(), ArrayList.class);
        String engineId = engineConfig.getId();
        String engineName = engineConfig.getName();
        String username = (String) params.get("username");

        for (Object column : columnList) {
            Map col = (Map) column;
            FieldSchema fieldSchema = new FieldSchema();

            fieldSchema.setName(col.get("name").toString());
            String type = col.get("type").toString();

            switch (type) {
                case "struct":
                    ArrayList collections = objectMapper.readValue(col.get("collection").toString(), ArrayList.class);
                    String struct = "";

                    for (Object collection : collections) {
                        Map<String, String> collMap = (Map<String, String>) collection;

                        struct += org.slf4j.helpers.MessageFormatter.arrayFormat("{}:{},", new String[]{collMap.get("name").toString(), collMap.get("type").toString()}).getMessage();
                    }

                    struct = StringUtils.removeEnd(struct, ",");

                    fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("struct<{}>", new String[]{struct}).getMessage());
                    break;
                case "map": {
                    HashMap<String, String> collection = objectMapper.readValue(col.get("collection").toString(), HashMap.class);
                    String mapKey = collection.get("key").toString();
                    String mapValue = collection.get("value").toString();
                    fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("map<{},{}>", new String[]{mapKey, mapValue}).getMessage());
                    break;
                }
                case "array": {
                    HashMap<String, String> collection = objectMapper.readValue(col.get("collection").toString(), HashMap.class);
                    String arrayKey = collection.get("key").toString();
                    fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("array<{}>", new String[]{arrayKey}).getMessage());
                    break;
                }
                default:
                    fieldSchema.setType(col.get("type").toString());
                    break;
            }

            if (!col.get("comment").toString().isEmpty()) {
                fieldSchema.setComment(col.get("comment").toString());
            }


            columns.add(fieldSchema);
        }

        storageDesc.setCols(columns);

        //Input/Output Format Setting
        storageDesc.setInputFormat(params.get("inputFormat").toString());
        storageDesc.setOutputFormat(params.get("outputFormat").toString());

        tbl.setDbName(params.get("database").toString());
        tbl.setTableName(params.get("table").toString());
        tbl.setTableType(params.get("tableType").toString());

        if (params.get("tableType").toString().equals("EXTERNAL_TABLE")) {
            String location = params.get("location").toString();
            fileSystemAuditRemoteService.log(engineId, engineName, username, FileSystemType.HDFS, AuditType.HIVE_TABLE, FileType.DIRECTORY, RequestType.UI, location, "", 0);
            Map extMap = new HashMap();
            extMap.put("EXTERNAL", "TRUE");
            tbl.setParameters(extMap);

            String HdfsUrl = "hdfs://" + engineConfig.getNnAddress() + ":" + engineConfig.getNnPort();
            storageDesc.setLocation(HdfsUrl + location);
        }

        //Partition Setting
        List<FieldSchema> partitions = new ArrayList<>();
        ArrayList partitionList = objectMapper.readValue(params.get("partitions").toString(), ArrayList.class);

        if (partitionList.size() > 0) {
            for (Object partition : partitionList) {
                Map par = (Map) partition;

                FieldSchema fieldSchema = new FieldSchema();

                fieldSchema.setName(par.get("name").toString());
                String type = par.get("type").toString();

                switch (type) {
                    case "struct":
                        ArrayList collections = objectMapper.readValue(par.get("collection").toString(), ArrayList.class);
                        String struct = "";

                        for (Object collection : collections) {
                            Map<String, String> collMap = (Map<String, String>) collection;

                            struct += org.slf4j.helpers.MessageFormatter.arrayFormat("{}:{},", new String[]{collMap.get("name").toString(), collMap.get("type").toString()}).getMessage();
                        }

                        struct = StringUtils.removeEnd(struct, ",");

                        fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("struct<{}>", new String[]{struct}).getMessage());
                        break;
                    case "map": {
                        HashMap<String, String> collection = objectMapper.readValue(par.get("collection").toString(), HashMap.class);
                        String mapKey = collection.get("key").toString();
                        String mapValue = collection.get("value").toString();
                        fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("map<{},{}>", new String[]{mapKey, mapValue}).getMessage());
                        break;
                    }
                    case "array": {
                        HashMap<String, String> collection = objectMapper.readValue(par.get("collection").toString(), HashMap.class);
                        String arrayKey = collection.get("key").toString();
                        fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("array<{}>", new String[]{arrayKey}).getMessage());
                        break;
                    }
                    default:
                        fieldSchema.setType(par.get("type").toString());
                        break;
                }
                if (!par.get("comment").toString().isEmpty()) {
                    fieldSchema.setComment(par.get("comment").toString());
                }

                partitions.add(fieldSchema);
            }

            tbl.setPartitionKeys(partitions);
        }

        Map delimiter = objectMapper.readValue(params.get("delimiter").toString(), Map.class);

        SerDeInfo serDeInfo = new SerDeInfo();
        if (params.get("serde") == null) {
            Map serdeParam = new HashMap();
            if (!delimiter.get("field").toString().isEmpty()) {
                serdeParam.put("field.delim", delimiter.get("field"));
            }
            if (!delimiter.get("collection").toString().isEmpty()) {
                serdeParam.put("colelction.delim", delimiter.get("collection"));
            }
            if (!delimiter.get("mapkeys").toString().isEmpty()) {
                serdeParam.put("mapkey.delim", delimiter.get("mapkeys"));
            }
            if (!delimiter.get("lines").toString().isEmpty()) {
                serdeParam.put("line.delim", delimiter.get("lines"));
            }

            serDeInfo.setParameters(serdeParam);
            serDeInfo.setSerializationLib("org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe");
        } else {
            serDeInfo.setSerializationLib(params.get("serde").toString());
        }

        storageDesc.setSerdeInfo(serDeInfo);
        tbl.setSd(storageDesc);

        //Set Table Paramater
        List<Object> propList = objectMapper.readValue(params.get("properties").toString(), List.class);
        Map paramMap = new HashMap();

        for (Object prop : propList) {
            Map propMap = (Map) prop;

            paramMap.put(propMap.get("key"), propMap.get("value"));
        }

        tbl.setParameters(paramMap);

        client.createTable(tbl);
    }

    @Override
    public void dropTable(EngineConfig engineConfig, String dbname, String tablename) throws TException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);
        client.dropTable(dbname, tablename);

    }

    @Override
    public Map getTableInfo(EngineConfig engineConfig, String dbname, String tablename) throws TException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);
        Map tableMap = new HashMap();

        Table table = client.getTable(dbname, tablename);
        StorageDescriptor sd = table.getSd();
        SerDeInfo serDeInfo = sd.getSerdeInfo();

        tableMap.put("tableName", table.getTableName());
        tableMap.put("tableType", table.getTableType());
        tableMap.put("tableParameter", table.getParameters());
        tableMap.put("location", sd.getLocation());
        tableMap.put("inputFormat", sd.getInputFormat());
        tableMap.put("outputFormat", sd.getOutputFormat());
        tableMap.put("serializationLib", serDeInfo.getSerializationLib());

        Map serDeParam = serDeInfo.getParameters();

        Object[] keyArray = serDeParam.keySet().toArray();
        for (Object keyObject : keyArray) {
            String key = keyObject.toString();

            //TODO 유니코드 또는 Octal을 사용해서 Delimiter 표현
            tableMap.put(key, String.format("0x%04X%n", (int) serDeParam.get(key).toString().charAt(0)));
        }

        return tableMap;
    }

    @Override
    public void alterTable(EngineConfig engineConfig, Map params) throws TException, IOException {
        HiveMetaStoreClient client = getMetaStoreClient(engineConfig);

        Map orgTable = (Map) params.get("orgTable");

        Table tbl = client.getTable(params.get("database").toString(), orgTable.get("tableName").toString());

        tbl.setTableName(params.get("table").toString());
        tbl.setTableType(params.get("tableType").toString());
        //Column Setting
        StorageDescriptor storageDesc = tbl.getSd(); //new StorageDescriptor();
        List<FieldSchema> columns = new ArrayList<>();
        ArrayList columnList = objectMapper.readValue(params.get("columns").toString(), ArrayList.class);

        for (Object column : columnList) {
            Map col = (Map) column;
            FieldSchema fieldSchema = new FieldSchema();

            fieldSchema.setName(col.get("name").toString());
            String type = col.get("type").toString();

            switch (type) {
                case "struct":
                    ArrayList collections = objectMapper.readValue(col.get("collection").toString(), ArrayList.class);
                    String struct = "";

                    for (Object collection : collections) {
                        Map<String, String> collMap = (Map<String, String>) collection;

                        struct += org.slf4j.helpers.MessageFormatter.arrayFormat("{}:{},", new String[]{collMap.get("name").toString(), collMap.get("type").toString()}).getMessage();
                    }

                    struct = StringUtils.removeEnd(struct, ",");

                    fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("struct<{}>", new String[]{struct}).getMessage());
                    break;
                case "map": {
                    HashMap<String, String> collection = objectMapper.readValue(col.get("collection").toString(), HashMap.class);
                    String mapKey = collection.get("key").toString();
                    String mapValue = collection.get("value").toString();
                    fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("map<{},{}>", new String[]{mapKey, mapValue}).getMessage());
                    break;
                }
                case "array": {
                    HashMap<String, String> collection = objectMapper.readValue(col.get("collection").toString(), HashMap.class);
                    String arrayKey = collection.get("key").toString();
                    fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("array<{}>", new String[]{arrayKey}).getMessage());
                    break;
                }
                default:
                    fieldSchema.setType(col.get("type").toString());
                    break;
            }

            if (!col.get("comment").toString().isEmpty()) {
                fieldSchema.setComment(col.get("comment").toString());
            }


            columns.add(fieldSchema);
        }

        storageDesc.setCols(columns);

        //Input/Output Format Setting
        storageDesc.setInputFormat(params.get("inputFormat").toString());
        storageDesc.setOutputFormat(params.get("outputFormat").toString());

        //External Table Setting
        if (tbl.getTableType().equals("EXTERNAL_TABLE")) {
            Map extMap = new HashMap();
            extMap.put("EXTERNAL", "TRUE");
            tbl.setParameters(extMap);

            storageDesc.setLocation(params.get("location").toString());
        }

        //Partition Setting
        List<FieldSchema> partitions = new ArrayList<>();
        ArrayList partitionList = objectMapper.readValue(params.get("partitions").toString(), ArrayList.class);

        if (partitionList.size() > 0) {
            for (Object partition : partitionList) {
                Map par = (Map) partition;

                FieldSchema fieldSchema = new FieldSchema();

                fieldSchema.setName(par.get("name").toString());
                String type = par.get("type").toString();

                switch (type) {
                    case "struct":
                        ArrayList collections = objectMapper.readValue(par.get("collection").toString(), ArrayList.class);
                        String struct = "";

                        for (Object collection : collections) {
                            Map<String, String> collMap = (Map<String, String>) collection;

                            struct += org.slf4j.helpers.MessageFormatter.arrayFormat("{}:{},", new String[]{collMap.get("name").toString(), collMap.get("type").toString()}).getMessage();
                        }

                        struct = StringUtils.removeEnd(struct, ",");

                        fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("struct<{}>", new String[]{struct}).getMessage());
                        break;
                    case "map": {
                        HashMap<String, String> collection = objectMapper.readValue(par.get("collection").toString(), HashMap.class);
                        String mapKey = collection.get("key").toString();
                        String mapValue = collection.get("value").toString();
                        fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("map<{},{}>", new String[]{mapKey, mapValue}).getMessage());
                        break;
                    }
                    case "array": {
                        HashMap<String, String> collection = objectMapper.readValue(par.get("collection").toString(), HashMap.class);
                        String arrayKey = collection.get("key").toString();
                        fieldSchema.setType(org.slf4j.helpers.MessageFormatter.arrayFormat("array<{}>", new String[]{arrayKey}).getMessage());
                        break;
                    }
                    default:
                        fieldSchema.setType(par.get("type").toString());
                        break;
                }
                if (!par.get("comment").toString().isEmpty()) {
                    fieldSchema.setComment(par.get("comment").toString());
                }

                partitions.add(fieldSchema);
            }

            tbl.setPartitionKeys(partitions);
        }

        Map delimiter = objectMapper.readValue(params.get("delimiter").toString(), Map.class);

        SerDeInfo serDeInfo = storageDesc.getSerdeInfo(); //new SerDeInfo();
        if (params.get("serde") == null) {
            Map serdeParam = new HashMap();
            if (!delimiter.get("field").toString().isEmpty()) {
                serdeParam.put("field.delim", delimiter.get("field"));
            }
            if (!delimiter.get("collection").toString().isEmpty()) {
                serdeParam.put("colelction.delim", delimiter.get("collection"));
            }
            if (!delimiter.get("mapkeys").toString().isEmpty()) {
                serdeParam.put("mapkey.delim", delimiter.get("mapkeys"));
            }
            if (!delimiter.get("lines").toString().isEmpty()) {
                serdeParam.put("line.delim", delimiter.get("lines"));
            }

            serDeInfo.setParameters(serdeParam);
            serDeInfo.setSerializationLib("org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe");
        } else {
            serDeInfo.setSerializationLib(params.get("serde").toString());
        }

        storageDesc.setSerdeInfo(serDeInfo);
        tbl.setSd(storageDesc);

        //Set Table Paramater
        List<Object> propList = objectMapper.readValue(params.get("properties").toString(), List.class);
        Map paramMap = new HashMap();

        for (Object prop : propList) {
            Map propMap = (Map) prop;

            paramMap.put(propMap.get("key"), propMap.get("value"));
        }

        tbl.setParameters(paramMap);

        client.alter_table(params.get("database").toString(), orgTable.get("tableName").toString(), tbl);
    }

    public HiveMetaStoreClient getMetaStoreClient(EngineConfig engineConfig) {
        try {
            HiveConf hiveConf = new HiveConf();
            String metastoreUri = MessageFormatter.arrayFormat("thrift://{}:{}/", new String[]{engineConfig.getHiveMetastoreAddress(), String.valueOf(engineConfig.hiveMetastorePort)}).getMessage();
            hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS, metastoreUri);

            return new HiveMetaStoreClient(hiveConf);
        } catch (MetaException e) {
            exceptionLogger.warn("Unable to create Hive Metastore Client.", e);
            return null;
        }
    }
}


