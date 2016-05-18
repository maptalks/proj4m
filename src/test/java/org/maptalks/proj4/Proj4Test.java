package org.maptalks.proj4;


import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Proj4Test {

    private static final String EPSG3857 = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext  +no_defs";
    private static final String EPSG4326 = "+proj=longlat +datum=WGS84 +no_defs";

    @Test
    public void testForward() throws Exception {
        Proj4 proj = new Proj4(EPSG4326, EPSG3857);
        double[] coord = proj.forward(new double[]{120.0, 30.0});
        double tolerance = 1e-3;
        assertEquals(coord[0], 13358338.895192828, tolerance);
        assertEquals(coord[1], 3503549.843504374, tolerance);
    }

    @Test
    public void testInverse() throws Exception {
        Proj4 proj = new Proj4(EPSG4326, EPSG3857);
        double[] coord = proj.inverse(new double[]{13358338.89, 3503549.84});
        double tolerance = 1e-7;
        assertEquals(coord[0], 120.0, tolerance);
        assertEquals(coord[1], 30.0, tolerance);
    }

    @Test
    public void testForwardWithDef() throws Exception {
        Proj4 proj = new Proj4("EPSG:4326", "EPSG:3857");
        double[] coord = proj.forward(new double[]{120.0, 30.0});
        double tolerance = 1e-3;
        assertEquals(coord[0], 13358338.895192828, tolerance);
        assertEquals(coord[1], 3503549.843504374, tolerance);
    }

    @Test
    public void testInverseWithDef() throws Exception {
        Proj4 proj = new Proj4("EPSG:4326", "EPSG:3857");
        double[] coord = proj.inverse(new double[]{13358338.89, 3503549.84});
        double tolerance = 1e-7;
        assertEquals(coord[0], 120.0, tolerance);
        assertEquals(coord[1], 30.0, tolerance);
    }

    @Test
    public void testForwardWithMixed() throws Exception {
        Proj4 proj = new Proj4(EPSG4326, "EPSG:3857");
        double[] coord = proj.forward(new double[]{120.0, 30.0});
        double tolerance = 1e-3;
        assertEquals(coord[0], 13358338.895192828, tolerance);
        assertEquals(coord[1], 3503549.843504374, tolerance);
    }

    @Test
    public void testInverseWithMixed() throws Exception {
        Proj4 proj = new Proj4("EPSG:4326", EPSG3857);
        double[] coord = proj.inverse(new double[]{13358338.89, 3503549.84});
        double tolerance = 1e-7;
        assertEquals(coord[0], 120.0, tolerance);
        assertEquals(coord[1], 30.0, tolerance);
    }

    @Test
    public void testSameProj() throws Exception {
        Proj4 proj = new Proj4("GCJ02", "GCJ02");
        double[] o = new double[]{121.8999, 21.3333};
        double[] iv = proj.inverse(o);
        double[] fw = proj.forward(o);
        double tolerance = 0;
        assertEquals(o[0], iv[0], tolerance);
        assertEquals(o[0], fw[0], tolerance);
        assertEquals(o[1], iv[1], tolerance);
        assertEquals(o[1], fw[1], tolerance);
    }

    @Test
    public void testGcj02ToBaidu() throws Exception {
        double[][] expected = new double[][]{
            {114.69490414027017, 33.639096507711685},
            {114.69488614273101, 33.63804850387785},
            {114.69500713986416, 33.63794251496537},
            {114.69578412001135, 33.63793958798685},
            {114.6959281162725, 33.637965601694006},
            {114.69751307493384, 33.637957753486745}
        };


        Proj4 proj = new Proj4("GCJ02", "BD09LL");
        double[][] lonlats = new double[][]{
            {114.68837663801743, 33.63312016454496},
            {114.68835840204522, 33.632072446353945},
            {114.68848002806972, 33.63196427051657},
            {114.68926112541861, 33.63194729708501},
            {114.68940588838505, 33.6319707051534},
            {114.69099925796665, 33.63193416046613}
        };

        for (int i = 0; i < lonlats.length; i++) {
            double[] coord = proj.forward(lonlats[i]);
            assertTrue(Arrays.equals(expected[i], coord));
        }
    }

    @Test
    public void testGlobalDef() throws Exception {
        Global.def("mars", "+proj=longlat +datum=GCJ02");

        assertTrue(Global.has("mars"));
        assertTrue(Global.has("MARS"));

        Proj mars = Global.def("MARS");
        assertEquals(Global.def("gcj02"), mars);
    }

    @Test
    public void testTestProj() throws Exception {
        assertTrue(Parser.testProj("+proj=longlat +datum=GCJ02"));
        assertTrue(!Parser.testProj("not proj4 string"));
    }

    @Test
    public void testTestWKT() throws Exception {
        String wkt = "PROJCS[\"WGS_1984_Web_Mercator_Auxiliary_Sphere\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Mercator_Auxiliary_Sphere\"],PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],PARAMETER[\"Standard_Parallel_1\",0.0],PARAMETER[\"Auxiliary_Sphere_Type\",0.0],UNIT[\"Meter\",1.0]]";
        assertTrue(Parser.testWKT(wkt));
        assertTrue(!Parser.testWKT("+proj=longlat +datum=GCJ02"));
    }

    @Test
    public void testProj4() throws Exception {
        String firstProjection = "PROJCS[\"NAD83 / Massachusetts Mainland\",GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],AUTHORITY[\"EPSG\",\"6269\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4269\"]],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],PROJECTION[\"Lambert_Conformal_Conic_2SP\"],PARAMETER[\"standard_parallel_1\",42.68333333333333],PARAMETER[\"standard_parallel_2\",41.71666666666667],PARAMETER[\"latitude_of_origin\",41],PARAMETER[\"central_meridian\",-71.5],PARAMETER[\"false_easting\",200000],PARAMETER[\"false_northing\",750000],AUTHORITY[\"EPSG\",\"26986\"],AXIS[\"X\",EAST],AXIS[\"Y\",NORTH]]";
        String secondProjection = "+proj=gnom +lat_0=90 +lon_0=0 +x_0=6300000 +y_0=6300000 +ellps=WGS84 +datum=WGS84 +units=m +no_defs";

        Proj4 proj = new Proj4(firstProjection, secondProjection);
        double[] result = proj.forward(new double[]{2, 5});
        assertEquals(-2690666.2977344505, result[0], 1E-6);
        assertEquals(3662659.885459918, result[1], 1E-6);
    }

}
