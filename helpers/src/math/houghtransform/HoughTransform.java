package math.houghtransform;

import datastructures.Pair;
import datastructures.RgbPixel;

import java.util.*;

/**
 * Created by burak on 11/8/16.
 */
public class HoughTransform {

    private final double THETA_UPPER_BD = 2*Math.PI;
    private final double THETA_LOWER_BD = 0;

    private double thetaIncrement;
    private int threshold;
    private Map<Pair<Double>, List<RgbPixel>> houghCurvePointsToPixelMap;      // Map of (theta, r) points to all pixels that correspond to hough curves that intersect here

    /**
     *
     * @param threshold
     * @param numThetaPartitions
     */
    public HoughTransform(int threshold, int numThetaPartitions) {
        if (numThetaPartitions < 1) {
            throw new RuntimeException("numThetaPartitions must be a positive integer");
        }

        this.threshold = threshold;
        this.thetaIncrement = THETA_UPPER_BD/numThetaPartitions;
        houghCurvePointsToPixelMap = new HashMap<>();
    }

    /**
     *
     * @param suspectPixels
     */
    public Collection<RgbPixel> getPixelsAlongObject(Collection<RgbPixel> suspectPixels, Shape shape) {
        houghCurvePointsToPixelMap.clear();

        //TODO TEST LINEAR
        for (RgbPixel pixel : suspectPixels) {
            sampelHoughCurve(pixel, shape.valueOf());
        }

        Set<RgbPixel>  pixelsAlongObject = new TreeSet<>();
        for (Pair<Double> houghPoint : houghCurvePointsToPixelMap.keySet()) {
            List<RgbPixel> pixelsAtThisHoughPoint = houghCurvePointsToPixelMap.get(houghPoint);

            int numIntersections = pixelsAtThisHoughPoint.size();
            if (numIntersections > threshold) {
                pixelsAlongObject.addAll(pixelsAtThisHoughPoint);
            }
        }

        return pixelsAlongObject;
    }


    /**
     * Converts a point to a hough curve and walks along the hough curve, adding the points visited to the Map<> above
     *
     * @param pixel         A datastructures.RgbPixel object
     */
    private <T extends HoughFunction> void sampelHoughCurve(RgbPixel pixel, Class<T> clazz) {
        int xCoord = pixel.getCoordinates().getValue1();
        int yCoord = pixel.getCoordinates().getValue2();

        // Use abstract class HoughFunction because it is general. This means that we can have any subtype, which will
        // be determined at runtime. (you could also use an interface)
        HoughFunction houghFunction = HoughFunctionFactory.createHoughFunction(clazz, xCoord, yCoord);

        for (double theta = THETA_LOWER_BD; theta <= THETA_UPPER_BD; theta+= thetaIncrement) {

            // Get the corresponding r value for this theta, thus we have a hough point (r, theta)
            double r = houghFunction.calculateR(theta);
            Pair<Double> houghPoint = new Pair<>(theta, r);

            // Update the list of pixels at this hough point
            List<RgbPixel> pixelsAtThisHoughPoint = houghCurvePointsToPixelMap.get(houghPoint);
            if (pixelsAtThisHoughPoint == null) {
                pixelsAtThisHoughPoint = new LinkedList<>();
                houghCurvePointsToPixelMap.put(houghPoint, pixelsAtThisHoughPoint);
            }
            pixelsAtThisHoughPoint.add(pixel);
        }
    }

    /**
     * Represents different shapes that can be detected in image
     */
    public enum Shape {
        LINE(LinearHoughFunction.class),
        CIRCLE(CircularHoughFunction.class);

        private Class clazz;

        Shape(Class clazz) {
            this.clazz = clazz;
        }

        public Class valueOf() {
            return clazz;
        }
    }

}
