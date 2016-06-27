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


import org.apache.hadoop.io.LongWritable;

/**
 * Aggregator for getting min long value.
 */
public class LongMinAggregator implements Aggregator<LongWritable> {

    /**
     * Internal aggregator
     */
    private long min = Long.MAX_VALUE;

    /**
     * Aggregate with a primitive long.
     *
     * @param value Long value to aggregate.
     */
    public void aggregate(long value) {
        long val = value;
        if (val < min) {
            min = val;
        }
    }

    @Override
    public void aggregate(LongWritable value) {
        long val = value.get();
        if (val < min) {
            min = val;
        }
    }

    /**
     * Set aggregated value using a primitive long.
     *
     * @param value Long value to set.
     */
    public void setAggregatedValue(long value) {
        min = value;
    }

    @Override
    public void setAggregatedValue(LongWritable value) {
        min = value.get();
    }

    @Override
    public LongWritable getAggregatedValue() {
        return new LongWritable(min);
    }

    @Override
    public LongWritable createAggregatedValue() {
        return new LongWritable();
    }

}
