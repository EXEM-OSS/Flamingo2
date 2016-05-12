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

import java.util.*;

/**
 * 배열 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class ArrayUtils {

    /**
     * 배열이 비어있는지 확인한다. 배열은 <tt>null</tt> 이거나 길이가 0인 경우에 비어있다고 판단한다.
     *
     * @param array 확인할 배열
     * @return 비어있다면 <tt>true</tt>
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * 문자열 배열을 정수 배열로 변환한다. 단, 문자열 배열은 반드시 정수로 구성되어 있어야 하며 <tt>null</tt>인 경우
     * 빈 정수 배열을 반환한다.
     *
     * @param values 문자열 배열
     * @return 정수 배열
     */
    public static Integer[] toIntegerArray(String... values) {
        if (values == null) {
            return new Integer[0];
        }
        Integer[] casted = new Integer[values.length];
        for (int i = 0; i < casted.length; i++) {
            casted[i] = Integer.parseInt(values[i]);
        }
        return casted;
    }

    /**
     * 문자열 배열을 정렬한다.
     *
     * @param array 정렬할 배열
     * @return 정렬한 배열(절대로 <tt>null</tt>이 되지는 않는다)
     */
    public static String[] sortStringArray(String[] array) {
        if (isEmpty(array)) {
            return new String[0];
        }
        Arrays.sort(array);
        return array;
    }

    /**
     * 문자열 배열을 큰건을 먼저 위로 정렬한다.
     *
     * @param array 정렬할 배열
     * @return 정렬한 배열(절대로 <tt>null</tt>이 되지는 않는다)
     */
    public static Integer[] sortReverseIntegerArray(Integer[] array) {
        if (isEmpty(array)) {
            return new Integer[0];
        }
        Arrays.sort(array, Collections.reverseOrder());
        return array;
    }

    /**
     * 문자열 배열을 natural order로 정렬한다.
     *
     * @param array 정렬할 배열
     * @return 정렬한 배열(절대로 <tt>null</tt>이 되지는 않는다)
     */
    public static Integer[] sortIntegerArray(Integer[] array) {
        if (isEmpty(array)) {
            return new Integer[0];
        }
        Arrays.sort(array);
        return array;
    }

    /**
     * 문자열로 구성된 Collection을 문자열 배열로 변환한다.
     *
     * @param collection 문자열로 구성된 Collection
     * @return Collection이 <tt>null</tt>인 경우 <tt>null</tt>, 그렇지 않으면 문자열 배열
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * 문자열로 구성된 Enumeration을 문자열 배열로 변환한다.
     *
     * @param enumeration 문자열로 구성된 Enumeration
     * @return Enumeration이 <tt>null</tt>인 경우 <tt>null</tt>, 그렇지 않으면 문자열 배열
     */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration == null) {
            return null;
        }
        List<String> list = Collections.list(enumeration);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 지정한 문자열 배열의 모든 엘리먼트에 대해서 trim 처리한다.
     *
     * @param array 문자열 배열
     * @return trim 처리한 문자열 배열.
     */
    public static String[] trimArrayElements(String[] array) {
        if (isEmpty(array)) {
            return new String[0];
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            result[i] = (element != null ? element.trim() : null);
        }
        return result;
    }

    /**
     * 지정한 문자열 배열에서 중복된 문자열을 제거하고 문자열을 정렬한다.
     * 정렬이 되는 이유는 내부적으로 {@link java.util.TreeSet}을 이용하기 때문이다.
     *
     * @param array 문자열 배열
     * @return 중복이 없는 문자열 배열. 결과는 natural order로 졍렬된다.
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (isEmpty(array)) {
            return array;
        }
        Set<String> set = new TreeSet<String>();
        for (String element : array) {
            set.add(element);
        }
        return toStringArray(set);
    }

    /**
     * 배열을 구분자를 포함한 단일 문자열로 변환한다.
     *
     * @param arr       배열
     * @param delimiter 구분자
     * @return 구분자를 포함한 단일 문자열
     */
    public static String arrayToDelimitedString(Object[] arr, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < arr.length; index++) {
            if (index > 0) {
                builder.append(delimiter);
            }
            builder.append(arr[index]);
        }
        return builder.toString();
    }

    /**
     * 배열을 콤마를 포함한 단일 문자열로 변환한다.
     *
     * @param arr 배열
     * @return 콤마를 포함한 단일 문자열
     */
    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

    /**
     * 문자열 배열을 문자열 List로 변환한다.
     *
     * @param arr 문자열 배열
     * @return 문자열 List
     */
    public static List<String> stringArrayToCollection(String[] arr) {
        ArrayList<String> list = new ArrayList<String>(arr.length);
        list.addAll(Arrays.asList(arr));
        return list;
    }

}
