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
package org.opencloudengine.flamingo2.mapreduce.etl.generate;

/**
 * Generate ETL에서 사용하는 유형을 표현하는 Enumeration.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public enum GenerateType {

    SEQUENCE("SEQUENCE"),
    TIMESTAMP("TIMESTAMP");

    /**
     * Generate Type
     */
    private String type;

    /**
     * Generate Type을 설정한다.
     *
     * @param type Generate Type
     */
    GenerateType(String type) {
        this.type = type;
    }

    /**
     * Generate Type을 반환한다.
     *
     * @return type Generate Type
     */
    public String getType() {
        return type;
    }
}
