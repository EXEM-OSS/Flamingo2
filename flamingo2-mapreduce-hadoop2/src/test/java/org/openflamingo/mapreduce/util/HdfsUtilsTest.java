package org.openflamingo.mapreduce.util;

import junit.framework.Assert;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.junit.Test;
import org.openflamingo.mapreduce.util.HdfsUtils;

/**
 * HDFS Utility Unit Test Case.
 *
 * @author Edward KIM
 * @since 0.1
 */
public class HdfsUtilsTest {

    @Test
    public void getFilename() {
        FileSplit split = new FileSplit(new Path("hdfs://192.168.1.1:9000/home/hadoop/test.txt"), 1000, 10000, new String[]{"192.168.1.1:9000"});
        String filename = HdfsUtils.getFilename(split);
        Assert.assertEquals("test.txt", filename);
    }

}
