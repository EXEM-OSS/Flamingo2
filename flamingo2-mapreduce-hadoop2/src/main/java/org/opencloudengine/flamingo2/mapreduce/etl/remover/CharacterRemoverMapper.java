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
package org.opencloudengine.flamingo2.mapreduce.etl.remover;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.util.CounterUtils;

import java.io.IOException;

/**
 * 지정한 문자열을 ROW에서 제거하는 ETL Mapper.
 *
 * @author Byoung Gon, Kim
 * @since 1.1
 */
public class CharacterRemoverMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private String characters;

    private Text output = new Text();

    @Override
    public void setup(Context context) {
        Configuration configuration = context.getConfiguration();
        characters = configuration.get("characters", " ");
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            String removed = StringUtils.remove(value.toString(), characters);
            output.set(removed);
            context.write(NullWritable.get(), output);
            CounterUtils.writerMapperCounter(this, "REMOVED", context);
        } catch (Exception ex) {
            CounterUtils.writerMapperCounter(this, "INVALID", context);
        }
    }
}
