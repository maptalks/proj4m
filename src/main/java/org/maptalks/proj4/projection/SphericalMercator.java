package org.maptalks.proj4.projection;

import org.maptalks.proj4.Point;

public class SphericalMercator implements Projection {

    private static final double R = 6378137;
    private static final double MAX_LATITUDE = 85.0511287798;

    public Point forward(Point point) {
        double d = Math.PI / 180;
        double max = MAX_LATITUDE;
        double lat = Math.max(Math.min(max, point.getY()), -max);
        double sin = Math.sin(lat * d);

        double x = R * point.getX() * d;
        double y = R * Math.log((1 + sin) / (1 - sin)) / 2;

        point.setX(x);
        point.setY(y);

        return point;
    }

    public Point inverse(Point point) {
        double d = 180 / Math.PI;
        double lng = point.getX() * d / R;
        double lat = (2 * Math.atan(Math.exp(point.getY() / R)) - (Math.PI / 2)) * d;

        point.setX(lng);
        point.setY(lat);

        return point;
    }

    public ProjectionType getType() {
        return ProjectionType.PROJECTED;
    }

}
