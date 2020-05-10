package be.jdevelopment.steiner2d.business.algorithmic.geometry;

/**
 * From a 4-points (y1, lengthAtY1, y2, lengthAtY2),
 * extrapolate the data to guess the length at some other y.
 *
 */
public strictfp class StraightLineUtils {

    public static double extrapolate(double y1, double lengthAtY1, double y2, double lengthAtY2, double y) {
        return Math.max(xOfIntersect(lengthAtY1, y1, lengthAtY2, y2, y), 0D);//lengthAtY1 + (y - y1) * /* invert of slope: */ (lengthAtY1 - lengthAtY2)/(y1 - y2);
    }

    public static boolean hasIntersection(double y1, double y2, double yRef) {
        double $y1 = COEFF_NUMERIC * y1;
        double $y2 = COEFF_NUMERIC * y2;
        double $yRef = COEFF_NUMERIC * yRef;
        // return (y1 < yRef && yRef < y2) || (y2 < yRef && yRef < y1);
        return ($y1 - $yRef) * ($y2 - $yRef) < 0D;
    }

    public static double xOfIntersect(double x1, double y1, double x2, double y2, double yRef) {
        double $x1 = COEFF_NUMERIC * x1;
        double $x2 = COEFF_NUMERIC * x2;
        double $y1 = COEFF_NUMERIC * y1;
        double $y2 = COEFF_NUMERIC * y2;
        double $yRef = COEFF_NUMERIC * yRef;
        return (
                $x1 + ($yRef - $y1) * /* invert of slope: */ ($x1 - $x2)/($y1 - $y2)
        ) / COEFF_NUMERIC;
    }

    private static final double COEFF_NUMERIC = 1000D;

}
