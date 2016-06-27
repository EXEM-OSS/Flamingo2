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


import org.apache.hadoop.io.Writable;

/**
 * Interface for Aggregator.  Allows aggregate operations for all vertices
 * in a given superstep.
 *
 * @param <A> Aggregated value
 */
public interface Aggregator<A extends Writable> {

    /**
     * Add a new value.
     * Needs to be commutative and associative
     *
     * @param value Value to be aggregated.
     */
    void aggregate(A value);

    /**
     * Set aggregated value.
     * Can be used for initialization or reset.
     *
     * @param value Value to be set.
     */
    void setAggregatedValue(A value);

    /**
     * Return current aggregated value.
     * Needs to be initialized if aggregate or setAggregatedValue
     * have not been called before.
     *
     * @return Aggregated
     */
    A getAggregatedValue();

    /**
     * Return new aggregated value.
     * Must be changeable without affecting internals of Aggregator
     *
     * @return Writable
     */
    A createAggregatedValue();
}
