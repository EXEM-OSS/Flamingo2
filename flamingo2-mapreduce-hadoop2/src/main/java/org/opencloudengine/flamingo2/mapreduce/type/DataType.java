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
package org.opencloudengine.flamingo2.mapreduce.type;

/**
 * 필터링시 해당 컬럼의 데이터 유형을 지정하기 위해서 사용하는 Enumeration.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public enum DataType {

    INTEGER("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    STRING("string");

    /**
     * 데이터 유형
     */
    private String dataType;

    /**
     * 데이터 유형을 설정한다.
     *
     * @param dataType 데이터 유형
     */
    DataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * 데이터 유형을 반환한다.
     *
     * @return 데이터 유형
     */
    public String getDataType() {
        return dataType;
    }
}