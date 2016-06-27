/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.flamingo2.mapreduce.applications.sabermetrics;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.opencloudengine.flamingo2.mapreduce.util.ResourceUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Sabermetrics Mapper.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class SabermetricsMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private Configuration configuration;

    private String script;

    private static Text output = new Text();

    private VariableResolverFactory variableFactory;

    private Serializable compiledScript;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        configuration = context.getConfiguration();
        this.script = ResourceUtils.getResourceTextContents("classpath:org/opencloudengine/flamingo2/mapreduce/applications/sabermetrics/sabermetrics.mvel");

        VariableResolverFactory functionFactory = new MapVariableResolverFactory();
        this.variableFactory = new MapVariableResolverFactory();
        variableFactory.setNextFactory(functionFactory);
        this.compiledScript = MVEL.compileExpression(this.script);
    }

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        HashMap vars = new HashMap();
        vars.put("row", value.toString());
        Object obj = MVEL.executeExpression(compiledScript, variableFactory);
        this.output.set(String.valueOf(obj));
        context.write(NullWritable.get(), this.output);
    }
}