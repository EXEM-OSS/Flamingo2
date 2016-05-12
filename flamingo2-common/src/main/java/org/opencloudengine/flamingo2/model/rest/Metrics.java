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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "id",
        "create",
        "engineId",
        "hadoopId",
        "hiveId",
        "type",
        "interval",
        "value1",
        "value2",
        "value3",
        "value4",
        "value5",
        "value6",
        "value7",
        "value8",
        "value9",
        "value10",
        "value11",
        "value12"
})
@XmlRootElement(name = "metrics")
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.ANY,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.ANY
)
public class Metrics implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private String id;

    private Date create;

    private long engineId;

    private long hadoopId;

    private long hiveId;

    private String type;

    private String interval;

    private String value1;

    private String value2;

    private String value3;

    private String value4;

    private long value5;

    private long value6;

    private long value7;

    private long value8;

    private long value9;

    private long value10;

    private long value11;

    private long value12;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Long getEngineId() {
        return engineId;
    }

    public void setEngineId(Long engineId) {
        this.engineId = engineId;
    }

    public Long getHadoopId() {
        return hadoopId;
    }

    public void setHadoopId(Long hadoopId) {
        this.hadoopId = hadoopId;
    }

    public Long getHiveId() {
        return hiveId;
    }

    public void setHiveId(Long hiveId) {
        this.hiveId = hiveId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public Long getValue5() {
        return value5;
    }

    public void setValue5(Long value5) {
        this.value5 = value5;
    }

    public Long getValue6() {
        return value6;
    }

    public void setValue6(Long value6) {
        this.value6 = value6;
    }

    public Long getValue7() {
        return value7;
    }

    public void setValue7(Long value7) {
        this.value7 = value7;
    }

    public Long getValue8() {
        return value8;
    }

    public void setValue8(Long value8) {
        this.value8 = value8;
    }

    public Long getValue9() {
        return value9;
    }

    public void setValue9(Long value9) {
        this.value9 = value9;
    }

    public Long getValue10() {
        return value10;
    }

    public void setValue10(Long value10) {
        this.value10 = value10;
    }

    public Long getValue11() {
        return value11;
    }

    public void setValue11(Long value11) {
        this.value11 = value11;
    }

    public Long getValue12() {
        return value12;
    }

    public void setValue12(Long value12) {
        this.value12 = value12;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("create", create)
                .append("engineId", engineId)
                .append("hadoopId", hadoopId)
                .append("hiveId", hiveId)
                .append("type", type)
                .append("interval", interval)
                .append("value1", value1)
                .append("value2", value2)
                .append("value3", value3)
                .append("value4", value4)
                .append("value5", value5)
                .append("value6", value6)
                .append("value7", value7)
                .append("value8", value8)
                .append("value9", value9)
                .append("value10", value10)
                .append("value11", value11)
                .append("value12", value12)
                .toString();
    }
}
