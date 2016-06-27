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

import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

/**
 * 지정한 컬럼이 비어있지 않은지 확인하는 필터.
 * 필터의 값이 일반적으로 문자열이므로 문자열이 비어있지 않아야 하며, 공백이어서도 안되고, 실제 길이가 0보다 커야 하는 경우 비어있지 않다고 판단한다.
 * 이 필터는 target 값을 판단하지 않으며 오직 source 값만 확인한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class NotEmptyColumnFilter extends FilterSupport {

    public boolean doFilter(Object source, Object target, String type) {
        return !StringUtils.isEmpty((String) source);
    }

}
