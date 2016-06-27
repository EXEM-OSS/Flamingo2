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


import org.apache.hadoop.io.FloatWritable;

/**
 * Aggregator for summing up float values.
 */
public class FloatSumAggregator implements Aggregator<FloatWritable> {

    /**
     * Aggregated sum
     */
    private float sum = 0;

    /**
     * Aggregate a primitive float.
     *
     * @param value Float value to aggregate.
     */
    public void aggregate(float value) {
        sum += value;
    }

    @Override
    public void aggregate(FloatWritable value) {
        sum += value.get();
    }

    /**
     * Set aggregated value using a primitive float.
     *
     * @param value Float value to set.
     */
    public void setAggregatedValue(float value) {
        sum = value;
    }

    @Override
    public void setAggregatedValue(FloatWritable value) {
        sum = value.get();
    }

    @Override
    public FloatWritable getAggregatedValue() {
        return new FloatWritable(sum);
    }

    @Override
    public FloatWritable createAggregatedValue() {
        return new FloatWritable();
    }

}
