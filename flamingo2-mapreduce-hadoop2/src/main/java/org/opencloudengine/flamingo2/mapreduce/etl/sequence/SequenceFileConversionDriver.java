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
package org.opencloudengine.flamingo2.mapreduce.etl.sequence;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

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

        job.setMapperClass(org.opencloudengine.flamingo2.mapreduce.etl.sequence.SequenceFileConversionMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        conf.set("mapred.output.dir", outputPath.toString());

        job.setNumReduceTasks(0);

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}