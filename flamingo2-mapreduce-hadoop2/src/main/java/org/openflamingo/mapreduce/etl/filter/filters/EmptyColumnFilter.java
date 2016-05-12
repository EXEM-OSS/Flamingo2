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

import org.openflamingo.mapreduce.util.StringUtils;

/**
 * 지정한 컬럼이 비어있는지 확인하는 필터.
 * 필터의 값이 일반적으로 문자열이므로 문자열이 비어있거나, 공백이거나, 실제 길이가 0인 경우 비어있다고 판단한다.
 * 이 필터는 target 값을 판단하지 않으며 오직 source 값만 확인한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class EmptyColumnFilter extends FilterSupport {

    public boolean doFilter(Object source, Object target, String type) {
        return StringUtils.isEmpty((String) source);
    }
}
