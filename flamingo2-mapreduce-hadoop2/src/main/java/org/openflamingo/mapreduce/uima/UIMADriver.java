/**
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

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.ToolRunner;
import org.openflamingo.mapreduce.core.AbstractJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;

import static org.openflamingo.mapreduce.core.Constants.JOB_FAIL;

public class UIMADriver extends AbstractJob {

    private static final Logger LOG = LoggerFactory.getLogger(UIMADriver.class);

    public static void main(String args[]) throws Exception {
        int res = ToolRunner.run(UIMAConfiguration.create(), new UIMADriver(), args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("pear", "p", "UIMA Pear File Path", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        final FileSystem fs = FileSystem.get(getConf());

        // check that the GATE application has been stored on HDFS
        Path zap = new Path(parsedArgs.get("--pear"));
        if (fs.exists(zap) == false) {
            System.err.println("The UIMA Application '" + parsedArgs.get("--pear") + "' can't be found on HDFS - aborting");
            return -1;
        }

        JobConf job = new JobConf(getConf());
        job.setJarByClass(this.getClass());
        job.setJobName("UIMA Application : " + parsedArgs.get("--pear"));
        job.set("uima.pear.path", parsedArgs.get("--pear"));

        job.setInputFormat(SequenceFileInputFormat.class);
        job.setOutputFormat(SequenceFileOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(UIMADocument.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(UIMADocument.class);

        job.setMapperClass(UIMAMapper.class);

        job.setNumReduceTasks(0);

        FileInputFormat.addInputPath(job, getInputPath());
        FileOutputFormat.setOutputPath(job, getOutputPath());

        // push the UIMA pear onto the DistributedCache
        DistributedCache.addCacheFile(new URI(parsedArgs.get("--pear")), job);

        try {
            long start = System.currentTimeMillis();
            JobClient.runJob(job);
            long finish = System.currentTimeMillis();
            if (LOG.isInfoEnabled()) {
                LOG.info("UIMADriver completed. Timing: " + (finish - start) + " ms");
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
            fs.delete(getOutputPath(), true);
        } finally {
        }
        return 0;
    }

}