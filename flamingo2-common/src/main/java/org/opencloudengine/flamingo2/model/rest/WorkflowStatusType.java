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

/**
 * Workflow & Job Status Type Enumeration.
 *
 * @author Byoung Gon, Kim
 * @since 0.2
 */
public enum WorkflowStatusType implements Serializable {

    CREATED("created"), REGISTERED("registered"), DESTROYED("destroyed"), COPIED("copied");

    /**
     * 상태를 표현하는 문자열값
     */
    public final String value;

    /**
     * 기본 생성자.
     *
     * @param value 상태를 표현하는 문자열값
     */
    WorkflowStatusType(String value) {
        this.value = value;
    }
}