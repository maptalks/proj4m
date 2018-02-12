package org.maptalks.proj4.projection;

import org.maptalks.proj4.PointAdaptor;

public class LonLat<T> implements Projection<T> {

    public T forward(T point, PointAdaptor<T> pointAdaptor) {
        return point;
    }

    public T inverse(T point, PointAdaptor<T> pointAdaptor) {
        return point;
    }

    public ProjectionType getType() {
        return ProjectionType.GEOGRAPHIC;
    }

}
