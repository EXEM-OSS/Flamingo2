/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.flamingo2.model.rest;

/**
 * Description.
 *
 * @author Hyokun Park
 * @since 0.2
 */
public class HiveMeta {
    private String database;
    private String table;
    private String createTime;
    private String tableName;
    private String tableType;
    private String location;
    private String serdeLibrary;
    private String isCompressed;
    private String isStoredAsSubDir;
    private String inputFormat;
    private String outputFormat;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSerdeLibrary() {
        return serdeLibrary;
    }

    public void setSerdeLibrary(String serdeLibrary) {
        this.serdeLibrary = serdeLibrary;
    }

    public String getIsCompressed() {
        return isCompressed;
    }

    public void setIsCompressed(String isCompressed) {
        this.isCompressed = isCompressed;
    }

    public String getIsStoredAsSubDir() {
        return isStoredAsSubDir;
    }

    public void setIsStoredAsSubDir(String isStoredAsSubDir) {
        this.isStoredAsSubDir = isStoredAsSubDir;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    @Override
    public String toString() {
        return "HiveMeta{" +
                "database='" + database + '\'' +
                ", table='" + table + '\'' +
                ", createTime='" + createTime + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableType='" + tableType + '\'' +
                ", location='" + location + '\'' +
                ", serdeLibrary='" + serdeLibrary + '\'' +
                ", isCompressed='" + isCompressed + '\'' +
                ", isStoredAsSubDir='" + isStoredAsSubDir + '\'' +
                ", inputFormat='" + inputFormat + '\'' +
                ", outputFormat='" + outputFormat + '\'' +
                '}';
    }
}
