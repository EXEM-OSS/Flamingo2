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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request JAXB Object.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "map",
        "list"
})
@XmlRootElement(name = "request")
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.ANY,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.ANY
)
public class Request implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    /**
     * 파일 처리시 사용하는 파일 목록. 파일의 이동, 삭제, 복사 등등에서 활용한다.
     */
    private List<Object> list;

    /**
     * Key Value 형식의 파라미터 전달시 사용하는 요청 변수.
     */
    private Map<String, String> map;

    public List<Object> getList() {
        if (list == null) {
            list = new ArrayList<Object>();
        }
        return list;
    }

    public void setFile(List<Object> list) {
        this.list = list;
    }

    public Map<String, String> getMap() {
        if (map == null) {
            map = new HashMap<String, String>();
        }
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
