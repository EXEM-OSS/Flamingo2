/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.mapreduce.util;


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

        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_MAP_INPUT_BYTES", "Mapper의 입력 바이트수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_REDUCE_INPUT_GROUPS", "Reducer의 입력 그룹수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_MAP_OUTPUT_RECORDS", "Mapper의 출력 레코드수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_COMBINE_INPUT_RECORDS", "Combiner의 입력 레코드수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_MAP_INPUT_RECORDS", "Mapper의 입력 레코드수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_MAP_OUTPUT_BYTES", "Mapper의 출력 바이트수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_REDUCE_OUTPUT_RECORDS", "Reducer의 출력 레코드수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_COMBINE_OUTPUT_RECORDS", "Combiner의 출력 레코드수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.Task$Counter_REDUCE_INPUT_RECORDS", "Reducer의 입력 레코드수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.lib.MultipleOutputs_SUPPORT", "Support");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.lib.MultipleOutputs_PREFIX", "Prefix");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.lib.MultipleOutputs_MEASURE", "Measure");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.JobInProgress$Counter_TOTAL_LAUNCHED_REDUCES", "총 실행된 Reducer의 개수");
        metrics.put("org.apache.org.opencloudengine.flamingo2.remote.thrift.mapred.JobInProgress$Counter_TOTAL_LAUNCHED_MAPS", "총 실행된 Mapper의 개수");
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
