package org.maptalks.proj4.datum;

import org.maptalks.proj4.PointAdaptor;

public interface Datum<T> {

    T toWGS84(T point, PointAdaptor<T> pointAdaptor);

    T fromWGS84(T point, PointAdaptor<T> pointAdaptor);

}
