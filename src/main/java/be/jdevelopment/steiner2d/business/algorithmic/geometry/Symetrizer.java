package be.jdevelopment.steiner2d.business.algorithmic.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeImpl;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public strictfp class Symetrizer {

    public strictfp ShapeImpl verticalSymetrize(ShapeImpl shape) {
        return symetrize(shape, 0D);
    }

    public strictfp ShapeImpl symetrize(ShapeImpl shape, double angle) {
        ShapeWrapper wrapper = new ShapeWrapper(shape);
        new Rotator(angle).rotate(wrapper);

        Slicer slicer = new Slicer(wrapper);
        double[] ys = wrapper.yTrace();

        List<double[]> pointsForNewShape = new ArrayList<>();

        { // Compute half of the shape
            double firstAnchor, secondAnchor;
            double currentExtrapolation, previousExtrapolation;
            double lengthAtFirstAnchor, lengthAtSecondAnchor;

            double[] lengths;
            for (int ysCursor = 0; ysCursor < ys.length - 1; ysCursor++) {

                firstAnchor = ys[ysCursor] + (ys[ysCursor + 1] - ys[ysCursor]) / 4D;
                secondAnchor = ys[ysCursor] + (ys[ysCursor + 1] - ys[ysCursor]) / 2D;

                lengths = slicer.lengthAt(firstAnchor, new double[]{firstAnchor, secondAnchor});
                lengthAtFirstAnchor = lengths[0];
                lengthAtSecondAnchor = lengths[1];

                currentExtrapolation = StraightLineUtils.extrapolate(
                        firstAnchor, lengthAtFirstAnchor / 2D,
                        secondAnchor, lengthAtSecondAnchor / 2D,
                        ys[ysCursor]);
                pointsForNewShape.add(new double[]{currentExtrapolation, ys[ysCursor]});

                previousExtrapolation = StraightLineUtils.extrapolate(
                        firstAnchor, lengthAtFirstAnchor / 2D,
                        secondAnchor, lengthAtSecondAnchor / 2D,
                        ys[ysCursor + 1]);
                pointsForNewShape.add(new double[]{previousExtrapolation, ys[ysCursor + 1]});
            }
        }

        { // Duplicate list in reverse order
            final int size = pointsForNewShape.size();

            pointsForNewShape = Stream.concat(
                    pointsForNewShape.stream(),
                    IntStream.range(0, size)
                            .map(i -> size - (i + 1))
                            .mapToObj(pointsForNewShape::get)
                            .map(arr -> new double[]{-arr[0], arr[1]})
            ).collect(Collectors.toList());
        }

        {// Backward rotation
            Rotator rotator = new Rotator(-angle);
            pointsForNewShape = pointsForNewShape.stream()
                    .peek(rotator::rotate)
                    .collect(Collectors.toList());
        };

        {// Cleaning phase: if a point is too close from the previous one, we kill it
            Iterator<double[]> it = pointsForNewShape.iterator();
            ArrayList<double[]> stack = new ArrayList<>(pointsForNewShape.size()/2);
            double[] previous = it.next();
            double[] first = previous;
            double[] current = null;
            while(it.hasNext()) {
                current = it.next();
                if (assertMetricDifferent(previous[0], previous[1], current[0], current[1])) {
                    stack.add(previous);
                    previous = current;
                    current = null;
                } else {
                    previous = current;
                }
            }
            if (current == null) { // previous was different from this one
                if (assertMetricDifferent(previous[0], previous[1], first[0], first[1])) {
                    stack.add(previous);
                }
            }
            pointsForNewShape = stack;
        }

        return ShapeImpl.fromFlat(flatten(pointsForNewShape));
    }

    private boolean assertMetricDifferent(double x1, double y1, double x2, double y2) {
        double diffX = COEFF_NUMERIC * x1 - COEFF_NUMERIC * x2;
        double diffY = COEFF_NUMERIC * y1 - COEFF_NUMERIC * y2;
        return !(Math.abs(diffX) + Math.abs(diffY) <= COEFF_NUMERIC * 0.01D);
    }

    private static final double COEFF_NUMERIC = 1000D;

    private static double[] flatten(List<double[]> coordinates) {
        double[] result = new double[coordinates.size()*2];
        int idx = 0;
        for (double[] coord : coordinates) {
            result[idx++] = coord[0];
            result[idx++] = coord[1];
        }
        return result;
    }

}
