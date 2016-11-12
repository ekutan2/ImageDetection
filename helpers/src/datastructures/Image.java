package datastructures;

import datastructures.Pixel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by burak on 11/8/16.
 */
public class Image {
    public static final Pixel INVALID_PIXEL = null;
    private List<Pixel> pixels;
    private int imageHeight;
    private int imageWidth;

    public Image(List<Pixel> pixels) {
        this.pixels = pixels;

        int maxXCoord = 0;
        int maxYCoord = 0;
        for (Pixel pixel : pixels) {
            maxXCoord = Math.max(maxXCoord, pixel.getXCoord());
            maxYCoord = Math.max(maxYCoord, pixel.getYCoord());
        }
    }

    public Image(Image other) {
        this(new LinkedList<Pixel>(other.getPixels()));
    }

    public List<Pixel> getPixels() {
        return pixels;
    }

    // Redesign to be faster
    public Pixel getPixelAt(int xCoord, int yCoord) {
        for (Pixel pixel : pixels) {
            if (pixel.getYCoord() == yCoord && pixel.getXCoord() == xCoord) {
                return pixel;
            }
        }
        return INVALID_PIXEL;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

}
