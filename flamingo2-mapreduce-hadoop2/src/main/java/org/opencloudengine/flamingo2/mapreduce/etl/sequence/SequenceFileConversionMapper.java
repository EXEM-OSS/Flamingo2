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
package org.opencloudengine.flamingo2.mapreduce.etl.sequence;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.util.HdfsUtils;

import java.io.IOException;

public class SequenceFileConversionMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

    private Text filename = new Text();

    private Text content = new Text();

    private StringBuilder builder = new StringBuilder();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        filename.set(HdfsUtils.getFilename(context.getInputSplit()));
    }

    @Override
    protected void map(LongWritable key, Text content, Context context) throws IOException, InterruptedException {
        builder.append(content.toString());
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        content.set(builder.toString());
        context.write(new LongWritable(1), content);
    }
}