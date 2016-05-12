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
 * Audit Log Type Enumeration.
 *
 * @author Byoung Gon, Kim
 * @author Myeongha KIM
 * @since 0.3
 */
public enum AuditType implements Serializable {

    /**
     * 디렉토리 또는 파일 생성
     */
    CREATE("create"),

    /**
     * 디렉토리 또는 파일 삭제
     */
    DELETE("delete"),

    /**
     * 디렉토리 또는 파일 이동
     */
    MOVE("move"),

    /**
     * 디렉토리 또는 파일의 이름 변경
     */
    RENAME("rename"),

    /**
     * 디렉토리 또는 파일의 복사
     */
    COPY("copy"),

    /**
     * 디렉토리 또는 파일의 정보 변경
     */
    MODIFY("modify"),

    /**
     * 디렉토리 또는 파일의 정보 확인
     */
    PROPERTY("property"),

    /**
     * 디렉토리 또는 파일의 권한 변경
     */
    PERMISSION("permission"),

    /**
     * 파일 저장
     */
    WRITE("write"),

    /**
     * 파일 다운로드
     */
    DOWNLOAD("download"),

    /**
     * 파일 업로드
     */
    UPLOAD("upload"),

    /**
     * 파일 내용 보기
     * */
    VIEW("view"),

    /**
     * 파일 병합
     */
    MERGE("merge"),

    /**
     * Hive Database 생성
     */
    HIVE_DB("HiveDB"),

    /**
     * Hive Table 생성
     */
    HIVE_TABLE("HiveTable"),

    /**
     * 시각화
     */
    VISUALIZATION("visualization");

    /**
     * 문자열 값
     */
    public final String value;

    /**
     * 기본 생성자.
     *
     * @param value Enumeration 문자열 값
     */
    AuditType(String value) {
        this.value = value;
    }
}