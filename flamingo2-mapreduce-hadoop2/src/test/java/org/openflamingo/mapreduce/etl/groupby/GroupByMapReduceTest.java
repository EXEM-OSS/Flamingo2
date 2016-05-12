package org.openflamingo.mapreduce.etl.groupby;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * GroupBy Mapper와 Reducer에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @author Seo Ji Hye
 * @since 1.0
 */
public class GroupByMapReduceTest {

    private Mapper mapper;
    private Reducer reducer;
    private MapReduceDriver driver;

    @Before
    public void setUp() {
        mapper = new GroupByMapper();
        reducer = new GroupByReducer();
        driver = new MapReduceDriver(mapper, reducer);
    }

    @Test
    public void test1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("valueDelimiter", ",");
        conf.set("allowDuplicate", "false");
        conf.set("allowSort", "false");
        conf.set("groupByKey", "0");

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("지혜,a,b"));
        driver.withInput(new LongWritable(2), new Text("지혜,b"));
        driver.withOutput(NullWritable.get(), new Text("지혜,a,b"));
        driver.runTest();
    }

    @Test
    public void testWithDuplicationTrue() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("valueDelimiter", ",");
        conf.set("allowDuplicate", "true");
        conf.set("allowSort", "false");
        conf.set("groupByKey", "0");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("지혜,a,b"));
        driver.withInput(new LongWritable(2), new Text("지혜,b"));
        driver.withOutput(NullWritable.get(), new Text("지혜,a,b,b"));
        driver.runTest();
    }

    @Test
    public void testWithSort() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("valueDelimiter", ",");
        conf.set("allowDuplicate", "true");
        conf.set("allowSort", "true");
        conf.set("groupByKey", "0");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("지혜,1,c,b"));
        driver.withInput(new LongWritable(2), new Text("지혜,2,a,b"));
        driver.withOutput(NullWritable.get(), new Text("지혜,1,2,a,b,b,c"));
        driver.runTest();
    }

    @Test
    public void test2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", "\t");
        conf.set("valueDelimiter", ",");
        conf.set("allowDuplicate", "false");
        conf.set("allowSort", "false");
        conf.set("groupByKey", "1");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,지혜,a"));
        driver.withInput(new LongWritable(2), new Text("2,지혜,b"));
        driver.withOutput(NullWritable.get(), new Text("지혜\t1,a,2,b"));
        driver.runTest();
    }
}
