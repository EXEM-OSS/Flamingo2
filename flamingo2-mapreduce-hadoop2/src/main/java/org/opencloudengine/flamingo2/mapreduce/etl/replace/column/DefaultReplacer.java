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
package org.opencloudengine.flamingo2.mapreduce.etl.replace.column;

/**
 * Description.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 1.0
 */
public class DefaultReplacer implements Replacer {

    /**
     * 변환할 문자열
     */
    private String to;

    /**
     * 기본 생성자.
     *
     * @param to 변환할 문자열
     */
    public DefaultReplacer(String to) {
        this.to = to;
    }

    /**
     * 기본 생성자.
     */
    public DefaultReplacer() {
    }

    @Override
    public String replace(String from) {
        return to;
    }

    /**
     * 변환할 문자열을 설정한다.
     *
     * @param to 변환할 문자열
     */
    @Override
    public void setTo(String to) {
        this.to = to;
    }
}
