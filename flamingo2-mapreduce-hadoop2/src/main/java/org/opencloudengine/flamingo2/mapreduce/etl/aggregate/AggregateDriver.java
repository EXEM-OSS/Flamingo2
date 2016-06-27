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
package org.opencloudengine.flamingo2.mapreduce.etl.aggregate;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;
import org.opencloudengine.flamingo2.mapreduce.util.HdfsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * 하나 이상의 입력 파일을 받아서 합치는 Aggregation ETL Driver.
 * 이 MapReduce ETL은 다음의 파라미터를 가진다.
 * <ul>
 * <li><tt>lineCountPerFile (c)</tt> - 파일당 라인수 측정 여부 (선택) (기본값 false)</li>
 * <li><tt>merge (c)</tt> - 파일 합치기 여부 (선택) (기본값 false)</li>
 * </ul>
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class AggregateDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(AggregateDriver.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new AggregateDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("lineCountPerFile", "lc", "파일당 라인수 측정(true | false)", "false");
        addOption("merge", "m", "파일 합치기(true | false)", "false");

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(getInputPath(), getOutputPath(),
                TextInputFormat.class,
                AggregateMapper.class,
                NullWritable.class,
                Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("lineCountPerFile", parsedArgs.get("--lineCountPerFile"));
        job.getConfiguration().set("merge", parsedArgs.get("--merge"));

        boolean aggregateJobCompletion = job.waitForCompletion(true);
        if (!aggregateJobCompletion) {
            return JOB_FAIL;
        }

        if (job.getConfiguration().getBoolean("merge", false)) {
            HdfsUtils.merge(job.getConfiguration(), getOutputPath().toString());
        }

        return JOB_SUCCESS;
    }

}