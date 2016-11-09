package math;

/**
 * Created by ekutan on 11/8/16.
 */
public abstract class HoughFunction {
    protected double x;
    protected double y;

    public HoughFunction(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public abstract double calculateR(double theta);

}
