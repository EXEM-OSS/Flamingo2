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
package org.opencloudengine.flamingo2.mapreduce.etl.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.core.CsvRowParser;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.etl.filter.filters.Filter;
import org.opencloudengine.flamingo2.mapreduce.etl.filter.filters.FilterRegistry;
import org.opencloudengine.flamingo2.mapreduce.util.ArrayUtils;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

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
