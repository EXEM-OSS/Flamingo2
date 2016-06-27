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
package org.opencloudengine.flamingo2.mapreduce.etl.apache;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.util.CounterUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opencloudengine.flamingo2.mapreduce.util.StringUtils.stripBoth;

/**
 * Apache HTTP Server 로그를 정형 로그로 변경하는 ETL Mapper.
 *
 * @author Byoung Gon, Kim
 * @since 1.1
 */
public class ApacheHttpAccessLogMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    public static final String PATTERN = "([^ ]*) ([^ ]*) ([^ ]*) (-|\\[[^\\]]*\\]) ([^ \"]*|\"[^\"]*\") (-|[0-9]*) (-|[0-9]*)(?: ([^ \"]*|\"[^\"]*\") ([^ \"]*|\"[^\"]*\"))?";

    /**
     * 출력 Row를 구성하기 위해서 사용하는 컬럼간 구분자
     */
    private String outputDelimiter;

    private boolean printMismatch;

    private Text output = new Text();

    private Pattern pattern;

    @Override
    public void setup(Context context) {
        Configuration configuration = context.getConfiguration();
        outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());
        printMismatch = configuration.getBoolean("printMismatch", false);
        pattern = Pattern.compile(PATTERN, Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            Matcher matcher = pattern.matcher(value.toString());
            if (matcher.matches()) {
                StringBuilder builder = new StringBuilder();
                builder.append(matcher.group(1)).append(outputDelimiter);
                builder.append(matcher.group(2)).append(outputDelimiter);
                builder.append(matcher.group(3)).append(outputDelimiter);
                builder.append(stripBoth(matcher.group(4), "[", "]")).append(outputDelimiter);

                String[] split = stripBoth(matcher.group(5), "\"", "\"").split(" ");
                builder.append(split[0]).append(outputDelimiter);
                builder.append(split[1]).append(outputDelimiter);
                builder.append(matcher.group(6)).append(outputDelimiter);
                builder.append(matcher.group(7)).append(outputDelimiter);
                builder.append(stripBoth(matcher.group(8), "\"", "\"")).append(outputDelimiter);
                builder.append(stripBoth(matcher.group(9), "\"", "\""));

                output.set(builder.toString());
                context.write(NullWritable.get(), output);
                CounterUtils.writerMapperCounter(this, "MATCH", context);
            } else {
                CounterUtils.writerMapperCounter(this, "MISMATCH", context);
                if (printMismatch) {
                    System.out.println(value.toString());
                }
            }
        } catch (Exception ex) {
            CounterUtils.writerMapperCounter(this, "INVALID", context);
        }
    }
}
