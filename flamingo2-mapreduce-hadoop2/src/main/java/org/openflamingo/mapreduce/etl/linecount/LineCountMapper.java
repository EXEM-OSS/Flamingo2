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
package org.openflamingo.mapreduce.etl.linecount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.openflamingo.mapreduce.core.Constants;
import org.openflamingo.mapreduce.util.CounterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 순서대로 번호를 부여하기 위해서 각각의 Mapper에 부여받은 Input Split의 시작 위치 및 라인수를 계산하는 Line Counter Mapper.
 * 모든 파일에 대해서 일관적인 방법으로 일련번호를 부여하기 위해서는 각 Mapper가 처리할 파일의 순서와 라인의 수를 알아야 한다.
 * 그리고 이렇게 생성된 정보를 MapReduce Driver에서 역으로 산정하여 각 Mapper의 시작 위치를 결정하고
 * 동일한 입력에 대해서 일련번호를 부여하도록 구현해야 한다. 이를 위해서 이 Mapper는 다음과 같이 Input Split의 시작 위치를 알아내고 Counter로 측정한다.
 * <p/>
 * <pre>
 *     long start = ((FileSplit) context.getInputSplit()).getStart();
 *     counter = context.getCounter(getClass().getName(), String.valueOf(start));
 *     counter.increment(1);
 * </pre>
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class LineCountMapper extends Mapper<LongWritable, Text, NullWritable, NullWritable> {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(LineCountMapper.class);

    /**
     * Line Count를 위한 Counter
     */
    private Counter counter;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        long start = ((FileSplit) context.getInputSplit()).getStart();
        logger.info("Input Split : ", context.getInputSplit().toString());
        logger.info("Input Split Start : {}", start);
        counter = context.getCounter(getClass().getName(), String.valueOf(start));
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        CounterUtils.writerMapperCounter(this, Constants.TOTAL_ROW_COUNT, context);
        counter.increment(1);
    }
}
