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
package org.opencloudengine.flamingo2.mapreduce.etl.groupby;

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
 * 지정한 키로 Group By하여 하나의 로우로 키와 값을 취합하는 Transpose ETL Driver.
 * 이 MapReduce ETL은 다음의 파라미터를 가진다.
 * <ul>
 * <li><tt>inputDelimiter (id)</tt> - 입력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>outputDelimiter (od)</tt> - 출력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>groupByKey (k)</tt> - Group By의 기준이 될 컬럼의 위치 (필수) (0부터 시작)</li>
 * <li><tt>allowSort (as)</tt> - Group By시 아이템의 정렬 여부 (선택) (기본값 false)</li>
 * <li><tt>valueDelimiter (vd)</tt> - Group By시 아이템간 구분자 (선택) (기본값 ,)</li>
 * </ul>
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class GroupByDriver extends AbstractJob {

    /**
     * SLF4J API
     */
    private static final Logger logger = LoggerFactory.getLogger(GroupByDriver.class);

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new GroupByDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("inputDelimiter", "id", "입력 파일 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("keyValueDelimiter", "kd", "출력 파일의 key와 values들간의 컬럼 구분자", Delimiter.TAB.getDelimiter());
        addOption("valueDelimiter", "vd", "아이템의 구분자", Delimiter.COMMA.getDelimiter());
        addOption("groupByKey", "gk", "Group By의 기준이 될 컬럼의 위치 (0부터 시작)", true);
        addOption("allowDuplicate", "ad", "중복 아이템의 허용 여부(기본값 false)", "false");
        addOption("allowSort", "as", "Group By시 아이템의 정렬 여부(기본값 false)", "false");

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Job job = prepareJob(getInputPath(), getOutputPath(),
                TextInputFormat.class,
                GroupByMapper.class,
                Text.class,
                Text.class,
                GroupByReducer.class,
                NullWritable.class,
                Text.class,
                TextOutputFormat.class);

        job.getConfiguration().set("inputDelimiter", parsedArgs.get("--inputDelimiter"));
        job.getConfiguration().set("keyValueDelimiter", parsedArgs.get("--keyValueDelimiter"));
        job.getConfiguration().set("groupByKey", parsedArgs.get("--groupByKey"));
        job.getConfiguration().set("allowDuplicate", parsedArgs.get("--allowDuplicate"));
        job.getConfiguration().set("allowSort", parsedArgs.get("--allowSort"));
        job.getConfiguration().set("valueDelimiter", parsedArgs.get("--valueDelimiter"));

        return job.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }
}