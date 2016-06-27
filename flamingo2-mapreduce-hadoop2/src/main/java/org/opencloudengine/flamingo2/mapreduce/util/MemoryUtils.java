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
