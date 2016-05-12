package org.openflamingo.mapreduce.etl.filter;

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
 * Filter Mapper에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @author Seo Ji Hye
 * @since 0.1
 */
public class FilterMapperTest {

    private Mapper mapper;

    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new FilterMapper();
        driver = new MapDriver(mapper);
    }

    private Configuration getConfiguration() {
        Configuration conf = new Configuration();
        conf.set("inputDelimiter", ",");
        conf.set("outputDelimiter", ",");
        return conf;
    }

    @Test
    public void emptyTest1() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");
        conf.set("filterModes", "empty");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,,d"));
        driver.withOutput(NullWritable.get(), new Text("a,b,,d"));
        driver.runTest();
    }

    @Test
    public void emptyTest2() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");
        conf.set("filterModes", "EMPTY");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c,d"));
        driver.runTest();
    }

    @Test
    public void nonEmptyTest1() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");
        conf.set("filterModes", "NEMPTY");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,c,d"));
        driver.withOutput(NullWritable.get(), new Text("a,b,c,d"));
        driver.runTest();
    }

    @Test
    public void nonEmptyTest2() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "NEMPTY");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("a,b,,d"));
        driver.runTest();
    }

    @Test
    public void equalNumberTest1() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "EQNUM");
        conf.set("dataTypes", "int");
        conf.set("columnsToFilter", "2");
        conf.set("filterValues", "3");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void equalNumberTest2() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "EQNUM");
        conf.set("filterValues", "4");
        conf.set("dataTypes", "int");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void nonEqualNumberTest1() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "NEQNUM");
        conf.set("columnsToFilter", "2");
        conf.set("dataTypes", "int");
        conf.set("filterValues", "4");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void nonEqualNumberTest2() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "NEQNUM");
        conf.set("columnsToFilter", "2");
        conf.set("filterValues", "3");
        conf.set("dataTypes", "int");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void lessThanTest() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "LT");
        conf.set("columnsToFilter", "2");
        conf.set("filterValues", "4");
        conf.set("dataTypes", "int");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void lessThanEqualTest() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "LTE");
        conf.set("columnsToFilter", "2");
        conf.set("filterValues", "3");
        conf.set("dataTypes", "int");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void greaterThanTest() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "GT");
        conf.set("columnsToFilter", "2");
        conf.set("filterValues", "2");
        conf.set("dataTypes", "int");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void greaterThanEqualTest() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "GTE");
        conf.set("filterValues", "3");
        conf.set("columnsToFilter", "2");
        conf.set("dataTypes", "int");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void startWith() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "STARTWITH");
        conf.set("filterValues", "start");
        conf.set("columnsToFilter", "2");
        conf.set("dataTypes", "string");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,start with,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,start with,4"));
        driver.runTest();
    }

    @Test
    public void endWith() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "ENDWITH");
        conf.set("filterValues", "with");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,end with,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,end with,4"));
        driver.runTest();
    }

    @Test
    public void equalString() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "EQSTR");
        conf.set("filterValues", "equal");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,equal,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,equal,4"));
        driver.runTest();
    }

    @Test
    public void nonEqualString() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");

        conf.set("filterModes", "NEQSTR");
        conf.set("filterValues", "equal");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,not equal,4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,not equal,4"));
        driver.runTest();
    }

    @Test(expected = IllegalStateException.class)
    public void test1() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "");
        conf.set("filterModes", "NEQSTR");
        conf.set("filterValues", "equal");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "2");
        driver.setConfiguration(conf);

        driver.runTest();
    }

    @Test(expected = IllegalStateException.class)
    public void test2() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");
        conf.set("filterModes", "NEQSTR");
        conf.set("filterValues", "equal");
        conf.set("dataTypes", "string");
        conf.set("columnsToFilter", "");
        driver.setConfiguration(conf);

        driver.runTest();
    }

    @Test
    public void complexFilterTest() throws IOException {
        Configuration conf = getConfiguration();
        conf.set("columnSize", "4");
        conf.set("filterModes", "STARTWITH,EMPTY");
        conf.set("filterValues", "h,");
        conf.set("dataTypes", "string,");
        conf.set("columnsToFilter", "0,1");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("hello,1,2,3"));
        driver.withOutput(NullWritable.get(), new Text("hello,1,2,3"));
        driver.runTest();
    }
}
