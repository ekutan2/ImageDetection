import datastructures.Triplet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

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

        String filePath = "C:\\Users\\burak\\IdeaProjects\\ImageDetection\\images\\besiktas.jpg";           // Put your filepath here
        File file = new File(filePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Getting pixel color by position x=100 and y=40
        Triplet<Integer>[][] rgbPixels = new Triplet[image.getHeight()][];

        for (int i = 0; i < image.getHeight(); i++) {

            rgbPixels[i] = new Triplet[image.getWidth()];
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = new Color(image.getRGB(j, i));

                rgbPixels[i][j] = new Triplet<>(color.getRed(), color.getGreen(), color.getBlue());
                System.out.println("RGB (r,g.b) value of pixel at coordinates (" + i + "," + j + ") is (" + color.getRed() + "," +
                                                                                                          + color.getGreen() + "," +
                                                                                                          + color.getBlue()
                                                                                                          + "). Adding pixel to data structures.");
            }
        }
    }

}
