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
package org.opencloudengine.flamingo2.engine.hawq.externaltable;

import org.opencloudengine.flamingo2.util.StringUtils;

/**
 * HAWQ External Table Format : TEXT.
 *
 * @author Ha Neul, Kim
 * @since 2.0
 */
public class Text extends Format {

    private boolean header;

    private String delimiter;

    private String nullString;

    private String escape;

    private String newLine;

    private boolean fillMissingFields;

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getNullString() {
        return nullString;
    }

    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    public String getEscape() {
        return escape;
    }

    public void setEscape(String escape) {
        this.escape = escape;
    }

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public boolean isFillMissingFields() {
        return fillMissingFields;
    }

    public void setFillMissingFields(boolean fillMissingFields) {
        this.fillMissingFields = fillMissingFields;
    }

    @Override
    public String toString() {
        return "Text{" +
                "type=" + this.getType() +
                ", header=" + header +
                ", delimiter=" + delimiter +
                ", nullString=" + nullString +
                ", escape=" + escape +
                ", newLine=" + newLine +
                ", fillMissingFields=" + fillMissingFields +
                '}';
    }

    @Override
    public boolean isEmptyOptions() {
        return this.header && StringUtils.isEmpty(this.delimiter) && StringUtils.isEmpty(this.nullString) &&
                StringUtils.isEmpty(this.escape) && StringUtils.isEmpty(this.newLine) && this.fillMissingFields;
    }

    @Override
    public String getFormatString() {
        String formatString = "";

        if (this.header) {
            formatString += "HEADER";
        }

        if (this.delimiter != null && !"".equals(this.delimiter)) {
            formatString += " DELIMITER '" + this.delimiter + "'";
        }

        if (this.nullString != null && !"".equals(this.nullString)) {
            formatString += " NULL '" + this.nullString + "'";
        }

        if (this.escape != null && !"".equals(this.escape)) {
            formatString += " ESCAPE '" + this.escape + "'";
        }

        if (!"null".equals(this.newLine) && !StringUtils.isEmpty(this.newLine)) {
            formatString += " NEWLINE '" + this.newLine + "'";
        }

        if (this.fillMissingFields) {
            formatString += " FILL MISSING FIELDS";
        }

        return formatString;
    }
}
