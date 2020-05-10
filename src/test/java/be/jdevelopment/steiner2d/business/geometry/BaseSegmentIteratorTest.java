package be.jdevelopment.steiner2d.business.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeImpl;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.BaseSegmentIterator;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.SegmentPredicate;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.StraightLineUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSegmentIteratorTest {

    @Test
    public void iterator_shouldIterate_givenPredicateOfSegment() {
        double[] flatCloud = {-1000,0,0,1000,1000,0,0,-1000};
        ShapeWrapper wrapper = new ShapeWrapper(ShapeImpl.fromFlat(flatCloud));
        SegmentPredicate predicate = (x1, y1, x2, y2) -> StraightLineUtils.hasIntersection(y1,y2, 0.5);
        BaseSegmentIterator iterator = new BaseSegmentIterator(wrapper, predicate);

        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.nextInt());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.nextInt());
        assertFalse(iterator.hasNext());

        predicate = (x1,y1,x2,y2) -> StraightLineUtils.hasIntersection(y1,y2, -0.5);
        iterator = new BaseSegmentIterator(wrapper, predicate);

        assertTrue(iterator.hasNext());
        assertEquals(flatCloud.length-2, iterator.nextInt());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.nextInt());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void iterator_shouldIterateAll_givenTrivialTruePredicate() {
        double[] flatCloud = {-1000,0,0,1000,1000,0,0,-1000};
        ShapeWrapper wrapper = new ShapeWrapper(ShapeImpl.fromFlat(flatCloud));
        SegmentPredicate predicate = (x1,y1,x2,y2) -> true;
        BaseSegmentIterator iterator = new BaseSegmentIterator(wrapper, predicate);

        assertTrue(iterator.hasNext());
        iterator.nextInt();
        assertTrue(iterator.hasNext());
        iterator.nextInt();
        assertTrue(iterator.hasNext());
        iterator.nextInt();
        assertTrue(iterator.hasNext());
        iterator.nextInt();

        assertFalse(iterator.hasNext());
    }

    @Test
    public void iterator_shouldIterateNone_givenTrivialFalse() {
        double[] flatCloud = {-1000,0,0,1000,1000,0,0,-1000};
        ShapeWrapper wrapper = new ShapeWrapper(ShapeImpl.fromFlat(flatCloud));
        SegmentPredicate predicate = (x1,y1,x2,y2) -> false;
        BaseSegmentIterator iterator = new BaseSegmentIterator(wrapper, predicate);
        assertFalse(iterator.hasNext());
    }

}
