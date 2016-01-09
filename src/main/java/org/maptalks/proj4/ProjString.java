package org.maptalks.proj4;

public class ProjString {

    public static Proj parse(String srs) {
        Proj proj = new Proj();
        String projName = null;
        String datumCode = null;
        String[] parts = srs.split("\\+");
        for (int i = 0; i < parts.length; i++) {
            String s = parts[i].trim();
            String[] kv = s.split("=");
            if (kv.length != 2) continue;
            String k = kv[0];
            String v = kv[1];
            if ("proj".equals(k)) {
                projName = v;
            } else if ("datum".equals(k)) {
                datumCode = v;
            }
        }
        proj.setProjName(projName);
        proj.setDatumCode(datumCode);

        return proj;
    }

}
