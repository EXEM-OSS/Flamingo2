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

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.openflamingo.mapreduce.core.AbstractJob;
import org.openflamingo.mapreduce.core.Delimiter;
import org.openflamingo.mapreduce.type.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.openflamingo.mapreduce.core.Constants.JOB_FAIL;
import static org.openflamingo.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * 지정한 컬럼의 값을 기준으로 필터링할 조건에 부합하는 경우 해당 ROW를 사용하는 Filter ETL Driver.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class FilterDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(FilterDriver.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new FilterDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("outputDelimiter", "od", "출력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("filterModes", "fm", "필터 옵션(EMPTY, NEMPTY, EQSTR, NEQSTR, EQNUM, NEQNUM, LT, LTE, GT, GTE, STARTWITH, ENDWITH)", true);
        addOption("columnsToFilter", "cf", "필터링 할 컬럼 입력(0부터 시작, ','로 구분)", true);
        addOption("filterValues", "fv", "필터링 할 정규 표현식 또는 숫자", true);
        addOption("dataTypes", "dt", "필터링 할 컬럼값의 데이터 유형(int, long, float, double, string)", DataType.LONG.getDataType());
        addOption("columnSize", "cs", "컬럼의 개수", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(
                getInputPath(), getOutputPath(),
                TextInputFormat.class, FilterMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        job.getConfiguration().set("outputDelimiter", parsedArgs.get("--outputDelimiter"));
        job.getConfiguration().set("columnSize", parsedArgs.get("--columnSize"));
        job.getConfiguration().set("filterModes", parsedArgs.get("--filterModes"));
        job.getConfiguration().set("columnsToFilter", parsedArgs.get("--columnsToFilter"));
        job.getConfiguration().set("filterValues", parsedArgs.get("--filterValues"));
        job.getConfiguration().set("dataTypes", parsedArgs.get("--dataTypes"));

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}