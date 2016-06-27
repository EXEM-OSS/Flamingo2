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
package org.opencloudengine.flamingo2.mapreduce.etl.grep;

/**
 * Regular Expression Enumeration.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public enum RegEx {

    EMAIL("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$"),
    JUMIN("[0-9]{6}-(1|2|3|4)[0-9]{6}"),
    HTML_LINK("(http|https|ftp)://[^\\s^\\.]+(\\.[^\\s^\\.]+)*"),
    HTML_TAG("<(?:.|\\s)*?>");

    /**
     * Regular Expression
     */
    private String regularExpression;

    /**
     * Regular Expression를 설정한다.
     *
     * @param regularExpression regularExpression
     */
    RegEx(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    /**
     * Regular Expression를 반환한다.
     *
     * @return regularExpression
     */
    public String getRegularExpression() {
        return regularExpression;
    }
}
