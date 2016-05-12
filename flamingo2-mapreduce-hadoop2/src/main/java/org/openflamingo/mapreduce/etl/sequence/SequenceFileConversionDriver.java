
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
package org.openflamingo.mapreduce.etl.sequence;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.openflamingo.mapreduce.core.AbstractJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.openflamingo.mapreduce.core.Constants.JOB_FAIL;
import static org.openflamingo.mapreduce.core.Constants.JOB_SUCCESS;

public class SequenceFileConversionDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger log = LoggerFactory.getLogger(AbstractJob.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new SequenceFileConversionDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Path outputPath = getOutputPath();
        Path inputPath = getInputPath();

        FileSystem fs = FileSystem.get(getConf());
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
            log.info("Deleted '" + outputPath + "'");
        }

        Job job = new Job(new Configuration(getConf()));
        Configuration conf = job.getConfiguration();

        job.setJobName("Full Text File To Sequence File MapReduce Job");

        job.setJarByClass(SequenceFileConversionDriver.class);

        conf.set("mapred.input.dir", inputPath.toString());

        job.setMapperClass(org.openflamingo.mapreduce.etl.sequence.SequenceFileConversionMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        conf.set("mapred.output.dir", outputPath.toString());

        job.setNumReduceTasks(0);

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}