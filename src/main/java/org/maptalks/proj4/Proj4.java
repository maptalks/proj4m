package org.maptalks.proj4;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.maptalks.proj4.rhino.JsonModuleScriptProvider;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.MultiModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

public class Proj4<T> implements Closeable {

    private static Context cx;
    private static ScriptableObject scope;
    private static Function proj4;
    private final PointAdaptor<T> pointAdaptor;
    private final Proj srcProj;
    private final Proj dstProj;
    private String srcSRS;
    private String dstSRS;

    public Proj4(String srcSRS, String dstSRS) throws Proj4Exception {
        this.srcSRS = srcSRS;
        this.dstSRS = dstSRS;
        this.srcProj = Parser.parseCode(srcSRS);
        this.dstProj = Parser.parseCode(dstSRS);
        this.pointAdaptor = new PointAdaptor<T>() {
            public double getX(T point) {
                return ((Point) point).getX();
            }

            public double getY(T point) {
                return ((Point) point).getY();
            }

            public void setX(T point, double x) {
                ((Point) point).setX(x);
            }

            public void setY(T point, double y) {
                ((Point) point).setY(y);
            }
        };
    }

    public Proj4(String srcSRS, String dstSRS, PointAdaptor<T> pointAdaptor) throws Proj4Exception {
        this.srcSRS = srcSRS;
        this.dstSRS = dstSRS;
        this.srcProj = Parser.parseCode(srcSRS);
        this.dstProj = Parser.parseCode(dstSRS);
        this.pointAdaptor = pointAdaptor;
    }

    public Proj4(Proj srcSRS, Proj dstSRS) throws Proj4Exception {
        this.srcProj = srcSRS;
        this.dstProj = dstSRS;
        this.pointAdaptor = new PointAdaptor<T>() {
            public double getX(T point) {
                return ((Point) point).getX();
            }

            public double getY(T point) {
                return ((Point) point).getY();
            }

            public void setX(T point, double x) {
                ((Point) point).setX(x);
            }

            public void setY(T point, double y) {
                ((Point) point).setY(y);
            }
        };
    }

    public Proj4(Proj srcSRS, Proj dstSRS, PointAdaptor<T> pointExtractor) throws Proj4Exception {
        this.srcProj = srcSRS;
        this.dstProj = dstSRS;
        this.pointAdaptor = pointExtractor;
    }

    private static void loadProj4() {
        if (proj4 != null) {
            return;
        }

        cx = Context.enter();
        scope = cx.initStandardObjects();

        RequireBuilder rb = new RequireBuilder();
        ModuleScriptProvider jsonModuleScriptProvider = new JsonModuleScriptProvider();
        ModuleScriptProvider genericModuleScriptProvider = new SoftCachingModuleScriptProvider(
            new UrlModuleSourceProvider(null, null)
        );
        List<ModuleScriptProvider> providers = new ArrayList<ModuleScriptProvider>();
        providers.add(jsonModuleScriptProvider);
        providers.add(genericModuleScriptProvider);
        rb.setModuleScriptProvider(new MultiModuleScriptProvider(providers));
        rb.setSandboxed(false);
        Require require = rb.createRequire(cx, scope);
        require.install(scope);

        URL base = Proj4.class.getResource("/proj4js");
        Scriptable script = require.requireMain(cx, base + "/lib/index");

        proj4 = (Function) script;
    }

    private static boolean isMarsDatum(String datum) {
        return "BD09".equalsIgnoreCase(datum) || "GCJ02".equalsIgnoreCase(datum);
    }

    private static boolean needProj4js(Proj srcProj, Proj dstProj) {
        return (!isMarsDatum(srcProj.getDatumCode()) && !isMarsDatum(dstProj.getDatumCode()));
    }

    public T forward(T point) throws IllegalArgumentException, Proj4Exception {
        if (srcProj.equals(dstProj)) {
            return point;
        }
        if (needProj4js(srcProj, dstProj)) {
            loadProj4();
            return forwardUsingProj4js(point);
        }
        point = Transform.transform(srcProj, dstProj, point, pointAdaptor);
        return point;
    }

    public T inverse(T point) throws IllegalArgumentException, Proj4Exception {
        if (srcProj.equals(dstProj)) {
            return point;
        }
        if (needProj4js(srcProj, dstProj)) {
            loadProj4();
            return inverseUsingProj4js(point);
        }
        point = Transform.transform(dstProj, srcProj, point, pointAdaptor);
        return point;
    }

    public void close() throws IOException {
        if (proj4 != null) {
            Context.exit();
        }
    }

    private T proj4js(String srcSRS, String dstSRS, T point) {
        SimplePoint input = new SimplePoint(pointAdaptor.getX(point), pointAdaptor.getY(point));
        Object result = proj4.call(cx, scope, scope, new Object[] {
            Context.javaToJS(srcSRS, scope),
            Context.javaToJS(dstSRS, scope),
            Context.javaToJS(input, scope)
        });
        SimplePoint output = (SimplePoint) Context.jsToJava(result, SimplePoint.class);
        pointAdaptor.setX(point, output.getX());
        pointAdaptor.setY(point, output.getY());
        return point;
    }

    private T forwardUsingProj4js(T point) {
        return proj4js(srcSRS, dstSRS, point);
    }

    private T inverseUsingProj4js(T point) {
        return proj4js(dstSRS, srcSRS, point);
    }

}
