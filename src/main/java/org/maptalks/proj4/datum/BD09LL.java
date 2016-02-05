package org.maptalks.proj4.datum;

import org.maptalks.proj4.Point;

public class BD09LL implements Datum {

    private static double PI = Math.PI;
    private static double X_PI = PI * 3000.0 / 180.0;

    private Datum gcj02 = new GCJ02();

    public Point toWGS84(Point point) {
        return gcj02.toWGS84(toGCJ02(point));
    }

    public Point fromWGS84(Point point) {
        return fromGCJ02(gcj02.fromWGS84(point));
    }

    public static Point toGCJ02(Point point) {
        double x = point.getX();
        double y = point.getY();
        x = x - 0.0065;
        y = y - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        x = z * Math.cos(theta);
        y = z * Math.sin(theta);
        point.setX(x);
        point.setY(y);

        return point;
    }

    public static Point fromGCJ02(Point point) {
        double x = point.getX();
        double y = point.getY();
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
        y = z * Math.sin(theta) + 0.006;
        x = z * Math.cos(theta) + 0.0065;
        point.setX(x);
        point.setY(y);
        return point;
    }

}
