package org.openflamingo.mapreduce.etl.generate;

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
 * Generate Sequence Mapper에 대한 단위 테스트 케이스.
 *
 * @author Jihye Seo
 * @since 0.1
 */
public class GenerateSequenceMapperTest {
    // MapperTest is useless
	private Mapper mapper;
	private MapDriver driver;

	@Before
	public void setUp() {
		mapper = new GenerateSequenceMapper();
		driver = new MapDriver(mapper);
	}

	@Test
	public void test1() throws IOException {
		Configuration conf = new Configuration();
		conf.set("inputDelimiter", ",");
		conf.set("outputDelimiter", ",");
		conf.set("sequenceIndex", "0");
		conf.set("startNumber", "0");
		conf.set("columnSize", "3");
		conf.set("generateType", "SEQUENCE");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("a,b,c"));
		driver.withOutput(NullWritable.get(), new Text("0,a,b,c"));
		driver.runTest();
	}

	@Test
	public void test2() throws IOException {
		Configuration conf = new Configuration();
		conf.set("inputDelimiter", ",");
		conf.set("outputDelimiter", ",");
		conf.set("sequenceIndex", "2");
		conf.set("startNumber", "0");
		conf.set("columnSize", "3");
        conf.set("generateType", "SEQUENCE");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("a,b,c"));
		driver.withOutput(NullWritable.get(), new Text("a,b,0,c"));
		driver.runTest();
	}
}
