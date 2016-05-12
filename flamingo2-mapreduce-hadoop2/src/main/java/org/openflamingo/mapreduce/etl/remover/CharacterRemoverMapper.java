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
package org.openflamingo.mapreduce.etl.remover;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.openflamingo.mapreduce.util.CounterUtils;

import java.io.IOException;

/**
 * 지정한 문자열을 ROW에서 제거하는 ETL Mapper.
 *
 * @author Byoung Gon, Kim
 * @since 1.1
 */
public class CharacterRemoverMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private String characters;

    private Text output = new Text();

    @Override
    public void setup(Context context) {
        Configuration configuration = context.getConfiguration();
        characters = configuration.get("characters", " ");
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            String removed = StringUtils.remove(value.toString(), characters);
            output.set(removed);
            context.write(NullWritable.get(), output);
            CounterUtils.writerMapperCounter(this, "REMOVED", context);
        } catch (Exception ex) {
            CounterUtils.writerMapperCounter(this, "INVALID", context);
        }
    }
}
