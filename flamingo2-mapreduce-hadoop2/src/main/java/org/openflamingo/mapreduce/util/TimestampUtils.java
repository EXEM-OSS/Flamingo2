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

import java.util.Date;

/**
 * 타임 스탬프를 생성하는 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class TimestampUtils {

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMMDD</tt>" 형식의 타임 스탬프를 생성한다.
     *
     * @return "<tt>YYYYMMDD</tt>" 형식의 문자열 타임 스탬프
     */
    public String getTimestamp() {
        return getToday();
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMMDDHH</tt>" 형식의 타임 스탬프를 생성한다.
     *
     * @return "<tt>YYYYMMDDHH</tt>" 형식의 문자열 타임 스탬프
     */
    public String getTimestampYYYYMMDDHH() {
        Date today = new Date();
        return DateUtils.parseDate(today, "yyyyMMddHH");
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMMDDHH</tt>" 형식의 타임 스탬프를 생성한다.
     *
     * @return "<tt>YYYYMMDDHH</tt>" 형식의 문자열 타임 스탬프
     */
    public String getTomorrow() {
        Date today = new Date();
        Date tomorrow = DateUtils.addDays(today, 1);
        return DateUtils.parseDate(tomorrow, "yyyyMMdd");
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMMDD</tt>" 형식의 내일 모레 날짜를 생성한다.
     *
     * @return "<tt>YYYYMMDD</tt>" 형식의 문자열 타임 스탬프
     */
    public String getTwoDays() {
        Date today = new Date();
        Date tomorrow = DateUtils.addDays(today, 2);
        return DateUtils.parseDate(tomorrow, "yyyyMMdd");
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMMDD</tt>" 형식의 어제 날짜를 생성한다.
     *
     * @return "<tt>YYYYMMDD</tt>" 형식의 문자열 타임 스탬프
     */
    public String getYesterday() {
        Date today = new Date();
        Date yesterday = DateUtils.addDays(today, -1);
        return DateUtils.parseDate(yesterday, "yyyyMMdd");
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMMDD</tt>" 형식의 엊그제 날짜를 생성한다.
     *
     * @return "<tt>YYYYMMDD</tt>" 형식의 문자열 타임 스탬프
     */
    public String getADayBeforeYesterday() {
        Date today = new Date();
        Date yesterday = DateUtils.addDays(today, -2);
        return DateUtils.parseDate(yesterday, "yyyyMMdd");
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMMDD</tt>" 형식의 엊그제 날짜를 생성한다.
     *
     * @return "<tt>YYYYMMDD</tt>" 형식의 문자열 타임 스탬프
     */
    public String getToday() {
        return DateUtils.getCurrentYyyymmdd();
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMM</tt>" 형식의 이번달을 생성한다.
     *
     * @return "<tt>YYYYMM</tt>" 형식의 문자열 타임 스탬프
     */
    public String getThisMonth() {
        return DateUtils.parseDate(new Date(), "yyyyMM");
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMM</tt>" 형식의 지난달을 생성한다.
     *
     * @return "<tt>YYYYMM</tt>" 형식의 문자열 타임 스탬프
     */
    public String getPreviousMonth() {
        Date today = new Date();
        Date previous = DateUtils.addMonths(today, -1);
        return DateUtils.parseDate(previous, "yyyyMM");
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMM</tt>" 형식의 지지난달을 생성한다.
     *
     * @return "<tt>YYYYMM</tt>" 형식의 문자열 타임 스탬프
     */
    public String getMonthBeforeLast() {
        Date today = new Date();
        Date previous = DateUtils.addMonths(today, -2);
        return DateUtils.parseDate(previous, "yyyyMM");
    }

    /**
     * 현재 시간을 기준으로 "<tt>YYYYMM</tt>" 형식의 다음달을 생성한다.
     *
     * @return "<tt>YYYYMM</tt>" 형식의 문자열 타임 스탬프
     */
    public String getNextMonth() {
        Date today = new Date();
        Date previous = DateUtils.addMonths(today, 1);
        return DateUtils.parseDate(previous, "yyyyMM");
    }
}
