/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.aggregator;

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
