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
package org.opencloudengine.flamingo2.mapreduce.etl.apache;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;

import java.util.Map;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * Apache HTTP Server 로그를 정형 로그로 변경하는 ETL MapReduce Driver.
 *
 * @author Byoung Gon, KIM
 * @since 1.1
 */
public class ApacheHttpAccessLogDriver extends AbstractJob {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new ApacheHttpAccessLogDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("outputDelimiter", "od", "출력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("printMismatch", "pd", "일치하지 않는 형식인 경우 출력", false);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(
                getInputPath(), getOutputPath(),
                TextInputFormat.class, ApacheHttpAccessLogMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("outputDelimiter", parsedArgs.get("--outputDelimiter"));
        job.getConfiguration().set("printMismatch", parsedArgs.get("--printMismatch"));

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}