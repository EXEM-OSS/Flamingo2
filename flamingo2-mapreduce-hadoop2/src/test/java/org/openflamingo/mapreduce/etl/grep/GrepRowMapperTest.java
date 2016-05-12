package org.openflamingo.mapreduce.etl.grep;

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
 * Grep Mapper에 대한 단위 테스트 케이스.
 *
 * @author Jihye Seo
 * @since 0.1
 */
public class GrepRowMapperTest {
    private Mapper mapper;
    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new GrepRowMapper();
        driver = new MapDriver(mapper);
    }

    @Test
    public void test1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("regEx", "서");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("서지혜,a,b,c"));
        driver.withOutput(NullWritable.get(), new Text("서지혜,a,b,c"));
        driver.runTest();
    }

    @Test
    public void test2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("regEx", "서");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,서지혜"));
        driver.withOutput(NullWritable.get(), new Text("a,b,서지혜"));
        driver.runTest();
    }

    @Test
    public void test3() throws IOException {
        Configuration conf = new Configuration();
        conf.set("regEx", "서");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,서지혜,b,서지혜"));
        driver.withOutput(NullWritable.get(), new Text("a,서지혜,b,서지혜"));
        driver.runTest();
    }

    @Test
    public void test4() throws IOException {
        Configuration conf = new Configuration();
        conf.set("regEx", "서");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c"));
        driver.runTest();
    }

    @Test
    public void testRegularExpression1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("regEx", "a+b");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("ab,a,b"));
        driver.withOutput(NullWritable.get(), new Text("ab,a,b"));
        driver.runTest();
    }

    @Test
    public void testRegularExpression2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("regEx", "a+b");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("aab,a,b"));
        driver.withOutput(NullWritable.get(), new Text("aab,a,b"));
        driver.runTest();
    }

    @Test
    public void testRegularExpression3() throws IOException {
        Configuration conf = new Configuration();
        conf.set("regEx", "a+b");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,a,b"));
        driver.runTest();
    }
}
