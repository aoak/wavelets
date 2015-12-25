package aoak.projects.hobby.dsp.transforms.fourier;

import aoak.projects.hobby.dsp.utils.ArrayUtils;
import java.util.Arrays;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author aniket
 *
 */
public class FourierTransform {

    /**
     * Inverse FFT using Cooley-Tukey algorithm.
     * @param input
     * @return
     */
    public static Complex[] inverseFft(Complex[] input) {
        Complex[] inverse = fft(input);
        for (int i = 0, N = inverse.length; i < N; i++) {
            inverse[i] = inverse[i].divide(-N);
        }
        return inverse;
    }

    /**
     * Implementation of simple Cooley-Tukey FFT algorithm. Not optimized for anything.
     * @param input Complex array of input signal. Must be power of 2
     * @return Complex array of fourier transform coeff.
     */
    public static Complex[] fft(Complex[] input) {
      //TODO: Make this whole thing more memory efficient
        int N = input.length;

        // check if N is power of two. Throw up if it is not for now
        if (N == 0 || ((N & -N) != N)) {
            throw new IllegalArgumentException("Length of the input array needs to be a non-zero power of 2. Received " + N);
        }

        // Check the base case
        if (N == 1) {
            return input;
        }

        // Now split the input into odd and even arrays
        Complex[] evenSamples = new Complex[N/2];
        Complex[] oddSamples = new Complex[N/2];
        for (int i = 0; i < N/2; i++) {
            evenSamples[i] = input[i*2];
            oddSamples[i] = input[i*2 + 1];
        }

        // Compute fft of odd and even samples
        Complex[] e = fft(evenSamples);
        Complex[] o = fft(oddSamples);

        /* Combine the odd and even ffts by formula:
         * X[k] = Xe[k] + Wk_N * Xo[k] and
         * X[k+N/2] = Xe[k] - Wk_N * Xo[k]
         */
        Complex[] result = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double w = (-2 * k * Math.PI) / N;
            Complex Wk_N = new Complex(Math.cos(w), Math.sin(w));
            result[k] = e[k].add(o[k].multiply(Wk_N));
            result[k + N/2] = e[k].subtract((o[k].multiply(Wk_N)));
        }
        return result;
    }

    /**
     * Compute short time fourier transform of input signal split
     * into non overlapping windows of length windowLen
     * @param signal
     * @param windowLen
     * @return
     */
    public static Complex[][] stft(Complex[] signal, int windowLen) {
        int numWindows = (int) Math.ceil(signal.length / (double) windowLen);
        Complex[][] transform = new Complex[numWindows][];

        Complex[] hammingWindow = new Complex[windowLen];
        for (int i = 0; i < hammingWindow.length; i++) {
            hammingWindow[i] = new Complex(0.54 - (0.45 * Math.cos(2 * Math.PI * i / (windowLen - 1))));
        }
        for (int i = 0, j = 0; i < signal.length; i += windowLen, j++) {
            Complex[] sigWindow = ArrayUtils.merge(hammingWindow,
                                                   Arrays.copyOfRange(signal, i, i + windowLen),
                                                   (sig, win) -> sig.multiply(win));
            transform[j] = fft(sigWindow);
        }
        return transform;
    }

    /**
     * Compute inverse short time fourier transform from the metrics
     * and join it to reconstruct original signal
     * @param transform
     * @return
     */
    public static Complex[] inverseStft(Complex[][] transform) {
        Complex[] signal = new Complex[transform.length * transform[0].length];
        int ind = 0;
        for (int i = 0; i < transform.length; i++) {
            Complex[] partialInverse = inverseFft(transform[i]);
            System.arraycopy(partialInverse, 0, signal, ind, partialInverse.length);
            ind += partialInverse.length;
        }
        return signal;
    }
}
