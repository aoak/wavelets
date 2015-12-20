package aoak.projects.hobby.dsp.utils;

import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import org.apache.commons.math3.complex.Complex;

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
        for (int n = 0; n < N + K -1; n++) {
            for (int k = Math.max(0, n-N+1); k <= Math.min(n, K-1); k++) {
                result[n] = result[n].add(signal[n-k].multiply(filter[k]));
            }
        }
        return result;
    }

    public static Complex[][] conv2d(Complex[][] signal, Double[][] filter) {
        int Nr = signal.length;
        int Nc = signal[0].length;
        int Kr = filter.length;
        int Kc = filter[0].length;

        Complex[][] result = new Complex[Nr+Kr-1][Nc+Kc-1];
        for (int i = 0; i < result.length; i++) {
            ArrayUtils.mapInPlace(result[i], ele -> Complex.ZERO);
        }

        for (int nr = 0; nr < Nr + Kr - 1; nr++) {
            for (int nc = 0; nc < Nc + Kc - 1; nc++) {
                for (int kr = Math.max(0, nr-Nr+1); kr <= Math.min(nr, Kr-1); kr++) {
                    for (int kc = Math.max(0, nc-Nc+1); kc <= Math.min(nc, Kc-1); kc++) {
                        result[nr][nc] = result[nr][nc].add(signal[nr-kr][nc-kc].multiply(filter[kr][kc]));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Compute cross correlation between two signals
     * @param signal1
     * @param signal2
     * @return
     */
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

    /**
     * Compute autocorrelation of the signal
     * @param signal
     * @return
     */
    public static Complex[] autocorr(Complex[] signal) {
        return xcorr(signal, signal);
    }

    /**
     * Calculate cross correlation between two signals when the second signal is shifted
     * by shift places to the right. The shift can be negative in which case the second
     * signal is shifted to the left
     * @param signal1
     * @param signal2
     * @param shift
     * @return
     */
    public static Complex xcorr(Complex[] signal1, Complex[] signal2, int shift) {
        if (shift < 0) {
            return xcorr(signal2, signal1, -shift);
        }
        Complex result = Complex.ZERO;
        if (shift >= signal1.length) {
            return result;
        }

        for (int i = shift; i < signal1.length && i - shift < signal2.length; i++) {
            result = result.add(signal1[i].multiply(signal2[i-shift]));
        }
        return result;
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

    /**
     * Upsample the signal to be of length targetLength. This form is useful
     * when the signal is to be upsampled to certain length.
     * This method should never loose a sample from the input signal which
     * can be the case with plain upsample + truncate
     * @param signal
     * @param targetLength
     * @return resultant array of length targetLength
     */
    public static Complex[] upsampleToLength(Complex[] signal, int targetLength) {
        return upsampleToLength(signal, targetLength, (ele1, ele2) -> Complex.ZERO);
    }

    private static Complex[] upsampleToLength(Complex[] signal, int targetLength, BinaryOperator<Complex> function) {
        if (targetLength < signal.length) {
            throw new IllegalArgumentException("target length cannot be less than original length");
        }

        int samplesToAdd = targetLength - signal.length;
        Complex[] result = new Complex[targetLength];

        // find out how many samples we need to add after each signal sample
        double sampleRatio = samplesToAdd / (double) signal.length;
        int signalSamplesPerIteration = 1;
        int additionalSamplesPerIteration = 0;
        if (sampleRatio >= 1) {
            additionalSamplesPerIteration = (int) Math.floor(sampleRatio);
        }

        /* because of the use of floor and possible sample ration of less then 1,
         * we may end up appending some samples at the end.
         * instead of that, we want to spread them out uniformly. calculate number of
         * samples we need to spread out.
         */
        int numExtras = (int) ((sampleRatio % 1.0) * (signal.length));

        int resultI = 0, sigI = 0;
        while (resultI < result.length && sigI < signal.length) {
            // add signal samples
            for (int j = 0; j < signalSamplesPerIteration; j++, resultI++, sigI++) {
                result[resultI] = signal[sigI];
            }
            // add padding samples
            for (int j = 0; j < additionalSamplesPerIteration; j++, resultI++) {
                result[resultI] = function.apply(getIfPresentOrZero(signal, sigI-1),
                                                 getIfPresentOrZero(signal, sigI));
            }
            /* If samples to spread is non zero, then add a sample here. Only one
             * should be enough because total number of samples to spread should
             * never be greater than the signal length
             */
            if (numExtras-- > 0) {
                result[resultI++] = function.apply(getIfPresentOrZero(signal, sigI-1),
                                                 getIfPresentOrZero(signal, sigI));
            }
        }
        return result;
    }

    /**
     * function to check if index is within range. return requested element
     * if it is, return 0 otherwise
     */
    private static Complex getIfPresentOrZero(Complex[] array, int index) {
        if (index < 0 || index >= array.length) {
            return Complex.ZERO;
        }
        return array[index];
    }

    /**
     * Upsample the given signal adding the samples that are average of the two signal
     * samples they are inserted between
     * @param signal
     * @param targetLength
     * @return resultant array of length targetLength
     */
    public static Complex[] upsampleWithInterpolation(Complex[] signal, int targetLength) {
        /* This function inserts samples that are average of two signal samples.
         * This means 1, 2 will be 1, 1.5, 2. If two samples are added, it will be
         * 1, 1.5, 1.5, 2. This is better than 1, 0, 0, 1 but can be better.
         * In future, it can be improved to be 1, 1.33, 1.66, 2
         */
        return upsampleToLength(signal, targetLength, (ele1, ele2) -> ele1.add(ele2).divide(2));
    }

    /**
     * Pads the signal at the start with it's mirror image
     * eg. 1, 2, 3 with padLength 2 will be 2, 1, 1, 2, 3
     * @param signal
     * @param padLength
     * @return new Array of length inputLength + padLength
     */
    public static Complex[] symmetricPrePadding(Complex[] signal, int padLength) {
        if (padLength > signal.length) {
            throw new IllegalArgumentException("Cannot add symmetric padding longer than signal");
        }
        Complex[] result = new Complex[signal.length + padLength];
        for (int i = 0; i < padLength; i++) {
            result[padLength - 1 - i] = signal[i];
        }
        System.arraycopy(signal, 0, result, padLength, signal.length);
        return result;
    }

    /**
     * Pads the signal at the end with it's mirror image
     * eg. 1, 2, 3 with padLength 2 will be 1, 2, 3, 3, 2
     * @param signal
     * @param padLength
     * @return new Array of length inputLength + padLength
     */
    public static Complex[] symmetricPostPadding(Complex[] signal, int padLength) {
        if (padLength > signal.length) {
            throw new IllegalArgumentException("Cannot add symmetric padding longer than signal");
        }
        Complex[] result = new Complex[signal.length + padLength];
        System.arraycopy(signal, 0, result, 0, signal.length);
        for (int i = 0; i < padLength; i++) {
            result[signal.length + i] = signal[signal.length - 1 - i];
        }
        return result;
    }

    /**
     * Pads the signal at both the ends with it's mirror image
     * eg. 1, 2, 3 with padLength 3 will be 2, 1, 1, 2, 3, 3
     * @param signal
     * @param padLength
     * @return new Array of length inputLength + padLength
     */
    public static Complex[] symmetricPadding(Complex[] signal, int padLength) {
        if (padLength > signal.length * 2) {
            throw new IllegalArgumentException("Cannot add symmetric padding longer than signal");
        }
        int prePadLen = (int) Math.ceil(padLength / 2.0);
        int postPadLen = (int) Math.floor(padLength / 2.0);
        Complex[] paddedSignal = symmetricPrePadding(signal, prePadLen);
        return symmetricPostPadding(paddedSignal, postPadLen);
    }

    /**
     * Wrapper around arrayCopy that discards part of the signal at both ends
     * eg 1 2 3 4 5 with truncateLength 3 will be 3, 4
     * @param signal
     * @param truncateLength
     * @return
     */
    public static Complex[] symmetricTruncate(Complex[] signal, int truncateLength) {
        if ((truncateLength / 2) > signal.length) {
            throw new IllegalArgumentException("Cannot add symmetric padding longer than signal");
        }
        Complex[] truncatedSignal = new Complex[signal.length - truncateLength];
        int preTrunLen = (int) Math.ceil(truncateLength / 2.0);
        System.arraycopy(signal, preTrunLen, truncatedSignal, 0, signal.length - truncateLength);
        return truncatedSignal;
    }
}
