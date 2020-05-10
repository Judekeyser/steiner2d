package be.jdevelopment.steiner2d.business.algorithmic.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public strictfp class Slicer {

    private final ShapeWrapper wrapper;
    public Slicer(ShapeWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public final double lengthAt(double yRef) {
        return lengthAt(yRef, new double[]{yRef})[0];
    }

    public final double[] lengthAt(final double yRef, double[] ys) {

        int[] indexes;
        {
            PrimitiveIterator.OfInt segmentIndexesIterator = new BaseSegmentIterator(wrapper,
                    (x1,y1,x2,y2) -> StraightLineUtils.hasIntersection(y1,y2, yRef)
            );
            indexes = StreamSupport.intStream(Spliterators.spliteratorUnknownSize(segmentIndexesIterator, Spliterator.ORDERED), false)
                    .toArray();
        }

        double[] lengths = new double[ys.length];

        for (int i = 0; i < lengths.length; i++) {
            LengthBuilder builder = new LengthBuilder(5);
            int index;
            PrimitiveIterator.OfInt segmentIndexesIterator = Arrays.stream(indexes).iterator();
            while (segmentIndexesIterator.hasNext()) {
                index = segmentIndexesIterator.nextInt();
                builder.stackDouble(StraightLineUtils.xOfIntersect(
                        wrapper.access(index), wrapper.access(index + 1),
                        wrapper.access((index + 2) % wrapper.complexity()), wrapper.access((index + 3) % wrapper.complexity()),
                        ys[i]));
            }
            lengths[i] = builder.build();
        }

        return lengths;
    }

}
