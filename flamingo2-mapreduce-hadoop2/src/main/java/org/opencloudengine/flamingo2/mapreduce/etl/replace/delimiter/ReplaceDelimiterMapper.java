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
package org.opencloudengine.flamingo2.mapreduce.etl.replace.delimiter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.util.CounterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Delimiter를 변경하는 Replace Delimiter Mapper.
 * 이 Mapper는 입력 ROW를 {@link String#replaceAll(String, String)}을 기반으로 동작한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class ReplaceDelimiterMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(ReplaceDelimiterMapper.class);

    /**
     * 변경하기전 원본 컬럼 구분자
     */
    private String from;

    /**
     * 변경할 컬럼 구분자
     */
    private String to;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        from = configuration.get("from");
        to = configuration.get("to");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            CounterUtils.writerMapperCounter(this, "YES", context);
            context.write(NullWritable.get(), new Text(value.toString().replaceAll(from, to)));
        } catch (IllegalArgumentException ex) {
            CounterUtils.writerMapperCounter(this, "NO", context);
        }
    }
}