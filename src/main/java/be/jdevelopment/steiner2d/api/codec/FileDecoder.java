package be.jdevelopment.steiner2d.api.codec;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public class FileDecoder {

    private static final char DEFAULT_DECIMAL_SEPARATOR = '.';
    private static final char DEFAULT_NUMBER_SEPARATOR = ' ';
    private static final char DEFAULT_MINUS_SIGN = '-';
    private static final char DEFAULT_COORDS_SEPARATOR = ';';
    private static final char DEFAULT_TRIANGLE_SEPARATOR = '#';

    public FileDecoder() { }

    private char decimalSeparator = DEFAULT_DECIMAL_SEPARATOR;
    private char minusSign = DEFAULT_MINUS_SIGN;
    private char coordSeparator = DEFAULT_COORDS_SEPARATOR;
    private char numberSeparator = DEFAULT_NUMBER_SEPARATOR;
    private char triangleSeparator = DEFAULT_TRIANGLE_SEPARATOR;

    public double[] decodeChain(InputStream inputStream) throws IOException {
        final double[] pointBuffer = new double[2];
        List<double[]> coordinates = new Stack<>();

        Consumer<double[]> flushBuffer = arr -> {
            double[] copy = new double[2];
            System.arraycopy(arr, 0, copy, 0, 2);
            coordinates.add(copy);
        };

        StringBuilder charSeq = new StringBuilder();
        int r;
        int count = 0;
        while ((r = inputStream.read()) != -1) {
            if (r == numberSeparator || r == coordSeparator || r == triangleSeparator) { // triangleSep is end of file
                pointBuffer[count] = Long.parseLong(charSeq.toString());
                charSeq = new StringBuilder();
                if (r == decimalSeparator) charSeq.append(DEFAULT_DECIMAL_SEPARATOR);
                if (r == numberSeparator || r == triangleSeparator) {
                    count = 0;
                    flushBuffer.accept(pointBuffer);
                }
                else count++;
            }
            else if (r == decimalSeparator) charSeq.append(DEFAULT_DECIMAL_SEPARATOR);
            else if (r == minusSign) charSeq.append(DEFAULT_MINUS_SIGN);
            else if (r >= 48 && r <= 57) charSeq.append((char) r);
        }

        return flatten(coordinates);
    }

    public static double[] flatten(List<double[]> coordinates) {
        double[] result = new double[coordinates.size()*2];
        int idx = 0;
        for (double[] coord : coordinates) {
            result[idx++] = coord[0];
            result[idx++] = coord[1];
        }
        return result;
    }
}
