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

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper와 Reducer에서 지정한 필터링 조건에 맞는 필터링을 수행하기 위해서
 * 필요한 필터를 등록하여 관리하는 레지스트리.
 * 신규 필터를 구성하는 경우 반드시 이 레지스트리에 등록해야 한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class FilterRegistry {

    /**
     * 필터의 이름과 필터의 구현체를 매핑하는 필터맵.
     */
    private static Map<String, Filter> filterMap = new HashMap<String, Filter>();

    static {
        filterMap.put("EMPTY", new EmptyColumnFilter());
        filterMap.put("NEMPTY", new NotEmptyColumnFilter());
        filterMap.put("EQSTR", new EqualStringFilter());
        filterMap.put("NEQSTR", new NotEqualStringFilter());
        filterMap.put("EQNUM", new EqualNumberFilter());
        filterMap.put("NEQNUM", new NotEqualNumberFilter());
        filterMap.put("LT", new LessThanFilter());
        filterMap.put("LTE", new LessThanEqualFilter());
        filterMap.put("GT", new GreaterThanFilter());
        filterMap.put("GTE", new GreaterThanEqualFilter());
        filterMap.put("STARTWITH", new StartWithFilter());
        filterMap.put("ENDWITH", new EndWithFilter());
    }

    /**
     * 지정한 이름읠 필터를 반환한다.
     *
     * @param name 필터명
     * @return 필터 구현체
     */
    public static Filter getFilter(String name) {
        return filterMap.get(name.toUpperCase());
    }
}
