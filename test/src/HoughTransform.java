import com.sun.java.swing.plaf.windows.WindowsTreeUI;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import javax.imageio.*;

public class HoughTransform
{
    public static CircleArrayData houghTransform(ArrayData inputData, int thetaAxisSize, int rAxisSize, int minContrast) {
        int width = inputData.width;
        int height = inputData.height;
        //int maxRadius = (int)Math.ceil(Math.hypot(width, height));
        int maxRadius = Math.min(width, height);
        int halfRAxisSize = rAxisSize >>> 1;
        CircleArrayData outputCircleData = new CircleArrayData(width, height, maxRadius);


        //ArrayData outputData = new ArrayData(thetaAxisSize, rAxisSize);
        // x output ranges from 0 to pi, partition into theaAxisSize increments
        // y output ranges from -maxRadius to maxRadius
        double[] sinTable = new double[thetaAxisSize];
        double[] cosTable = new double[thetaAxisSize];
        for (int theta = thetaAxisSize - 1; theta >= 0; theta--) {
            double thetaRadians = theta * Math.PI / thetaAxisSize;
            sinTable[theta] = Math.sin(thetaRadians);
            cosTable[theta] = Math.cos(thetaRadians);
        }

        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                if (inputData.contrast(x, y, minContrast)) {

                    // If (x,y) has an adjacent pixel exceeding the contrast, calculate hough curve
                    for (int theta = thetaAxisSize - 1; theta >= 0; theta--) {
                        //double radius = cosTable[theta] * x + sinTable[theta] * y;
                        //int rScaled = (int)Math.round(radius * halfRAxisSize / maxRadius) + halfRAxisSize;

                        // Do hough circles
                        for (int r = 0; r < maxRadius; r++) {

                            double a = x - r*cosTable[theta];
                            double b = y - r*sinTable[theta];
                            int aScaled = (int) a;
                            int bScaled = (int) b;
                            if (aScaled >= width || aScaled < 0 || bScaled >= height || bScaled < 0) {
                                continue;
                            }
                            /*String msg = "Accumulating to (" + aScaled + "," + bScaled + "," + r + ")";
                            /*String remainingMsg = "On (x,y,theta,r) (" + x + "," + y + "," + theta + "," + r  + ") of max ("
                                    + width + "," + height + "," + thetaAxisSize  + "," + maxRadius  + "): ";
                            System.out.println(msg);*/
                            System.out.println("Rounded " + a + " to " + aScaled + " and " + b + " to " + bScaled);
                            //TODO determine how much rounding is going on. This could be a big deal
                            outputCircleData.accumulate(aScaled, bScaled, r, 1);
                        }
                        // Accumulate by adding 1 to this accumulator bin
                        //outputData.accumulate(theta, rScaled, 1);
                    }
                }
            }
            System.out.println(y);
        }
        return outputCircleData;
    }

    public static class CircleArrayData {
        public final int[][][] dataArray;
        public final int width;
        public final int height;
        public final int depth;

        public CircleArrayData(int width, int height, int depth) {
            this(new int[width][height][depth], width, height, depth);
        }

        public CircleArrayData(int[][][] dataArray, int width, int height, int depth) {
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

        public int getMax() {
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

            System.out.println("Found max of " + max + " at (" + maxX + "," + maxY + "," + maxZ + ")");
            return max;
        }
    }

    public static class ArrayData {
        public final int[] dataArray;
        public final int width;
        public final int height;

        public ArrayData(int width, int height) {
            this(new int[width * height], width, height);
        }

        public ArrayData(int[] dataArray, int width, int height) {
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

            // TODO--really stupid way of checking contrast in all adjacent cells. Change this to readable form
            for (int i = 8; i >= 0; i--) {
                if (i == 4)
                    continue;
                int newx = x + (i % 3) - 1;
                int newy = y + (i / 3) - 1;
                if ((newx < 0) || (newx >= width) || (newy < 0) || (newy >= height))
                    continue;
                int contrast = Math.abs(get(newx, newy) - centerValue);
                if (contrast >= minContrast)
                    return true;
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

    public static ArrayData getArrayDataFromImage(String filename) throws IOException {
        BufferedImage inputImage = ImageIO.read(new File(filename));
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] rgbData = inputImage.getRGB(0, 0, width, height, null, 0, width);
        ArrayData arrayData = new ArrayData(width, height);
        // Flip y axis when reading image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgbValue = rgbData[y * width + x];
                rgbValue = (int)(((rgbValue & 0xFF0000) >>> 16) * 0.30 + ((rgbValue & 0xFF00) >>> 8) * 0.59 + (rgbValue & 0xFF) * 0.11);
                arrayData.set(x, height - 1 - y, rgbValue);
            }
        }
        return arrayData;
    }

    public static void writeOutputImage(String filename, ArrayData arrayData) throws IOException {
        int max = arrayData.getMax();
        BufferedImage outputImage = new BufferedImage(arrayData.width, arrayData.height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < arrayData.height; y++) {
            for (int x = 0; x < arrayData.width; x++) {
                int n = Math.min((int)Math.round(arrayData.get(x, y) * 255.0 / max), 255);
                outputImage.setRGB(x, arrayData.height - 1 - y, (n << 16) | (n << 8) | 0x90 | -0x01000000);
            }
        }
        ImageIO.write(outputImage, "PNG", new File(filename));
        return;
    }

    public static void main(String[] args) throws IOException {

        // HoughTransform pentagon.png JavaHoughTransform.png 640 480 100
        String fileName;// = args[0];
        String outputFileName;// = args[1];
        int thetaAxisSize; //= Integer.parseInt(args[2]);
        int rAxisSize; // = Integer.parseInt(args[3]);
        int inputMinContrast; // = Integer.parseInt(args[4]);

        fileName = "images/TestImage.jpg";
        outputFileName = "images/output-pentagon.png";
        thetaAxisSize = 640;
        rAxisSize = 480;
        inputMinContrast = 100;

        ArrayData inputData = getArrayDataFromImage(fileName);
        int minContrast = (args.length >= 4) ? 64 : inputMinContrast;
        CircleArrayData outputData = houghTransform(inputData, thetaAxisSize, rAxisSize, minContrast);
        outputData.getMax();
        //writeOutputImage(outputFileName, outputData);
        return;
    }
}