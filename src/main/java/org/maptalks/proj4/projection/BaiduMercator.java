package org.maptalks.proj4.projection;

import org.maptalks.proj4.Point;

public class BaiduMercator implements Projection {

    public Point forward(Point point) {
        return this.convertLL2MC(point);
    }

    public Point inverse(Point point) {
        return this.convertMC2LL(point);
    }

    private Point convertMC2LL(Point point) {
        double y_abs = Math.abs(point.getY());

        double[] table = null;
        for (int i = 0; i < MCBAND.length; i++) {
            if (y_abs >= MCBAND[i]) {
                table = MC2LL[i];
                break;
            }
        }

        return this.convertor(point, table);
    }

    private Point convertLL2MC(Point point) {
        double lng = point.getX();
        double lat = point.getY();
        lng = this.getLoop(lng, -180, 180);
        lat = this.getRange(lat, -74, 74);
        point.setX(lng);
        point.setY(lat);

        double[] table = null;
        for (int i = 0; i < LLBAND.length; i++) {
            if (lat >= LLBAND[i]) {
                table = LL2MC[i];
                break;
            }
        }
        if (table == null) {
            for (int i = LLBAND.length - 1; i >= 0; i--) {
                if (lat <= -LLBAND[i]) {
                    table = LL2MC[i];
                    break;
                }
            }
        }

        return this.convertor(point, table);
    }

    private Point convertor(Point point, double[] table) {
        double px = point.getX();
        double py = point.getY();
        double x = table[0] + table[1] * Math.abs(px);
        double d = Math.abs(py) / table[9];
        double y = table[2]
            + table[3]
            * d
            + table[4]
            * d
            * d
            + table[5]
            * d
            * d
            * d
            + table[6]
            * d
            * d
            * d
            * d
            + table[7]
            * d
            * d
            * d
            * d
            * d
            + table[8]
            * d
            * d
            * d
            * d
            * d
            * d;

        x *= (px < 0 ? -1 : 1);
        y *= (py < 0 ? -1 : 1);

        point.setX(x);
        point.setY(y);

        return point;
    }

    private double getRange(double v, double min, double max) {
        v = Math.max(v, min);
        v = Math.min(v, max);

        return v;
    }

    private double getLoop(double v, double min, double max) {
        double d = max - min;
        while (v > max) {
            v -= d;
        }
        while (v < min) {
            v += d;
        }

        return v;
    }

    private static double[] MCBAND = new double[]{12890594.86, 8362377.87,
        5591021, 3481989.83, 1678043.12, 0};

    private static int[] LLBAND = new int[]{75, 60, 45, 30, 15, 0};

    private static double[][] MC2LL = new double[][]{
        {1.410526172116255e-8, 0.00000898305509648872, -1.9939833816331,
            200.9824383106796, -187.2403703815547, 91.6087516669843,
            -23.38765649603339, 2.57121317296198, -0.03801003308653,
            17337981.2},
        {-7.435856389565537e-9, 0.000008983055097726239,
            -0.78625201886289, 96.32687599759846, -1.85204757529826,
            -59.36935905485877, 47.40033549296737, -16.50741931063887,
            2.28786674699375, 10260144.86},
        {-3.030883460898826e-8, 0.00000898305509983578, 0.30071316287616,
            59.74293618442277, 7.357984074871, -25.38371002664745,
            13.45380521110908, -3.29883767235584, 0.32710905363475,
            6856817.37},
        {-1.981981304930552e-8, 0.000008983055099779535, 0.03278182852591,
            40.31678527705744, 0.65659298677277, -4.44255534477492,
            0.85341911805263, 0.12923347998204, -0.04625736007561,
            4482777.06},
        {3.09191371068437e-9, 0.000008983055096812155, 0.00006995724062,
            23.10934304144901, -0.00023663490511, -0.6321817810242,
            -0.00663494467273, 0.03430082397953, -0.00466043876332,
            2555164.4},
        {2.890871144776878e-9, 0.000008983055095805407, -3.068298e-8,
            7.47137025468032, -0.00000353937994, -0.02145144861037,
            -0.00001234426596, 0.00010322952773, -0.00000323890364,
            826088.5}};

    private static double[][] LL2MC = new double[][]{
        {-0.0015702102444, 111320.7020616939, 1704480524535203d,
            -10338987376042340d, 26112667856603880d,
            -35149669176653700d, 26595700718403920d,
            -10725012454188240d, 1800819912950474d, 82.5},
        {0.0008277824516172526, 111320.7020463578, 647795574.6671607,
            -4082003173.641316, 10774905663.51142, -15171875531.51559,
            12053065338.62167, -5124939663.577472, 913311935.9512032,
            67.5},
        {0.00337398766765, 111320.7020202162, 4481351.045890365,
            -23393751.19931662, 79682215.47186455, -115964993.2797253,
            97236711.15602145, -43661946.33752821, 8477230.501135234,
            52.5},
        {0.00220636496208, 111320.7020209128, 51751.86112841131,
            3796837.749470245, 992013.7397791013, -1221952.21711287,
            1340652.697009075, -620943.6990984312, 144416.9293806241,
            37.5},
        {-0.0003441963504368392, 111320.7020576856, 278.2353980772752,
            2485758.690035394, 6070.750963243378, 54821.18345352118,
            9540.606633304236, -2710.55326746645, 1405.483844121726,
            22.5},
        {-0.0003218135878613132, 111320.7020701615, 0.00369383431289,
            823725.6402795718, 0.46104986909093, 2351.343141331292,
            1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45}};

}
