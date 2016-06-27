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
package org.opencloudengine.flamingo2.mapreduce.rules.drools;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;
import org.opencloudengine.flamingo2.mapreduce.util.ResourceUtils;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * Drools .drl Rule 파일을 실행하는 MapReduce Driver.
 * <ul>
 * <li><tt>encodedRule (e)</tt> - Escape 처리한 Drools .drl 파일의 내용</li>
 * <li><tt>rulePath (s)</tt> - Drools .drl 파일의 HDFS 경로</li>
 * <li><tt>columnNames (n)</tt> - 로그의 컬럼명 (구분자 콤마; 예) a,b,c)</li>
 * <li><tt>delimiter (d)</tt> - 로그 파일의 컬럼 구분자</li>
 * <li><tt>deleteOutput (do)</tt> - 출력 경로가 존재하는 경우 삭제할지 여부</li>
 * </ul>
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class DroolsDriver extends AbstractJob {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new DroolsDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("encodedRule", "e", "Escape 처리한 Drools .drl 파일의 내용", StringUtils.escape(""));
        addOption("rulePath", "s", "Drools .drl 파일의 HDFS 경로", false);
        addOption("columnNames", "n", "로그의 컬럼명 (구분자 콤마; 예) a,b,c)", true);
        addOption("delimiter", "d", "로그 파일의 컬럼 구분자", true);
        addOption("deleteOutput", "do", "출력 경로가 존재하는 경우 삭제할지 여부", "true");

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(getInputPath(), getOutputPath(),
                TextInputFormat.class,
                DroolsMapper.class,
                NullWritable.class,
                Text.class,
                TextOutputFormat.class);

        // If exists output path, then delete output path.
        if (Boolean.parseBoolean(parsedArgs.get("--deleteOutput"))) {
            FileSystem fs = FileSystem.get(job.getConfiguration());
            fs.delete(new Path(parsedArgs.get("--output")), true);

            log.info("Deleted output path : " + parsedArgs.get("--output"));
        }

        job.getConfiguration().set("columnNames", parsedArgs.get("--columnNames"));
        job.getConfiguration().set("delimiter", parsedArgs.get("--delimiter"));

        if (!StringUtils.isEmpty(parsedArgs.get("--encodedRule"))) {
            job.getConfiguration().set("encodedRule", parsedArgs.get("--encodedRule"));
        }

        // If the script file exists in working directory, load the script file.
        String rulePath = parsedArgs.get("--rulePath");
        if (!StringUtils.isEmpty(rulePath)) {
            String workingDirectory = System.getProperty("user.dir");
            File sf = new File(workingDirectory, rulePath);
            if (sf.exists()) {
                log.info("Found Drools rule file '" + sf.getAbsolutePath() + "'. We will use a found rule file.");
                String script = ResourceUtils.getResourceTextContents(new FileInputStream(sf));
                job.getConfiguration().set("encodedRule", StringUtils.escape(script));
            } else {
                job.getConfiguration().set("rulePath", parsedArgs.get("--rulePath"));
            }
        }

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}