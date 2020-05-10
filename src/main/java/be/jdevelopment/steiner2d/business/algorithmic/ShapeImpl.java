package be.jdevelopment.steiner2d.business.algorithmic;

public class ShapeImpl {

    public static ShapeImpl fromFlat(double[] cloud) {
        return new ShapeImpl(cloud);
    }

    protected final double[] flat;
    protected ShapeImpl(double[] cloud) {
        flat = cloud;
    }

}
