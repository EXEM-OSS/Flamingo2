package org.opencloudengine.flamingo2.mapreduce.gis.esri.shp;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import kr.hyosang.coordinate.CoordPoint;
import kr.hyosang.coordinate.TransCoord;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.io.PolylineMWritable;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 */
public class PolylineMTest {

    @Test
    public void testReadPolygon() throws IOException {
        final InputStream inputStream = this.getClass()
                .getResourceAsStream("/testpolylinem.shp");
        assertNotNull(inputStream);
        try {
            final ShpReader shpReader = new ShpReader(new DataInputStream(inputStream));
            final ShpHeader shpHeader = shpReader.getHeader();

            assertEquals(10.0, shpHeader.mmin, 0.000001);
            assertEquals(20.0, shpHeader.mmax, 0.000001);

            assertTrue(shpReader.hasMore());
            final PolylineMWritable polylineM = shpReader.readPolylineMWritable();

            assertNotNull(polylineM.lens);
            assertEquals(1, polylineM.lens.length);
            assertEquals(2, polylineM.lens[0]);

            assertNotNull(polylineM.x);
            assertNotNull(polylineM.y);
            assertNotNull(polylineM.m);

            assertEquals(2, polylineM.x.length);
            assertEquals(2, polylineM.y.length);
            assertEquals(2, polylineM.m.length);

            assertEquals(0.0, polylineM.x[0], 0.000001);
            assertEquals(100.0, polylineM.x[1], 0.000001);

            assertEquals(0.0, polylineM.y[0], 0.000001);
            assertEquals(200.0, polylineM.y[1], 0.000001);

            assertEquals(10.0, polylineM.m[0], 0.000001);
            assertEquals(20.0, polylineM.m[1], 0.000001);

        } finally {
            inputStream.close();
        }
    }

    @Test
    public void distanceTest1() {
        Polyline polyline = new Polyline();
        polyline.startPath(0, 2);
        polyline.lineTo(2, 0);

        double distance = GeometryEngine.distance(polyline, new Point(0, 0), null);
        assertEquals(Math.sqrt(2.0), distance, 0.000001);
    }

    @Test
    public void distanceTest2() {
        Polyline polyline = new Polyline();
        polyline.startPath(0, 2);
        polyline.lineTo(2, 2);
        polyline.lineTo(2, 0);

        double distance = GeometryEngine.distance(polyline, new Point(0, 0), null);
        assertEquals(2, distance, 0.000001);
    }
}
