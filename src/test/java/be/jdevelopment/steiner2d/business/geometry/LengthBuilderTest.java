package be.jdevelopment.steiner2d.business.geometry;

import be.jdevelopment.steiner2d.business.algorithmic.geometry.LengthBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class LengthBuilderTest {

    private final double delta = 0.001D;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void build_shouldComputeLength_givenStackOfFourAndSmallSize() {
        LengthBuilder builder = new LengthBuilder(3);

        builder.stackDouble(3);
        builder.stackDouble(0);
        builder.stackDouble(2);
        builder.stackDouble(1);

        assertEquals(2.0D, builder.build(), delta);
    }

    @Test
    public void build_shouldComputeLength_givenStackOfFourAndLargeSize() {
        LengthBuilder builder = new LengthBuilder(10);

        builder.stackDouble(3);
        builder.stackDouble(0);
        builder.stackDouble(2);
        builder.stackDouble(1);

        assertEquals(2.0D, builder.build(), delta);
    }

    @Test
    public void build_shouldComputeLength_givenStackOfFourAndExactSize() {
        LengthBuilder builder = new LengthBuilder(4);

        builder.stackDouble(3);
        builder.stackDouble(0);
        builder.stackDouble(2);
        builder.stackDouble(1);

        assertEquals(2.0D, builder.build(), delta);
    }

    @Test
    public void build_shouldReturnZero_givenNoRecord() {
        LengthBuilder builder = new LengthBuilder(3);

        assertEquals(0D, builder.build(), delta);
    }

    @Test
    public void build_shouldThrowException_givenOddStack() {
        LengthBuilder builder = new LengthBuilder(3);
        builder.stackDouble(0D);

        expected.expect(IllegalStateException.class);
        expected.expectMessage(String.format("Unable to compute length from 1-size array: %f", 0D));
        builder.build();
    }

}
