package org.maptalks.proj4;

public class Point {

    private double x;
    private double y;

    public static Point fromArray(double[] coord) throws IllegalArgumentException {
        if (coord == null) {
            throw new IllegalArgumentException("coord is null");
        }
        if (coord.length < 2) {
            throw new IllegalArgumentException("length of coord less than 2");
        }

        Point point = new Point();
        point.setX(coord[0]);
        point.setY(coord[1]);

        return point;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
