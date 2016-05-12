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
package org.openflamingo.mapreduce.etl.aggregate;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.openflamingo.mapreduce.util.CounterUtils;
import org.openflamingo.mapreduce.util.HdfsUtils;

import java.io.IOException;

/**
 * 하나 이상의 입력 파일을 받아서 합치는 Aggregation ETL Mapper.
 * 이 Mapper는 입력을 그대로 다시 출력한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class AggregateMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * Input Split의 file name
     */
    private String filename;

    /**
     * 개별 파일당 라인의 개수를 수집할지 여부
     */
    private boolean lineCountPerFile;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        lineCountPerFile = configuration.getBoolean("lineCountPerFile", false);
        if (lineCountPerFile) {
            InputSplit inputSplit = context.getInputSplit();
            try {
                filename = HdfsUtils.getFilename(inputSplit);
            } catch (Exception ex) {
                CounterUtils.writerMapperCounter(this, "Cannot get a file name from input split", context);
            }
        }
    }

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (lineCountPerFile) CounterUtils.writerMapperCounter(this, filename, context);
        context.write(NullWritable.get(), value);
    }

}