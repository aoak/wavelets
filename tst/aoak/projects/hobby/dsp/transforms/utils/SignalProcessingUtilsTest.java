package aoak.projects.hobby.dsp.transforms.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
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
}
