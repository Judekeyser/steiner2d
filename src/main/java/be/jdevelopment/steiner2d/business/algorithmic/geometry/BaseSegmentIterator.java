package be.jdevelopment.steiner2d.business.algorithmic.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;

import java.util.PrimitiveIterator;

public class BaseSegmentIterator implements PrimitiveIterator.OfInt {

    private final SegmentPredicate predicate;
    private final ShapeWrapper shapeWrapper;
    private final int startInclusive, endExclusive, xBackward, yBackward;
    public BaseSegmentIterator(ShapeWrapper wrapper, SegmentPredicate predicate) {
        shapeWrapper = wrapper;
        this.predicate = predicate;

        startInclusive = 0;
        endExclusive = shapeWrapper.complexity();
        xBackward = shapeWrapper.complexity() - 2;
        yBackward = shapeWrapper.complexity() - 1;

        cursor = init();
    }

    private boolean okWithTail;
    private int cursor;

    @Override public boolean hasNext() {
        return cursor < endExclusive - 3 || okWithTail;
    }

    @Override public int nextInt() {
        int ghost = cursor;
        cursor = foresee();
        return ghost;
    }

    private int foresee() {
        if (okWithTail) {
            okWithTail = false;
            cursor = startInclusive - 2;
        }
        while(cursor < endExclusive - 4) {
            cursor += 2;
            if(predicate.test(
                    shapeWrapper.access(cursor), shapeWrapper.access(cursor+1),
                    shapeWrapper.access(cursor+2), shapeWrapper.access(cursor+3)
            )) return cursor;
        }
        return endExclusive;
    }

    private int init() {
        okWithTail = true;
        // Test predicate against end point and start point
        if (predicate.test(
                shapeWrapper.access(xBackward), shapeWrapper.access(yBackward),
                shapeWrapper.access(startInclusive), shapeWrapper.access(startInclusive + 1)
        )) return endExclusive - 2;
        return foresee();
    }

}
