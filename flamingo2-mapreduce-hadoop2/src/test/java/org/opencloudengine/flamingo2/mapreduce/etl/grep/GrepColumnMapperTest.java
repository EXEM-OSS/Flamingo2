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
package org.opencloudengine.flamingo2.mapreduce.etl.grep;

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
