package be.jdevelopment.steiner2d.training.dichotomy;

import java.util.Iterator;

public class MarkovProcess implements Iterator<Action> {

    private final TransitionFunction function;
    private Action state;
    private int maxIterations;
    public MarkovProcess(TransitionFunction function) {
        this.function = function;
        state = Action.ZERO;
        maxIterations = 12;
    }

    @Override
    public boolean hasNext() {
        return maxIterations > 0;
    }

    @Override
    public strictfp Action next() {
        maxIterations -= 1;
        double probabilityToStay = function.transition(state, state);

        if (Math.random() > probabilityToStay) state = state == Action.ZERO ? Action.ONE : Action.ZERO;

        return state;
    }
}
