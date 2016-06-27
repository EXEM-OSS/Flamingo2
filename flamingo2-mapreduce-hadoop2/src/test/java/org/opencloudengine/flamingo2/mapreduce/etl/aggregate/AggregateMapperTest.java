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
package org.opencloudengine.flamingo2.mapreduce.etl.aggregate;

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
