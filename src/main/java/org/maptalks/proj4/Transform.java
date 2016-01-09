package org.maptalks.proj4;

import org.maptalks.proj4.projection.Projection;

public class Transform {

    public static Point transform(Proj srcProj, Proj dstProj, Point point) throws Proj4Exception {
        String srcProjName = srcProj.getProjName();
        String dstProjName = dstProj.getProjName();
        Projection srcProjection = ProjectionCache.get(srcProjName);
        Projection dstProjection = ProjectionCache.get(dstProjName);
        if (!srcProjName.equals("longlat")) {
            point = srcProjection.inverse(point);
        }

        String srcDatumCode = srcProj.getDatumCode();
        String dstDatumCode = dstProj.getDatumCode();
        point = DatumTransform.transform(srcDatumCode, dstDatumCode, point);

        if (!dstProjName.equals("longlat")) {
            point = dstProjection.forward(point);
        }

        return point;
    }

}
