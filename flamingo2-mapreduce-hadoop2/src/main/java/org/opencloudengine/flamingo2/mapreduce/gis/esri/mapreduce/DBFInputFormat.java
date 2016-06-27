package org.opencloudengine.flamingo2.mapreduce.gis.esri.mapreduce;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 */
public class DBFInputFormat extends FileInputFormat<LongWritable, MapWritable> {

    @Override
    protected List<FileStatus> listStatus(final JobContext job) throws IOException {
        List<FileStatus> list = super.listStatus(job);
        Iterator<FileStatus> iterator = list.iterator();
        while (iterator.hasNext()) {
            FileStatus fileStatus = iterator.next();
            if (!fileStatus.getPath().getName().toLowerCase().endsWith(".dbf")) {
                iterator.remove();
            }
        }
        return list;
    }

    @Override
    protected boolean isSplitable(final JobContext context, final Path filename) {
        return false;
    }

    @Override
    public RecordReader<LongWritable, MapWritable> createRecordReader(
            final InputSplit inputSplit,
            final TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        String characterSet = taskAttemptContext.getConfiguration().get("characterSet", "UTF-8");
        return new DBFRecordReader(inputSplit, taskAttemptContext, characterSet);
    }
}