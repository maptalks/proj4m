package org.maptalks.proj4.rhino;

import org.mozilla.javascript.*;
import org.mozilla.javascript.commonjs.module.ModuleScript;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.json.JsonParser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

public class JsonModuleScriptProvider implements ModuleScriptProvider {

    @Override
    public ModuleScript getModuleScript(Context cx, String moduleId, URI moduleUri,
                                        URI baseUri, Scriptable paths) throws Exception {
        if (!moduleId.endsWith(".json")) {
            return null;
        }

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(moduleUri)));
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

        @Override
        public Object exec(Context cx, Scriptable scope) {
            // TODO: how to assign obj to exports
            scope.put("exports", scope, obj);
            return Undefined.instance;
        }
    }
}
