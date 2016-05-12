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

import java.math.BigDecimal;

/**
 * 수학적 연산을 처리하는 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class MathUtils {

    public static final int FLOAT_POSITION = 6;

    /**
     * Scientific Notation을 Standard Notation으로 변환한다.
     *
     * @param value Double의 Scientific Notation
     * @return Double의 Standard Notation
     */
    public static String convertScientificToStandard(double value) {
        return (new BigDecimal(Double.toString(value))).toPlainString();
    }

    /**
     * Scientific Notation을 Standard Notation으로 변환한다.
     *
     * @param value Float의 Scientific Notation
     * @return Float의 Standard Notation
     */
    public static String convertScientificToStandard(float value) {
        return (new BigDecimal(Float.toString(value))).toPlainString();
    }

    /**
     * 더블형 입력값의 제곱을 구한다.
     *
     * @param value 입력값
     * @return 입력값의 제곱
     */
    public static double pow(double value) {
        return Math.pow(value, 2);
    }

    /**
     * 배열에서 최소값을 구한다.
     *
     * @param values 배열
     * @return 최소값
     */
    public static double min(double[] values) {
        double min = values[0];
        for (double value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    /**
     * 배열에서 최대값을 구한다.
     *
     * @param values 배열
     * @return 최대값
     */
    public static double max(double[] values) {
        double max = values[0];
        for (double value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    /**
     * 배열에서 최대값을 구한다.
     *
     * @param values 배열
     * @return 최대값
     */
    public static float max(float[] values) {
        float max = values[0];
        for (float value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    /**
     * 배열에서 최대값을 구한다.
     *
     * @param values 배열
     * @return 최대값
     */
    public static int max(int[] values) {
        int max = values[0];
        for (int value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    /**
     * 지정한 정수가 배열 안에 하나라도 포함되어 있는지 확인한다.
     *
     * @param source 확인할 값
     * @param values 값 목록
     * @return 포함되어 있다면 <tt>true</tt>
     */
    public static boolean in(long source, long[] values) {
        for (long value : values) {
            if (value == source) {
                return true;
            }
        }
        return false;
    }

    /**
     * 지정한 문자열이 배열 안에 하나라도 포함되어 있는지 확인한다.
     *
     * @param source 확인할 값
     * @param values 값 목록
     * @return 포함되어 있다면 <tt>true</tt>
     */
    public static boolean in(String source, String[] values) {
        for (String value : values) {
            if (value.equals(source)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 지정한 값의 제곱근 값을 계산한다.
     *
     * @param value 입력값
     * @return 제곱근
     */
    public static double sqrt(double value) {
        return Math.sqrt(value);
    }

    /**
     * 지수를 계산한다.
     *
     * @param value 입력값
     * @return e의 value승
     */
    public static double exp(double value) {
        return Math.exp(value);
    }

    /**
     * 자연대수를 계산한다.
     *
     * @param value 입력값
     * @return log(value)
     */
    public static double log(double value) {
        return Math.log(value);
    }

    /**
     * 거듭제곱의 값을 계산한다.
     *
     * @param value1 입력값
     * @param value2 입력값
     * @return value1의 value2승
     */
    public static double pow(double value1, double value2) {
        return Math.pow(value1, value2);
    }

    /**
     * 반올림한다.
     *
     * @param value 입력값
     * @return Value의 반올림값
     */
    public static double round(double value) {
        return Math.round(value);
    }

    /**
     * BigDecimal의 소수점 앞 부분만 추출한다.
     *
     * @param bigDecimal Big Decimal
     * @return 소수점 앞 부분
     */
    public static String toBeforeDecimalPointString(BigDecimal bigDecimal) {
        String value = bigDecimal.toPlainString();
        if (!value.contains(".")) {
            return bigDecimal.toPlainString();
        }
        String[] strings = value.split("\\.");
        return strings[0];
    }

    /**
     * Double의 소수점 앞 부분만 추출한다.
     *
     * @param value Double
     * @return 소수점 앞 부분
     */
    public static String toBeforeDecimalPointString(double value) {
        return toBeforeDecimalPointString(new BigDecimal(value));
    }

    /**
     * 지정한 개수만큼 0으로 채운다.
     *
     * @param count 0을 채울 개수
     * @return 0으로 된 문자열
     */
    public static String fillZero(int count) {
        String zero = "";
        for (int index = 0; index < count; index++) {
            zero += "0";
        }
        return zero;
    }

    /**
     * 소수점에 대해서 {@link #FLOAT_POSITION} 자리수만큼 0을 채운다.
     * <ul>
     * <li>0.5 -> 0.500000</li>
     * <li>0.50 -> 0.500000</li>
     * <li>0.500 -> 0.500000</li>
     * </ul>
     *
     * @param floatValue 부동 소수점값
     * @return 자리수만큼 0을 채운 부동 소수점값
     */
    public static String correctFloatValueForECMiner(String floatValue) {
        // 부동소수점에서 . 전후를 추출한다.
        String[] strings = floatValue.split("\\.");

        // .가 없다면 전체 길이가 1일 것이므로 그 보정할 필요가 없다. 그래서 그냥 입력값을 그대로 사용한다.
        if (strings.length == 1) {
            return floatValue;
        }
        // 부동소수점에서 . 이후의 값을 찾아서 그 길이가 최대 길이보다 크다면 잘라버리고 그 값만 사용한다.
        if (strings[1].length() > FLOAT_POSITION) {
            return strings[0] + "." + strings[1].substring(0, FLOAT_POSITION + 1);
        }

        // 부동소수점에서 . 이후의 값을 찾아서 그 길이가 최대 길이와 같다면 그냥 사용한다.
        if (strings[1].length() == FLOAT_POSITION) {
            return strings[0] + "." + strings[1];
        }

        // 부동소수점에서 . 이후의 값을 찾아서 그 길이가 최대 길이보다 작다면 보정한다.
        if (strings[1].length() < FLOAT_POSITION) {
            int diff = FLOAT_POSITION - strings[1].length();
            StringBuilder builder = new StringBuilder();
            builder.append(strings[0]);
            builder.append(".");
            builder.append(strings[1]);
            for (int index = 0; index < diff; index++) {
                builder.append("0");
            }
            return builder.toString();
        }
        return floatValue;
    }

    /**
     * 소숫점 이하가 모두 0인 경우 0를 모두 삭제한다.
     *
     * @param value 부동소숫점 값
     * @return 0을 제거한 값
     */
    public static String deleteZero(String value) {
        String[] strings = value.split("\\.");
        if (strings.length > 1 && Integer.parseInt(strings[1]) == 0) {
            return strings[0];
        }
        return value;
    }
}
