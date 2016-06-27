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

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.math.BigDecimal;

/**
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class TextArrayWritable extends ArrayWritable {

    public TextArrayWritable() {
        super(Text.class);
    }

    public TextArrayWritable(BigDecimal[] values) {
        super(Text.class, getValueText(values));
    }

    private static Text[] getValueText(BigDecimal[] values) {
        Text[] text = new Text[values.length];
        for (int i = 0; i < text.length; i++) {
            text[i] = new Text(values[i].toPlainString());
        }
        return text;
    }

    public int compareTo(TextArrayWritable taw) {
        Writable[] source = this.get();
        Writable[] target = taw.get();

        int cmp = source.length - target.length;
        if (cmp != 0) {
            return cmp;
        }

        for (int i = 0; i < source.length; i++) {
            cmp = ((Text) source[i]).compareTo((Text) target[i]);
            if (cmp != 0) {
                return cmp;
            }
        }

        return cmp;
    }
}