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
package org.opencloudengine.flamingo2.mapreduce.document;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

public class PdfTextExtractionDriver {

    public static void main(String[] args) throws Exception {
        JobConf job = new JobConf(PdfTextExtractionDriver.class);
        parseArguements(args, job);

        job.setJarByClass(PdfTextExtractionDriver.class);

        job.setMapperClass(PdfTextExtractionMapper.class);

        job.setInputFormat(SequenceFileAsBinaryInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setNumReduceTasks(0);

        RunningJob runningJob = JobClient.runJob(job);

        while (true) {
            Thread.sleep(500);
            System.out.println("Map : " + runningJob.mapProgress() * 100 + ", Reducer : " + runningJob.reduceProgress() * 100 + "");
            if (runningJob.isComplete()) {
                System.exit(0);
            } else {
                System.exit(-1);
            }
        }
    }

    private static void parseArguements(String[] args, JobConf job) throws IOException {
        for (int i = 0; i < args.length; ++i) {
            if ("-input".equals(args[i])) {
                SequenceFileAsBinaryInputFormat.setInputPaths(job, args[++i]);
            } else if ("-output".equals(args[i])) {
                FileOutputFormat.setOutputPath(job, new Path(args[++i]));
            } else if ("-line.delimiter".equals(args[i])) {
                job.set("line.delimiter", args[++i]);
            } else if ("-kv.delimiter".equals(args[i])) {
                job.set("kv.delimiter", args[++i]);
            }
        }
    }
}