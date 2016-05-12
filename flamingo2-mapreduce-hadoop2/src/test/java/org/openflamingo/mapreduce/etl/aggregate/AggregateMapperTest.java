package org.openflamingo.mapreduce.etl.aggregate;

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
 * Aggregate Mapper에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @since 0.1*/
public class AggregateMapperTest {

    private Mapper mapper;
    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new AggregateMapper();
        driver = new MapDriver(mapper);
    }

    @Test
    public void map() throws IOException {
        Configuration conf = new Configuration();
        //conf.set("mapred.input.dir", "hdfs://192.168.1.1:9000/home/hadoop/test1.txt,hdfs://192.168.1.1:9000/home/hadoop/test2.txt");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c,d"));
        driver.withOutput(NullWritable.get(), new Text("a,b,c,d"));
        driver.runTest();
    }
}
