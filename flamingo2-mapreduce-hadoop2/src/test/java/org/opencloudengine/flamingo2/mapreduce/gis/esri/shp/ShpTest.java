package org.opencloudengine.flamingo2.mapreduce.gis.esri.shp;

import com.esri.core.geometry.*;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * local 의 shp 파일 테스트.
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class ShpTest {

    @Test
    public void readPoint() throws IOException {
        File shpFile = new File("/Users/cloudine/Downloads/korea_standard_nodelink_2015-07-31/MOCT_NODE.shp");
        InputStream fis = new FileInputStream(shpFile);
        ShpReader shpReader = new ShpReader(new DataInputStream(fis));
        ShpHeader shpHeader = shpReader.getHeader();
        assertEquals("Point", getShapeType(shpHeader.shapeType));
    }

    @Test
    public void readPolyLine() throws IOException {
        File shpFile = new File("/Users/cloudine/Downloads/korea_standard_nodelink_2015-07-31/MOCT_LINK.shp");
        InputStream fis = new FileInputStream(shpFile);
        ShpReader shpReader = new ShpReader(new DataInputStream(fis));
        ShpHeader shpHeader = shpReader.getHeader();
        assertEquals("PolyLine", getShapeType(shpHeader.shapeType));
        /*PolylineMWritable polylineMWritable = shpReader.readPolylineMWritable();
        int[] lens = polylineMWritable.lens;
        for (int i = 0; i < lens.length; i++) {
            int len = lens[i];
            for (int j = 0; j < len; j++) {
                double x = polylineMWritable.x[j];
                double y = polylineMWritable.y[j];
                double m = polylineMWritable.m[j];
            }
        }*/
    }

    @Test
    public void readPolygon() throws IOException {
        File shpFile = new File("/Users/cloudine/Downloads/seoul/seoul.shp");
        InputStream fis = new FileInputStream(shpFile);
        ShpReader shpReader = new ShpReader(new DataInputStream(fis));
        ShpHeader shpHeader = shpReader.getHeader();
        assertEquals("Polygon", getShapeType(shpHeader.shapeType));
    }

    @Test
    public void testIntersects() throws IOException {
        File shpFile = new File("/Users/cloudine/Downloads/seoul/seoul.shp");
        InputStream fis = new FileInputStream(shpFile);
        ShpReader shpReader = new ShpReader(new DataInputStream(fis));
        Polygon polygon = shpReader.readPolygon();
        Point truePoint = new Point();
        truePoint.setXY(200892, 464496);

        Point falsePoint = new Point();
        falsePoint.setXY(202790, 464311);

        SpatialReference wgs84 = SpatialReference.create(4326);

        Geometry trueIntersects = GeometryEngine.intersect(truePoint, polygon, wgs84);
        assertEquals(true, !trueIntersects.isEmpty());

        Geometry falseIntersects = GeometryEngine.intersect(falsePoint, polygon, wgs84);
        assertEquals(false, !falseIntersects.isEmpty());
    }

    @Test
    public void getSpatialReferenceFromText() throws IOException {
        File shpFile = new File("/Users/cloudine/Downloads/seoul/seoul.shp");
        InputStream fis = new FileInputStream(shpFile);
        ShpReader shpReader = new ShpReader(new DataInputStream(fis));
        Polygon polygon = shpReader.readPolygon();
        Point truePoint = new Point();
        truePoint.setXY(200892, 464496);

        Point falsePoint = new Point();
        falsePoint.setXY(202790, 464311);

        File prjFile = new File("/Users/cloudine/Downloads/korea/MOCT_LINK.prj");
        BufferedReader in = new BufferedReader(new FileReader(prjFile));

        String prj = "";
        String s = "";
        while ((s = in.readLine()) != null) {
            prj += s;
        }
        in.close();

        SpatialReference spatialReference = SpatialReference.create(prj);

        Geometry trueIntersects = GeometryEngine.intersect(truePoint, polygon, spatialReference);
        assertEquals(true, !trueIntersects.isEmpty());

        Geometry falseIntersects = GeometryEngine.intersect(falsePoint, polygon, spatialReference);
        assertEquals(false, !falseIntersects.isEmpty());
    }

    private String getShapeType(int shapeType) {
        String shapeTypeString = "";
        switch (shapeType) {
            case 0:
                shapeTypeString = "Null Shape";
                break;
            case 1:
                shapeTypeString = "Point";
                break;
            case 3:
                shapeTypeString = "PolyLine";
                break;
            case 5:
                shapeTypeString = "Polygon";
                break;
            case 8:
                shapeTypeString = "MultiPoint";
                break;
            case 11:
                shapeTypeString = "PointZ";
                break;
            case 13:
                shapeTypeString = "PolyLineZ";
                break;
            case 15:
                shapeTypeString = "PolygonZ";
                break;
            case 18:
                shapeTypeString = "MultiPointZ";
                break;
            case 21:
                shapeTypeString = "PointM";
                break;
            case 23:
                shapeTypeString = "PolyLineM";
                break;
            case 25:
                shapeTypeString = "PolygonM";
                break;
            case 28:
                shapeTypeString = "MultiPointM";
                break;
            case 31:
                shapeTypeString = "MultiPatch";
                break;
            default:
                break;
        }
        return shapeTypeString;
    }
}
