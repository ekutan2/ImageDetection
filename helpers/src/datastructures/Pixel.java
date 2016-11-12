package datastructures;

/**
 * Created by burak on 11/8/16.
 */
public class Pixel {
    private static final int MAX_RGB_VALUE = 255;
    private static final int MIN_RGB_VALUE = 0;

    private Pair<Integer> coordinates;
    private Triplet<Integer> rgbPixel;

    public Pixel(Pair<Integer> coordinates, Triplet<Integer> rgbPixel) {
        this.coordinates = coordinates;
        this.rgbPixel = rgbPixel;
    }

    public Pair<Integer> getCoordinates() {
        return coordinates;
    }

    public int getXCoord() {
        return coordinates.getValue1();
    }

    public int getYCoord() {
        return  coordinates.getValue2();
    }

    public void setXCoord(int newXCoord) {
        coordinates.setValue1(Math.max(0, newXCoord));
    }

    public void setYCoord(int newYCoord) {
        coordinates.setValue2(Math.max(0, newYCoord));
    }

    public void addRed(int redDiff) {
        setRed(rgbPixel.getValue1() + redDiff);
    }

    public void addGreen(int greenDiff) {
        setGreen(rgbPixel.getValue2() + greenDiff);
    }

    public void addBlue(int blueDiff) {
        setBlue(rgbPixel.getValue3() + blueDiff);
    }

    public void setRed(int newRedVal) {
        newRedVal = Math.min(newRedVal, MAX_RGB_VALUE);
        newRedVal = Math.max(newRedVal, MIN_RGB_VALUE);
        rgbPixel.setValue1(newRedVal);
    }

    public void setGreen(int newGreenVal) {
        newGreenVal = Math.min(newGreenVal, MAX_RGB_VALUE);
        newGreenVal = Math.max(newGreenVal, MIN_RGB_VALUE);
        rgbPixel.setValue2(newGreenVal);
    }

    public void setBlue(int newBlueVal) {
        newBlueVal = Math.max(newBlueVal, MAX_RGB_VALUE);
        newBlueVal = Math.min(newBlueVal, MIN_RGB_VALUE);
        rgbPixel.setValue3(newBlueVal);
    }

    public int getRed() {
        return rgbPixel.getValue1();
    }

    public int getGreen() {
        return rgbPixel.getValue2();
    }

    public int getBlue() {
        return rgbPixel.getValue3();
    }

    public void applyThresholdPixel(Pixel thresholdPixel) {
        if ((thresholdPixel.getRed() > getRed()) && (thresholdPixel.getGreen() > getGreen()) && (thresholdPixel.getBlue() > getBlue())) {
            setRed(MIN_RGB_VALUE);
            setGreen(MIN_RGB_VALUE);
            setBlue(MIN_RGB_VALUE);
        } else {
            setRed(MAX_RGB_VALUE);
            setGreen(MAX_RGB_VALUE);
            setBlue(MAX_RGB_VALUE);
        }
    }
}
