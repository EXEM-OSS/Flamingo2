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
package org.opencloudengine.flamingo2.mapreduce.etl.filter.filters;

import org.opencloudengine.flamingo2.mapreduce.type.DataType;
import org.slf4j.helpers.MessageFormatter;

/**
 * 원소스값이 비교 대상값보다 큰지 확인하는 필터.
 * 원소스 및 비교 대상값이 원시 자료형인 경우 해당 자료형에 따라서 비교 대상이 달라지므로 데이터 유형에 따라서
 * 서로 다른 비교 로직을 제공해야 한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class GreaterThanEqualFilter extends FilterSupport {

    public boolean doFilter(Object source, Object target, String type) {
        if (DataType.INTEGER.getDataType().equals(type.trim())) {
            return doFilterIntegerValue(source, target);
        } else if (DataType.LONG.getDataType().equals(type.trim())) {
            return doFilterLongValue(source, target);
        } else if (DataType.FLOAT.getDataType().equals(type.trim())) {
            return doFilterFloatValue(source, target);
        } else if (DataType.DOUBLE.getDataType().equals(type.trim())) {
            return doFilterDoubleValue(source, target);
        } else {
            String message = MessageFormatter.format("Not Supported Data Type ({} >= {} ?) ({}) ", new Object[]{
                    source, target, type
            }).getMessage();
            throw new IllegalArgumentException(message);
        }
    }

    private boolean doFilterDoubleValue(Object source, Object target) {
        try {
            double sourceValue = Double.parseDouble((String) source);
            double targetValue = Double.parseDouble((String) target);
            return sourceValue >= targetValue;
        } catch (Exception ex) {
            String message = MessageFormatter.format("Data Type Mismatch (Double) ({} >= {}) ", source, target).getMessage();
            throw new IllegalArgumentException(message, ex);
        }
    }

    private boolean doFilterFloatValue(Object source, Object target) {
        try {
            float sourceValue = Float.parseFloat((String) source);
            float targetValue = Float.parseFloat((String) target);
            return sourceValue >= targetValue;
        } catch (Exception ex) {
            String message = MessageFormatter.format("Data Type Mismatch (Float) ({} >= {}) ", source, target).getMessage();
            throw new IllegalArgumentException(message, ex);
        }
    }

    private boolean doFilterLongValue(Object source, Object target) {
        try {
            long sourceValue = Long.parseLong((String) source);
            long targetValue = Long.parseLong((String) target);
            return sourceValue >= targetValue;
        } catch (Exception ex) {
            String message = MessageFormatter.format("Data Type Mismatch (Long) ({} >= {}) ", source, target).getMessage();
            throw new IllegalArgumentException(message, ex);
        }
    }

    private boolean doFilterIntegerValue(Object source, Object target) {
        try {
            int sourceValue = Integer.parseInt((String) source);
            int targetValue = Integer.parseInt((String) target);
            return sourceValue >= targetValue;
        } catch (Exception ex) {
            String message = MessageFormatter.format("Data Type Mismatch (Integer) ({} >= {}) ", source, target).getMessage();
            throw new IllegalArgumentException(message, ex);
        }
    }
}
