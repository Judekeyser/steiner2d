package be.jdevelopment.steiner2d.training.dichotomy;

/**
 * Memo:
 *  - a transition that says at same place: {0, 1}
 *  - maximal shifting: {0, 0}
 *  - ZERO is attractor: {1, 0}
 *  - ALPHA is attractor: {1, 1}
 */
public class TransitionFunction {

    public boolean obtainedByMutation = false;

    private final double[] geneticCode;
    public TransitionFunction (double[] geneticCode) {
        this.geneticCode = geneticCode;
    }

    public strictfp double transition (Action previous, Action next) {
        if (previous == Action.ZERO) {
            if (next == Action.ZERO) return geneticCode[0];
            else return 1D - geneticCode[0];
        } else if (previous == Action.ONE) {
            if (next == Action.ONE) return geneticCode[1];
            else return 1D - geneticCode[1];
        }
        throw new IllegalStateException();
    }

    public static strictfp TransitionFunction crossOver(TransitionFunction f, TransitionFunction g) {
        double[] mixed = new double[] {
                rdConvexCombination(f.geneticCode[0], g.geneticCode[0]),
                rdConvexCombination(f.geneticCode[1], g.geneticCode[1])
        };
        return new TransitionFunction(mixed);
    }

    public static strictfp TransitionFunction mutate(TransitionFunction f) {
        double[] bumped = new double[] {
                rdConvexCombination(f.geneticCode[1], Math.random() < 0.5D ? 1D : 0D),
                rdConvexCombination(f.geneticCode[0], Math.random() < 0.5D ? 1D : 0D)
        };
        TransitionFunction g = new TransitionFunction(bumped);
        g.obtainedByMutation = true;
        return g;
    }

    private static strictfp double rdConvexCombination(double x, double y) {
        double weight = Math.random();
        return weight * x + (1D - weight) * y;
    }

    @Override
    public String toString() {
        return String.format("{ %f, %f }", geneticCode[0], geneticCode[1]);
    }
}
