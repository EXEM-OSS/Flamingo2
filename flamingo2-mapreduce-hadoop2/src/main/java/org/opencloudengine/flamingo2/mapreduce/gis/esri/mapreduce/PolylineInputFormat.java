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
package org.opencloudengine.flamingo2.mapreduce.gis.esri.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.io.PolylineWritable;

import java.io.IOException;

/**
 * PolylineToCsvMapper 를 위한 InputFormat.
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class PolylineInputFormat extends AbstractInputFormat<PolylineWritable> {

    private final class PolylineReader extends AbstractReader<PolylineWritable> {

        private final PolylineWritable m_polylineWritable = new PolylineWritable();

        public PolylineReader(
                final InputSplit inputSplit,
                final TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            initialize(inputSplit, taskAttemptContext);
        }

        @Override
        protected void next() throws IOException {
            m_shpReader.queryPolyline(m_polylineWritable.polyline);
        }

        @Override
        public PolylineWritable getCurrentValue() throws IOException, InterruptedException {
            return m_polylineWritable;
        }
    }

    @Override
    public RecordReader<LongWritable, PolylineWritable> createRecordReader(
            final InputSplit inputSplit,
            final TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new PolylineReader(inputSplit, taskAttemptContext);
    }
}