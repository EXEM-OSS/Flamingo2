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
 * 지정한 칼럼의 값을 변경하는 Replace ETL Driver.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class ReplaceColumnDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(ReplaceColumnDriver.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new ReplaceColumnDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("outputDelimiter", "od", "출력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("columnSize", "cs", "변경할 컬럼 갯수", true);
        addOption("columnsToReplace", "cr", "변경할 컬럼의 위치(콤마로 구분)", true);
        addOption("fromColumnsValues", "fv", "원본값(콤마로 구분)", true);
        addOption("toColumnsValues", "tv", "변경할 값(콤마로 구분)", true);
        //addOption("replacerClassNames", "r", "Replacer의 클래스명(콤마로 구분)", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(
                getInputPath(), getOutputPath(),
                TextInputFormat.class, ReplaceColumnMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        job.getConfiguration().set("outputDelimiter", parsedArgs.get("--outputDelimiter"));
        job.getConfiguration().set("columnSize", parsedArgs.get("--columnSize"));
        job.getConfiguration().set("columnsToReplace", parsedArgs.get("--columnsToReplace"));
        job.getConfiguration().set("fromColumnsValues", parsedArgs.get("--fromColumnsValues"));
        job.getConfiguration().set("toColumnsValues", parsedArgs.get("--toColumnsValues"));
        // FIXME Replacer is not need yet.
        //job.getConfiguration().set("replacerClassNames", parsedArgs.get("--replacerClassNames"));

        // Run a Hadoop Job
        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}