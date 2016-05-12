package org.openflamingo.mapreduce.etl.accounting;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.ScriptRuntimeException;
import org.openflamingo.mapreduce.etl.clean.CleanMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Description.
 *
 * @author Edward KIM
 * @since 1.0
 */
public class AccountingMapTest {
	private Mapper mapper;
	private MapDriver driver;

	@Before
	public void setUp() {
		mapper = new AccountingMapper();
		driver = new MapDriver(mapper);
	}
/*
	@Test
	public void test1() {
		Configuration conf = new Configuration();
		conf.set("columnSize", "3");
		conf.set("expression", "$1+$2");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("harry,100,100"));
		driver.withOutput(NullWritable.get(), new Text("200.0"));
		driver.runTest();
	}

	@Test
	public void test2() {
		Configuration conf = new Configuration();
		conf.set("columnSize", "4");
		conf.set("expression", "($1+$2)*$3");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("harry,100,100,2"));
		driver.withOutput(NullWritable.get(), new Text("400.0"));
		driver.runTest();
	}

	@Test
	public void test3() {
		Configuration conf = new Configuration();
		conf.set("columnSize", "4");
		conf.set("expression", "($1+$2)/$3");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("harry,100,100,2"));
		driver.withOutput(NullWritable.get(), new Text("100.0"));
		driver.runTest();
	}

	@Test
	public void test4() {
		Configuration conf = new Configuration();
		conf.set("columnSize", "4");
		conf.set("expression", "($1-$2)");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("harry,100,100,2"));
		driver.withOutput(NullWritable.get(), new Text("0.0"));
		driver.runTest();
	}

	@Test(expected = CompileException.class)    // TODO
	public void test5() {
		Configuration conf = new Configuration();
		conf.set("columnSize", "4");
		conf.set("expression", "($1-$2");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("harry,100,100,2"));
		driver.withOutput(NullWritable.get(), new Text("0.0"));
		driver.runTest();
	}

	@Test(expected = IllegalArgumentException.class)
	public void test6() {
		Configuration conf = new Configuration();
		conf.set("columnSize", "4");
		conf.set("expression", "$1-$2");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("harry,100"));
		driver.withOutput(NullWritable.get(), new Text("0.0"));
		driver.runTest();
	}

	@Test(expected = RuntimeException.class)  // TODO exception unify
	public void test7() {
		Configuration conf = new Configuration();
		conf.set("columnSize", "2");
		conf.set("expression", "$1-");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("harry,100"));
		driver.withOutput(NullWritable.get(), new Text("0.0"));
		driver.runTest();
	}*/

	// TODO make this test success
	/*@Test
	public void test8() {
		Configuration conf = new Configuration();
		conf.set("columnSize", "2");
		conf.set("expression", "$1-1");
		driver.setConfiguration(conf);

		driver.withInput(new LongWritable(1), new Text("harry,100"));
		driver.withOutput(NullWritable.get(), new Text("99.0"));
		driver.runTest();
	}*/
}
