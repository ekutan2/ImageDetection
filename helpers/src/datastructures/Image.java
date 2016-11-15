package datastructures;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by burak on 11/8/16.
 */
public class Image {
    public static final RgbPixel INVALID_PIXEL = null;
    private Map<Integer, Map<Integer, RgbPixel>> pixels;
    private int imageHeight;
    private int imageWidth;

    public Image(Map<Integer, Map<Integer, RgbPixel>> pixels) {
        this.pixels = pixels;

        int maxXCoord = 0;
        int maxYCoord = 0;
        for (Integer xCoord : pixels.keySet()) {
            for (Integer yCoord : pixels.get(xCoord).keySet()) {
                RgbPixel pixel = pixels.get(xCoord).get(yCoord);
                maxXCoord = Math.max(maxXCoord, pixel.getXCoord());
                maxYCoord = Math.max(maxYCoord, pixel.getYCoord());
            }
        }
        imageWidth = maxXCoord;
        imageHeight = maxYCoord;
    }

    public Image(Image other) {
        this(new HashMap<>(other.getPixels()));
    }

    public Map<Integer, Map<Integer, RgbPixel>> getPixels() {
        return pixels;
    }

    // Redesign to be faster
    public RgbPixel getPixelAt(int xCoord, int yCoord) {
        if (pixels.containsKey(xCoord)) {
            Map<Integer, RgbPixel> yCoordToPixels = pixels.get(xCoord);
            if (yCoordToPixels.containsKey(yCoord)) {
                return yCoordToPixels.get(yCoord);
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

    public Image toGreyScale() {
        Image greyScaleImage = new Image(this);
        for (Integer xCoord : greyScaleImage.getPixels().keySet()) {
            for (Integer yCoord : greyScaleImage.getPixels().get(xCoord).keySet()) {
                greyScaleImage.getPixelAt(xCoord, yCoord).toGreyScale();
            }
        }

        return greyScaleImage;
    }


}
