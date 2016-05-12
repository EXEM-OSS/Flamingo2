package org.openflamingo.mapreduce.etl.rank;

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
 * Rank Mapper와 Reducer에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @author Seo Ji Hye
 * @since 1.0
 */
public class RankMapReduceTest {

    private Mapper mapper;
    private Reducer reducer;
    private MapReduceDriver driver;

    @Before
    public void setUp() {
        mapper = new RankMapper();
        reducer = new RankReducer();
        driver = new MapReduceDriver(mapper, reducer);
    }

    @Test
    public void test1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("columnToRank", "0");
        conf.set("startNumber", "1");
        conf.set("columnSize", "2");
        conf.set("generatedSequenceIndex", "0");

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,Harry,a"));
        driver.withInput(new LongWritable(2), new Text("2,Harry,b"));
        driver.withOutput(NullWritable.get(), new Text("Harry,a,1"));
        driver.withOutput(NullWritable.get(), new Text("Harry,b,2"));
        driver.runTest();
    }

    @Test
    public void test2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("columnToRank", "0");
        conf.set("startNumber", "1");
        conf.set("columnSize", "2");
        conf.set("generatedSequenceIndex", "0"); // driver에서 입력해야 할 속성.

        driver.setConfiguration(conf);

        // test의 경우 0번째가 sequence이지만 실제 실행시의 file에는 sequence가 없으므로
        // columnToRank는 sequence의 인덱스를 무시하고 작성하면 된다.
        driver.withInput(new LongWritable(1), new Text("0,Harry,a"));
        driver.withInput(new LongWritable(2), new Text("1,Harry,b"));
        driver.withInput(new LongWritable(3), new Text("2,Ron,a"));

        driver.withOutput(NullWritable.get(), new Text("Harry,a,1"));
        driver.withOutput(NullWritable.get(), new Text("Harry,b,2"));
        driver.withOutput(NullWritable.get(), new Text("Ron,a,1"));
        driver.runTest();
    }

    @Test
    public void test3() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("columnToRank", "0");
        conf.set("startNumber", "1");
        conf.set("columnSize", "2");
        conf.set("generatedSequenceIndex", "1");

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("Harry,1,a"));
        driver.withInput(new LongWritable(2), new Text("Harry,2,b"));
        driver.withInput(new LongWritable(3), new Text("Ron,3,a"));

        driver.withOutput(NullWritable.get(), new Text("Harry,a,1"));
        driver.withOutput(NullWritable.get(), new Text("Harry,b,2"));
        driver.withOutput(NullWritable.get(), new Text("Ron,a,1"));
        driver.runTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test4() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("columnToRank", "");
        conf.set("startNumber", "1");
        conf.set("columnSize", "2");
        conf.set("generatedSequenceIndex", "0");

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("0,Harry,a"));
        driver.withInput(new LongWritable(2), new Text("1,Harry,b"));
        driver.withInput(new LongWritable(3), new Text("2,Ron,a"));

        driver.withOutput(NullWritable.get(), new Text("Harry,a,1"));
        driver.withOutput(NullWritable.get(), new Text("Harry,b,2"));
        driver.withOutput(NullWritable.get(), new Text("Ron,a,1"));
        driver.runTest();
    }

    @Test
    public void testTopK() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("columnToRank", "0");
        conf.set("startNumber", "1");
        conf.set("topK", "3");
        conf.set("columnSize", "2");
        conf.set("generatedSequenceIndex", "0");

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("0,Harry,a"));
        driver.withInput(new LongWritable(2), new Text("1,Harry,b"));
        driver.withInput(new LongWritable(3), new Text("2,Harry,c"));
        driver.withInput(new LongWritable(4), new Text("3,Harry,d"));
        driver.withInput(new LongWritable(5), new Text("4,Harry,e"));

        driver.withOutput(NullWritable.get(), new Text("Harry,a,1"));
        driver.withOutput(NullWritable.get(), new Text("Harry,b,2"));
        driver.withOutput(NullWritable.get(), new Text("Harry,c,3"));
        driver.runTest();
    }

    @Test
    public void testTopK2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("keyValueDelimiter", ",");
        conf.set("columnToRank", "0");
        conf.set("startNumber", "1");
        conf.set("topK", "3");
        conf.set("columnSize", "2");
        conf.set("generatedSequenceIndex", "0");

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("0,Harry,a"));
        driver.withInput(new LongWritable(2), new Text("1,Harry,b"));
        driver.withInput(new LongWritable(3), new Text("3,Ron,a"));
        driver.withInput(new LongWritable(4), new Text("4,Ron,b"));
        driver.withInput(new LongWritable(5), new Text("5,Ron,c"));
        driver.withInput(new LongWritable(6), new Text("6,Ron,d"));

        driver.withOutput(NullWritable.get(), new Text("Harry,a,1"));
        driver.withOutput(NullWritable.get(), new Text("Harry,b,2"));
        driver.withOutput(NullWritable.get(), new Text("Ron,a,1"));
        driver.withOutput(NullWritable.get(), new Text("Ron,b,2"));
        driver.withOutput(NullWritable.get(), new Text("Ron,c,3"));
        driver.runTest();
    }
}
