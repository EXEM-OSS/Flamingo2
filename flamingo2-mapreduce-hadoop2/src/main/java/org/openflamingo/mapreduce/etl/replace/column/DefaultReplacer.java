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
package org.openflamingo.mapreduce.etl.replace.column;

/**
 * Description.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 1.0
 */
public class DefaultReplacer implements Replacer {

    /**
     * 변환할 문자열
     */
    private String to;

    /**
     * 기본 생성자.
     *
     * @param to 변환할 문자열
     */
    public DefaultReplacer(String to) {
        this.to = to;
    }

    /**
     * 기본 생성자.
     */
    public DefaultReplacer() {
    }

    @Override
    public String replace(String from) {
        return to;
    }

    /**
     * 변환할 문자열을 설정한다.
     *
     * @param to 변환할 문자열
     */
    @Override
    public void setTo(String to) {
        this.to = to;
    }
}
