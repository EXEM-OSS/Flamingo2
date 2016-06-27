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


import org.apache.hadoop.io.IntWritable;

/**
 * Aggregator for calculating products of long and integer values.
 */
public class IntProductAggregator implements Aggregator<IntWritable> {

    /**
     * Internal product
     */
    private int product = 1;

    /**
     * Aggregate a primitive integer.
     *
     * @param value Integer value to aggregate.
     */
    public void aggregate(int value) {
        product *= value;
    }

    @Override
    public void aggregate(IntWritable value) {
        product *= value.get();
    }

    /**
     * Set aggregated value using a primitive integer.
     *
     * @param value Integer value to set.
     */
    public void setAggregatedValue(int value) {
        product = value;
    }

    @Override
    public void setAggregatedValue(IntWritable value) {
        product = value.get();
    }

    @Override
    public IntWritable getAggregatedValue() {
        return new IntWritable(product);
    }

    @Override
    public IntWritable createAggregatedValue() {
        return new IntWritable();
    }
}
