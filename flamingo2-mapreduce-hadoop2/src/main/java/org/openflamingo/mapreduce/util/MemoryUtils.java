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

/**
 * JVM의 Heap 정보를 처리하는 유틸맅.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class MemoryUtils {

    /**
     * JVM Runtime Heap 정보를 문자열로 반환한다.
     *
     * @return 문자열로 구성된 JVM Runtime Heap 정보.
     */
    public static String getRuntimeMemoryStats() {
        return "totalMem = " +
                (Runtime.getRuntime().totalMemory() / 1024f / 1024f) +
                "M, maxMem = " +
                (Runtime.getRuntime().maxMemory() / 1024f / 1024f) +
                "M, freeMem = " +
                (Runtime.getRuntime().freeMemory() / 1024f / 1024f) + "M";
    }

}
