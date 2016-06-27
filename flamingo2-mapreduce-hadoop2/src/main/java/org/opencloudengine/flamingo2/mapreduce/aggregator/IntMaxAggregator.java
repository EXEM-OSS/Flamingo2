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
 * Aggregator for getting max integer value.
 */
public class IntMaxAggregator implements Aggregator<IntWritable> {

    /**
     * Saved maximum value
     */
    private int max = Integer.MIN_VALUE;

    /**
     * Aggregate with a primitive integer.
     *
     * @param value Integer value to aggregate.
     */
    public void aggregate(int value) {
        int val = value;
        if (val > max) {
            max = val;
        }
    }

    @Override
    public void aggregate(IntWritable value) {
        int val = value.get();
        if (val > max) {
            max = val;
        }
    }

    /**
     * Set aggregated value using a primitive integer.
     *
     * @param value Integer value to set.
     */
    public void setAggregatedValue(int value) {
        max = value;
    }

    @Override
    public void setAggregatedValue(IntWritable value) {
        max = value.get();
    }

    @Override
    public IntWritable getAggregatedValue() {
        return new IntWritable(max);
    }

    @Override
    public IntWritable createAggregatedValue() {
        return new IntWritable();
    }
}
