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