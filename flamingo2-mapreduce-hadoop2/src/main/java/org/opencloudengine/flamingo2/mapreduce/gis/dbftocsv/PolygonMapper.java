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

import com.esri.core.geometry.Point2D;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.io.PolygonWritable;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 타입이 Polygon 인 SHP 파일을 CSV 파일로 바꿔주는 Mapper
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class PolygonMapper extends Mapper<LongWritable, PolygonWritable, LongWritable, MapWritable> {

    private String coordinateDelimiter;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        this.coordinateDelimiter = Delimiter.COMMA.getDelimiter().equals(configuration.get("outputDelimiter")) ? "|" : ", ";
    }

    @Override
    protected void map(LongWritable key, PolygonWritable value, Context context) throws IOException, InterruptedException {
        // write value
        Point2D[] coordinates2D = value.polygon.getCoordinates2D();
        List<String> xys = new ArrayList<>();
        for (Point2D point : coordinates2D) {
            xys.add(point.x + " " + point.y);
        }
        MapWritable mapWritable = new MapWritable();
        mapWritable.put(new Text(DbfNShp2CsvDriver.COORDINATES_COLUMN_NAME), new Text(StringUtils.join(xys, this.coordinateDelimiter)));
        context.write(key, mapWritable);
    }
}