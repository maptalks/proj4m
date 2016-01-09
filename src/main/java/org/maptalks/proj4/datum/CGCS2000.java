package org.maptalks.proj4.datum;

import org.maptalks.proj4.Point;

public class CGCS2000 implements Datum {

    public Point toWGS84(Point point) {
        return point;
    }

    public Point fromWGS84(Point point) {
        return point;
    }

}
