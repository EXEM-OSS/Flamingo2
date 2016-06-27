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
package org.opencloudengine.flamingo2.mapreduce.gis.gps;

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
 * GPS 위도, 경도로부터 LINK ID 를 추출하는 MapReduce Driver.
 * 이 MapReduce Driver 는 다음의 파라미터를 가진다.
 * <ul>
 * <li><tt>inputDelimiter (id)</tt> - 입력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>csvPath (cp)</tt> - CSV 파일의 경로 (필수)</li>
 * <li><tt>csvDelimiter (cd)</tt> - CSV 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>linkidColumnIndex (lidc)</tt> - CSV 파일에서 Link ID 컬럼 위치 (필수) (0 부터 시작)</li>
 * <li><tt>longitudeColumnIndex (lngc)</tt> - INPUT 파일에서 경도 컬럼 위치 (필수) (0 부터 시작)</li>
 * <li><tt>latitudeColumnIndex (latc)</tt> - INPUT 파일에서 위도 컬럼 위치 (필수) (0 부터 시작)</li>
 * <li><tt>detectDistance (dd)</tt> - 탐지 거리 (필수) (예: 1.0)</li>
 * </ul>
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class ExtractLinkDriver extends AbstractJob {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new ExtractLinkDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 파일 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("csvPath", "cp", "csv 파일 경로", "");
        addOption("csvDelimiter", "cd", "csv 파일 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("linkidColumnIndex", "lidc", "Link ID 컬럼 위치", true);
        addOption("latitudeColumnIndex", "latc", "위도 컬럼 위치", true);
        addOption("longitudeColumnIndex", "lngc", "경도 컬럼 위치", true);
        addOption("detectDistance", "dd", "탐지 거리", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(
                getInputPath(), getOutputPath(),
                TextInputFormat.class, ExtractLinkMapper.class,
                NullWritable.class, Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        job.getConfiguration().set("csvPath", parsedArgs.get("--csvPath"));
        job.getConfiguration().set("csvDelimiter", parsedArgs.get("--csvDelimiter"));
        job.getConfiguration().set("linkidColumnIndex", parsedArgs.get("--linkidColumnIndex"));
        job.getConfiguration().set("latitudeColumnIndex", parsedArgs.get("--latitudeColumnIndex"));
        job.getConfiguration().set("longitudeColumnIndex", parsedArgs.get("--longitudeColumnIndex"));
        job.getConfiguration().set("detectDistance", parsedArgs.get("--detectDistance"));

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}