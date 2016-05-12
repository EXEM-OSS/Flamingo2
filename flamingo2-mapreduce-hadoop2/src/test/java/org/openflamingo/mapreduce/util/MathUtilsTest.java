package org.openflamingo.mapreduce.util;

import org.junit.Assert;
import org.junit.Test;
import org.openflamingo.mapreduce.util.MathUtils;

/**
 * Description.
 *
 * @author Edward KIM
 * @since 1.0
 */
public class MathUtilsTest {

    @Test
    public void convertScientificToStandard() {
        Assert.assertEquals(MathUtils.convertScientificToStandard(1.0), "1.0");
    }
}
