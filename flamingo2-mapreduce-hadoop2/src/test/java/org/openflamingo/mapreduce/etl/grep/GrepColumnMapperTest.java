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
import java.lang.reflect.Method;

/**
 * Grep Mapper에 대한 단위 테스트 케이스.
 *
 * @author Jihye Seo
 * @since 0.1
 */
public class GrepColumnMapperTest {
    private Mapper mapper;
    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new GrepColumnMapper();
        driver = new MapDriver(mapper);
    }

    @Test
    public void test1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "4");
        conf.set("columnsToGrep", "2");
        conf.set("regEx", "b");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("서지혜,a,b,c"));
        driver.withOutput(NullWritable.get(), new Text("서지혜,a,b,c"));
        driver.runTest();
    }

    @Test
    public void test2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "3");
        conf.set("columnsToGrep", "1");
        conf.set("regEx", "b");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,bc,서지혜"));
        driver.withOutput(NullWritable.get(), new Text("a,bc,서지혜"));
        driver.runTest();
    }

    @Test
    public void test3() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "3");
        conf.set("columnsToGrep", "2");
        conf.set("regEx", "김");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,서지혜"));
        driver.runTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test4() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "4");
        conf.set("columnsToGrep", "0,2");
        conf.set("regEx", "서");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("서지혜,a,b,c"));
        driver.withOutput(NullWritable.get(), new Text("서지혜,a,b,c"));
        driver.runTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test5() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "");
        conf.set("columnsToGrep", "0");
        conf.set("regEx", "서");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("서지혜,a,b,c"));
        driver.withOutput(NullWritable.get(), new Text("서지혜,a,b,c"));
        driver.runTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test6() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "4");
        conf.set("columnsToGrep", "");
        conf.set("regEx", "서");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("서지혜,a,b,c"));
        driver.runTest();
    }
}
