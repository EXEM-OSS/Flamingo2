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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.util.CounterUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 정규 표현식을 이용하여 로우를 Grep하는 Grep ETL 매퍼
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class GrepRowMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * 정규 표현식
     */
    private String regEx;

    /**
     * 정규 표현식 패턴
     */
    private Pattern pattern;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        regEx = configuration.get("regEx", null);
        pattern = Pattern.compile(regEx);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String column = value.toString();
        Matcher matcher = pattern.matcher(column);
        if (matcher.find()) {
            CounterUtils.writerMapperCounter(this, "YES", context);
            context.write(NullWritable.get(), new Text(value));
        } else {
            CounterUtils.writerMapperCounter(this, "NO", context);
        }
    }
}