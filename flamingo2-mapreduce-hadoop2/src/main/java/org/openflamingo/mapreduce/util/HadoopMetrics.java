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
package org.openflamingo.mapreduce.util;


import java.util.Hashtable;
import java.util.Map;

/**
 * Hadoop MapReduce Metrics 리소스 번들.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class HadoopMetrics {

    /**
     * MapReduce Job 및 Hadoop이 관리하는 자체 Metrics 정보의 Key Value 리소스 번들.
     */
    public static Map<String, String> metrics = new Hashtable<String, String>();

    static {
        metrics.put("FileSystemCounters_FILE_BYTES_WRITTEN", "파일에 기록한 바이트");
        metrics.put("FileSystemCounters_HDFS_BYTES_WRITTEN", "HDFS에 기록한 바이트");
        metrics.put("FileSystemCounters_HDFS_BYTES_READ", "HDFS에서 읽은 바이트");
        metrics.put("FileSystemCounters_FILE_BYTES_READ", "파일에서 읽은 바이트");

        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_MAP_INPUT_BYTES", "Mapper의 입력 바이트수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_REDUCE_INPUT_GROUPS", "Reducer의 입력 그룹수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_MAP_OUTPUT_RECORDS", "Mapper의 출력 레코드수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_COMBINE_INPUT_RECORDS", "Combiner의 입력 레코드수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_MAP_INPUT_RECORDS", "Mapper의 입력 레코드수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_MAP_OUTPUT_BYTES", "Mapper의 출력 바이트수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_REDUCE_OUTPUT_RECORDS", "Reducer의 출력 레코드수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_COMBINE_OUTPUT_RECORDS", "Combiner의 출력 레코드수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.Task$Counter_REDUCE_INPUT_RECORDS", "Reducer의 입력 레코드수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.lib.MultipleOutputs_SUPPORT", "Support");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.lib.MultipleOutputs_PREFIX", "Prefix");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.lib.MultipleOutputs_MEASURE", "Measure");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.JobInProgress$Counter_TOTAL_LAUNCHED_REDUCES", "총 실행된 Reducer의 개수");
        metrics.put("org.apache.org.openflamingo.remote.thrift.mapred.JobInProgress$Counter_TOTAL_LAUNCHED_MAPS", "총 실행된 Mapper의 개수");
    }

    /**
     * 지정한 Metric 식별자 Key의 명칭을 반환한다.
     *
     * @param key Metric 식별자 Key
     * @return Metric의 명칭
     */
    public static String getMetricName(String key) {
        return metrics.get(key);
    }

}
