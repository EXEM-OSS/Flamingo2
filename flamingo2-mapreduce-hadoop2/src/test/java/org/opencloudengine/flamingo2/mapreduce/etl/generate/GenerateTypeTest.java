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
package org.opencloudengine.flamingo2.mapreduce.etl.generate;

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
