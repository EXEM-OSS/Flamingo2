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
package org.openflamingo.mapreduce.type;

/**
 * 필터링시 해당 컬럼의 데이터 유형을 지정하기 위해서 사용하는 Enumeration.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public enum DataType {

    INTEGER("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    STRING("string");

    /**
     * 데이터 유형
     */
    private String dataType;

    /**
     * 데이터 유형을 설정한다.
     *
     * @param dataType 데이터 유형
     */
    DataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * 데이터 유형을 반환한다.
     *
     * @return 데이터 유형
     */
    public String getDataType() {
        return dataType;
    }
}