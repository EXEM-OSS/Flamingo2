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
 * Aggregator for getting min integer value.
 */
public class IntMinAggregator implements Aggregator<IntWritable> {

    /**
     * Internal aggregator
     */
    private int min = Integer.MAX_VALUE;

    /**
     * Aggregate with a primitive integer.
     *
     * @param value Integer value to aggregate.
     */
    public void aggregate(int value) {
        int val = value;
        if (val < min) {
            min = val;
        }
    }

    @Override
    public void aggregate(IntWritable value) {
        int val = value.get();
        if (val < min) {
            min = val;
        }
    }

    /**
     * Set aggregated value using a primitive integer.
     *
     * @param value Integer value to set.
     */
    public void setAggregatedValue(int value) {
        min = value;
    }

    @Override
    public void setAggregatedValue(IntWritable value) {
        min = value.get();
    }

    @Override
    public IntWritable getAggregatedValue() {
        return new IntWritable(min);
    }

    @Override
    public IntWritable createAggregatedValue() {
        return new IntWritable();
    }

}
