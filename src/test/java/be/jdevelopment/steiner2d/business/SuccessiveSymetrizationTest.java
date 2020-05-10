package be.jdevelopment.steiner2d.business;

import be.jdevelopment.steiner2d.api.codec.FileDecoder;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.Rotator;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeImpl;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.Symetrizer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class SuccessiveSymetrizationTest {

    private static final double delta = 1D;
    private static ShapeWrapper shape = null;
    @Before
    public void setup() throws IOException {
        try(InputStream inputStream = SuccessiveSymetrizationTest.class.getClassLoader().getResourceAsStream("long-chain.txt")) {
            shape = new ShapeWrapper(ShapeImpl.fromFlat(new FileDecoder().decodeChain(inputStream)));
        }
        assertEquals(471.67D, shape.perimeter(), delta);
        System.out.printf("\nInitial shape: complexity %d, perimeter %f\n",
                shape.complexity(),
                shape.perimeter());
    }

    @Test
    public void successiveSymetrization_performingSameTwice_doesNotChangeMuchOnSimpleShape() {
        Rotator rotator = new Rotator(0D);
        Symetrizer symetrizer = new Symetrizer();

        rotator.rotate(shape);
        shape = new ShapeWrapper(symetrizer.verticalSymetrize(shape));
        System.out.printf("\nIteration 1: complexity %d, perimeter %f",
                shape.complexity(),
                shape.perimeter());

        rotator.rotate(shape);
        shape = new ShapeWrapper(symetrizer.verticalSymetrize(shape));
        System.out.printf("\nIteration 2: complexity %d, perimeter %f",
                shape.complexity(),
                shape.perimeter());
    }

    @Test
    public void successiveSymetrization_performingSameTwice_doesNotChangeMuchOnComplexShape() {
        Rotator rotator;
        Symetrizer symetrizer = new Symetrizer();

        for(int i = 0; i < 10; i++) {
            rotator = new Rotator(random());
            rotator.rotate(shape);
            shape = new ShapeWrapper(symetrizer.verticalSymetrize(shape));
        }

        rotator = new Rotator(0D);

        rotator.rotate(shape);
        shape = new ShapeWrapper(symetrizer.verticalSymetrize(shape));
        System.out.printf("\nIteration 1: complexity %d, perimeter %f",
                shape.complexity(),
                shape.perimeter());

        rotator.rotate(shape);
        shape = new ShapeWrapper(symetrizer.verticalSymetrize(shape));
        System.out.printf("\nIteration 2: complexity %d, perimeter %f",
                shape.complexity(),
                shape.perimeter());

        rotator.rotate(shape);
        shape = new ShapeWrapper(symetrizer.verticalSymetrize(shape));
        System.out.printf("\nIteration 2: complexity %d, perimeter %f",
                shape.complexity(),
                shape.perimeter());
    }

    @Test
    public void successiveSymetrization_takenAtRandom_shouldConvergeToDisk() {
        int number = 20;
        int complexity = 0;
        double angle;
        double previousAngle = 0D;
        Rotator rotator;
        while (number >= 0) {
            number--;
            angle = random();
            rotator = new Rotator(angle - previousAngle);
            rotator.rotate(shape);
            previousAngle = angle;
            shape = new ShapeWrapper(new Symetrizer().verticalSymetrize(shape));
            if (shape.complexity() > complexity) complexity = shape.complexity();

            System.out.printf("\nIteration %d: complexity %d, perimeter %f",
                    20 - number,
                    shape.complexity(),
                    shape.perimeter());
        }

        double expectedPerimeter = 10* 2D * Math.sqrt(Math.PI * 112.5D);
        double expectedBlow = 14 * Math.pow(2, 14);
        assertEquals(expectedPerimeter, shape.perimeter(), delta);
        assertTrue(complexity <= expectedBlow);
    }

    @Test
    public void successiveSymetrization_takenAlternative_shouldConvergeToDisk() {
        double[] angles = {0D , 1D};
        int angleIndex = 0;

        int number = 20;
        int complexity = 0;
        double angle;
        double previousAngle = 0D;
        Rotator rotator;
        Symetrizer symetrizer = new Symetrizer();
        while (number >= 0) {
            number--;
            angle = angles[angleIndex++ % 2];
            rotator = new Rotator(angle - previousAngle);
            rotator.rotate(shape);
            previousAngle = angle;
            shape = new ShapeWrapper(symetrizer.verticalSymetrize(shape));
            if (shape.complexity() > complexity) complexity = shape.complexity();

            System.out.printf("\nIteration %d: complexity %d, perimeter %f",
                    number,
                    shape.complexity(),
                    shape.perimeter());
        }

        double expectedPerimeter = 10* 2D * Math.sqrt(Math.PI * 112.5D);
        double expectedBlow = 14 * Math.pow(2, 14);
        assertEquals(expectedPerimeter, shape.perimeter(), delta);
        assertTrue(complexity <= expectedBlow);
    }

    private double random() {
        return 3*Math.random();
    }

}
