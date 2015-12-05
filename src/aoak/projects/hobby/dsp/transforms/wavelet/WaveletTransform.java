package aoak.projects.hobby.dsp.transforms.wavelet;

import org.apache.commons.math3.complex.Complex;

import aoak.projects.hobby.dsp.transforms.utils.SignalProcessingUtils;

public class WaveletTransform {

    public static final Double[] DB3 = new Double[] {0.2352, 0.5706, 0.3252, -0.0955, -0.0604, 0.0249};

    public static Complex[] decomposeAndSubsample(Complex[] signal, Double[] lowPassFilter) {
        int N = signal.length;

        // check if N is power of two. Throw up if it is not for now
        if (N == 0 || ((N & -N) != N)) {
            throw new IllegalArgumentException("Length of the input array needs to be a non-zero power of 2. Received " + N);
        }

        if (N == 1) {
            return signal;
        }

        Double[] highPassFilter = SignalProcessingUtils.getQMF(lowPassFilter);
        Complex[] y_high = new Complex[N/2];
        Complex[] y_low = new Complex[N/2];

        for (int k = 0; k < N/2; k++) {
            for (int n = 0; n < N; n++) {
                y_high[k] = y_high[k].add(signal[n].multiply(highPassFilter[-n + 2*k]));
                y_low[k] = y_low[k].add(signal[n].multiply(lowPassFilter[-n + 2*k]));
            }
        }
        return null;
    }
}
