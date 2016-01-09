package org.maptalks.proj4;

import org.maptalks.proj4.projection.BaiduMercator;
import org.maptalks.proj4.projection.LonLat;
import org.maptalks.proj4.projection.Projection;
import org.maptalks.proj4.projection.SphericalMercator;

import java.util.HashMap;
import java.util.Map;

public class ProjectionCache {

    private static Map<String, Projection> cache = new HashMap<String, Projection>();

    static {
        Projection merc = new SphericalMercator();
        Projection baidu = new BaiduMercator();
        Projection longlat = new LonLat();
        cache.put("merc", merc);
        cache.put("bmerc", baidu);
        cache.put("baidu", baidu);
        cache.put("longlat", longlat);
    }

    public static Projection get(String name) throws Proj4Exception {
        if (name == null || name.trim().length() == 0) {
            return cache.get("merc");
        }
        name = name.toLowerCase();
        if (cache.containsKey(name)) {
            return cache.get(name);
        } else {
            throw new Proj4Exception(String.format("No such projection: %s", name));
        }
    }

}
