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
package org.opencloudengine.flamingo2.mapreduce.etl.clean;

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
 * CSV 형식의 입력 파일에서 지정한 칼럼을 삭제하는 Clean ETL Driver.
 * 이 MapReduce ETL은 다음의 파라미터를 가진다.
 * <ul>
 * <li><tt>inputDelimiter (id)</tt> - 입력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>outputDelimiter (od)</tt> - 출력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>columnsToClean (cc)</tt> - 삭제할 컬럼의 인덱스 (필수) (예; 1,2,3)</li>
 * <li><tt>columnSize (cs)</tt> - 컬럼의 개수 (필수) (예; 4)</li>
 * </ul>
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class CleanDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(CleanDriver.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new CleanDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("outputDelimiter", "od", "출력 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("columnsToClean", "cc", "삭제할 컬럼 목록(0부터 시작, ','로 구분)", true);
        addOption("columnSize", "cs", "컬럼의 개수", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(
                getInputPath(), getOutputPath(),
                TextInputFormat.class, CleanMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        job.getConfiguration().set("outputDelimiter", parsedArgs.get("--outputDelimiter"));
        job.getConfiguration().set("columnsToClean", parsedArgs.get("--columnsToClean"));
        job.getConfiguration().set("columnSize", parsedArgs.get("--columnSize"));

        // Run a Hadoop Job
        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}