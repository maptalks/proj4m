package org.maptalks.proj4;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Proj4Test {

    @Test
    public void testForward() throws Exception {
        Proj4 proj = new Proj4("+proj=longlat +datum=WGS84", "+proj=merc +datum=WGS84");
        double[] coord = proj.forward(new double[]{120.0, 30.0});
        double tolerance = 1e2;
        assertEquals(coord[0], 13358338.895192828, tolerance);
        assertEquals(coord[1], 3503549.843504374, tolerance);
    }

    @Test
    public void testInverse() throws Exception {
        Proj4 proj = new Proj4("+proj=longlat +datum=WGS84", "+proj=merc +datum=WGS84");
        double[] coord = proj.inverse(new double[]{13358338.89, 3503549.84});
        double tolerance = 1e6;
        assertEquals(coord[0], 120.0, tolerance);
        assertEquals(coord[1], 30.0, tolerance);
    }

}
