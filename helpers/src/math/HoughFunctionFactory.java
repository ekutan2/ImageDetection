package math;

/**
 * Created by ekutan on 11/8/16.
 */
public class HoughFunctionFactory {

    public static <T extends HoughFunction> HoughFunction createHoughFunction(Class<T> clazz, double xCoord, double yCoord) {
        HoughFunction houghFunction = null;
        if (LinearHoughFunction.class.equals(clazz)) {
            houghFunction = new LinearHoughFunction(xCoord, yCoord);
        } else if (CircularHoughFunction.class.equals(clazz)) {
            houghFunction =  new CircularHoughFunction(xCoord, yCoord);
        }

        return  houghFunction;
    }
}
