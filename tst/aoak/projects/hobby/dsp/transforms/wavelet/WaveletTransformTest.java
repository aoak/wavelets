package aoak.projects.hobby.dsp.transforms.wavelet;

import org.apache.commons.math3.complex.Complex;
import org.junit.Assert;
import org.junit.Test;

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
}
