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


import org.apache.hadoop.io.DoubleWritable;

/**
 * Aggregator for getting max double value.
 */
public class DoubleMaxAggregator implements Aggregator<DoubleWritable> {

    /**
     * Saved maximum value
     */
    private double max = Double.MIN_VALUE;

    /**
     * Aggregate with a primitive double.
     *
     * @param value Double value to aggregate.
     */
    public void aggregate(double value) {
        double val = value;
        if (val > max) {
            max = val;
        }
    }

    @Override
    public void aggregate(DoubleWritable value) {
        double val = value.get();
        if (val > max) {
            max = val;
        }
    }

    /**
     * Set aggregated value using a primitive double.
     *
     * @param value Double value to set.
     */
    public void setAggregatedValue(double value) {
        max = value;
    }

    @Override
    public void setAggregatedValue(DoubleWritable value) {
        max = value.get();
    }

    @Override
    public DoubleWritable getAggregatedValue() {
        return new DoubleWritable(max);
    }

    @Override
    public DoubleWritable createAggregatedValue() {
        return new DoubleWritable();
    }
}
