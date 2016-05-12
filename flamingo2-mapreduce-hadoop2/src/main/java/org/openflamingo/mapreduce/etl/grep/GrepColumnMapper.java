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
package org.openflamingo.mapreduce.etl.grep;

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
public class GrepColumnMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * 입력 Row를 컬럼으로 구분하기 위해서 사용하는 컬럼간 구분자
     */
    private String inputDelimiter;

    /**
     * 출력 Row를 구성하기 위해서 사용하는 컬럼간 구분자
     */
    private String outputDelimiter;

    /**
     * Grep할 컬럼들의 인덱스 배열
     */
    private Integer[] columnsToGrep;

    /**
     * Row의 컬럼 개수
     */
    private int columnSize;

    /**
     * CSV Row Parser
     */
    private CsvRowParser parser;

    /**
     * 정규 표현식
     */
    private String[] regEx;

    /**
     * 정규 표현식 패턴
     */
    private Pattern pattern;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        inputDelimiter = configuration.get("inputDelimiter", Delimiter.COMMA.getDelimiter());
        outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());

        columnSize = configuration.getInt("columnSize", -1);
        if (columnSize == -1) {
            throw new IllegalArgumentException("You must specify 'columnSize' for validating the column size.");
        }

        regEx = StringUtils.commaDelimitedListToStringArray(configuration.get("regEx"));

        String[] stringArrayColumns = StringUtils.commaDelimitedListToStringArray(configuration.get("columnsToGrep"));
        if (stringArrayColumns.length == 0) {
            throw new IllegalArgumentException("You must specify 'columnsToFilter' for cleaning some columns.");
        }

        columnsToGrep = ArrayUtils.toIntegerArray(stringArrayColumns);

        if (regEx.length != columnsToGrep.length) {
            throw new IllegalArgumentException("Invalid Parameter Length");
        }

        parser = new CsvRowParser(columnSize, inputDelimiter, outputDelimiter);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        parser.parse(value.toString());

        int i = 0;
        int count = 0;
        while (i < columnsToGrep.length) {
            pattern = Pattern.compile(regEx[i]);
            Matcher matcher = pattern.matcher(parser.get(columnsToGrep[i]));
            // columns들이 모두 match 되었는지 검사
            if (matcher.find()) {
                count++;
            }
            i++;
        }

        if (count == columnsToGrep.length) {
            CounterUtils.writerMapperCounter(this, "YES", context);
            context.write(NullWritable.get(), new Text(value));
        } else {
            CounterUtils.writerMapperCounter(this, "NO", context);
        }
    }
}