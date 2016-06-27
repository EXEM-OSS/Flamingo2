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
import org.junit.Test;

/**
 * String Utility Unit Test Case.
 *
 * @author Edward KIM
 * @since 0.1
 */
public class StringUtilsTest {

    @Test
    public void commaDelimitedListToStringArray() {
        String[] strings = StringUtils.commaDelimitedListToStringArray("0");
        Assert.assertEquals(1, strings.length);
        Assert.assertEquals("0", strings[0]);

        strings = StringUtils.commaDelimitedListToStringArray("0,1,2,");
        Assert.assertEquals(4, strings.length);
        Assert.assertEquals("0", strings[0]);
        Assert.assertEquals("1", strings[1]);
        Assert.assertEquals("2", strings[2]);
        Assert.assertEquals("", strings[3]);
    }

}
