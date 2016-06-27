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
package org.opencloudengine.flamingo2.mapreduce.rules.drools;

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
 * GroupBy Mapper와 Reducer에 대한 단위 테스트 케이스.
 *
 * @author Haneul, Kim
 * @since 2.0
 */
public class DroolsMRTest {

    private Mapper mapper;
    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new DroolsMapper();
        driver = new MapDriver(mapper);
    }

    @Test
    public void test1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("delimiter", ",");
        conf.set("columnNames", "name,product,id");
        conf.set("encodedRule", "import%20java.util.HashMap%3B%0A%0Adialect%20%22mvel%22%0A%0Arule%20%22rule-1-1%22%0A%09no-loop%0A%09when%0A%09%09t%20%3A%20HashMap%28%20id%20%3D%3D%20%221%22%20%29%0A%09then%0A%09%09t.result%20%3D%20%22rule-1-1%22%20+%20t.id%3B%0Aend");
        conf.set("deleteOutput", "true");

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("john,car,5"));
        driver.withInput(new LongWritable(2), new Text("john,computer,9"));
        driver.withInput(new LongWritable(3), new Text("jim,mouse,1"));
        driver.withInput(new LongWritable(4), new Text("jim,keyboard,2"));
        driver.withInput(new LongWritable(5), new Text("john,mouse,8"));
        driver.withOutput(NullWritable.get(), new Text("rule-1-11"));
        driver.runTest();
    }
}
