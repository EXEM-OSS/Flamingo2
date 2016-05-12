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
package org.openflamingo.mapreduce.etl.replace.column;

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
public class ReplaceColumnMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(ReplaceColumnMapper.class);

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
     * 값을 변경할 컬럼의 정보
     */
    private Integer[] columnsToReplace;

    /**
     *
     */
    private String[] fromColumnsValues;

    /**
     *
     */
    private String[] toColumnsValues;

    /**
     *
     */
    private Replacer replacer;

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

        // 모든 파라미터를 배열로 변환하여 동일한 인덱스는 동일한 Rule을 적용할 수 있도록 한다.
        String[] stringColumnsToReplace = StringUtils.commaDelimitedListToStringArray(configuration.get("columnsToReplace"));
        fromColumnsValues = StringUtils.commaDelimitedListToStringArray(configuration.get("fromColumnsValues"));
        toColumnsValues = StringUtils.commaDelimitedListToStringArray(configuration.get("toColumnsValues"));

        if (columnSize == -1) {
            throw new IllegalArgumentException("You must specify 'columnSize' for validating the column size.");
        }

        // 모든 파라미터의 개수는 동일해야한다. 동일하지 않으면 Replace를 수행할 수 없다.
        if (stringColumnsToReplace.length != fromColumnsValues.length
                && stringColumnsToReplace.length != toColumnsValues.length) {
            throw new IllegalArgumentException("Invalid Parameter Length");
        }

        columnsToReplace = ArrayUtils.toIntegerArray(stringColumnsToReplace);

        // Replacer를 생성한다.
        replacer = new DefaultReplacer();
        /*replacers = new Replacer[columnSize];
        String stringReplacerClassName = null;
		for (int i = 0; i < stringReplacerClassNames.length; i++) {
			try {
				stringReplacerClassName = stringReplacerClassNames[i];
				Class clazz = Class.forName(stringReplacerClassName);
				Replacer replacer = (Replacer) clazz.newInstance();
				replacers[i] = replacer;
				replacers[i].setTo(toColumnsValues[i]);
			} catch (Exception ex) {
				throw new IllegalArgumentException("Cannot create instance '" + stringReplacerClassName + "'");
			}
		}*/
        parser = new CsvRowParser(columnSize, inputDelimiter, outputDelimiter);
    }

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            parser.parse(value.toString());
        } catch (IllegalArgumentException ex) {
            CounterUtils.writerMapperCounter(this, "Wrong Column Size", context);
            logger.warn("[Wrong Column Size] [{}] {}", columnSize, value.toString());
            return;
        }

        // 변경할 컬럼의 위치를 찾아서 Replacer로 값을 변경한다.
        for (int i = 0; i < columnsToReplace.length; i++) {
            Integer index = columnsToReplace[i];
            replacer.setTo(toColumnsValues[i]);
            if (parser.get(index).equals(fromColumnsValues[i])) {
                parser.change(replacer.replace(fromColumnsValues[i]), index);
            }
        }
        context.write(NullWritable.get(), parser.toRowText());
    }
}