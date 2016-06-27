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
package org.opencloudengine.flamingo2.mapreduce.util;

import junit.framework.Assert;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.junit.Test;
import org.opencloudengine.flamingo2.mapreduce.util.HdfsUtils;

/**
 * HDFS Utility Unit Test Case.
 *
 * @author Edward KIM
 * @since 0.1
 */
public class HdfsUtilsTest {

    @Test
    public void getFilename() {
        FileSplit split = new FileSplit(new Path("hdfs://192.168.1.1:9000/home/hadoop/test.txt"), 1000, 10000, new String[]{"192.168.1.1:9000"});
        String filename = HdfsUtils.getFilename(split);
        Assert.assertEquals("test.txt", filename);
    }

}
