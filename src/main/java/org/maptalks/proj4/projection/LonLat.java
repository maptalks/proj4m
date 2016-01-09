package org.maptalks.proj4.projection;

import org.maptalks.proj4.Point;

public class LonLat implements Projection {

    public Point forward(Point point) {
        return point;
    }

    public Point inverse(Point point) {
        return point;
    }

}
