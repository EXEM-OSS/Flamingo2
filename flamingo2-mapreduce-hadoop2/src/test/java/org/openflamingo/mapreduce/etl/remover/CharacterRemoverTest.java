package org.openflamingo.mapreduce.etl.remover;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;
import org.openflamingo.mapreduce.etl.filter.FilterMapper;

import java.io.IOException;

/**
 * Character Remover Mapper에 대한 단위 테스트 케이스.
 *
 * @author Byoung Gon, Kim
 * @since 1.1
 */
public class CharacterRemoverTest {

    private Mapper mapper;

    private MapDriver driver;

    @Before
    public void setUp() {
        mapper = new CharacterRemoverMapper();
        driver = new MapDriver(mapper);
    }

    private Configuration getConfiguration() {
        Configuration conf = new Configuration();
        conf.set("characters", " ");
        return conf;
    }

    @Test
    public void removeTest1() throws IOException {
	    Configuration conf = new Configuration();
	    conf.set("characters", " "); // SPACE

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("192.168.114.201, -, 03/20/01, 7:55:20, W3SVC2, SERVER, 172.21.13.45, 4502, 163, 3223, 200, 0, GET, /DeptLogo.gif, -,"));
        driver.withOutput(NullWritable.get(), new Text("192.168.114.201,-,03/20/01,7:55:20,W3SVC2,SERVER,172.21.13.45,4502,163,3223,200,0,GET,/DeptLogo.gif,-,"));
        driver.runTest();
    }

    @Test
    public void removeTest2() throws IOException {
	    Configuration conf = new Configuration();
	    conf.set("characters", "\u0020"); // SPACE

        driver.setConfiguration(conf);

        driver.withInput(new LongWritable(1), new Text("192.168.114.201, -, 03/20/01, 7:55:20, W3SVC2, SERVER, 172.21.13.45, 4502, 163, 3223, 200, 0, GET, /DeptLogo.gif, -,"));
        driver.withOutput(NullWritable.get(), new Text("192.168.114.201,-,03/20/01,7:55:20,W3SVC2,SERVER,172.21.13.45,4502,163,3223,200,0,GET,/DeptLogo.gif,-,"));
        driver.runTest();
    }
}
