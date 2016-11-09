package math;

/**
 * A function of the form r = x*cos(theta) + y*sin(theta), where x and y are passed in.
 *
 * Created by ekutan on 11/8/16.
 */
public class LinearHoughFunction extends HoughFunction {

    public LinearHoughFunction(double xCoord, double yCoord) {
        super(xCoord, yCoord);
    }

    public double calculateR(double theta) {
        return x*Math.cos(theta) + y*Math.sin(theta);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
