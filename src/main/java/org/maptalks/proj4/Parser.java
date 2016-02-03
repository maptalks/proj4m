package org.maptalks.proj4;

public class Parser {

    public static Proj parseCode(String code) throws Proj4Exception {
        if (testDef(code)) {
            return Global.def(code);
        }

        if (testProj(code)) {
            return ProjString.parse(code);
        }

        throw new Proj4Exception(String.format("Unknown code: '%s'", code));
    }

    private static boolean testDef(String name) {
        return Global.has(name);
    }

    private static boolean testProj(String code) {
        return code.startsWith("+");
    }

}
