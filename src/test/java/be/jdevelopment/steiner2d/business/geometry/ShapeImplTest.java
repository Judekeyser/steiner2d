package be.jdevelopment.steiner2d.business.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeImpl;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShapeImplTest {

    private double delta = 1L;

    @Test
    public void getPerimeter_shouldComputePerimeter_givenShape() {
        double[] points = {-1000,0, 0,0, 0,-2000, 1000,0, 1000,1000, 0,1000};
        ShapeImpl shape = ShapeImpl.fromFlat(points);

        assertEquals(1000 * (5D + Math.sqrt(5D) + Math.sqrt(2D)), new ShapeWrapper(shape).perimeter(), delta);
    }

}
