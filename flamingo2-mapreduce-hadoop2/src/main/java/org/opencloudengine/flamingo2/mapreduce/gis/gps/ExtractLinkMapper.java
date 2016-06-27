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
package org.opencloudengine.flamingo2.mapreduce.gis.gps;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.gis.CoordConvert;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * GPS 위도, 경도로부터 LINK ID 를 추출하는 MapReduce Mapper.
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class ExtractLinkMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private FSDataInputStream fsdis;

    private BufferedReader br;

    private String inputDelimiter;

    private String csvDelimiter;

    private String coordinateDelimiter;

    private int longitudeColumnIndex;

    private int latitudeColumnIndex;

    private double detectDistance;

    private int linkidColumnIndex;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        this.fsdis = FileSystem.get(configuration).open(new Path(configuration.get("csvPath")));
        this.br = new BufferedReader(new InputStreamReader(fsdis));
        this.inputDelimiter = configuration.get("inputDelimiter");
        this.csvDelimiter = configuration.get("csvDelimiter");
        this.coordinateDelimiter = Delimiter.COMMA.getDelimiter().equals(this.csvDelimiter) ? "|" : ", ";
        this.longitudeColumnIndex = configuration.getInt("longitudeColumnIndex", -1);
        this.latitudeColumnIndex = configuration.getInt("latitudeColumnIndex", -1);
        this.detectDistance = configuration.getDouble("detectDistance", -1);
        this.linkidColumnIndex = configuration.getInt("linkidColumnIndex", -1);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        this.fsdis.seek(0);

        Point gpsPoint = getGPSPoint(value);

        List<String> ids = new ArrayList<>();
        String linkLine = "";
        while ((linkLine = this.br.readLine()) != null) {
            String[] linkValues = StringUtils.delimitedListToStringArray(linkLine, this.csvDelimiter);
            String[] linkCoordinates = StringUtils.delimitedListToStringArray(linkValues[linkValues.length - 1], this.coordinateDelimiter);
            Polyline link = new Polyline();
            for (int i = 0, length = linkCoordinates.length; i < length; i++) {
                String[] coordinate = StringUtils.delimitedListToStringArray(linkCoordinates[i], " ");
                double x = Double.parseDouble(coordinate[0]);
                double y = Double.parseDouble(coordinate[1]);
                if (i == 0) {
                    link.startPath(x, y);
                } else {
                    link.lineTo(x, y);
                }
            }
            double distance = GeometryEngine.distance(link, gpsPoint, null);
            if (distance <= this.detectDistance) {
                ids.add(linkValues[this.linkidColumnIndex]);
            }
        }

        context.write(NullWritable.get(), new Text(value + this.inputDelimiter + StringUtils.join(ids, "|")));
    }

    private Point getGPSPoint(Text value) {
        String[] gpsColumns = StringUtils.delimitedListToStringArray(value.toString(), this.inputDelimiter);
        double[] tm = CoordConvert.lngLat2TM(
                Double.parseDouble(gpsColumns[this.longitudeColumnIndex]),
                Double.parseDouble(gpsColumns[this.latitudeColumnIndex])
        );
        return new Point(tm[0], tm[1]);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        this.fsdis.close();
        this.br.close();
    }
}