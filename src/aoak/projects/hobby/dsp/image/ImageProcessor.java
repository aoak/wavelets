package aoak.projects.hobby.dsp.image;

import aoak.projects.hobby.dsp.utils.SignalProcessingUtils;
import java.util.SortedMap;
import java.util.TreeMap;

public class ImageProcessor {

    public static double[][] filter(double[][] image, double[][] filter) {
        return SignalProcessingUtils.conv2d(image, filter);
    }

    public static SortedMap<Double, Integer> histogram(double[][] image, double lowest,
                                                       double highest, double step) {
        if (lowest > highest || (highest - lowest) % step != 0) {
            throw new IllegalArgumentException("Illegal lower, upper bound and step size combination." +
                    " (highest - lowest) / step should be int and lower bound should be grater than upper bound");
        }
        int numElems = (int) ((highest - lowest) / step) + 1; // total number of possible values

        // initialize the histogram to possible values and count 0
        double[][] hist = new double[numElems][2];
        double value = lowest;
        for (int i = 0; i < hist.length; i++, value += step) {
            hist[i][0] = value;
        }

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                value = image[i][j];
                // now need to find the index of this value
                int index = (int) ((value - lowest) / step);
                hist[index][1]++;
            }
        }
        /* not sure if I should box the types and return a map.
         * map is easier to navigate and more intuitive than
         * array of two columns but needs boxing
         */
        SortedMap<Double, Integer> histMap = new TreeMap<>();
        for (int i = 0; i < hist.length; i++) {
            histMap.put(hist[i][0], (int) hist[i][1]);
        }
        return histMap;
    }
}
