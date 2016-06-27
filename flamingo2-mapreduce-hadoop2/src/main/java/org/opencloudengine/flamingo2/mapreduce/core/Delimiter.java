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
 * 범용적으로 사용하는 구분자를 표현하는 Enumeration.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public enum Delimiter {

    SPACE("\u0020"),
    TAB("\u0009"),
    PIPE("|"),
    DOUBLE_PIPE("||"),
    COMMA("\u002c"),
    PERIOD("."),
    SEMICOLON(";"),
    COLON(":"),
    ASTERISK("*"),
    HYPEN("-"),
    TILDE("~"),
    CROSSHATCH("#"),
    EXCLAMATION_MARK("!"),
    DOLLAR("$"),
    AMPERSAND("&"),
    PERCENT("%"),
    QUOTATION_MARK("\""),
    CIRCUMFLEX("^");

    /**
     * 컬럼을 구분하는 delimiter
     */
    private String delimiter;

    /**
     * Delimiter를 설정한다.
     *
     * @param delimiter delimiter
     */
    Delimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Delimiter를 반환한다.
     *
     * @return delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }
}
