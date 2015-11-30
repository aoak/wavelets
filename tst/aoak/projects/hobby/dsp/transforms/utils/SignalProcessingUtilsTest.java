package aoak.projects.hobby.dsp.transforms.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

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
}
