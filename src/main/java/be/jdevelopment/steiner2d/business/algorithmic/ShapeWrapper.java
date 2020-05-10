package be.jdevelopment.steiner2d.business.algorithmic;

import be.jdevelopment.steiner2d.business.Shape;

import java.util.StringJoiner;
import java.util.stream.IntStream;

public class ShapeWrapper extends ShapeImpl implements Shape {

    public ShapeWrapper(final ShapeImpl shapeImpl) {
        super(shapeImpl.flat);
    }

    public final double access(int idx) {
        return flat[idx];
    }

    public final void access(int idx, double value) { flat[idx] = value; }

    public final int complexity() {
        return flat.length;
    }

    public final boolean contain(long x, long y) {
        double delta = 0.01D;
        for (int i = 0; i < flat.length; i += 2) {
            if (Math.abs(flat[i] - x) <= delta && Math.abs(flat[i+1]-y) <= delta) {
                return true;
            }
        }
        return false;
    }

    public double[] yTrace() {
        return IntStream.range(0,flat.length/2)
                .map(i -> 2*i+1)
                .mapToDouble(i -> flat[i])
                .sorted()
                .distinct()
                .toArray();
    }

    @Override public strictfp double perimeter() {
        double perimeter = norm(flat.length-2, 0);
        for (int i = 0; i < flat.length - 2; i += 2) {
            perimeter += norm(i, i+2);
        }
        return perimeter;
    }

    private strictfp double norm(int i1, int i2) {
        return norm(flat[i1], flat[i1+1], flat[i2], flat[i2+1]);
    }

    private static final double COEFF_NUMERIC = 1000D;
    private strictfp double norm(double x1, double y1, double x2, double y2) {
        double diffX = COEFF_NUMERIC * x1 - COEFF_NUMERIC * x2;
        double diffY = COEFF_NUMERIC * y1 - COEFF_NUMERIC * y2;
        return Math.hypot(Math.abs(diffX), Math.abs(diffY)) / COEFF_NUMERIC;
    }

    @Override public String toString() {
        StringJoiner joiner = new StringJoiner(" ");
        for(int i = 0; i < flat.length; i+= 2) {
            joiner.add(String.format("%f;%f", flat[i], flat[i+1]));
        }
        return joiner.toString();
    }

}
