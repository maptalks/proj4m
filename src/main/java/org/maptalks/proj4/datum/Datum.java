package org.maptalks.proj4.datum;

import org.maptalks.proj4.Point;

public interface Datum {

    Point toWGS84(Point point);

    Point fromWGS84(Point point);

}
