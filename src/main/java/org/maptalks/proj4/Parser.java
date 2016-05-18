package org.maptalks.proj4;

class Parser {

    private static final String[] codeWords = new String[]{
        "GEOGCS", "GEOCCS", "PROJCS", "LOCAL_CS"
    };

    static Proj parseCode(String code) throws Proj4Exception {
        if (testDef(code)) {
            return Global.def(code);
        }

        if (testWKT(code)) {
            return WKTParser.parse(code);
        }

        if (testProj(code)) {
            return ProjString.parse(code);
        }

        throw new Proj4Exception(String.format("Unknown code: '%s'", code));
    }

    private static boolean testDef(String name) {
        return Global.has(name);
    }

    static boolean testWKT(String code) {
        int a = 0;
        for (String word : codeWords) {
            a = a + 1 + code.indexOf(word);
        }
        return a > 0;
    }

    static boolean testProj(String code) {
        return code.startsWith("+");
    }

}
