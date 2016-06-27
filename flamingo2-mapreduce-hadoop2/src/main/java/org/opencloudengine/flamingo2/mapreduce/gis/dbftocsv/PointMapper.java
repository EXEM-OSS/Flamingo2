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
package org.opencloudengine.flamingo2.mapreduce.gis.dbftocsv;

import com.esri.core.geometry.Point;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.io.PointWritable;

import java.io.IOException;

/**
 * 타입이 Point 인 SHP 파일을 CSV 파일로 바꿔주는 Mapper
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class PointMapper extends Mapper<LongWritable, PointWritable, LongWritable, MapWritable> {

    private int divideSectionMeter;

    private boolean divideSection;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        this.divideSectionMeter = configuration.getInt("divideSectionMeter", -1);
        this.divideSection = divideSectionMeter > -1;
    }

    @Override
    protected void map(LongWritable key, PointWritable value, Context context) throws IOException, InterruptedException {
        // write value
        Point point = value.point;
        double x = point.getX();
        double y = point.getY();
        MapWritable mapWritable = new MapWritable();
        if (this.divideSection) {
            int xSection = (int) x / this.divideSectionMeter;
            int ySection = (int) y / this.divideSectionMeter;
            mapWritable.put(new Text(DbfNShp2CsvDriver.COORDINATES_SECTION), new Text(xSection + " " + ySection));
        }
        mapWritable.put(new Text(DbfNShp2CsvDriver.COORDINATES_COLUMN_NAME), new Text(x + " " + y));
        context.write(key, mapWritable);
    }
}