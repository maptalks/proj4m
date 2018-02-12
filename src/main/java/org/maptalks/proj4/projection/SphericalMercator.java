package org.maptalks.proj4.projection;

import org.maptalks.proj4.PointAdaptor;

public class SphericalMercator<T> implements Projection<T> {

    private static final double R = 6378137;
    private static final double MAX_LATITUDE = 85.0511287798;

    public T forward(T point, PointAdaptor<T> pointAdaptor) {
        double d = Math.PI / 180;
        double max = MAX_LATITUDE;
        double lat = Math.max(Math.min(max, pointAdaptor.getY(point)), -max);
        double sin = Math.sin(lat * d);

        double x = R * pointAdaptor.getX(point) * d;
        double y = R * Math.log((1 + sin) / (1 - sin)) / 2;

        pointAdaptor.setX(point, x);
        pointAdaptor.setY(point, y);

        return point;
    }

    public T inverse(T point, PointAdaptor<T> pointAdaptor) {
        double d = 180 / Math.PI;
        double lng = pointAdaptor.getX(point) * d / R;
        double lat = (2 * Math.atan(Math.exp(pointAdaptor.getY(point) / R)) - (Math.PI / 2)) * d;

        pointAdaptor.setX(point, lng);
        pointAdaptor.setY(point, lat);

        return point;
    }

    public ProjectionType getType() {
        return ProjectionType.PROJECTED;
    }

}
