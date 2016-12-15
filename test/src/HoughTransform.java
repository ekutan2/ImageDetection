import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.*;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class HoughTransform
{
    public static HoughData houghTransform(CartesianData inputData, int thetaIncrements, int minContrast) {
        int width = inputData.width;
        int height = inputData.height;
        int maxRadius = Math.min(width, height);
        HoughData outputCircleData = new HoughData(width, height, maxRadius);

        // x output ranges from 0 to pi, partition into theaAxisSize increments
        // y output ranges from -maxRadius to maxRadius
        double[] sinTable = new double[thetaIncrements];
        double[] cosTable = new double[thetaIncrements];
        for (int theta = thetaIncrements - 1; theta >= 0; theta--) {
            double thetaRadians = theta*2*Math.PI / thetaIncrements;
            sinTable[theta] = Math.sin(thetaRadians);
            cosTable[theta] = Math.cos(thetaRadians);
        }

        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                if (inputData.contrast(x, y, minContrast)) {
                    // If (x,y) has an adjacent pixel exceeding the contrast, calculate hough curve
                    for (int theta = thetaIncrements - 1; theta >= 0; theta--) {
                        for (int r = maxRadius - 1; r >= 10; r--) {
                            double a = x - r * cosTable[theta];     // X coordinate of potential center
                            double b = y - r * sinTable[theta];     // Y coordinate of potential center
                            int aScaled = (int) a;                  // Discretize
                            int bScaled = (int) b;
                            if (aScaled >= width || aScaled < 0 || bScaled >= height || bScaled < 0) {
                                continue;
                            }
                            outputCircleData.accumulate(aScaled, bScaled, r, 1);    // Vote
                        }
                    }
                }
            }
            System.out.println(y);
        }
        return outputCircleData;
    }

    /**
     * Represents data in the hough-transform parameter space
     */
    public static class HoughData {
        public final int[][][] dataArray;
        public final int width;
        public final int height;
        public final int depth;

        private final int UNSET_HOUGH_PARAM = -100;
        private int maxA = UNSET_HOUGH_PARAM;
        private int maxB = UNSET_HOUGH_PARAM;
        private int maxR = UNSET_HOUGH_PARAM;

        public HoughData(int width, int height, int depth) {
            this(new int[width][height][depth], width, height, depth);
        }

        public HoughData(int[][][] dataArray, int width, int height, int depth) {
            this.dataArray = dataArray;
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        public int get(int x, int y, int z) {
            return dataArray[x][y][z];
        }

        public void set(int x, int y, int z, int value) {
            dataArray[x][y][z] = value;
        }

        public void accumulate(int x, int y, int z, int delta) {
            set(x, y, z, get(x, y, z) + delta);
        }

        public int calcMax() {
            int max = dataArray[0][0][0];
            int maxX = 0;
            int maxY = 0;
            int maxZ = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    for (int z = 0; z < depth; z++) {
                        if (get(x, y, z) > max) {
                            max = get(x, y, z);
                            maxX = x;
                            maxY = y;
                            maxZ = z;
                        }
                    }
                }
            }

            this.maxA = maxX;
            this.maxB = maxY;
            this.maxR = maxZ;

            System.out.println("Found max of " + max + " at (" + maxX + "," + maxY + "," + maxZ + ")");
            return max;
        }

        public int getMaxA() {
            return maxA;
        }

        public int getMaxB() {
            return maxB;
        }

        public int getMaxR() {
            return maxR;
        }
    }

    public static class CartesianData {
        public final int[] dataArray;
        public final int width;
        public final int height;

        public CartesianData(int width, int height) {
            this(new int[width * height], width, height);
        }

        public CartesianData(int[] dataArray, int width, int height) {
            this.dataArray = dataArray;
            this.width = width;
            this.height = height;
        }

        public int get(int x, int y) {
            return dataArray[y * width + x];
        }

        public void set(int x, int y, int value) {
            dataArray[y * width + x] = value;
        }

        public void accumulate(int x, int y, int delta) {
            set(x, y, get(x, y) + delta);
        }

        /**
         * Determines whether or not any adjacent pixel of (x, y) is outside of minContrast value of (x,y)
         *
         * @param x
         * @param y
         * @param minContrast
         * @return
         */
        public boolean contrast(int x, int y, int minContrast) {
            int centerValue = get(x, y);
            Set<Integer> mask = new HashSet<>();
            mask.add(-1);
            mask.add(0);
            mask.add(1);

            for (int xOffset : mask) {
                for (int yOffset : mask) {
                    int newX = x + xOffset;
                    int newY = y + yOffset;
                    if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
                        continue;
                    }

                    int contrast = Math.abs(get(newX, newY) - centerValue);
                    if (contrast >= minContrast) {
                        return  true;
                    }
                }
            }

            return false;
        }

        public int getMax() {
            int max = dataArray[0];
            for (int i = width * height - 1; i > 0; i--)
                if (dataArray[i] > max)
                    max = dataArray[i];
            return max;
        }
    }

    /**
     *
     */
    public static class SobelEdgeDetector {

        static int[][] pixelMatrix=new int[3][3];
        public static void getSobelImage(BufferedImage inputImg, String outputFileName) throws IOException {

            BufferedImage outputImg = new BufferedImage(inputImg.getWidth(),inputImg.getHeight(),TYPE_INT_RGB);
            for(int i=1;i<inputImg.getWidth()-1;i++){
                for(int j=1;j<inputImg.getHeight()-1;j++){
                    pixelMatrix[0][0]=new Color(inputImg.getRGB(i-1,j-1)).getRed();
                    pixelMatrix[0][1]=new Color(inputImg.getRGB(i-1,j)).getRed();
                    pixelMatrix[0][2]=new Color(inputImg.getRGB(i-1,j+1)).getRed();
                    pixelMatrix[1][0]=new Color(inputImg.getRGB(i,j-1)).getRed();
                    pixelMatrix[1][2]=new Color(inputImg.getRGB(i,j+1)).getRed();
                    pixelMatrix[2][0]=new Color(inputImg.getRGB(i+1,j-1)).getRed();
                    pixelMatrix[2][1]=new Color(inputImg.getRGB(i+1,j)).getRed();
                    pixelMatrix[2][2]=new Color(inputImg.getRGB(i+1,j+1)).getRed();

                    int edge=(int) convolution(pixelMatrix);
                    outputImg.setRGB(i,j,(edge<<16 | edge<<8 | edge));
                }
            }

            File outputfile = new File(outputFileName);
            ImageIO.write(outputImg,"jpg", outputfile);
        }

        private static double convolution(int[][] pixelMatrix) {
            int gy=(pixelMatrix[0][0]*-1)+(pixelMatrix[0][1]*-2)+(pixelMatrix[0][2]*-1)+(pixelMatrix[2][0])+(pixelMatrix[2][1]*2)+(pixelMatrix[2][2]*1);
            int gx=(pixelMatrix[0][0])+(pixelMatrix[0][2]*-1)+(pixelMatrix[1][0]*2)+(pixelMatrix[1][2]*-2)+(pixelMatrix[2][0])+(pixelMatrix[2][2]*-1);
            return Math.sqrt(Math.pow(gy,2)+Math.pow(gx,2));

        }
    }

    public static CartesianData getArrayDataFromImage(BufferedImage inputImage) throws IOException {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] rgbData = inputImage.getRGB(0, 0, width, height, null, 0, width);
        CartesianData arrayData = new CartesianData(width, height);
        // Flip y axis when reading image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgbValue = rgbData[y * width + x];
                rgbValue = (int)(((rgbValue & 0xFF) >> 16) + ((rgbValue & 0xFF) >> 8) + (rgbValue & 0xFF));
                arrayData.set(x, height - 1 - y, rgbValue);
            }
        }
        return arrayData;
    }

    public static void highLightCircles(HoughData circleArrayData) {

    }

    public static void writeOutputImage(String filename, CartesianData arrayData) throws IOException {
        BufferedImage outputImage = new BufferedImage(arrayData.width, arrayData.height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < arrayData.height; y++) {
            for (int x = 0; x < arrayData.width; x++) {
                int n = arrayData.get(x, y);
                outputImage.setRGB(x, arrayData.height - 1 - y, n);
            }
        }
        ImageIO.write(outputImage, "JPG", new File(filename));
        return;
    }


    /**
     *
     */
    public static void writeOutputImage(String fileName, BufferedImage originalImage, HoughData circleArrayData, int thetaIncrements) throws IOException {
        circleArrayData.calcMax();
        int maxA = circleArrayData.getMaxA();
        int maxB = circleArrayData.getMaxB();
        int maxR = circleArrayData.getMaxR();

        Color outlineColor = Color.red;
        int red = outlineColor.getRed();
        int green = outlineColor.getGreen();
        int blue = outlineColor.getBlue();
        int col = (red << 16) | (green << 8) | blue;     // how RGB pixels are represented as one int: http://www.javamex.com/tutorials/graphics/bufferedimage_setrgb.shtml

        ColorModel cm = originalImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = originalImage.copyData(null);
        BufferedImage outputImage =  new BufferedImage(cm, raster, isAlphaPremultiplied, null);

        int width = circleArrayData.width;
        int height = circleArrayData.height;
        int maxRadius = Math.min(width, height);

        double[] sinTable = new double[thetaIncrements];
        double[] cosTable = new double[thetaIncrements];
        for (int theta = thetaIncrements - 1; theta >= 0; theta--) {
            double thetaRadians = theta*2*Math.PI / thetaIncrements;
            sinTable[theta] = Math.sin(thetaRadians);
            cosTable[theta] = Math.cos(thetaRadians);
        }

        // Color circle
        for (int theta = thetaIncrements - 1; theta >= 0; theta--) {
            double x = maxA + maxR * cosTable[theta];                   // X coordinate of potential center
            double y = (height - maxB) + maxR * sinTable[theta];        // Y coordinate of potential center
            int xScaled = (int) x;                                      // Discretize
            int yScaled = (int) y;
            if (xScaled >= width || xScaled < 0 || yScaled >= height || yScaled < 0) {
                continue;
            }
            outputImage.setRGB(xScaled, yScaled,  col);
        }

        ImageIO.write(outputImage, "JPG", new File(fileName));
    }

    public static void makeGray(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); ++x) {
            for (int y = 0; y < img.getHeight(); ++y) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                img.setRGB(x, y, gray);
            }
        }
    }

    public static void applyThreshold (BufferedImage img, int threshold) {
        for (int x = 0; x < img.getWidth(); ++x) {
            for (int y = 0; y < img.getHeight(); ++y) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                if (grayLevel >= threshold) {
                    int white = (255 << 16) + (255 << 8) + 255;
                    img.setRGB(x, y, white);
                } else {
                    int black = 0;
                    img.setRGB(x, y, black);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {

        // HoughTransform pentagon.png JavaHoughTransform.png 640 480 100
        int thetaAxisSize; //= Integer.parseInt(args[2]);
        int inputMinContrast; // = Integer.parseInt(args[4]);

        // Get input and output filename
        String filePath = "images";
        String fileName = "coin-on-desksmall.jpeg";
        String[] fileNameParts = fileName.split("\\.");
        String fileNameWithoutExt = fileNameParts[0];
        String fileExt = fileNameParts[1];
        String sobelFileName = fileNameWithoutExt + ".sobel." + fileExt;
        String outputFileName = fileNameWithoutExt + ".output." + fileExt;
        String grayScaleFileName = fileNameWithoutExt + ".grayscale." + fileExt;
        String blackWhiteFileName = fileNameWithoutExt + ".blackwhite." + fileExt;

        // Determine axes sizes
        thetaAxisSize = 360;
        inputMinContrast = 150;
        int minContrast = (args.length >= 4) ? 64 : inputMinContrast;

        // Read data, compute transform, then write output data
        BufferedImage inputImage = ImageIO.read(new File(filePath + "/" + fileName));


        // Gray scale
        BufferedImage grayScaleImage = ImageIO.read(new File(filePath + "/" + fileName));
        makeGray(grayScaleImage);
        ImageIO.write(grayScaleImage, "JPG", new File(filePath + "/" + grayScaleFileName));

        // Pass image through sobel filter
        SobelEdgeDetector.getSobelImage(grayScaleImage, filePath + "/" + sobelFileName);

        // Black and white (binary)
        BufferedImage blackWhiteImage = ImageIO.read(new File(filePath + "/" + sobelFileName));
        applyThreshold(blackWhiteImage, 150);
        ImageIO.write(blackWhiteImage, "JPG", new File(filePath + "/" + blackWhiteFileName));

        // Apply Hough Transform
        CartesianData inputData = getArrayDataFromImage(grayScaleImage);
        HoughData outputData = houghTransform(inputData, thetaAxisSize, minContrast);
        //writeOutputImage(filePath + "/" + outputFileName, inputData);
        writeOutputImage(filePath + "/" + outputFileName, inputImage, outputData, thetaAxisSize);
    }
}