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
 * Language Domain Object.
 *
 * @author Seungpil PARK
 * @since 2.0
 */
public class Language {

    private String id;

    private String ko_KR;

    private String en_US;

    private String ja_JP;

    private String zh_CN;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKo_KR() {
        return ko_KR;
    }

    public void setKo_KR(String ko_KR) {
        this.ko_KR = ko_KR;
    }

    public String getEn_US() {
        return en_US;
    }

    public void setEn_US(String en_US) {
        this.en_US = en_US;
    }

    public String getJa_JP() {
        return ja_JP;
    }

    public void setJa_JP(String ja_JP) {
        this.ja_JP = ja_JP;
    }

    public String getZh_CN() {
        return zh_CN;
    }

    public void setZh_CN(String zh_CN) {
        this.zh_CN = zh_CN;
    }

    @Override
    public String toString() {
        return "Language{" +
                "id='" + id + '\'' +
                ", ko_KR='" + ko_KR + '\'' +
                ", en_US='" + en_US + '\'' +
                ", ja_JP='" + ja_JP + '\'' +
                ", zh_CN='" + zh_CN + '\'' +
                '}';
    }
}
