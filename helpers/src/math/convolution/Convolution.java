package math.convolution;

import datastructures.Image;
import datastructures.Pair;
import datastructures.RgbPixel;

import java.util.Map;

/**
 * Created by ekutan on 11/11/16.
 */
public class Convolution {

    private Map<Pair<Integer>, Integer> kernel;
    private Image image;

    public Convolution(Map<Pair<Integer>, Integer> kernel, Image image) {
        this.kernel = kernel;
        this.image = image;
    }

    public Image convolute () {

        long startTimeNanos = System.nanoTime();
        Image convolutedImage = new Image(image);           // deep copy of image
        long copyCtrTime = System.nanoTime() - startTimeNanos;
        startTimeNanos = System.nanoTime();

        int counter = 0;
        for (Integer xCoord : image.getPixels().keySet()) {
            for (Integer yCoord : image.getPixels().get(xCoord).keySet()) {

                RgbPixel pixel = image.getPixelAt(xCoord, yCoord);
                boolean isEdgePixel = pixel.getXCoord() == convolutedImage.getImageWidth() || pixel.getXCoord() == 0 ||
                        pixel.getYCoord() == convolutedImage.getImageHeight() || pixel.getYCoord() == 0;
                if (!isEdgePixel) {
                    applyKernelToImage(convolutedImage, pixel);
                }
                System.out.println("Convoluted pixel " + counter);
                counter++;
            }
        }
        long loopTime = System.nanoTime() - startTimeNanos;
        System.out.println("Ctr creation time " + copyCtrTime + " nanos; loop time " + loopTime + " nanos");

        return convolutedImage;
    }

    private void applyKernelToImage(Image image, RgbPixel pixel) {
        Pair<Integer> coordinates = pixel.getCoordinates();
        int xCoord = coordinates.getValue1();
        int yCoord = coordinates.getValue2();

        int sumRedContent = 0;
        int sumGreenContent = 0;
        int sumBlueContent = 0;

        for (Pair<Integer> relativeCoords : kernel.keySet()) {
            int newXCoord = xCoord + relativeCoords.getValue1();
            int newYCoord = yCoord + relativeCoords.getValue2();
            RgbPixel newPixel = image.getPixelAt(newXCoord, newYCoord);
            if (newPixel == null) {
                continue;
            }

            sumRedContent += newPixel.getRed()*kernel.get(relativeCoords);
            sumGreenContent += newPixel.getGreen()*kernel.get(relativeCoords);
            sumBlueContent += newPixel.getBlue()*kernel.get(relativeCoords);
        }

        pixel.setRed(sumRedContent);
        pixel.setGreen(sumGreenContent);
        pixel.setBlue(sumBlueContent);
    }
}
