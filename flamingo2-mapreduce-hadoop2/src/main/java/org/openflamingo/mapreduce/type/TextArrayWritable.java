/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.type;

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