package org.openflamingo.mapreduce.etl.clean;

import junit.framework.Assert;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Clean Mapper에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @since 0.1
 */
public class CleanMapperTest {

    private Mapper mapper;
    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new CleanMapper();
        driver = new MapDriver(mapper);
    }

    @Test
    public void map1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("outputDelimiter", ",");
        conf.set("columnsToClean", "0");
        conf.set("columnSize", "4");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c,d"));
        driver.withOutput(NullWritable.get(), new Text("b,c,d"));
        driver.runTest();
    }

    @Test
    public void map2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("outputDelimiter", ",");
        conf.set("columnsToClean", "0,1");
        conf.set("columnSize", "4");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c,d"));
        driver.withOutput(NullWritable.get(), new Text("c,d"));
        driver.runTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void map3() throws IOException {
        Configuration conf = new Configuration();
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c,d"));
        driver.withOutput(NullWritable.get(), new Text("c,d"));
        driver.runTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void map4() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "4");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c,d"));
        driver.withOutput(NullWritable.get(), new Text("a,b,c,d"));
        driver.runTest();
    }

    @Test
    public void map5() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnsToClean", "0,1");
        conf.set("columnSize", "5");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c,d"));
        driver.run();

        Assert.assertEquals(1, driver.getCounters().findCounter(CleanMapper.class.getName(), "Wrong Column Size").getValue());
    }
}
