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

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.openflamingo.mapreduce.core.AbstractJob;
import org.openflamingo.mapreduce.core.Delimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.openflamingo.mapreduce.core.Constants.JOB_FAIL;
import static org.openflamingo.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * CSV 형식의 입력 파일에서 지정한 칼럼을 삭제하는 Clean ETL Driver.
 * 이 MapReduce ETL은 다음의 파라미터를 가진다.
 * <ul>
 * <li><tt>inputDelimiter (id)</tt> - 입력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>outputDelimiter (od)</tt> - 출력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>columnsToClean (c)</tt> - 삭제할 컬럼의 인덱스 (필수) (예; 1,2,3)</li>
 * <li><tt>columnSize (s)</tt> - 컬럼의 개수 (필수) (예; 4)</li>
 * </ul>
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class CleanDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(CleanDriver.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new CleanDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("outputDelimiter", "od", "출력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("columnsToClean", "cc", "삭제할 컬럼 목록(0부터 시작, ','로 구분)", true);
        addOption("columnSize", "cs", "컬럼의 개수", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(
                getInputPath(), getOutputPath(),
                TextInputFormat.class, CleanMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        job.getConfiguration().set("outputDelimiter", parsedArgs.get("--outputDelimiter"));
        job.getConfiguration().set("columnsToClean", parsedArgs.get("--columnsToClean"));
        job.getConfiguration().set("columnSize", parsedArgs.get("--columnSize"));

        // Run a Hadoop Job
        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}