package org.maptalks.proj4;

public class Proj4 {

    private Proj srcProj;
    private Proj dstProj;

    public Proj4(String srcSRS, String dstSRS) throws Proj4Exception {
        this.srcProj = Parser.parseCode(srcSRS);
        this.dstProj = Parser.parseCode(dstSRS);
    }

    public double[] forward(double[] coord) throws IllegalArgumentException, Proj4Exception {
        Point point = Point.fromArray(coord);
        point = Transform.transform(srcProj, dstProj, point);
        return new double[]{point.getX(), point.getY()};
    }

    public double[] inverse(double[] coord) throws IllegalArgumentException, Proj4Exception {
        Point point = Point.fromArray(coord);
        point = Transform.transform(dstProj, srcProj, point);
        return new double[]{point.getX(), point.getY()};
    }

}
