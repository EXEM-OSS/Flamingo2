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
 * 정규 표현식을 이용하여 로우를 Grep하는 Grep ETL Driver.
 * 이 MapReduce ETL은 다음의 파라미터를 가진다.
 * <ul>
 * <li><tt>regEx (r)</tt> - 정규 표현식 (필수)</li>
 * </ul>
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class GrepDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(GrepDriver.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new GrepDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("outputDelimiter", "od", "출력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("columnSize", "cs", "컬럼의 개수", true);
        addOption("grepMode", "gm", "Grep Mode 선택(ROW, COLUMN)", true);
        addOption("columnsToGrep", "cg", "COLUMN모드시 grep할 컬럼 입력(0부터 시작)", false);
        addOption("regEx", "re", "Grep할 정규 표현식", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = null;
        String grepMode = parsedArgs.get("--grepMode");
        if ("ROW".equalsIgnoreCase(grepMode)) {
            // make row mapper job
            job = prepareJob(getInputPath(), getOutputPath(),
                    TextInputFormat.class,
                    GrepRowMapper.class,
                    NullWritable.class,
                    Text.class,
                    TextOutputFormat.class);
        } else if ("COLUMN".equalsIgnoreCase(grepMode)) {
            // make column mapper job
            job = prepareJob(getInputPath(), getOutputPath(),
                    TextInputFormat.class,
                    GrepColumnMapper.class,
                    NullWritable.class,
                    Text.class,
                    TextOutputFormat.class);

            job.getConfiguration().set("columnsToGrep", parsedArgs.get("--columnsToGrep"));
        } else {
            throw new IllegalArgumentException("Grep Mode가 올바르지 않습니다. {}");
        }

        job.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        job.getConfiguration().set("outputDelimiter", parsedArgs.get("--outputDelimiter"));
        job.getConfiguration().set("columnSize", parsedArgs.get("--columnSize"));
        job.getConfiguration().set("regEx", parsedArgs.get("--regEx"));

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}