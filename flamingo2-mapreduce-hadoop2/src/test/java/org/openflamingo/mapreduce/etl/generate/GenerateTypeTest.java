package org.openflamingo.mapreduce.etl.generate;

import org.junit.*;

/**
 * GenerateType에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @author Seo Ji Hye
 * @since 1.0
 */
public class GenerateTypeTest {

    @Test
    public void valueOfSequence() {
        Assert.assertTrue(GenerateType.valueOf("SEQUENCE").equals(GenerateType.SEQUENCE));
    }

    @Test
    public void getTypeSequence() {
        String type = GenerateType.SEQUENCE.getType();
        Assert.assertTrue("SEQUENCE".equals(type));
    }

    @Test
    public void nameSequence() {
        String type = GenerateType.SEQUENCE.name();
        Assert.assertTrue("SEQUENCE".equals(type));
    }

    @Test
    public void valueOf() {
        Assert.assertTrue(GenerateType.valueOf("TIMESTAMP").equals(GenerateType.TIMESTAMP));
    }

    @Test
    public void getType() {
        String type = GenerateType.TIMESTAMP.getType();
        Assert.assertTrue("TIMESTAMP".equals(type));
    }

    @Test
    public void name() {
        String type = GenerateType.TIMESTAMP.name();
        Assert.assertTrue("TIMESTAMP".equals(type));
    }
}
