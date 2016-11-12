package math.edgedetection;

import datastructures.Image;
import datastructures.Pair;
import math.convolution.Convolution;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by burak on 11/8/2016.
 */
public class SobelEdgeDetector extends EdgeDetector {

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
        horizontalSobelConvolution = new Convolution(horizontalMask, image);
        verticalSobelConvolution = new Convolution(verticalMask, image);
    }

    //TODO apply sobel gradient
}
