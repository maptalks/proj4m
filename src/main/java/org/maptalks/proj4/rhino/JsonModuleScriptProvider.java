package org.maptalks.proj4.rhino;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.Map;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.TopLevel;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.commonjs.module.ModuleScript;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.ModuleSource;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;
import org.mozilla.javascript.json.JsonParser;

public class JsonModuleScriptProvider implements ModuleScriptProvider {

    private ModuleSourceProvider moduleSourceProvider;

    public JsonModuleScriptProvider(Iterable<URI> privilegedUris) {
        this.moduleSourceProvider = new JsonUrlModuleSourceProvider(privilegedUris);
    }

    public ModuleScript getModuleScript(Context cx, String moduleId, URI moduleUri,
                                        URI baseUri, Scriptable paths) throws Exception {
        if (!moduleId.endsWith(".json")) {
            return null;
        }

        ModuleSource moduleSource = moduleSourceProvider.loadSource(moduleUri, baseUri, null);
        Reader reader = moduleSource.getReader();
        String json = Kit.readReader(reader);

        JsonParser parser = new JsonParser(cx, new TopLevel());
        Object obj = parser.parseValue(json);

        JsonScript script = new JsonScript(obj);

        return new ModuleScript(script, moduleUri, baseUri);
    }

    private class JsonUrlModuleSourceProvider extends UrlModuleSourceProvider {

        JsonUrlModuleSourceProvider(Iterable<URI> privilegedUris) {
            super(privilegedUris, null);
        }

        @Override
        protected ModuleSource loadFromUri(URI uri, URI base, Object validator) throws IOException {
            return loadFromActualUri(uri, base, validator);
        }

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
