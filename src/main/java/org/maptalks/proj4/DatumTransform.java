package org.maptalks.proj4;

import org.maptalks.proj4.datum.Datum;

public class DatumTransform {

    public static Point transform(String srcCode, String dstCode, Point point) throws Proj4Exception {
        if (srcCode == null || dstCode == null) {
            return point;
        }

        if (srcCode.equals(dstCode)) {
            return point;
        }

        Datum srcDatum = DatumCache.get(srcCode);
        Datum dstDatum = DatumCache.get(dstCode);

        Point wgs84 = srcDatum.toWGS84(point);

        return dstDatum.fromWGS84(wgs84);
    }

}
