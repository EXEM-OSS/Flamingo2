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
package org.opencloudengine.flamingo2.mapreduce.gis;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import org.junit.Test;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 좌표 변환 테스트.
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class TransCoordTest {

    @Test
    public void degree2Decimal() {
        /*
        서울 시청
        37°33'58.87"N
        126°58'40.63"E
         */
        double latDegree = 37;
        double latMinute = 33;
        double latSecond = 58.87;
        double lat = CoordConvert.degree2Decimal(latDegree, latMinute, latSecond);
        assertEquals(37.566353, lat, 0.000001);

        double lngDegree = 126;
        double lngMinute = 58;
        double lngSecond = 40.63;
        double lng = CoordConvert.degree2Decimal(lngDegree, lngMinute, lngSecond);
        assertEquals(126.977953, lng, 0.000001);
    }

    @Test
    public void decimal2Degree() {
        /*
        37°33'58.87"N
        126°58'40.63"E
         */
        double latitude = 37.566353;
        double lngitude = 126.977953;

        double[] latDegrees = CoordConvert.decimal2Degree(latitude);
        assertEquals(37, latDegrees[0], 0.01);
        assertEquals(33, latDegrees[1], 0.01);
        assertEquals(58.87, latDegrees[2], 0.01);

        double[] lngDegrees = CoordConvert.decimal2Degree(lngitude);
        assertEquals(126, lngDegrees[0], 0.01);
        assertEquals(58, lngDegrees[1], 0.01);
        assertEquals(40.63, lngDegrees[2], 0.01);
    }

    @Test
    public void convertLngLat2TM() {
        double[] tm = CoordConvert.lngLat2TM(127.0518187, 37.5855002);

        assertTrue(204576.86204 - 5 < tm[0] && tm[0] < 204576.86204 + 5);
        assertTrue(553994.86857 - 5 < tm[1] && tm[1] < 553994.86857 + 5);
    }

    @Test
    public void convertTM2LngLat() {
        double[] lnglat = CoordConvert.tm2LngLat(204576.86204, 553994.86857);

        assertTrue(127.0518187 - 0.005 < lnglat[0] && lnglat[0] < 127.0518187 + 0.005);
        assertTrue(37.5855002 - 0.005 < lnglat[1] && lnglat[1] < 37.5855002 + 0.005);
    }

    @Test
    public void distanceTest() {
        String value = "1,37.5855002,127.0518187";
        String[] gpsColumns = StringUtils.delimitedListToStringArray(value.toString(), ",");
        double[] tm = CoordConvert.lngLat2TM(Double.parseDouble(gpsColumns[2]), Double.parseDouble(gpsColumns[1]));
        Point gpsPoint = new Point(tm[0], tm[1]);
        assertEquals(204576.862039, gpsPoint.getX(), 0.000001);
        assertEquals(553991.009219, gpsPoint.getY(), 0.000001);

        String linkCoordinates = "204735.47620000038 553773.5728999991, 204732.78899999987 553778.9954000004, 204690.01719999965 553865.3059, 204669.85599999968 553901.5072000008, 204664.2259999998 553911.6163999997, 204662.97890000045 553913.8555999994, 204662.51659999974 553914.6857999992, 204631.11010000017 553955.3333999999, 204617.8230999997 553966.3224, 204609.97360000014 553972.8142000008, 204587.71920000017 553991.2196999993, 204576.8794999998 553996.8970999997";
        String[] coordinates = StringUtils.delimitedListToStringArray(linkCoordinates, ", ");
        Polyline link = new Polyline();
        for (int i = 0; i < coordinates.length; i++) {
            String[] coordinate = StringUtils.delimitedListToStringArray(coordinates[i], " ");
            if (i == 0) {
                link.startPath(Double.parseDouble(coordinate[0]), Double.parseDouble(coordinate[1]));
            } else {
                link.lineTo(Double.parseDouble(coordinate[0]), Double.parseDouble(coordinate[1]));
            }
        }
        assertEquals(coordinates.length, link.getPointCount());

        double distance = GeometryEngine.distance(gpsPoint, link, null);
        assertTrue(distance < 10);
    }
}