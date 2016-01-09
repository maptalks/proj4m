package org.maptalks.proj4.datum;

import org.maptalks.proj4.Point;

public class GCJ02 implements Datum {

    private static double PI = Math.PI;
    private static double AXIS = 6378245.0;  //
    private static double OFFSET = 0.00669342162296594323;  //(a^2 - b^2) / a^2

    public Point toWGS84(Point point) {
        double lng = point.getX();
        double lat = point.getY();

        if (outOfChina(lng, lat)) {
            return point;
        }

        double[] deltaD = delta(lng, lat);
        lng = lng - deltaD[0];
        lat = lat - deltaD[1];

        point.setX(lng);
        point.setY(lat);

        return point;
    }

    public Point fromWGS84(Point point) {
        double lng = point.getX();
        double lat = point.getY();

        if (outOfChina(lng, lat)) {
            return point;
        }

        double[] deltaD = delta(lng, lat);
        lng = lng + deltaD[0];
        lat = lat + deltaD[1];

        point.setX(lng);
        point.setY(lat);

        return point;
    }

    private static double[] delta(double wgLon, double wgLat) {
        double[] lngLat = new double[2];
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - OFFSET * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((AXIS * (1 - OFFSET)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (AXIS / sqrtMagic * Math.cos(radLat) * PI);
        lngLat[1] = dLat;
        lngLat[0] = dLon;
        return lngLat;
    }

    private static boolean outOfChina(double lon, double lat) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

}
