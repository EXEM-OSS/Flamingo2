package org.openflamingo.mapreduce.document;

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