package org.openflamingo.mapreduce.util;

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
