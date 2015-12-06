package aoak.projects.hobby.dsp.transforms.wavelet;

import static aoak.projects.hobby.dsp.transforms.utils.SignalProcessingUtils.*;

import org.apache.commons.math3.complex.Complex;

import aoak.projects.hobby.dsp.transforms.utils.ArrayUtils;

public class WaveletTransform {

    public static final Double[] DB3 = new Double[] {0.2352, 0.5706, 0.3252, -0.0955, -0.0604, 0.0249};

    /**
     * Calculate discrete wavelet transform by convolution and sub-sampling
     * with the input filter as low pass filter and it's QMF pair.
     * TODO: Implement cascading
     * @param signal
     * @param lpFilter
     * @return
     */
    public static Complex[][] dwt(Complex[] signal, Double[] lpFilter) {
        int N = signal.length;

        // check if N is power of two. Throw up if it is not for now
        if (N == 0 || ((N & -N) != N)) {
            throw new IllegalArgumentException("Length of the input array needs to be a non-zero power of 2. Received " + N);
        }

        int numberOfDecompositions = 2; //(int) (Math.log(N)/Math.log(2));
        Complex[][] result = new Complex[numberOfDecompositions][];

        // now get the QMF pair filter
        Double[] hpFilter = getQMF(lpFilter);

        result[0] = convolveAndSubsample(signal, lpFilter);
        result[1] = convolveAndSubsample(signal, hpFilter);
        return result;
    }

    /**
     * NOT READY TO USE AT ALL
     * @param approx
     * @param details
     * @param lpFilter
     * @return
     */
    public static Complex[] iDwt(Complex[] approx, Complex[] details, Double[] lpFilter) {

        approx = upsample(approx, 2);
        details = upsample(details, 2);
        Double[] hpFilter = getQMF(lpFilter);
        return approx;
    }

    static Complex[] convolveAndSubsample(Complex[] signal, Double[] filter) {
        int N = signal.length;
        int K = filter.length;

        if (N == 1) {
            return signal;
        }
        if ((N+K-1) % 2 != 0) {
            filter = ArrayUtils.pad(filter, 1, 0);
            K = filter.length;
        }

        Complex[] result = new Complex[(N+K-1)/2];
        ArrayUtils.mapInPlace(result, ele -> Complex.ZERO);

        /* filter[k], signal[n-k], result[n] result length is k+n-1
         * sum over all k
         */
        for (int n = 0; n < result.length; n++) {
            for (int k = Math.max(0, 2*n-N+1); k <= Math.min(2*n, K-1); k++) {
                result[n] = result[n].add(signal[2*n-k].multiply(filter[k]));
            }
        }
        return result;
    }
}
