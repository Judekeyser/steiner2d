package be.jdevelopment.steiner2d.business;

import be.jdevelopment.steiner2d.api.codec.FileDecoder;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeImpl;
import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.Rotator;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.Symetrizer;
import be.jdevelopment.steiner2d.training.dichotomy.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MarkovPerformance {

    private static ShapeWrapper shape = null;
    @BeforeClass
    public static void setup() throws IOException {
        try(InputStream inputStream = MarkovPerformance.class.getClassLoader().getResourceAsStream("long-chain.txt")) {
            shape = new ShapeWrapper(ShapeImpl.fromFlat(new FileDecoder().decodeChain(inputStream)));
        }
        System.out.printf("\nInitial shape: complexity %d, perimeter %f\n",
                shape.complexity(),
                shape.perimeter());
        System.out.printf("\nTarget perimeter is %f\n",
                10* 2D * Math.sqrt(Math.PI * 112.5D));
    }

    @Test
    public void actionPerformance_shouldTurnAlpha_ifAlphaIsAttractor() {
        TransitionFunction transition = new TransitionFunction(new double[]{ 0D, 1D });
        List<Action> actions = randomActions(transition);

        new ActionPerformance().onShape(givenShape(), actions);
    }

    @Test
    public void actionPerformance_shouldTurnZero_ifZeroIsAttractor() {
        TransitionFunction transition = new TransitionFunction(new double[]{ 1D, 0D });
        List<Action> actions = randomActions(transition);

        new ActionPerformance().onShape(givenShape(), actions);
    }

    @Test
    public void actionPerformance_shouldOscillating_givenOscillatingDistribution() {
        TransitionFunction transition = new TransitionFunction(new double[]{ 0D, 0D });
        List<Action> actions = randomActions(transition);

        new ActionPerformance().onShape(givenShape(), actions);
    }

    @Test
    public void actionPerformance_shouldKeepAtZero_givenFullInertia() {
        TransitionFunction transition = new TransitionFunction(new double[]{ 1D, 1D });
        List<Action> actions = randomActions(transition);

         new ActionPerformance().onShape(givenShape(), actions);
    }

    @Test
    public void selection_shouldGetRidOfTwoBads() {
        TransitionFunction not_so_bad = new TransitionFunction(new double[]{ 1D, 1D });
        TransitionFunction very_bad_1 = new TransitionFunction(new double[]{ 0D, 1D });
        TransitionFunction very_bad_2 = new TransitionFunction(new double[]{ 1D, 0D });
        TransitionFunction ultra_good = new TransitionFunction(new double[]{ 0D, 0D });

        List<TransitionFunction> nextGeneration = next(List.of(
                not_so_bad, very_bad_2, very_bad_1, ultra_good
        ));

        System.out.print("\nThe next generation has come:");
        for (TransitionFunction e : nextGeneration) {
            System.out.printf("\n\tEntry with genetic code %s",
                    e.toString());
        }

        assertEquals(4, nextGeneration.size());
        assertTrue(nextGeneration.stream().anyMatch(ultra_good::equals));
    }

    @Test
    public void afterSeveralGenerations_geneticCode_shouldLookLike__0_0() {
        int populationSize = 15;
        Random rd = new Random();

        List<TransitionFunction> population = IntStream.range(0, populationSize)
                .mapToObj(i -> new double[]{rd.nextDouble(), rd.nextDouble()})
                .map(TransitionFunction::new)
                .collect(Collectors.toList());

        System.out.print("\n\n*************************************");
        System.out.print("\nThe first generation was:");
        for (TransitionFunction e : population) {
            System.out.printf("\n\tEntry with genetic code %s (mutated?: %s)",
                    e.toString(),
                    e.obtainedByMutation ? "yes" : "no");
        }

        int generationCount = 30;
        int count = generationCount;
        while(count-- > 0) population = next(population);

        System.out.print("\n\n*************************************");
        System.out.printf("\nThe %dth generation looks like:", generationCount);
        for (TransitionFunction e : population) {
            System.out.printf("\n\tEntry with genetic code %s (mutated?: %s)",
                    e.toString(),
                    e.obtainedByMutation ? "yes" : "no");
        }
    }

    private List<Action> randomActions(TransitionFunction f) {
        MarkovProcess process = new MarkovProcess(f);
        List<Action> actions = new Stack<>();
        while(process.hasNext()) actions.add(process.next());
        return actions;
    }

    private ShapeWrapper givenShape() {
        try(InputStream inputStream = MarkovPerformance.class.getClassLoader().getResourceAsStream("long-chain.txt")) {
            return new ShapeWrapper(ShapeImpl.fromFlat(new FileDecoder().decodeChain(inputStream)));
        } catch(IOException e) { throw new RuntimeException(e); }
    }

    private List<TransitionFunction> next(List<TransitionFunction> fs) {
        Map<TransitionFunction, Double> results = fs.stream()
                .collect(Collectors.toMap(
                        identity(),
                        f -> new ActionPerformance().onShape(givenShape(), randomActions(f)).perimeter()
                ));
        return new SelectionStrategy().select(results);
    }

}
