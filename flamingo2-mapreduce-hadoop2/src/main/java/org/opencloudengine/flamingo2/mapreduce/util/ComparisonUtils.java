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

import java.util.Iterator;

/**
 * 두 집합의 구성요소의 개수, 순서 및 내용이 동일한지 확인하는 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class ComparisonUtils {

    /**
     * 구성요소의 내용 및 순서와 길이가 동일한지 비교한다.
     *
     * @param <T>    비교한 구성요소의 유형.
     * @param first  비교할 첫번째 구성요소의 집합.
     * @param second 비교할 두번째 구성요소의 집합.
     * @return 동일하다면 <tt>true</tt>, 그렇지 않다면 <tt>false</tt>
     */
    public static <T> boolean equal(Iterable<T> first, Iterable<T> second) {
        return equal(first.iterator(), second.iterator());
    }

    /**
     * 구성요소의 내용 및 순서와 길이가 동일한지 비교한다.
     *
     * @param <T>    비교한 구성요소의 유형.
     * @param first  비교할 첫번째 구성요소의 집합.
     * @param second 비교할 두번째 구성요소의 집합.
     * @return 동일하다면 <tt>true</tt>, 그렇지 않다면 <tt>false</tt>
     */
    public static <T> boolean equal(Iterator<T> first, Iterator<T> second) {
        while (first.hasNext() && second.hasNext()) {
            T message = first.next();
            T otherMessage = second.next();  /* 구성요소도 같아야 한다 */
            if (!(message == null ? otherMessage == null :
                    message.equals(otherMessage))) {
                return false;
            }
        }
        /* 길이도 같아야 한다. */
        return !(first.hasNext() || second.hasNext());
    }
}
