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
package org.openflamingo.mapreduce.etl.rank;

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
import org.openflamingo.mapreduce.core.AbstractJob;
import org.openflamingo.mapreduce.core.Delimiter;
import org.openflamingo.mapreduce.etl.generate.GenerateSequenceMapper;
import org.openflamingo.mapreduce.etl.generate.GenerateType;
import org.openflamingo.mapreduce.etl.linecount.LineCountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

import static org.openflamingo.mapreduce.core.Constants.JOB_FAIL;
import static org.openflamingo.mapreduce.core.Constants.JOB_SUCCESS;

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
                NullWritable.class, Text.class,
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