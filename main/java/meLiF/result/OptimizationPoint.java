package meLiF.result;

import java.util.function.Consumer;



public class OptimizationPoint extends Point {

    private final Point parent;

    public OptimizationPoint(Point parent, Consumer<double[]> diff) {
        super(parent.getCoordinates().clone());
        diff.accept(getCoordinates());
        this.parent = parent;
    }

    public Point getParent() {
        return parent;
    }
}
