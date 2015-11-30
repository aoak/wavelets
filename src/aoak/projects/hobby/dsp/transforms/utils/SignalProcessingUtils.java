package aoak.projects.hobby.dsp.transforms.utils;

import java.util.Arrays;
import java.util.List;
import com.google.common.collect.Iterables;

public class SignalProcessingUtils {

    /**
     * Given an array of doubles, calculate it's norm (L2-norm or magnitude)
     * @param vector
     * @return
     */
    public static double getNorm(Double[] vectorArray) {
        if (vectorArray == null || vectorArray.length == 0) {
            throw new IllegalArgumentException("Input array has to be non null, non empty");
        }
        List<Double> vector = Arrays.asList(vectorArray);
        if (vector.size() == 1) {
            return Iterables.getOnlyElement(vector);
        }
        return vector.
                parallelStream().
                map(ele -> ele * ele).
                reduce((ele1, ele2) -> ele1 + ele2).
                map(o -> Math.sqrt(o)).
                get();
    }

    /**
     * Takes an array of doubles representing filter coefficients. Returns another array of
     * same length representing a filter that forms quadrature mirror filter (QMF) pair
     * with input filter.
     * @param firstFilter
     * @return
     */
    public static Double[] getQMF(Double[] filter) {
        if (filter == null || filter.length == 0) {
            throw new IllegalArgumentException("Input array has to be non null, non empty");
        }
        if (filter.length == 1) {
            return new Double[] {filter[0]};
        }
        Double[] qmfFilter = new Double[filter.length];

        for (int i = 0, N = filter.length; i < N; i++) {
            qmfFilter[N -1 - i] = (i % 2 == 0 ? 1 : -1) * filter[i];
        }
        return qmfFilter;
    }
}
