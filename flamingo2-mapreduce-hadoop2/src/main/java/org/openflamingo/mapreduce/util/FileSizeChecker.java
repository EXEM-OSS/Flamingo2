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

import java.io.File;

/**
 * 로컬 파일 시스템의 지정한 파일의 크기를 확인하는 유틸리티 클래스.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class FileSizeChecker {

    /**
     * 1K Bytes
     */
    public static long ONE_KILO_BYTES = 1 * 1024;

    /**
     * 10K Bytes
     */
    public static long TEN_HUNDRED_KILO_BYTES = 10 * 1024;

    /**
     * 100K Bytes
     */
    public static long ONE_HUNDRED_KILO_BYTES = 100 * 1024;

    /**
     * 1M Bytes
     */
    public static long ONE_MEGA_BYTES = 1 * 1024 * 1024;

    /**
     * 10M Bytes
     */
    public static long TEN_MEGA_BYTES = 10 * 1024 * 1024;

    /**
     * 100M Bytes
     */
    public static long ONE_HUNDRED_MEGA_BYTES = 100 * 1024 * 1024;

    /**
     * 지정한 경로의 파일의 1K 바이트보다 작은지 확인한다.
     *
     * @param path 로컬 파일의 절대 경로
     * @return 1M 보다 큰 경우 <tt>false</tt>, 작은 경우 <tt>true</tt>
     */
    public static boolean lessThan1KBytes(String path) {
        return lessThanSpecificSize(ONE_HUNDRED_KILO_BYTES, path);
    }

    /**
     * 지정한 경로의 파일의 10K 바이트보다 작은지 확인한다.
     *
     * @param path 로컬 파일의 절대 경로
     * @return 1M 보다 큰 경우 <tt>false</tt>, 작은 경우 <tt>true</tt>
     */
    public static boolean lessThan10KBytes(String path) {
        return lessThanSpecificSize(ONE_HUNDRED_KILO_BYTES, path);
    }

    /**
     * 지정한 경로의 파일의 100K 바이트보다 작은지 확인한다.
     *
     * @param path 로컬 파일의 절대 경로
     * @return 1M 보다 큰 경우 <tt>false</tt>, 작은 경우 <tt>true</tt>
     */
    public static boolean lessThan100KBytes(String path) {
        return lessThanSpecificSize(ONE_HUNDRED_KILO_BYTES, path);
    }

    /**
     * 지정한 경로의 파일의 1M 바이트보다 작은지 확인한다.
     *
     * @param path 로컬 파일의 절대 경로
     * @return 1M 보다 큰 경우 <tt>false</tt>, 작은 경우 <tt>true</tt>
     */
    public static boolean lessThan1MBytes(String path) {
        return lessThanSpecificSize(ONE_MEGA_BYTES, path);
    }

    /**
     * 지정한 경로의 파일의 100K 바이트보다 작은지 확인한다.
     *
     * @param path 로컬 파일의 절대 경로
     * @return 1M 보다 큰 경우 <tt>false</tt>, 작은 경우 <tt>true</tt>
     */
    public static boolean lessThan10MBytes(String path) {
        return lessThanSpecificSize(TEN_MEGA_BYTES, path);
    }

    /**
     * 지정한 경로의 파일의 100K 바이트보다 작은지 확인한다.
     *
     * @param path 로컬 파일의 절대 경로
     * @return 1M 보다 큰 경우 <tt>false</tt>, 작은 경우 <tt>true</tt>
     */
    public static boolean lessThan100MBytes(String path) {
        return lessThanSpecificSize(ONE_HUNDRED_MEGA_BYTES, path);
    }

    /**
     * 지정한 경로의 파일의 1M 바이트보다 작은지 확인한다.
     *
     * @param path 로컬 파일의 절대 경로
     * @return 1M 보다 큰 경우 <tt>false</tt>, 작은 경우 <tt>true</tt>
     */
    public static boolean lessThanSpecificSize(long size, String path) {
        return new File(path).length() < size;
    }

}