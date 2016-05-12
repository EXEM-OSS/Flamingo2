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
package org.openflamingo.mapreduce.etl.generate;

/**
 * Generate ETL에서 사용하는 유형을 표현하는 Enumeration.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public enum GenerateType {

    SEQUENCE("SEQUENCE"),
    TIMESTAMP("TIMESTAMP");

    /**
     * Generate Type
     */
    private String type;

    /**
     * Generate Type을 설정한다.
     *
     * @param type Generate Type
     */
    GenerateType(String type) {
        this.type = type;
    }

    /**
     * Generate Type을 반환한다.
     *
     * @return type Generate Type
     */
    public String getType() {
        return type;
    }
}
