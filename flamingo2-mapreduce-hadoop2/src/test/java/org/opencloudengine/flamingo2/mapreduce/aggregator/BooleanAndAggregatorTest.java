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
package org.opencloudengine.flamingo2.mapreduce.aggregator;

import junit.framework.Assert;
import org.apache.hadoop.io.BooleanWritable;
import org.junit.Test;

/**
 * BooleanAndAggregator의 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @since 0.3
 */
public class BooleanAndAggregatorTest {

    @Test
    public void aggregatePrimitive() throws Exception {
        BooleanAndAggregator aggregator = new BooleanAndAggregator();
        aggregator.aggregate(true);
        aggregator.aggregate(true);
        aggregator.aggregate(true);
        aggregator.aggregate(false);

        BooleanWritable value = aggregator.getAggregatedValue();

        Assert.assertFalse(value.get());
    }

    @Test
    public void aggregateWritable() throws Exception {
        BooleanAndAggregator aggregator = new BooleanAndAggregator();
        aggregator.aggregate(new BooleanWritable(false));
        aggregator.aggregate(new BooleanWritable(true));
        aggregator.aggregate(new BooleanWritable(true));

        BooleanWritable value = aggregator.getAggregatedValue();

        Assert.assertFalse(value.get());
    }

    @Test
    public void setAggregatedValue() throws Exception {
        BooleanAndAggregator aggregator = new BooleanAndAggregator();

        aggregator.setAggregatedValue(true);
        Assert.assertTrue(aggregator.getAggregatedValue().get());

        aggregator.setAggregatedValue(false);
        Assert.assertFalse(aggregator.getAggregatedValue().get());
    }

    @Test
    public void createAggregatedValue() throws Exception {
        BooleanAndAggregator aggregator = new BooleanAndAggregator();
        Assert.assertFalse(aggregator.createAggregatedValue().get());
    }
}
