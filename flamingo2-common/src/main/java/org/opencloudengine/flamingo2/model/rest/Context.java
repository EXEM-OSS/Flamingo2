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

import java.io.Serializable;
import java.util.HashMap;

/**
 * File System 처리를 위한 각종 정보를 담고 있는 컨텍스트.
 *
 * @author Byoung Gon, Kim
 * @since 0.3
 */
public class Context implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    /**
     * Permission을 위한 User Key
     */
    public final static String AUTHORITY = "AUTHORITY";

    /**
     * Native File System을 구분하기 위한 Key
     */
    public final static String HADOOP_CLUSTER = "HADOOP_CLUSTER";

    /**
     * Workflow Engine Identifier
     */
    public final static String AGENT_ID = "AGENT";

    /**
     * Amazon Web Services S3
     */
    public final static String S3 = "S3";

    /**
     * Key Value를 저장하기 위한 Map
     */
    private HashMap map = new HashMap();

    /**
     * 지정한 Key에 대해서 값을 꺼내온다.
     *
     * @param key 값을 꺼내올 Key
     * @return 값
     */
    public String getString(String key) {
        return (String) map.get(key);
    }

    /**
     * 지정한 Key에 대해서 값을 꺼내온다.
     *
     * @param key 값을 꺼내올 Key
     * @return 값
     */
    public Object getObject(String key) {
        return map.get(key);
    }

    /**
     * 지정한 Key로 값을 추가한다.
     *
     * @param key   값을 넣을 Key
     * @param value 값
     */
    public void putString(String key, String value) {
        map.put(key, value);
    }

    /**
     * 지정한 Key로 값을 추가한다.
     *
     * @param key   값을 넣을 Key
     * @param value 값
     */
    public void putObject(String key, Object value) {
        map.put(key, value);
    }

}
