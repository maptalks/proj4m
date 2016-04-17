package org.maptalks.proj4;

import org.maptalks.proj4.rhino.JsonModuleScriptProvider;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.MultiModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Proj4 implements Closeable {

    private Proj srcProj;
    private Proj dstProj;
    private String srcSRS;
    private String dstSRS;
    private Context cx;
    private ScriptableObject scope;
    private Function proj4;

    public Proj4(String srcSRS, String dstSRS) throws Proj4Exception {
        this.srcSRS = srcSRS;
        this.dstSRS = dstSRS;
        this.srcProj = Parser.parseCode(srcSRS);
        this.dstProj = Parser.parseCode(dstSRS);
    }

    public double[] forward(double[] coord) throws IllegalArgumentException, Proj4Exception {
        if (srcProj.equals(dstProj)) {
            return new double[]{coord[0], coord[1]};
        }
        if (needProj4js(srcProj, dstProj)) {
            loadProj4();
            return forwardUsingProj4js(coord);
        }
        Point point = Point.fromArray(coord);
        point = Transform.transform(srcProj, dstProj, point);
        return new double[]{point.getX(), point.getY()};
    }

    public double[] inverse(double[] coord) throws IllegalArgumentException, Proj4Exception {
        if (srcProj.equals(dstProj)) {
            return new double[]{coord[0], coord[1]};
        }
        if (needProj4js(srcProj, dstProj)) {
            loadProj4();
            return inverseUsingProj4js(coord);
        }
        Point point = Point.fromArray(coord);
        point = Transform.transform(dstProj, srcProj, point);
        return new double[]{point.getX(), point.getY()};
    }

    public void close() throws IOException {
        if (proj4 != null) {
            Context.exit();
        }
    }

    private void loadProj4() {
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

        URL base = getClass().getResource("/proj4js");
        // TODO: how to load...
        require.requireMain(cx, base + "/lib/index");
        Object obj = scope.get("proj4", scope);

        proj4 = (Function) obj;
    }

    private double[] forwardUsingProj4js(double[] coord) {
        // TODO: convert args
        Object result = proj4.call(cx, scope, scope, new Object[]{srcSRS, dstSRS, coord});
        return (double[]) Context.jsToJava(result, Double[].class);
    }

    private double[] inverseUsingProj4js(double[] coord) {
        Object result = proj4.call(cx, scope, scope, new Object[]{dstSRS, srcSRS, coord});
        return (double[]) Context.jsToJava(result, Double[].class);
    }

    private static boolean isMarsDatum(String datum) {
        return "BD09".equalsIgnoreCase(datum) || "GCJ02".equalsIgnoreCase(datum);
    }

    private static boolean needProj4js(Proj srcProj, Proj dstProj) {
        return (!isMarsDatum(srcProj.getDatumCode()) && !isMarsDatum(dstProj.getDatumCode()));
    }

}
