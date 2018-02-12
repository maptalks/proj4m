package org.maptalks.proj4;

public interface PointAdaptor<T> {
    double getX(T point);

    double getY(T point);

    void setX(T point, double x);

    void setY(T point, double y);
}
