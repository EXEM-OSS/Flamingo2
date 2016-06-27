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

import org.apache.hadoop.mapreduce.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Hadoop MapReduce Counter 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class CounterUtils {

    /**
     * Mapper의 Counter를 기록한다.
     *
     * @param mapper  Mapper
     * @param name    Counter Name
     * @param context Mapper의 Context
     */
    public static void writerMapperCounter(Mapper mapper, String name, Mapper.Context context) {
        context.getCounter(mapper.getClass().getName(), name).increment(1);
    }

    /**
     * Mapper의 Counter를 기록한다.
     *
     * @param group   Counter Group
     * @param name    Counter Name
     * @param context Mapper의 Context
     */
    public static void writerMapperCounter(String group, String name, Mapper.Context context) {
        context.getCounter(group, name).increment(1);
    }

    /**
     * Reducer의 Counter를 기록한다.
     *
     * @param reducer Reducer
     * @param name    Counter Name
     * @param context Reducer의 Context
     */
    public static void writerReducerCounter(Reducer reducer, String name, Reducer.Context context) {
        context.getCounter(reducer.getClass().getName(), name).increment(1);
    }

    /**
     * Reducer의 Counter를 기록한다.
     *
     * @param group   Counter Group
     * @param name    Counter Name
     * @param context Reducer의 Context
     */
    public static void writerReducerCounter(String group, String name, Reducer.Context context) {
        context.getCounter(group, name).increment(1);
    }

    /**
     * 문자열이 비어있거나 NULL인지 확인한다.
     * <p/>
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str 확인할 문자열
     * @return 문자열이 비어있거나 NULL인 경우 <tt>true</tt>
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 문자열이 공백이 아니거나 또는 비어있지 않거나 NULL이 아닌지 확인한다.
     * <p/>
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str 확인할 문자열
     * @return 문자열이 공백이 아니거나 또는 비어있지 않거나 NULL이 아닌경우 <tt>true</tt>
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 문자열이 공백 또는 비어있거나 NULL인지 확인한다.
     * <p/>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str 확인할 문자열
     * @return 문자열이 공백 또는 비어있거나 NULL인 경우 <tt>true</tt>
     */
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (char character : str.toCharArray()) {
            if ((Character.isWhitespace(character) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Job에 정의되어 있는 모든 Counter를 Map 형식으로 변환한다.
     * Key값은 <tt>GROUP_COUNTER</tt> 형식으로 예를 들면 Group Name이
     * <tt>CLEAN</tt>이고, Counter가 <tt>VALID</tt>라면 실제 Key는
     * <tt>CLEAN_VALID</tt>가 된다.
     *
     * @param job Hadoop Job
     * @return Counter와 값을 포함하는 Map
     */
    public static Map<String, String> getCounters(Job job) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            Counters counters = job.getCounters();
            Iterable<String> groupNames = counters.getGroupNames();
            for (String groupName : groupNames) {
                CounterGroup group = counters.getGroup(groupName);
                for (Counter counter : group) {
                    String realName = HadoopMetrics.getMetricName(group.getName() + "_" + counter.getName());
                    if (!isEmpty(realName)) {
                        resultMap.put(realName, String.valueOf(counter.getValue()));
                    }
                }
            }
        } catch (Exception ex) {
        }
        return resultMap;
    }
}
