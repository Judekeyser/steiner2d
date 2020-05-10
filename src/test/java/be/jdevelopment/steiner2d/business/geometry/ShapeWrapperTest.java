package be.jdevelopment.steiner2d.business.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeImpl;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ShapeWrapperTest {

    private double delta = 0.001D;

    @Test
    public void getYTrace_shouldReturnCorrectTrace_givenUnorderedBunchWithRepetitions() {
        double[] points = {0,1000, 2000,3000, 5000,9000, 6000,3000, 4000,5000};
        ShapeImpl shape = ShapeImpl.fromFlat(points);
        ShapeWrapper wrapper = new ShapeWrapper(shape);
        double[] ys = wrapper.yTrace();

        assertEquals(4, ys.length);

        assertEquals(1000, ys[0], delta);
        assertEquals(3000, ys[1], delta);
        assertEquals(5000, ys[2], delta);
        assertEquals(9000, ys[3], delta);
    }

}
