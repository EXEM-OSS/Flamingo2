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

import java.util.Iterator;

/**
 * 두 집합의 구성요소의 개수, 순서 및 내용이 동일한지 확인하는 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class ComparisonUtils {

    /**
     * 구성요소의 내용 및 순서와 길이가 동일한지 비교한다.
     *
     * @param <T>    비교한 구성요소의 유형.
     * @param first  비교할 첫번째 구성요소의 집합.
     * @param second 비교할 두번째 구성요소의 집합.
     * @return 동일하다면 <tt>true</tt>, 그렇지 않다면 <tt>false</tt>
     */
    public static <T> boolean equal(Iterable<T> first, Iterable<T> second) {
        return equal(first.iterator(), second.iterator());
    }

    /**
     * 구성요소의 내용 및 순서와 길이가 동일한지 비교한다.
     *
     * @param <T>    비교한 구성요소의 유형.
     * @param first  비교할 첫번째 구성요소의 집합.
     * @param second 비교할 두번째 구성요소의 집합.
     * @return 동일하다면 <tt>true</tt>, 그렇지 않다면 <tt>false</tt>
     */
    public static <T> boolean equal(Iterator<T> first, Iterator<T> second) {
        while (first.hasNext() && second.hasNext()) {
            T message = first.next();
            T otherMessage = second.next();  /* 구성요소도 같아야 한다 */
            if (!(message == null ? otherMessage == null :
                    message.equals(otherMessage))) {
                return false;
            }
        }
        /* 길이도 같아야 한다. */
        return !(first.hasNext() || second.hasNext());
    }
}
