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
package org.opencloudengine.flamingo2.mapreduce.applications.sabermetrics;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import java.util.Map;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * Sabermetrics MapReduce Driver.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class SabermetricsDriver extends AbstractJob {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new SabermetricsDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("encodingScript", "s", "Escape 처리한 MVEL 스크립트", StringUtils.escape(""));

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(getInputPath(), getOutputPath(),
                TextInputFormat.class,
                SabermetricsMapper.class,
                NullWritable.class,
                Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("encodingScript", parsedArgs.get("--encodingScript"));

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}