package aoak.projects.hobby.dsp.transforms.wavelet;

import org.apache.commons.math3.complex.Complex;
import org.junit.Assert;
import org.junit.Test;

import aoak.projects.hobby.dsp.transforms.utils.ArrayUtils;
import aoak.projects.hobby.dsp.transforms.utils.SignalProcessingUtils;

public class WaveletTransformTest {

    @Test
    public void convolveAndSubsampleTest() {

        Complex[] s = new Complex[3];
        s[0] = new Complex(1);
        s[1] = new Complex(2);
        s[2] = new Complex(3);
        Double[] h = new Double[] {5.0, 6.0,7.0, 8.0};
        Complex[] e = new Complex[3];
        e[0] = new Complex(5);
        e[1] = new Complex(34);
        e[2] = new Complex(37);

        Assert.assertArrayEquals(e, WaveletTransform.convolveAndSubsample(s, h));
    }

    @Test
    public void dwtTest() {
        Complex[] input = new Complex[8];
        input[0] = Complex.ZERO;
        input[1] = new Complex(0.7071);
        input[2] = new Complex(1);
        input[3] = new Complex(0.7071);
        input[4] = Complex.ZERO;
        input[5] = new Complex(-0.7071);
        input[6] = new Complex(-1);
        input[7] = new Complex(-0.7071);

        double norm = SignalProcessingUtils.getNorm(WaveletTransform.DB3);
        Double[] filterR = ArrayUtils.map(WaveletTransform.DB3, ele -> ele/norm);
        Double[] filter = SignalProcessingUtils.getReverse(filterR);

        Complex[][] trans = WaveletTransform.dwt(input, filter);
        WaveletTransform.iDwt(trans[0], trans[1], filterR);
    }
}
