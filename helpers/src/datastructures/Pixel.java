package datastructures;

/**
 * Created by ekutan on 11/8/16.
 */
public class Pixel {
    private Pair<Integer> coordinates;
    private Triplet<Integer> rgbPixel;

    public Pixel(Pair<Integer> coordinates, Triplet<Integer> rgbPixel) {
        this.coordinates = coordinates;
        this.rgbPixel = rgbPixel;
    }

    public Pair<Integer> getCoordinates() {
        return coordinates;
    }

    public Triplet<Integer> getRgbPixel() {
        return rgbPixel;
    }
}
