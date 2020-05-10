package be.jdevelopment.steiner2d.business.algorithmic.geometry;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * LengthBuilder can internally stack double's x.
 *
 * The build process perform a sort and reduce the length
 */
public strictfp class LengthBuilder {

    private final int BUNCH_SIZE;
    public LengthBuilder(int bunchSize) {
        BUNCH_SIZE = bunchSize;

        stack = new double[bunchSize];
        cursor = -1;
    }

    private double[] stack;
    private int cursor;

    public void stackDouble(double record) {
        if (++cursor >= stack.length) {
            double[] augmented = new double[stack.length + BUNCH_SIZE];
            System.arraycopy(stack, 0, augmented, 0, stack.length);
            stack = augmented;
        }
        stack[cursor] = record;
    }

    public double build() {
        if (cursor == -1) return 0D;
        double[] extracted = new double[cursor+1];
        System.arraycopy(stack, 0, extracted, 0, cursor+1);
        Arrays.sort(extracted);

        if (extracted.length % 2 != 0)
            throw new IllegalStateException(errorMessage(extracted));

        double length = 0D;
        for(int i =0; i < extracted.length; i += 2) {
            length += COEFF_NUMERIC * extracted[i+1] - COEFF_NUMERIC * extracted[i];
        }
        return length / COEFF_NUMERIC;
    }

    private static final double COEFF_NUMERIC = 1000D;

    private static String errorMessage(double[] stack) {
        StringJoiner joiner = new StringJoiner(",");
        for(double d : stack)
            joiner.add(String.format("%f", d));
        return String.format("Unable to compute length from %d-size array: %s", stack.length, joiner.toString());
    }

}
