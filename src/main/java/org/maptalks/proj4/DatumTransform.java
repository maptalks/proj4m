package org.maptalks.proj4;

import org.maptalks.proj4.datum.BD09LL;
import org.maptalks.proj4.datum.Datum;

class DatumTransform {

    static <T> T transform(String srcCode, String dstCode, T point, PointAdaptor<T> pointAdaptor) throws Proj4Exception {
        if (srcCode == null || dstCode == null) {
            return point;
        }

        if (srcCode.equals(dstCode)) {
            return point;
        }

        Datum<T> srcDatum = DatumCache.get(srcCode);
        Datum<T> dstDatum = DatumCache.get(dstCode);

        if (srcCode.equalsIgnoreCase("GCJ02") && dstCode.equalsIgnoreCase("BD09")) {
            return BD09LL.fromGCJ02(point, pointAdaptor);
        }

        if (dstCode.equalsIgnoreCase("GCJ02") && srcCode.equalsIgnoreCase("BD09")) {
            return BD09LL.toGCJ02(point, pointAdaptor);
        }

        T wgs84 = srcDatum.toWGS84(point, pointAdaptor);

        return dstDatum.fromWGS84(wgs84, pointAdaptor);
    }

}
