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
package org.opencloudengine.flamingo2.mapreduce.rules.mvel;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.mvel2.MVEL;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * MVEL Expression을 실행하는 MVEL Mapper.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class MvelMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private Configuration configuration;

    private String script;

    private static Text output = new Text();

    private Serializable compiledScript;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        configuration = context.getConfiguration();
        this.script = StringUtils.unescape(configuration.get("encodedScript", ""));

        if (configuration.get("scriptPath") != null) {
            FileSystem fs = FileSystem.get(context.getConfiguration());
            this.script = loadFromFile(fs, configuration.get("scriptPath"));
        }

        this.compiledScript = MVEL.compileExpression(this.script);
    }

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        HashMap vars = new HashMap();
        vars.put("row", value.toString());
        Object obj = MVEL.executeExpression(compiledScript, vars);
        this.output.set(String.valueOf(obj));
        context.write(NullWritable.get(), this.output);
    }

    public static String loadFromFile(FileSystem fs, String sourceFilePath) throws IOException {
        byte[] bytes = loadBytesFromFile(fs, sourceFilePath);
        return new String(bytes);
    }

    public static byte[] loadBytesFromFile(FileSystem fs, String sourceFilePath) throws IOException {
        FSDataInputStream is = fs.open(new Path(sourceFilePath));
        byte[] bytes = FileCopyUtils.copyToByteArray(is);
        IOUtils.closeQuietly(is);
        return bytes;
    }
}