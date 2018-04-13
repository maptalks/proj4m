package org.maptalks.proj4;

import java.io.Closeable;
import java.net.URI;
import java.net.URISyntaxException;
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

public class Proj4 implements Closeable {

    private static Context cx;
    private static ScriptableObject scope;
    private static Function proj4;
    private Proj srcProj;
    private Proj dstProj;
    private String srcSRS;
    private String dstSRS;

    public Proj4(String srcSRS, String dstSRS) throws Proj4Exception {
        this.srcSRS = srcSRS;
        this.dstSRS = dstSRS;
        this.srcProj = Parser.parseCode(srcSRS);
        this.dstProj = Parser.parseCode(dstSRS);
    }

    public Proj4(Proj srcSRS, Proj dstSRS) {
        this.srcProj = srcSRS;
        this.dstProj = dstSRS;
    }

    private static void loadProj4() {
        if (proj4 != null) {
            return;
        }

        cx = Context.enter();
        scope = cx.initStandardObjects();

        List<URI> uris = new ArrayList<URI>();
        URL base = Proj4.class.getResource("/proj4js/");
        try {
            uris.add(base.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        RequireBuilder rb = new RequireBuilder();
        ModuleScriptProvider jsonModuleScriptProvider = new JsonModuleScriptProvider(uris);
        ModuleScriptProvider genericModuleScriptProvider = new SoftCachingModuleScriptProvider(
            new UrlModuleSourceProvider(uris, null)
        );
        List<ModuleScriptProvider> providers = new ArrayList<ModuleScriptProvider>();
        providers.add(jsonModuleScriptProvider);
        providers.add(genericModuleScriptProvider);
        rb.setModuleScriptProvider(new MultiModuleScriptProvider(providers));
        rb.setSandboxed(false);
        Require require = rb.createRequire(cx, scope);

        Scriptable script = require.requireMain(cx, "lib/index");

        proj4 = (Function) script;
    }

    private static boolean isNotMarsDatum(String datum) {
        return !"BD09".equalsIgnoreCase(datum) && !"GCJ02".equalsIgnoreCase(datum);
    }

    private static boolean needProj4js(Proj srcProj, Proj dstProj) {
        return (isNotMarsDatum(srcProj.getDatumCode()) && isNotMarsDatum(dstProj.getDatumCode()));
    }

    public double[] forward(double[] coord) throws IllegalArgumentException, Proj4Exception {
        if (srcProj.equals(dstProj)) {
            return new double[] {coord[0], coord[1]};
        }
        Point point = Point.fromArray(coord);
        if (needProj4js(srcProj, dstProj)) {
            loadProj4();
            return forwardUsingProj4js(point);
        }
        point = Transform.transform(srcProj, dstProj, point);
        return new double[] {point.getX(), point.getY()};
    }

    public double[] inverse(double[] coord) throws IllegalArgumentException, Proj4Exception {
        if (srcProj.equals(dstProj)) {
            return new double[] {coord[0], coord[1]};
        }
        Point point = Point.fromArray(coord);
        if (needProj4js(srcProj, dstProj)) {
            loadProj4();
            return inverseUsingProj4js(point);
        }
        point = Transform.transform(dstProj, srcProj, point);
        return new double[] {point.getX(), point.getY()};
    }

    public void close() {
        if (proj4 != null) {
            Context.exit();
        }
    }

    private Point proj4js(String srcSRS, String dstSRS, Point point) {
        Object result = proj4.call(cx, scope, scope, new Object[] {
            Context.javaToJS(srcSRS, scope),
            Context.javaToJS(dstSRS, scope),
            Context.javaToJS(point, scope)
        });
        return (Point) Context.jsToJava(result, Point.class);
    }

    private double[] forwardUsingProj4js(Point point) {
        Point p = proj4js(srcSRS, dstSRS, point);
        return new double[] {p.getX(), p.getY()};
    }

    private double[] inverseUsingProj4js(Point point) {
        Point p = proj4js(dstSRS, srcSRS, point);
        return new double[] {p.getX(), p.getY()};
    }

}
