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
package org.opencloudengine.flamingo2.mapreduce.etl.rank;

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
import org.opencloudengine.flamingo2.mapreduce.etl.generate.GenerateSequenceMapper;
import org.opencloudengine.flamingo2.mapreduce.etl.generate.GenerateType;
import org.opencloudengine.flamingo2.mapreduce.etl.linecount.LineCountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class RankDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(RankDriver.class);

    /**
     * temporary index for Generate Sequence
     */
    private final String TEMPORARY_INDEX_FOR_GENERATE = "0";

    public static void main(String[] args) throws Exception {

        int res = ToolRunner.run(new RankDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {

        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("outputDelimiter", "od", "출력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("columnToRank", "cr", "랭킹을 적용할 키 컬럼 인덱스(0부터 시작, 기본값 0)", "0");
        addOption("startNumber", "sn", "랭킹 번호의 시작값(기본값 1)", "1");
        addOption("topK", "tk", "상위 k까지의 순위만 기록한다(기본으로 적용되지 않음)", "-1");
        addOption("columnSize", "cs", "컬럼의 개수", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }


        ////////////////////////////////////////
        // Line Count Hadoop Job
        ///////////////////////////////////////

        Path temporaryPathForLineCounter = getTimestampTempPath();
        logger.info("Temporary Path Line Counter : {}", temporaryPathForLineCounter.toString());

        Job lineCountJob = prepareJob(
                getInputPath(), temporaryPathForLineCounter,
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

        Path temporaryPathForGenerateSequence = getTimestampTempPath();
        logger.info("Temporary Path Generate Sequence : {}", temporaryPathForGenerateSequence.toString());

        Job generateSequenceJob = prepareJob(
                getInputPath(), temporaryPathForGenerateSequence,
                TextInputFormat.class, GenerateSequenceMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        generateSequenceJob.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        generateSequenceJob.getConfiguration().set("outputDelimiter", parsedArgs.get("--inputDelimiter"));
        generateSequenceJob.getConfiguration().set("columnSize", parsedArgs.get("--columnSize"));
        generateSequenceJob.getConfiguration().set("generateType", GenerateType.SEQUENCE.getType());
        generateSequenceJob.getConfiguration().set("sequenceIndex", TEMPORARY_INDEX_FOR_GENERATE);
        generateSequenceJob.getConfiguration().set("startNumber", parsedArgs.get("--startNumber"));

        int index = generateSequenceJob.getConfiguration().getInt("startNumber", 0);

        for (long position : counterMap.keySet()) {
            generateSequenceJob.getConfiguration().set(String.valueOf(position), String.valueOf(index));
            index += counterMap.get(position);
        }

        boolean step2 = generateSequenceJob.waitForCompletion(true);
        if (!step2) {
            return JOB_FAIL;
        }


        ////////////////////////////////////////
        // Rank Hadoop Job
        ///////////////////////////////////////

        Job rankJob = prepareJob(
                temporaryPathForGenerateSequence, getOutputPath(),
                TextInputFormat.class, RankMapper.class,
                Text.class, Text.class,
                RankReducer.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        rankJob.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        rankJob.getConfiguration().set("outputDelimiter", parsedArgs.get("--inputDelimiter"));
        rankJob.getConfiguration().set("columnToRank", parsedArgs.get("--columnToRank"));
        rankJob.getConfiguration().set("startNumber", parsedArgs.get("--startNumber"));
        rankJob.getConfiguration().set("topK", parsedArgs.get("--topK"));
        rankJob.getConfiguration().set("columnSize", String.valueOf(Integer.parseInt(parsedArgs.get("--columnSize")) + 1));
        rankJob.getConfiguration().set("generatedSequenceIndex", TEMPORARY_INDEX_FOR_GENERATE);


        // Run a Hadoop Job
        boolean step3 = rankJob.waitForCompletion(true);
        if (!step3) {
            return JOB_FAIL;
        }

        ////////////////////////////////////////
        // Remove Temporary directories
        ///////////////////////////////////////

        try {
            FileSystem.get(rankJob.getConfiguration()).delete(temporaryPathForLineCounter, true);
            logger.info("Now removed {}", temporaryPathForLineCounter.toString());
            FileSystem.get(rankJob.getConfiguration()).delete(temporaryPathForGenerateSequence, true);
            logger.info("Now removed {}", temporaryPathForGenerateSequence.toString());
        } catch (Exception ex) {
            // Exception handling is not need.
        }

        return JOB_SUCCESS;
    }
}