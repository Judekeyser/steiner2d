package be.jdevelopment.steiner2d.business.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeImpl;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.Symetrizer;
import org.junit.Test;

import static org.junit.Assert.*;

public class SymetrizerTest {

    @Test
    public void verticalSymetrize_shouldLetInvariant_givenSymetricTriangle() {
        double[] points = {1000,0, -1000,0, 0,1000};
        ShapeImpl shape = ShapeImpl.fromFlat(points);
        ShapeWrapper symetrized = new ShapeWrapper(new Symetrizer().verticalSymetrize(shape));

        assertTrue(symetrized.contain(1000, 0));
        assertTrue(symetrized.contain(-1000,0));
        assertTrue(symetrized.contain(0,1000));
    }

    @Test
    public void verticalSymetrize_shouldSymetrize_shiftedTriangle() {
        double[] points = {1000,0, 0,0, 0,-1000};
        ShapeImpl shape = ShapeImpl.fromFlat(points);
        ShapeWrapper symetrized = new ShapeWrapper(new Symetrizer().verticalSymetrize(shape));

        assertTrue(symetrized.contain(500, 0));
        assertTrue(symetrized.contain(-500, 0));
        assertTrue(symetrized.contain(0,-1000));
    }

    @Test
    public void verticalSymetrize_shouldSymetrize_givenTwoTrianglesWithJump() {
        double[] points = {1000,0, 0,-1000, 0,0, -1000,0, 0,1000};
        ShapeImpl shape = ShapeImpl.fromFlat(points);
        ShapeWrapper symetrized = new ShapeWrapper(new Symetrizer().verticalSymetrize(shape));

        assertTrue(symetrized.contain(-1000, 0));
        assertTrue(symetrized.contain(1000, 0));
        assertTrue(symetrized.contain(500,0));
        assertTrue(symetrized.contain(-500,0));
        assertTrue(symetrized.contain(0,-1000));
    }

    @Test
    public void verticalSymetrize_shouldSymetrize_givenSquare() {
        double[] points = {1000,-1000, 1000,1000, -1000,1000, -1000,-1000};
        ShapeImpl shape = ShapeImpl.fromFlat(points);
        ShapeWrapper symetrized = new ShapeWrapper(new Symetrizer().verticalSymetrize(shape));

        assertTrue(symetrized.contain(-1000, 1000));
        assertTrue(symetrized.contain(1000, 1000));
        assertTrue(symetrized.contain(1000,-1000));
        assertTrue(symetrized.contain(-1000,-1000));
    }

    @Test
    public void verticalSymetrize_shouldSymetrize_oscillatingAndJumpShape() {
        double[] points = {1000,0, 0,-1000, 0,0, -2000,0, -1000,1000, 0,0, 1000,2000, 2000,0};
        ShapeImpl shape = ShapeImpl.fromFlat(points);
        ShapeWrapper symetrized = new ShapeWrapper(new Symetrizer().verticalSymetrize(shape));

        assertTrue(symetrized.contain(0, -1000));
        assertTrue(symetrized.contain(500, 0));
        assertTrue(symetrized.contain(-500, 0));
        assertTrue(symetrized.contain(2000, 0));
        assertTrue(symetrized.contain(-2000, 0));
        assertTrue(symetrized.contain(0, 2000));
        assertTrue(symetrized.contain(500, 1000));
        assertTrue(symetrized.contain(-500, 1000));
        assertFalse(symetrized.contain(0, 1000));
        assertFalse(symetrized.contain(1000, 0));
        assertFalse(symetrized.contain(-1000, 0));
    }

}
