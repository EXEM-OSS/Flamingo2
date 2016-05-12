/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.core;

/**
 * 범용적으로 사용하는 구분자를 표현하는 Enumeration.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public enum Delimiter {

    SPACE("\u0020"),
    TAB("\u0009"),
    PIPE("|"),
    DOUBLE_PIPE("||"),
    COMMA("\u002c"),
    PERIOD("."),
    SEMICOLON(";"),
    COLON(":"),
    ASTERISK("*"),
    HYPEN("-"),
    TILDE("~"),
    CROSSHATCH("#"),
    EXCLAMATION_MARK("!"),
    DOLLAR("$"),
    AMPERSAND("&"),
    PERCENT("%"),
    QUOTATION_MARK("\""),
    CIRCUMFLEX("^");

    /**
     * 컬럼을 구분하는 delimiter
     */
    private String delimiter;

    /**
     * Delimiter를 설정한다.
     *
     * @param delimiter delimiter
     */
    Delimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Delimiter를 반환한다.
     *
     * @return delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }
}
