package be.jdevelopment.steiner2d.business.algorithmic.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Take an array of double and an angle,
 * and rotate the array.
 *
 * This operation mutates the provided array.
 *
 */
public strictfp class Rotator {

    private final double cos, sin;
    public Rotator(double angle) {
        this.cos = cos(angle);
        this.sin = sin(angle);
    }

    public void rotate(double[] coordinates) {
        double x = coordinates[0];
        double y = coordinates[1];

        coordinates[0] = cos*x - sin*y;
        coordinates[1] = cos*y + sin*x;
    }

    public void rotate(ShapeWrapper wrapper) {
        double[] buffer = {0,0};
        for(int i = 0; i < wrapper.complexity(); i += 2) {
            buffer[0] = wrapper.access(i);
            buffer[1] = wrapper.access(i+1);

            rotate(buffer);

            wrapper.access(i, buffer[0]);
            wrapper.access(i+1, buffer[1]);
        }
    }

}
