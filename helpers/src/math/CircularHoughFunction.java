package math;

/**
 * Created by ekutan on 11/8/16.
 */
public class CircularHoughFunction extends HoughFunction {
    public CircularHoughFunction(double x, double y) {
        super(x, y);
    }

    public double calculateR(double theta) {
        return 0D;
    }
}
