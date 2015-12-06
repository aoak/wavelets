package aoak.projects.hobby.dsp.transforms.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.junit.Assert;
import org.junit.Test;

public class SignalProcessingUtilsTest {

    @Test
    public void normTest() {
        // db3 scaling filter as a vector
        Double[] vec = new Double[] {0.2352, 0.5706, 0.3252, -0.0955, -0.0604, 0.0249};
        assertEquals(0.7071, SignalProcessingUtils.getNorm(vec), 1E-2);
    }

    @Test
    public void qmfTest() {
        Double[] vec = new Double[] {0.2352, 0.5706, 0.3252, -0.0955, -0.0604, 0.0249};
        Double[] qmf = SignalProcessingUtils.getQMF(vec);
        System.out.println(Arrays.asList(qmf));
    }

    @Test
    public void complexNormTest() {
        // db3 scaling filter as a vector
        Complex[] input = new Complex[6];
        input[0] = new Complex(0.2352);
        input[1] = new Complex(0.5706);
        input[2] = new Complex(0.3252);
        input[3] = new Complex(-0.0955);
        input[4] = new Complex(-0.0604);
        input[5] = new Complex(0.0249);
        assertEquals(0.7071, SignalProcessingUtils.getNorm(input), 1E-2);
    }

    @Test
    public void convolveTest() {

        Complex[] s = new Complex[3];
        s[0] = new Complex(1);
        s[1] = new Complex(2);
        s[2] = new Complex(3);
        Double[] h = new Double[] {5.0, 6.0,7.0, 8.0};
        Complex[] e = new Complex[6];
        e[0] = new Complex(5);
        e[1] = new Complex(16);
        e[2] = new Complex(34);
        e[3] = new Complex(40);
        e[4] = new Complex(37);
        e[5] = new Complex(24);

        Assert.assertArrayEquals(e, SignalProcessingUtils.convolve(s, h));
    }
}
