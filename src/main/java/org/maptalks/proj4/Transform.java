package org.maptalks.proj4;

import org.maptalks.proj4.projection.Projection;
import org.maptalks.proj4.projection.ProjectionType;

class Transform {

    static <T> T transform(Proj srcProj, Proj dstProj, T point, PointAdaptor<T> pointAdaptor) throws Proj4Exception {
        String srcProjName = srcProj.getProjName();
        String dstProjName = dstProj.getProjName();
        Projection<T> srcProjection = ProjectionCache.get(srcProjName);
        Projection<T> dstProjection = ProjectionCache.get(dstProjName);
        if (srcProjection.getType() == ProjectionType.PROJECTED) {
            point = srcProjection.inverse(point, pointAdaptor);
        }

        String srcDatumCode = srcProj.getDatumCode();
        String dstDatumCode = dstProj.getDatumCode();
        point = DatumTransform.transform(srcDatumCode, dstDatumCode, point, pointAdaptor);

        if (dstProjection.getType() == ProjectionType.PROJECTED) {
            point = dstProjection.forward(point, pointAdaptor);
        }

        return point;
    }

}
