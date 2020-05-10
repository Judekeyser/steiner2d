package be.jdevelopment.steiner2d.business.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeImpl;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.Slicer;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.Bidi;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class SlicerTest {

    private double delta = 1D;

    @Test
    public void lengthAt_shouldGiveLength_givenVariousHeightsNonOscillatingShape() {
        double[] points = {-1000,0, 0,0, 0,-2000, 1000,0, 1000,1000, 0,1000};
        ShapeImpl shape = ShapeImpl.fromFlat(points);
        ShapeWrapper wrapper = new ShapeWrapper(shape);

        Slicer slicer = new Slicer(wrapper);

        assertEquals(0, slicer.lengthAt(2000), delta);
        assertEquals(2000, slicer.lengthAt(1), delta);
        assertEquals(1000, slicer.lengthAt(999), delta);
        assertEquals(0, slicer.lengthAt(-2000), delta);
        assertEquals(500, slicer.lengthAt(-1000), delta);
    }

    @Test
    public void lengthAt_shouldGiveLength_givenOscillatingShape() {
        double[] points = {-2000,0, -1000,1000, 0,0, 1000,1000, 2000,0};
        ShapeImpl shape = ShapeImpl.fromFlat(points);
        ShapeWrapper wrapper = new ShapeWrapper(shape);

        Slicer slicer = new Slicer(wrapper);

        assertEquals(2000, slicer.lengthAt(500), delta);
    }

}
