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
package org.opencloudengine.flamingo2.mapreduce.etl.replace.column;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

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
        addOption("columnSize", "cs", "컬럼 개수", true);
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