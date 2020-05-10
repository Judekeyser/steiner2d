package be.jdevelopment.steiner2d.training.dichotomy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static be.jdevelopment.steiner2d.training.dichotomy.TransitionFunction.crossOver;
import static be.jdevelopment.steiner2d.training.dichotomy.TransitionFunction.mutate;
import static java.util.Comparator.comparingDouble;

public class SelectionStrategy {

    public List<TransitionFunction> select (Map<TransitionFunction, Double> results) {
        int targetPopulationSize = results.size();
        List<TransitionFunction> afterSelection = computeVirtualPopulation(results);

        // Fill population with 25% children
        List<TransitionFunction> children = IntStream.range(0, targetPopulationSize - targetPopulationSize / 2)
                .mapToObj($ -> new ArrayList<>(afterSelection))
                .peek(Collections::shuffle)
                .map(List::stream)
                .map(stream -> stream.limit(2).toArray(TransitionFunction[]::new))
                .map(arr -> generateNewChild(arr[0], arr[1]))
                .collect(Collectors.toList());

        return Stream.of(children, afterSelection)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private TransitionFunction generateNewChild(TransitionFunction father, TransitionFunction mother) {
        TransitionFunction child = crossOver(father, mother);
        if(Math.random() < 0.5)
            child = mutate(child);

        return child;
    }

    private List<TransitionFunction> computeVirtualPopulation(Map<TransitionFunction, Double> results) {
        int totalSize = results.size();
        return results.entrySet().stream()
                .sorted(comparingDouble(Map.Entry<TransitionFunction, Double>::getValue))
                .limit(totalSize / 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
