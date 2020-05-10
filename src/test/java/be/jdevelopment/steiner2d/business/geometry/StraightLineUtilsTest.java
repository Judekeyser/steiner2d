package be.jdevelopment.steiner2d.business.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.geometry.StraightLineUtils;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class StraightLineUtilsTest {

    private static double delta = 0.001D;

    @Test
    public void extrapolate_shouldProvideSameLength_givenVerticalConfiguration() {

        Random r = new Random();
        double length = r.nextDouble();

        assertEquals(length, extrapolate(r.nextDouble(), length, r.nextDouble(), length, r.nextDouble()),
                delta);
    }

    @Test
    public void extrapolate_shouldProvideCorrectEstimates_givenFairPair() {
        double y1 = 0;
        double y2 = 1;

        assertEquals(5D, extrapolate(y1, 1, y2, 3, 2), delta);
        assertEquals(1.5D, extrapolate(y1, 1, y2, 3, 1D/4D), delta);
        assertEquals(0.5D, extrapolate(y1, 1, y2, 3, -1D/4D), delta);
    }

    @Test
    public void extrapolate_shouldNotBeNegative_givenOutOfRangeTarget() {
        double y1 = 0;
        double y2 = 1;

        assertEquals(0D, extrapolate(y1, 1, y2, 3, -1), delta);
    }

    @Test
    public void hasIntersection_shouldReturnFalse_givenOutOfRangeHeights() {
        double[] p1 = {0, 0};
        double[] p2 = {1, 1};

        assertFalse(hasIntersection(p2[1], p1[1], 2));
        assertFalse(hasIntersection(p2[1], p1[1], 1));
    }

    @Test
    public void hasIntersection_shouldReturnTrue_givenIntersectingSegment() {
        double[] p1 = {0, 0};
        double[] p2 = {1, 1};

        double ref = 0.5;

        assertTrue(hasIntersection(p2[1], p1[1], ref));
    }

    @Test
    public void xofIntercept_shouldComputeXOfIntersection_givenIntersectingSegment() {
        double[] p1 = {0, 0};
        double[] p2 = {1, 1};

        double ref = 2.0/3.0;

        assertTrue(hasIntersection(p2[1], p1[1], ref));
        assertEquals(ref, xOfIntersect(p1[0], p1[1], p2[0], p2[1], ref), delta);
    }

    /** Shortcut from and to double */

    private static double extrapolate(double y1, double lengthAtY1, double y2, double lengthAtY2, double y) {
        return StraightLineUtils.extrapolate(
                y1, lengthAtY1,
                y2, lengthAtY2,
                y
        );
    }

    private static boolean hasIntersection(double y1, double y2, double yRef) {
        return StraightLineUtils.hasIntersection(
                y1,y2,yRef
        );
    }

    private static double xOfIntersect(double x1, double y1, double x2, double y2, double yRef) {
        return StraightLineUtils.xOfIntersect(
                x1, y1,
                x2, y2,
                yRef
        );
    }

}
