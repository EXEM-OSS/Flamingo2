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
package org.opencloudengine.flamingo2.mapreduce.etl.groupby;

import org.apache.commons.collections.list.TreeList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.util.CounterUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 지정한 키로 Group By하여 하나의 로우로 키와 값을 취합하는 Transpose ETL 리듀서
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class GroupByReducer extends Reducer<Text, Text, NullWritable, Text> {

    /**
     * Key와 Value의 구분자.
     */
    private String outputDelimiter;

    /**
     * Value를 구성하는 값들의 구분자.
     */
    private String valueDelimiter;

    /**
     * Value를 구성하는 값들의 구분자의 길이.
    */
    private int valueDelimiterLength;

    /**
     * 중복 허용 여부. <tt>true</tt>인 경우 중복을 허용한다.
     */
    private boolean allowDuplicate = true;

    /**
     * Value를 구성하는 값들을 정렬할지 여부. <tt>true</tt>인 경우 정렬한다.
     */
    private boolean allowSort = false;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        outputDelimiter = configuration.get("keyValueDelimiter", Delimiter.TAB.getDelimiter());
        valueDelimiter = configuration.get("valueDelimiter");
        valueDelimiterLength = valueDelimiter.length();
        allowDuplicate = configuration.getBoolean("allowDuplicate", false);
        allowSort = configuration.getBoolean("allowSort", false);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        CounterUtils.writerReducerCounter(this, "ITEMS", context);
        List<String> items = new TreeList();

        StringBuilder builder = new StringBuilder();
        for (Text value : values) {
            if (allowDuplicate) { // 중복을 허용하면 그대로 추가
                items.add(value.toString());
            } else if (!items.contains(value.toString())) { // 중복을 허용하지 않으면 없는 아이템만 추가
                items.add(value.toString());
            }
        }

        if (allowSort) {
            Collections.sort(items);    // FIXME Comparator를 정의하지 않아도 되나?
        }

        for (String item : items) {
            builder.append(item).append(valueDelimiter);
        }

        String valueString = builder.toString();
        String finalValueString = valueString.substring(0, valueString.length() - valueDelimiterLength);// 마지막 구분자 삭제
        context.write(NullWritable.get(), new Text(key.toString() + outputDelimiter + finalValueString));
    }
}