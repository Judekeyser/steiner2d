package be.jdevelopment.steiner2d.business.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.geometry.Rotator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RotatorTest {

    @Test
    public void rotate_shouldRotate_given90Angle() {
        Rotator rotator = new Rotator(Math.PI / 2D);

        double[] coordinates = {1D , 1D};
        rotator.rotate(coordinates);

        double delta = 0.001D;
        assertEquals(1D, coordinates[1], delta);
        assertEquals(-1D, coordinates[0], delta);
    }

}
