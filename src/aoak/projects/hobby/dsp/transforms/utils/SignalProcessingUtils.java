package aoak.projects.hobby.dsp.transforms.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

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
            qmfFilter[i] = (i % 2 == 0 ? 1 : -1) * filter[N-i-1];
        }
        return qmfFilter;
    }

    /**
     * Returns the reverse of the given vector. Does not alter the input array.
     * @param filter
     * @return
     */
    public static <T> T[] getReverse(T[] filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Input array has to be non null, non empty");
        }
        T[] reverse = Arrays.copyOf(filter, filter.length);
        for (int i = 0, N = filter.length; i < N; i++) {
            reverse[N-1-i] = filter[i];
        }
        return reverse;
    }

    /**
     * Returns L2 norm of complex vector i.e. sqrt(sum(abs(vector) * abs(vector)))
     * @param vectorArray
     * @return
     */
    public static double getNorm(Complex[] vectorArray) {
        if (vectorArray == null || vectorArray.length == 0) {
            throw new IllegalArgumentException("Input array has to be non null, non empty");
        }
        List<Complex> vector = Arrays.asList(vectorArray);
        if (vector.size() == 1) {
            return Iterables.getOnlyElement(vector).abs();
        }
        return vector.
                parallelStream().
                map(ele -> ele.abs() * ele.abs()).
                reduce((ele1, ele2) -> ele1 + ele2).
                map(o -> Math.sqrt(o)).
                get();
    }

    /**
     * Convolution of two signals of length N and K.
     * @param signal
     * @param filter
     * @return Resultant array of length N+K-1.
     */
    public static Complex[] conv(Complex[] signal, Double[] filter) {
        int N = signal.length;
        int K = filter.length;

        if (N == 1) {
            return signal;
        }

        Complex[] result = new Complex[N+K-1];
        ArrayUtils.mapInPlace(result, ele -> Complex.ZERO);

        /* filter[k], signal[n-k], result[n] result length is k+n-1
         * sum over all k
         */
        for (int n = 0; n < N + K -1; n++) { // 0-6
            for (int k = Math.max(0, n-N+1); k <= Math.min(n, K-1); k++) {
                result[n] = result[n].add(signal[n-k].multiply(filter[k]));
            }
        }
        return result;
    }

    public static Complex[] xcorr(Complex[] signal1, Complex[] signal2) {
        /* Cross correlation is time flipped convolution
         */
        signal2 = getReverse(signal2);
        Double[] secondSig = new Double[signal2.length];
        for (int i = 0; i < secondSig.length; i++) {
            secondSig[i] = signal2[i].getReal();
        }
        return conv(signal1, secondSig);
    }

    public static Complex[] autocorr(Complex[] signal) {
        return xcorr(signal, signal);
    }

    /**
     * Adds a zero at every padInterval samples. For example, setting padInteval
     * to 2 means every other sample is 0
     * @param signal
     * @param padInterval
     * @return
     */
    public static Complex[] upsample(Complex[] signal, int padInterval) {
        if (padInterval < 2) {
            throw new IllegalArgumentException("Padding interval needs to be at least 2 (every other sample)");
        }

        int samplesToInsert = signal.length / (padInterval - 1);
        Complex[] result = new Complex[signal.length + samplesToInsert];
        for (int i = 0, j = 0; i < signal.length + 1 && j < result.length; j++) {
            if ((j+1) % padInterval == 0) {
                result[j] = Complex.ZERO;
            } else {
                result[j] = signal[i++];
            }
        }
        return result;
    }
}
