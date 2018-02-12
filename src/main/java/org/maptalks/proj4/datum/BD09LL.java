package org.maptalks.proj4.datum;

import org.maptalks.proj4.PointAdaptor;

public class BD09LL<T> implements Datum<T> {

    private static double PI = Math.PI;
    private static double X_PI = PI * 3000.0 / 180.0;

    private Datum<T> gcj02 = new GCJ02<T>();

    public T toWGS84(T point, PointAdaptor<T> pointAdaptor) {
        return gcj02.toWGS84(toGCJ02(point, pointAdaptor), pointAdaptor);
    }

    public T fromWGS84(T point, PointAdaptor<T> pointAdaptor) {
        return fromGCJ02(gcj02.fromWGS84(point, pointAdaptor), pointAdaptor);
    }

    public static <T> T toGCJ02(T point, PointAdaptor<T> pointAdaptor) {
        double x = pointAdaptor.getX(point);
        double y = pointAdaptor.getY(point);
        x = x - 0.0065;
        y = y - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        x = z * Math.cos(theta);
        y = z * Math.sin(theta);
        pointAdaptor.setX(point, x);
        pointAdaptor.setY(point, y);

        return point;
    }

    public static <T> T fromGCJ02(T point, PointAdaptor<T> pointAdaptor) {
        double x = pointAdaptor.getX(point);
        double y = pointAdaptor.getY(point);
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
        y = z * Math.sin(theta) + 0.006;
        x = z * Math.cos(theta) + 0.0065;
        pointAdaptor.setX(point, x);
        pointAdaptor.setY(point, y);
        return point;
    }

}
