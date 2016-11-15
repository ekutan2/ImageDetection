import datastructures.*;
import datastructures.Image;
import datastructures.RgbPixel;
import math.edgedetection.SobelEdgeDetector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * Created by burak on 11/3/16.
 */
public class HelloWorld {

    public int count = 5;

    public static void main(String[] args) {


        // javac HelloWorld.java compiles the java code into the class HelloWorld.class (class files are bytecode)
        // java HelloWorld runs the bytecode in HelloWorld.class

        /*int commandLineArgOne = 0;
        if (args.length > 0) {
            commandLineArgOne = Integer.parseInt(args[0]);
        }

        int commandLineArgTwo = 0;
        if (args.length > 1) {
            commandLineArgTwo = Integer.parseInt(args[1]);
        }

        // HelloWorld is the class
        // testObject is an instance of HelloWorld
        HelloWorld testObject = new HelloWorld();

        // you can have multiple instances of a class
        HelloWorld testObjectTwo = new HelloWorld();


        // Adds commandLineArgOne to commandLineArgTwo and THEN concatenates to String "Hello World..."s
        System.out.println("Hello World. Count is " + (commandLineArgOne + commandLineArgTwo));

        // Concatenates commandLineArgOne to String "Hello World..." and then concatenates commandLineArgTwo
        System.out.println("Hello World. Count is " + commandLineArgOne + commandLineArgTwo);
        */

        String filePath = "/home/ekutan/IdeaProjects/ImageDetection/images/besiktas.jpg";           // Put your filepath here
        File file = new File(filePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Getting pixel color by position x=100 and y=40
        Triplet<Integer>[][] rgbPixels = new Triplet[image.getHeight()][];
        Map<Integer, Map<Integer, RgbPixel>> pixels = new HashMap<>();

        for (int x = 0; x < image.getWidth(); x++) {

            Map<Integer, RgbPixel> yCoordToPixels = new HashMap<>();
            rgbPixels[x] = new Triplet[image.getWidth()];
            for (int y = 0; y < image.getHeight(); y++) {

                Color color = new Color(image.getRGB(x, y));
                //rgbPixels[x][y] = new Triplet<>(color.getRed(), color.getGreen(), color.getBlue());
                /*System.out.println("RGB (r,g.b) value of pixel at coordinates (" + i + "," + j + ") is (" + color.getRed() + "," +
                                                                                                          + color.getGreen() + "," +
                                                                                                          + color.getBlue()
                                                                                                             + "). Adding pixel to data structures.");*/
                Pair<Integer> coordinates = new Pair<>(x, y);
                Triplet<Integer> rgbContent = new Triplet<>(color.getRed(), color.getGreen(), color.getBlue());
                yCoordToPixels.put(y, new RgbPixel(coordinates, rgbContent));
            }
            pixels.put(x, yCoordToPixels);
        }

        datastructures.Image image1 = new Image(pixels);
        image1.toGreyScale();

        SobelEdgeDetector sobelEdgeDetector = new SobelEdgeDetector(image1);
        Image sobelImage = sobelEdgeDetector.applySobelDetector();

        // Convert back to a BufferedImage
        BufferedImage modifiedImage = new BufferedImage(sobelImage.getImageWidth(), sobelImage.getImageHeight(), BufferedImage.TYPE_INT_RGB);
        System.out.println("Image has dimensions " + sobelImage.getImageHeight() + "," + sobelImage.getImageWidth());
        for (int x = 0; x < sobelImage.getImageHeight(); x++) {
            for (int y = 0; y < sobelImage.getImageWidth(); y++) {
                RgbPixel pixel = sobelImage.getPixelAt(x, y);
                Color rgbColor = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
                modifiedImage.setRGB(x, y, rgbColor.getRGB());
                System.out.println("RGB (r,g.b) value of pixel at coordinates (" + x + "," + y + ") is ("
                                                                    + pixel.getRed() + "," +
                                                                    + pixel.getGreen() + "," +
                                                                    + pixel.getBlue()  + "). Adding pixel to data structures.");
            }
        }
        File modifiedFile = new File(file.getName()+".modified");
        try {
            modifiedFile.createNewFile();
            ImageIO.write(modifiedImage, "jpg", modifiedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
