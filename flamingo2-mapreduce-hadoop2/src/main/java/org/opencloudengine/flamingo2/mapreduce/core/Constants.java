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
package org.opencloudengine.flamingo2.mapreduce.core;

/**
 * Hadoop MapReduce Job에서 사용하는 각종 상수를 정의한 상수 클래스.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class Constants {

    /**
     * Default Delimiter
     */
    public static final String DEFAULT_DELIMITER = Delimiter.COMMA.getDelimiter();

    /**
     * Hadoop Job Fail
     */
    public static final int JOB_FAIL = -1;

    /**
     * Hadoop Job Success
     */
    public static final int JOB_SUCCESS = 0;

    /**
     * Total Row Count Counter Name
     */
    public static final String TOTAL_ROW_COUNT = "Total Row Count";

    /**
     * HDFS 임시 디렉토리 위치의 Key 값
     */
    public static final String TEMP_DIR = "tempDir";

    /**
     * YES
     */
    public static final String YES = "yes";

    /**
     * NO
     */
    public static final String NO = "no";
}
