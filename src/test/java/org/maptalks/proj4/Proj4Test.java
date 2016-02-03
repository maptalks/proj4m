package org.maptalks.proj4;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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

}
