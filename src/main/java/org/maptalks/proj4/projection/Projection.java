package org.maptalks.proj4.projection;

import org.maptalks.proj4.Point;

public interface Projection {

    Point forward(Point point);

    Point inverse(Point point);

}
