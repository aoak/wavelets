package aoak.projects.hobby.dsp.transforms.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import aoak.projects.hobby.dsp.transforms.utils.VectorHelper;;

public class VectorHelperTest {

    @Test
    public void normTest() {
        // db3 scaling filter as a vector
        List<Double> vec = Arrays.asList(0.2352, 0.5706, 0.3252, -0.0955, -0.0604, 0.0249);
        assertEquals(0.7071, VectorHelper.getNorm(vec), 1E-2);
    }
}
