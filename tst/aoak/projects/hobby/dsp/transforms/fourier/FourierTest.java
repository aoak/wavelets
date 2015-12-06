package aoak.projects.hobby.dsp.transforms.fourier;

import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.complex.Complex;
import org.junit.Test;

import aoak.projects.hobby.dsp.transforms.fourier.Transforms;

public class FourierTest {

    @Test(expected=IllegalArgumentException.class)
    public void fftInvalidInputTest() {
        Complex[] input = new Complex[5];
        Transforms.fft(input);
    }

    @Test(expected=IllegalArgumentException.class)
    public void fftZeroLengthInputTest() {
        Complex[] input = new Complex[0];
        Transforms.fft(input);
    }

    @Test
    public void fftTest() {
        Complex[] input = new Complex[8];
        input[0] = Complex.ZERO;
        input[1] = new Complex(0.7071);
        input[2] = new Complex(1);
        input[3] = new Complex(0.7071);
        input[4] = Complex.ZERO;
        input[5] = new Complex(-0.7071);
        input[6] = new Complex(-1);
        input[7] = new Complex(-0.7071);
        Complex[] result = Transforms.fft(input);
        input[0] = Complex.ZERO;
        input[1] = new Complex(0, -4);
        input[2] = Complex.ZERO;
        input[3] = Complex.ZERO;
        input[4] = Complex.ZERO;
        input[5] = Complex.ZERO;
        input[6] = Complex.ZERO;
        input[7] = new Complex(0, 4);
        for (int i = 0; i < result.length; i++) {
            assertTrue(Complex.equals(input[i], result[i], 1E-3));
        }
    }

    @Test
    public void inverseFftTest() {
        Complex[] input = new Complex[8];
        input[0] = Complex.ZERO;
        input[1] = new Complex(0.7071);
        input[2] = new Complex(1);
        input[3] = new Complex(0.7071);
        input[4] = Complex.ZERO;
        input[5] = new Complex(-0.7071);
        input[6] = new Complex(-1);
        input[7] = new Complex(-0.7071);
        Complex[] signal = Transforms.inverseFft(Transforms.fft(input));
        for (int i = 0; i < signal.length; i++) {
            assertTrue(Complex.equals(input[i], signal[i], 1E-3));
        }
    }
}
