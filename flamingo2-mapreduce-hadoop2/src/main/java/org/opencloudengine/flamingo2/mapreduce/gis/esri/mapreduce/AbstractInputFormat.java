package org.opencloudengine.flamingo2.mapreduce.gis.esri.mapreduce;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
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
abstract class AbstractInputFormat<T extends Writable> extends FileInputFormat<LongWritable, T> {

    @Override
    protected List<FileStatus> listStatus(final JobContext job) throws IOException {
        final List<FileStatus> list = super.listStatus(job);
        Iterator<FileStatus> iterator = list.iterator();
        while (iterator.hasNext()) {
            FileStatus fileStatus = iterator.next();
            if (!fileStatus.getPath().getName().toLowerCase().endsWith(".shp")) {
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
    public abstract RecordReader<LongWritable, T> createRecordReader(
            final InputSplit inputSplit,
            final TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException;
}