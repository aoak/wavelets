package aoak.projects.hobby.dsp.transforms.wavelet;

import java.util.Arrays;

public enum Wavelet {

    DB3 (new Double[] {0.2352, 0.5706, 0.3252, -0.0955, -0.0604, 0.0249});

    private final Double[] coeffs;
    private Wavelet(Double[] coeffs) {
        this.coeffs = coeffs;
    }

    public Double[] getWavelet() {
        return Arrays.copyOf(coeffs, coeffs.length);
    }
}
