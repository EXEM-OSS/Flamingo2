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
package org.opencloudengine.flamingo2.mapreduce.etl.replace;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;
import org.opencloudengine.flamingo2.mapreduce.etl.replace.delimiter.ReplaceDelimiterMapper;

import java.io.IOException;

/**
 * Replace Delimiter Mapper에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @author Seo Ji Hye
 * @since 1.0
 */
public class ReplaceDelimiterMapperTest {
    private Mapper mapper;
    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new ReplaceDelimiterMapper();
        driver = new MapDriver(mapper);
    }

    @Test
    public void testSingleCharInputDelimiter() throws IOException {
        Configuration conf = new Configuration();
        conf.set("from", ":");
        conf.set("to", ",");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1:2:3:4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }

    @Test
    public void testSingleCharInputDeliToMultiCharOutDeli() throws IOException {
        Configuration conf = new Configuration();
        conf.set("from", ",");
        conf.set("to", "::");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1,2,3,4"));
        driver.withOutput(NullWritable.get(), new Text("1::2::3::4"));
        driver.runTest();
    }

    @Test
    public void testMultiCharInputDelimiter() throws IOException {
        Configuration conf = new Configuration();
        conf.set("from", "::");
        conf.set("to", ",");
        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("1::2::3::4"));
        driver.withOutput(NullWritable.get(), new Text("1,2,3,4"));
        driver.runTest();
    }
}
