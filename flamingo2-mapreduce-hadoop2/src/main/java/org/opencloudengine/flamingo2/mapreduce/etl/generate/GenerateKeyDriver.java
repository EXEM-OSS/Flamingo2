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

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.etl.linecount.LineCountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * CSV 형식의 입력 파일에서 지정한 위치에 새로운 키값을 생성하는 Generate ETL Driver.
 * 이 MapReduce ETL은 다음의 파라미터를 가진다.
 * <ul>
 * <li><tt>inputDelimiter (id)</tt> - 입력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>outputDelimiter (od)</tt> - 출력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>sequenceIndex (si)</tt> - 시퀀스를 추가할 인덱스(0부터 시작) (선택) (기본값 0)</li>
 * <li><tt>startNumber (sn)</tt> - 시작할 시퀀스 번호(기본값 0) (선택) (기본값 0)</li>
 * <li><tt>columnSize (c)</tt> - 컬럼의 개수 (필수) (예; 4)</li>
 * <li><tt>sequenceType (st)</tt> - 시퀀스의 유형(SEQUENCE, TIMESTAMP) (필수)</li>
 * <li><tt>dateFormat (d)</tt> - 날짜 패턴(SimpleDateFormat) (선택) (기본값 yyyyMMdd-HHmmss-SSS)</li>
 * </ul>
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class GenerateKeyDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(GenerateKeyDriver.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new GenerateKeyDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("outputDelimiter", "od", "출력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("sequenceIndex", "si", "시퀀스를 삽입할 컬럼의 인덱스(0부터 시작, 기본값 0)", "0");
        addOption("startNumber", "sn", "시퀀스 번호의 시작값(기본값 0)", "0");
        addOption("columnSize", "cs", "컬럼의 개수", true);
        addOption("generateType", "gt", "생성할 시퀀스의 유형(SEQUENCE, TIMESTAMP)", GenerateType.SEQUENCE.getType());
        addOption("dateFormat", "df", "날짜 패턴(SimpleDateFormat)", /*FIXME*/"yyyyMMdd");

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        ////////////////////////////////////////
        // Line Count Hadoop Job
        ///////////////////////////////////////

        // 임시 디렉토리를 가져온다. flamingo-mapreduce-site.xml 파일에 기본값이 정의되어 있다.
        Path temporaryPath = getTimestampTempPath();
        logger.info("Temporary Path : {}", temporaryPath.toString());

        Job lineCountJob = prepareJob(
                getInputPath(), temporaryPath,
                TextInputFormat.class, LineCountMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        boolean step1 = lineCountJob.waitForCompletion(true);
        if (!step1) {
            return JOB_FAIL;
        }

        ////////////////////////////////////////////////
        // Calculating a start number per input split
        ////////////////////////////////////////////////

        // Counter에는 각 Mapper 별로 파일별 위치와 총 ROW의 개수를 포함하고 있다.
        Counters counters = lineCountJob.getCounters();
        CounterGroup group = counters.getGroup(LineCountMapper.class.getName());

        // 파일의 위치별로 정렬한다.
        TreeMap<Long, Long> counterMap = new TreeMap<Long, Long>();
        for (Counter counter : group) {
            try {
                counterMap.put(Long.parseLong(counter.getName()), counter.getValue());
            } catch (NumberFormatException ex) {
            }
        }

        ////////////////////////////////////////
        // Generate Sequence Hadoop Job
        ///////////////////////////////////////

        Job generateSequenceJob = prepareJob(
                getInputPath(), getOutputPath(),
                TextInputFormat.class, GenerateSequenceMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        generateSequenceJob.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        generateSequenceJob.getConfiguration().set("outputDelimiter", parsedArgs.get("--outputDelimiter"));
        generateSequenceJob.getConfiguration().set("sequenceIndex", parsedArgs.get("--sequenceIndex"));
        generateSequenceJob.getConfiguration().set("generateType", parsedArgs.get("--generateType"));
        generateSequenceJob.getConfiguration().set("columnSize", parsedArgs.get("--columnSize"));

        if (GenerateType.valueOf(parsedArgs.get("--generateType")).equals(GenerateType.SEQUENCE)) { // SEQUENCE
            generateSequenceJob.getConfiguration().set("startNumber", parsedArgs.get("--startNumber"));
            int index = generateSequenceJob.getConfiguration().getInt("startNumber", 0);

            for (long position : counterMap.keySet()) {
                generateSequenceJob.getConfiguration().set(String.valueOf(position), String.valueOf(index));
                index += counterMap.get(position);
            }
        } else { // TIMESTAMP
            generateSequenceJob.getConfiguration().set("dateFormat", parsedArgs.get("--dateFormat"));
        }

        boolean step2 = generateSequenceJob.waitForCompletion(true);
        if (!step2) {
            return JOB_FAIL;
        }

        try {
            // 임시 경로를 삭제한다.
            FileSystem.get(generateSequenceJob.getConfiguration()).delete(temporaryPath, true);
            logger.info("Now removed {}", temporaryPath.toString());
        } catch (Exception ex) {
            // Exception handling is not need.
        }
        return JOB_SUCCESS;
    }
}
