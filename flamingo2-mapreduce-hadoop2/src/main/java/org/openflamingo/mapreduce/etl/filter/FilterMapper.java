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
package org.openflamingo.mapreduce.etl.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.openflamingo.mapreduce.core.CsvRowParser;
import org.openflamingo.mapreduce.core.Delimiter;
import org.openflamingo.mapreduce.etl.filter.filters.Filter;
import org.openflamingo.mapreduce.etl.filter.filters.FilterRegistry;
import org.openflamingo.mapreduce.util.ArrayUtils;
import org.openflamingo.mapreduce.util.StringUtils;

import java.io.IOException;

/**
 * 지정한 컬럼의 값을 기준으로 필터링할 조건에 부합하는 경우 해당 ROW를 사용하는 Filter ETL Mapper.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class FilterMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * 입력 Row를 컬럼으로 구분하기 위해서 사용하는 컬럼간 구분자
     */
    private String inputDelimiter;

    /**
     * 출력 Row를 구성하기 위해서 사용하는 컬럼간 구분자
     */
    private String outputDelimiter;

    /**
     * 필터링 할 컬럼들의 필터링 규칙
     */
    private String[] filterModes;

    /**
     * 필터링 할 컬럼들의 필터 값.
     */
    private String[] filterValues;

    /**
     * 필터링 할 컬럼들의 데이터 타입 배열
     */
    private String[] dataTypes;

    /**
     * 필터링 할 컬럼의 인덱스 배열
     */
    private Integer[] columnsToFilter;

    /**
     * Row의 컬럼 개수
     */
    private int columnSize;

    /**
     * CSV Row Parser
     */
    private CsvRowParser parser;


    @Override
    public void setup(Context context) {
        Configuration configuration = context.getConfiguration();
        inputDelimiter = configuration.get("inputDelimiter", Delimiter.COMMA.getDelimiter());
        outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());
        // TODO validation
        filterModes = StringUtils.commaDelimitedListToStringArray(configuration.get("filterModes"));
        filterValues = StringUtils.commaDelimitedListToStringArray(configuration.get("filterValues"));
        dataTypes = StringUtils.commaDelimitedListToStringArray(configuration.get("dataTypes"));

        columnSize = configuration.getInt("columnSize", -1);
        if (columnSize == -1) {
            throw new IllegalArgumentException("You must specify 'columnSize' for validating the column size.");
        }

        String[] stringArrayColumns = StringUtils.commaDelimitedListToStringArray(configuration.get("columnsToFilter"));
        if (stringArrayColumns.length == 0) {
            throw new IllegalArgumentException("You must specify 'columnsToFilter' for cleaning some columns.");
        }

        columnsToFilter = ArrayUtils.toIntegerArray(stringArrayColumns);

        parser = new CsvRowParser(columnSize, inputDelimiter, outputDelimiter);
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        parser.parse(value.toString());
        for (int i = 0; i < filterModes.length; i++) {
            Filter filter = FilterRegistry.getFilter(filterModes[i]);
            if (filter.doFilter(parser.get(columnsToFilter[i]), filterValues.length == 0 ? "" : filterValues[i], /*FIXME use DataType*/dataTypes[i])) {
                context.write(NullWritable.get(), value);
                break;
            }
        }
    }
}
