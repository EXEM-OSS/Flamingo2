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
package org.opencloudengine.flamingo2.mapreduce.etl.generate;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.opencloudengine.flamingo2.mapreduce.core.CsvRowParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Generate Key Sequence Mapper.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class GenerateSequenceMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(GenerateSequenceMapper.class);

    /**
     * 입력 Row를 컬럼으로 구분하기 위해서 사용하는 컬럼간 구분자
     */
    private String inputDelimiter;

    /**
     * 출력 Row를 구성하기 위해서 사용하는 컬럼간 구분자
     */
    private String outputDelimiter;

    /**
     * Sequence의 시작 번호
     */
    private long startIndex = 0;

    /**
     * Row의 컬럼 개수
     */
    private int columnSize;

    /**
     * 인덱스를 생성할 위치
     */
    private int sequenceIndex;

    /**
     * CSV Row Parser
     */
    private CsvRowParser parser;

    /**
     * 시퀀스를 생성하는 방법이 {@link GenerateType#TIMESTAMP}인 경우 사용하는 날짜 포맷.
     * 날짜 포맷은 <tt>dateFormat</tt>으로 지정할 수 있으며 지정하지 않으면 기본값으로 <tt>yyyyMMdd</tt>을 사용한다.
     */
    private SimpleDateFormat dateFormat;

    /**
     * 시퀀스를 생성하는 방법이 {@link GenerateType#TIMESTAMP}인 경우 <tt>dateFormat</tt>으로 지정한 문자열 날짜.
     */
    private String dateFormatString;

    /**
     * 시퀀스 생성 방법. 시퀀스를 생성하는 방법은 다음 두가지가 있다.
     * <ul>
     * <li>{@link GenerateType#SEQUENCE}</li>
     * <li>{@link GenerateType#TIMESTAMP}</li>
     * </ul>
     */
    private String generateType;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        inputDelimiter = configuration.get("inputDelimiter");
        outputDelimiter = configuration.get("outputDelimiter");
        columnSize = configuration.getInt("columnSize", -1);
        generateType = configuration.get("generateType").toUpperCase();
        sequenceIndex = configuration.getInt("sequenceIndex", 0);

        if (GenerateType.valueOf(generateType).equals(GenerateType.SEQUENCE)) { // SEQUENCE
            long start = ((FileSplit) context.getInputSplit()).getStart();
            startIndex = configuration.getLong(String.valueOf(start), 0);
            if (startIndex == 0 && start != 0) {
                throw new IllegalArgumentException("Cannot find a start position for generating.");
            }
        } else { // TIMSTAMP
            dateFormat = new SimpleDateFormat(configuration.get("dateFormat"));
            dateFormatString = dateFormat.format(new Date());
        }

        if (columnSize == -1) {
            throw new IllegalArgumentException("You must specify 'columnSize' for validating the column size.");
        }

        parser = new CsvRowParser(columnSize, inputDelimiter, outputDelimiter);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        parser.parse(value.toString());
        if (GenerateType.valueOf(generateType).equals(GenerateType.SEQUENCE)) { // SEQUENCE
            parser.insert(String.valueOf(startIndex), sequenceIndex);
            startIndex++;
        } else {
            parser.insert(dateFormatString, sequenceIndex);
        }
        context.write(NullWritable.get(), new Text(parser.toRow()));
    }
}
