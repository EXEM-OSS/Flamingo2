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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.util.CounterUtils;
import org.opencloudengine.flamingo2.mapreduce.util.HdfsUtils;

import java.io.IOException;

/**
 * 하나 이상의 입력 파일을 받아서 합치는 Aggregation ETL Mapper.
 * 이 Mapper는 입력을 그대로 다시 출력한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class AggregateMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     * Input Split의 file name
     */
    private String filename;

    /**
     * 개별 파일당 라인의 개수를 수집할지 여부
     */
    private boolean lineCountPerFile;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        lineCountPerFile = configuration.getBoolean("lineCountPerFile", false);
        if (lineCountPerFile) {
            InputSplit inputSplit = context.getInputSplit();
            try {
                filename = HdfsUtils.getFilename(inputSplit);
            } catch (Exception ex) {
                CounterUtils.writerMapperCounter(this, "Cannot get a file name from input split", context);
            }
        }
    }

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (lineCountPerFile) CounterUtils.writerMapperCounter(this, filename, context);
        context.write(NullWritable.get(), value);
    }

}