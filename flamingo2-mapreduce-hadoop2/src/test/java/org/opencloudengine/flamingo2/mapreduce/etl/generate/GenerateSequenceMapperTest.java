/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.mapreduce.etl.generate;

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
