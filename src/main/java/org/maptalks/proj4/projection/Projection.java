package org.maptalks.proj4.projection;

import org.maptalks.proj4.PointAdaptor;

public interface Projection<T> {

    T forward(T point, PointAdaptor<T> pointAdaptor);

    T inverse(T point, PointAdaptor<T> pointAdaptor);

    ProjectionType getType();

}
