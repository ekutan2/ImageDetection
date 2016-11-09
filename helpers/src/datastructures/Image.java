package datastructures;

import datastructures.Pixel;

import java.util.List;

/**
 * Created by ekutan on 11/8/16.
 */
public class Image {
    private List<Pixel> pixels;

    public Image(List<Pixel> pixels) {
        this.pixels = pixels;
    }

    public List<Pixel> getPixels() {
        return pixels;
    }
}
