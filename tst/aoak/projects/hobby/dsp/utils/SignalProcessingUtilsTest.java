package aoak.projects.hobby.dsp.utils;

import static aoak.projects.hobby.dsp.utils.SignalProcessingUtils.*;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.apache.commons.math3.complex.Complex;
import org.junit.Assert;
import org.junit.Test;

public class SignalProcessingUtilsTest {

    @Test
    public void normTest() {
        // db3 scaling filter as a vector
        Double[] vec = new Double[] {0.2352, 0.5706, 0.3252, -0.0955, -0.0604, 0.0249};
        assertEquals(0.7071, getNorm(vec), 1E-2);
    }

    @Test
    public void qmfTest() {
        Double[] vec = new Double[] {0.2352, 0.5706, 0.3252, -0.0955, -0.0604, 0.0249};
        Double[] ex = new Double[] {0.0249, 0.0604, -0.0955, -0.3252, 0.5706, -0.2352};
        Double[] qmf = getQMF(vec);
        Assert.assertArrayEquals(ex, qmf);
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
        assertEquals(0.7071, getNorm(input), 1E-2);
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

        Assert.assertArrayEquals(e, conv(s, h));
    }

    @Test
    public void xcorrTest() throws IOException {

        Complex[] s1 = new Complex[5];
        s1[0] = new Complex(1);
        s1[1] = new Complex(2);
        s1[2] = new Complex(3);
        s1[3] = new Complex(4);
        s1[4] = new Complex(5);

        Complex[] s2 = new Complex[3];
        s2[0] = new Complex(3);
        s2[1] = new Complex(4);
        s2[2] = new Complex(-2);

        Complex[] ex = new Complex[7];
        ex[0] = new Complex(-2);
        ex[1] = new Complex(0);
        ex[2] = new Complex(5);
        ex[3] = new Complex(10);
        ex[4] = new Complex(15);
        ex[5] = new Complex(32);
        ex[6] = new Complex(15);
        Assert.assertArrayEquals(ex, xcorr(s1, s2));
    }

    @Test
    public void autocorrTest() throws IOException {

        Complex[] s = new Complex[5];
        s[0] = new Complex(1);
        s[1] = new Complex(2);
        s[2] = new Complex(3);
        s[3] = new Complex(4);
        s[4] = new Complex(5);

        Complex[] ex = new Complex[9];
        ex[0] = new Complex(5);
        ex[1] = new Complex(14);
        ex[2] = new Complex(26);
        ex[3] = new Complex(40);
        ex[4] = new Complex(55);
        ex[5] = new Complex(40);
        ex[6] = new Complex(26);
        ex[7] = new Complex(14);
        ex[8] = new Complex(5);
        Assert.assertArrayEquals(ex, autocorr(s));
    }

    @Test
    public void xcorrShiftTest() throws IOException {

        Complex[] s = new Complex[5];
        s[0] = new Complex(1);
        s[1] = new Complex(2);
        s[2] = new Complex(3);
        s[3] = new Complex(4);
        s[4] = new Complex(5);

        assertEquals(new Complex(40), xcorr(s, s, 1));
    }

    @Test
    public void xcorrNegShiftTest() throws IOException {

        Complex[] s1 = new Complex[5];
        s1[0] = new Complex(1);
        s1[1] = new Complex(2);
        s1[2] = new Complex(3);
        s1[3] = new Complex(4);
        s1[4] = new Complex(5);

        Complex[] s2 = new Complex[3];
        s2[0] = new Complex(3);
        s2[1] = new Complex(4);
        s2[2] = new Complex(-2);

        assertEquals(new Complex(-2), xcorr(s1, s2, -2));
    }

    @Test
    public void upsampleToLengthSimpleTest() {
        Complex[] s1 = new Complex[] {Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE};
        Complex[] exp = new Complex[] {Complex.ONE, Complex.ZERO, Complex.ONE, Complex.ZERO, Complex.ONE, Complex.ZERO, Complex.ONE, Complex.ZERO};
        Assert.assertArrayEquals(exp, upsampleToLength(s1, 8));
    }

    @Test
    public void upsampleToLengthNonEvenTest() {
        Complex[] s1 = new Complex[] {Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE};
        Complex[] exp = new Complex[] {Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE, Complex.ZERO, Complex.ONE, Complex.ZERO};
        Assert.assertArrayEquals(exp, upsampleToLength(s1, 10));
    }

    @Test
    public void upsampleToLengthShortTest() {
        Complex[] s1 = new Complex[] {Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE};
        Complex[] exp = new Complex[] {Complex.ONE, Complex.ZERO, Complex.ONE, Complex.ZERO, Complex.ONE, Complex.ONE};
        Assert.assertArrayEquals(exp, upsampleToLength(s1, 6));
    }

    @Test
    public void interpolateToLengthSimpleTest() {
        Complex[] s1 = new Complex[] {Complex.ONE, new Complex(2), Complex.ONE, Complex.ONE};
        Complex[] exp = new Complex[] {Complex.ONE, new Complex(1.5), new Complex(2), new Complex(1.5), Complex.ONE, Complex.ONE, Complex.ONE, new Complex(0.5)};
        Assert.assertArrayEquals(exp, upsampleWithInterpolation(s1, 8));
    }

    @Test
    public void interpolateToLengthNonEvenTest() {
        Complex[] s1 = new Complex[] {Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE};
        Complex[] exp = new Complex[] {Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE, new Complex(0.5)};
        Assert.assertArrayEquals(exp, upsampleWithInterpolation(s1, 10));
    }

    @Test
    public void interpolateToLengthShortTest() {
        Complex[] s1 = new Complex[] {Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE};
        Complex[] exp = new Complex[] {Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE, Complex.ONE};
        Assert.assertArrayEquals(exp, upsampleWithInterpolation(s1, 6));
    }
}
