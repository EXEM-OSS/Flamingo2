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

import org.junit.Assert;
import org.junit.Test;
import org.opencloudengine.flamingo2.mapreduce.util.MathUtils;

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
