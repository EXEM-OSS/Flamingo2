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
package org.opencloudengine.flamingo2.mapreduce.etl.groupby;

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
