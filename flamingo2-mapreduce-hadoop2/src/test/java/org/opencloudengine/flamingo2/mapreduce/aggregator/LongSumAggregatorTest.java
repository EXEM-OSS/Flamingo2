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

import org.junit.Assert;
import org.junit.Test;

/**
 * LongSumAggregator의 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @since 0.1
 */
public class LongSumAggregatorTest {

	@Test
	public void aggregate() {
		LongSumAggregator aggregator = new LongSumAggregator();
		aggregator.aggregate(1);
		aggregator.aggregate(2);
		aggregator.aggregate(3);

		Assert.assertEquals(6, aggregator.getAggregatedValue().get());
	}
}
