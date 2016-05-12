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
package org.openflamingo.mapreduce.etl.filter.filters;

/**
 * Filter ETL의 필터링 조건을 만족하는지 판단하는 필터 인터페이스.
 * 다수의 필터링 조건을 구현하기 위해서 이 인터페이스를 구현하고
 * Filter Registry에 등록해야 한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public interface Filter {

    /**
     * 필터링을 할 것인지 결정한다.
     *
     * @param source 원본 소스 객체
     * @param target 비교 대상 객체
     * @param type   자료형
     * @return 필터링시 <tt>true</tt>
     */
    boolean doFilter(Object source, Object target, String type);

}
