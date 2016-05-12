/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.etl.sequence;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.openflamingo.mapreduce.util.HdfsUtils;

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