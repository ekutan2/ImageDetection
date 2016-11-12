package math.edgedetection;

import datastructures.Image;
import datastructures.Pair;
import datastructures.Pixel;
import math.convolution.Convolution;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by burak on 11/8/2016.
 */
public class SobelEdgeDetector extends EdgeDetector {

    private Image image;
    private Convolution horizontalSobelConvolution;
    private Convolution verticalSobelConvolution;

    private Map<Pair<Integer>, Integer> horizontalMask = new HashMap<Pair<Integer>, Integer>() {{
                                                            put(new Pair<>(-1, -1), -1);
                                                            put(new Pair<>(0, -1), -2);
                                                            put(new Pair<>(1, -1), -1);
                                                            put(new Pair<>(-1, 1), 1);
                                                            put(new Pair<>(0, 1), 2);
                                                            put(new Pair<>(1, 1), 1);
                                                        }};

    private Map<Pair<Integer>, Integer> verticalMask = new HashMap<Pair<Integer>, Integer>() {{
                                                            put(new Pair<>(-1, -1), -1);
                                                            put(new Pair<>(-1, 0), -2);
                                                            put(new Pair<>(-1, 1), -1);
                                                            put(new Pair<>(1, -1), 1);
                                                            put(new Pair<>(1, 0), 2);
                                                            put(new Pair<>(1, 1), 1);
                                                        }};

    public SobelEdgeDetector(Image image) {
        this.image = image;
        horizontalSobelConvolution = new Convolution(horizontalMask, this.image);
        verticalSobelConvolution = new Convolution(verticalMask, this.image);
    }

    //TODO apply sobel gradient
    public Image applySobelDetector() {
        Image modifiedImage = new Image(image);
        Image horizontalSobelImage = horizontalSobelConvolution.convolute();
        Image verticalSobelImage = verticalSobelConvolution.convolute();

        for (Pixel pixel : image.getPixels()) {
            int xCoord = pixel.getXCoord();
            int yCoord = pixel.getYCoord();

            Pixel horizontalSobelImagePixel = horizontalSobelImage.getPixelAt(xCoord, yCoord);
            Pixel verticalSobelImagePixel = verticalSobelImage.getPixelAt(xCoord, yCoord);

            int redMagnitudeAtPixel = (int) Math.sqrt(Math.pow(horizontalSobelImagePixel.getRed(), 2) + Math.pow(verticalSobelImagePixel.getRed(), 2));
            int greenMagnitudeAtPixel = (int) Math.sqrt(Math.pow(horizontalSobelImagePixel.getGreen(), 2) + Math.pow(verticalSobelImagePixel.getGreen(), 2));
            int blueMagnitudeAtPixel = (int) Math.sqrt(Math.pow(horizontalSobelImagePixel.getBlue(), 2) + Math.pow(verticalSobelImagePixel.getBlue(), 2));

            modifiedImage.getPixelAt(xCoord, yCoord).setRed(redMagnitudeAtPixel);
            modifiedImage.getPixelAt(xCoord, yCoord).setGreen(greenMagnitudeAtPixel);
            modifiedImage.getPixelAt(xCoord, yCoord).setBlue(blueMagnitudeAtPixel);
        }

        return modifiedImage;
    }

}
