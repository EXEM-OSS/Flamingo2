package org.openflamingo.mapreduce.etl.replace;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;
import org.openflamingo.mapreduce.etl.replace.column.ReplaceColumnMapper;

import java.io.IOException;

/**
 * Replace Column Mapper에 대한 단위 테스트 케이스.
 *
 * @author Jihye Seo
 * @since 0.1
 */
public class ReplaceColumnMapperTest {
    private Mapper mapper;
    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new ReplaceColumnMapper();
        driver = new MapDriver(mapper);
    }

    @Test
    public void test1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "3");
        conf.set("columnsToReplace", "0");
        conf.set("fromColumnsValues", "a");
        conf.set("toColumnsValues", "b");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c"));
        driver.withOutput(NullWritable.get(), new Text("b,b,c"));
        driver.runTest();
    }

    @Test
    public void test2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("columnSize", "3");
        conf.set("columnsToReplace", "0,2");
        conf.set("fromColumnsValues", "a,c");
        conf.set("toColumnsValues", "b,b");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c"));
        driver.withOutput(NullWritable.get(), new Text("b,b,b"));
        driver.runTest();
    }
}
