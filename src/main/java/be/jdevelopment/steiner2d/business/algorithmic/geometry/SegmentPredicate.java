package be.jdevelopment.steiner2d.business.algorithmic.geometry;

@FunctionalInterface
public strictfp interface SegmentPredicate {

    boolean test(double x1, double y1, double x2, double y2);

}
