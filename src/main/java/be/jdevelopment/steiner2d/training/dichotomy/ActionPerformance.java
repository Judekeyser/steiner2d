package be.jdevelopment.steiner2d.training.dichotomy;

import be.jdevelopment.steiner2d.business.algorithmic.ShapeWrapper;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.Rotator;
import be.jdevelopment.steiner2d.business.algorithmic.geometry.Symetrizer;

import java.util.ArrayList;
import java.util.List;

public class ActionPerformance {

    public strictfp ShapeWrapper onShape(ShapeWrapper shape, List<Action> actions) {
        List<Action> copy = new ArrayList<>();
        Action previous = null;
        for(Action action : actions) {
            if (action != previous) {
                copy.add(action);
                previous = action;
            }
        }

        double previousAngle = 0D;

        for (Action action : copy) {
            double angle = getAngle(action);
            new Rotator(angle - previousAngle).rotate(shape);
            shape = new ShapeWrapper(new Symetrizer().verticalSymetrize(shape));
            previousAngle = angle;
        }

        new Rotator(previousAngle).rotate(shape);
        return shape;
    }

    private strictfp double getAngle(Action action) {
        return action == Action.ZERO ? 0D : 1D;
    }

}
