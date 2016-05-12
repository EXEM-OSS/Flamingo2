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
package org.openflamingo.mapreduce.etl.apache;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.openflamingo.mapreduce.core.Delimiter;
import org.openflamingo.mapreduce.util.CounterUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openflamingo.mapreduce.util.StringUtils.stripBoth;

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
        printMismatch = configuration.getBoolean("printMismatch", true);
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
