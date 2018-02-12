package org.maptalks.proj4.datum;

import org.maptalks.proj4.PointAdaptor;

public class WGS84<T> implements Datum<T> {

    public T toWGS84(T point, PointAdaptor<T> pointAdaptor) {
        return point;
    }

    public T fromWGS84(T point, PointAdaptor<T> pointAdaptor) {
        return point;
    }

}
