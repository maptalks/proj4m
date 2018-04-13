package org.maptalks.proj4.rhino;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.TopLevel;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.commonjs.module.ModuleScript;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.json.JsonParser;

public class JsonModuleScriptProvider implements ModuleScriptProvider {

    public ModuleScript getModuleScript(Context cx, String moduleId, URI moduleUri,
                                        URI baseUri, Scriptable paths) throws Exception {
        if (!moduleId.endsWith(".json")) {
            return null;
        }

        URL url = new URL(baseUri == null ? null : baseUri.toURL(), moduleUri.toString());
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(url.toURI())));
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int n;
        while ((n = in.read(buf, 0, buf.length)) != -1) {
            baos.write(buf, 0, n);
        }
        String json = baos.toString("UTF-8");

        JsonParser parser = new JsonParser(cx, new TopLevel());
        Object obj = parser.parseValue(json);

        JsonScript script = new JsonScript(obj);

        return new ModuleScript(script, moduleUri, baseUri);
    }

    private class JsonScript implements Script {

        private Object obj;

        JsonScript(Object obj) {
            this.obj = obj;
        }

        public Object exec(Context cx, Scriptable scope) {
            Object exports = ScriptableObject.getProperty(scope, "exports");
            NativeObject nobj = (NativeObject) obj;
            for (Map.Entry<Object, Object> entry : nobj.entrySet()) {
                ScriptRuntime.setObjectProp(exports, (String) entry.getKey(), entry.getValue(), cx);
            }
            return Undefined.instance;
        }
    }
}
