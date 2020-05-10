package be.jdevelopment.steiner2d.api;

import be.jdevelopment.steiner2d.api.codec.FileDecoder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileDecoderTest {

    @Test
    public void decodeChain_shouldRead_givenFileOfChain() throws IOException {
        List<double[]> points;
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("initial-chain.txt")) {
            double[] flat = new FileDecoder().decodeChain(inputStream);
            points = new ArrayList<>(flat.length /2);
            for (int i = 0; i < flat.length / 2; i++) {
                double[] p = {flat[2*i], flat[2*i+1]};
                points.add(p);
            }
        }

        assertNotNull(points);
        assertEquals(6, points.size());
        for(double[] arr : points) {
            assertEquals(2, arr.length);
            System.out.printf("(%f,%f)\n",
                    arr[0], arr[1]);
        }
    }

}
