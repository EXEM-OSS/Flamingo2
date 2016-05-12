/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.uima;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.openflamingo.mapreduce.core.AbstractJob;

import static org.openflamingo.mapreduce.core.Constants.JOB_FAIL;
import static org.openflamingo.mapreduce.core.Constants.JOB_SUCCESS;

public class SequenceFileConversionDriver extends AbstractJob {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(UIMAConfiguration.create(), new SequenceFileConversionDriver(), args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        if (parseArguments(args) == null) {
            return -1;
        }

        Path input = getInputPath();
        Path output = getOutputPath();

        FileSystem fs = FileSystem.get(getConf());
        if (fs.exists(output)) {
            fs.delete(output, true);
            System.out.println("Deleted '" + output + "'");
        }

        Job job = new Job(new Configuration(getConf()));
        Configuration configuration = job.getConfiguration();

        job.setJobName("UIMA : Text To Sequence");
        job.setJarByClass(SequenceFileConversionDriver.class);

        configuration.set("mapred.input.dir", input.toString());
        configuration.set("mapred.output.dir", output.toString());

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        job.setMapperClass(SequenceFileConversionMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(UIMADocument.class);

        job.setNumReduceTasks(0);

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }

}
