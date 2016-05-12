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
package org.openflamingo.mapreduce.etl.clean;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.openflamingo.mapreduce.core.CsvRowParser;
import org.openflamingo.mapreduce.core.Delimiter;
import org.openflamingo.mapreduce.util.ArrayUtils;
import org.openflamingo.mapreduce.util.CounterUtils;
import org.openflamingo.mapreduce.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 지정한 칼럼을 삭제하는 Clean ETL Mapper.
 * 이 Mapper는 지정한 칼럼을 삭제하고 다시 Delimiter를 이용하여 조립하고 Context Write를 한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class CleanMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(CleanMapper.class);

    /**
     * 입력 Row를 컬럼으로 구분하기 위해서 사용하는 컬럼간 구분자
     */
    private String inputDelimiter;

    /**
     * 출력 Row를 구성하기 위해서 사용하는 컬럼간 구분자
     */
    private String outputDelimiter;

    /**
     * Row의 컬럼 개수
     */
    private int columnSize;

    /**
     * 삭제할 컬럼의 정보
     */
    private Integer[] columnsToClean;

    /**
     * CSV Row Parser
     */
    private CsvRowParser parser;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        inputDelimiter = configuration.get("inputDelimiter", Delimiter.COMMA.getDelimiter());
        outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());
        columnSize = configuration.getInt("columnSize", -1);
        String[] stringArrayColumns = StringUtils.commaDelimitedListToStringArray(configuration.get("columnsToClean"));

        if (columnSize == -1) {
            throw new IllegalArgumentException("You must specify 'columnSize' for validating the column size.");
        }

        if (stringArrayColumns.length == 0) {
            throw new IllegalArgumentException("You must specify 'columnsToClean' for cleaning some columns.");
        }

        columnsToClean = ArrayUtils.toIntegerArray(stringArrayColumns);
        parser = new CsvRowParser(columnSize, inputDelimiter, outputDelimiter);
    }

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            parser.parse(value.toString());
            CounterUtils.writerMapperCounter(this, "YES", context);
        } catch (IllegalArgumentException ex) {
            CounterUtils.writerMapperCounter(this, "NO", context);
            CounterUtils.writerMapperCounter(this, "Wrong Column Size", context);
            logger.warn("Wrong Column Size!! [Size: {}] [Value: {}]", columnSize, value.toString());
            return;
        }
        parser.remove(columnsToClean);
        context.write(NullWritable.get(), parser.toRowText());
    }
}