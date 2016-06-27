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
package org.opencloudengine.flamingo2.mapreduce.document;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

public class SequenceFileWriter extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new SequenceFileWriter(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        File inDir = new File(args[0]);
        final String ext = args[1];
        Path name = new Path(args[2]);

        BytesWritable key = new BytesWritable();
        BytesWritable value = new BytesWritable();

        Configuration conf = getConf();
        FileSystem fs = FileSystem.get(conf);
        SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, name,
                BytesWritable.class, BytesWritable.class, CompressionType.NONE);

        String[] files = inDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String filename) {
                return filename.endsWith(ext);
            }
        });

        for (String file : files) {
            String filename = args[0] + File.separator + file;
            System.out.println("Converting " + filename + "  (" + new File(filename).length() + ")");

            FileInputStream fis = new FileInputStream(filename);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(file.length());
            int b;
            while (-1 != (b = fis.read())) {
                baos.write(b);
            }
            fis.close();
            baos.close();

            byte[] bytes = baos.toByteArray();

            key.set(file.getBytes(), 0, file.length());
            value.set(bytes, 0, bytes.length);

            writer.append(key, value);
        }
        writer.close();
        return 0;
    }
}