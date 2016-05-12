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
 * Hadoop MapReduce Job에서 사용하는 각종 상수를 정의한 상수 클래스.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class Constants {

    /**
     * Default Delimiter
     */
    public static final String DEFAULT_DELIMITER = Delimiter.COMMA.getDelimiter();

    /**
     * Hadoop Job Fail
     */
    public static final int JOB_FAIL = -1;

    /**
     * Hadoop Job Success
     */
    public static final int JOB_SUCCESS = 0;

    /**
     * Total Row Count Counter Name
     */
    public static final String TOTAL_ROW_COUNT = "Total Row Count";

    /**
     * HDFS 임시 디렉토리 위치의 Key 값
     */
    public static final String TEMP_DIR = "tempDir";

    /**
     * YES
     */
    public static final String YES = "yes";

    /**
     * NO
     */
    public static final String NO = "no";
}
