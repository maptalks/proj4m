package org.maptalks.proj4;

import org.maptalks.proj4.projection.Projection;
import org.maptalks.proj4.projection.ProjectionType;

public class Transform {

    public static Point transform(Proj srcProj, Proj dstProj, Point point) throws Proj4Exception {
        String srcProjName = srcProj.getProjName();
        String dstProjName = dstProj.getProjName();
        Projection srcProjection = ProjectionCache.get(srcProjName);
        Projection dstProjection = ProjectionCache.get(dstProjName);
        if (srcProjection.getType() == ProjectionType.PROJECTED) {
            point = srcProjection.inverse(point);
        }

        String srcDatumCode = srcProj.getDatumCode();
        String dstDatumCode = dstProj.getDatumCode();
        point = DatumTransform.transform(srcDatumCode, dstDatumCode, point);

        if (dstProjection.getType() == ProjectionType.PROJECTED) {
            point = dstProjection.forward(point);
        }

        return point;
    }

}
