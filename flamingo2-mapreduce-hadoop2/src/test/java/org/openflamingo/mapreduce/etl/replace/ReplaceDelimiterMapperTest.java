package org.openflamingo.mapreduce.etl.replace;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;
import org.openflamingo.mapreduce.etl.replace.delimiter.ReplaceDelimiterMapper;

import java.io.IOException;

/**
 * Replace Delimiter Mapper에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @author Seo Ji Hye
 * @since 1.0
 */
public class ReplaceDelimiterMapperTest {
    private Mapper mapper;
    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new ReplaceDelimiterMapper();
        driver = new MapDriver(mapper);
    }

    @Test
    public void testSingleCharInputDelimiter() throws IOException {
        Configuration conf = new Configuration();
        conf.set("from", ":");
        conf.set("to", ",");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1:2:3:4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void testSingleCharInputDeliToMultiCharOutDeli() throws IOException {
        Configuration conf = new Configuration();
        conf.set("from", ",");
        conf.set("to", "::");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.withOutput(NullWritable.get(), new Text("1::2::3::4"));
        driver.runTest();
    }

    @Test
    public void testMultiCharInputDelimiter() throws IOException {
        Configuration conf = new Configuration();
        conf.set("from", "::");
        conf.set("to", ",");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1::2::3::4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }
}
