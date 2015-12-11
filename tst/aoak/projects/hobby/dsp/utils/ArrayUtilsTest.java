package aoak.projects.hobby.dsp.utils;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import aoak.projects.hobby.dsp.utils.ArrayUtils;

public class ArrayUtilsTest {

    @Test
    public void sqaureTest() {
        Integer[] in = new Integer[] {1, 2, 3, 4};
        Integer[] exp = new Integer[] {1, 4, 9, 16};
        Assert.assertArrayEquals(exp, ArrayUtils.mapInPlace(in, ele -> ele * ele));
    }

    @Test
    public void sqaureCopyTest() {
        Integer[] in = new Integer[] {1, 2, 3, 4};
        Integer[] inCopy = new Integer[] {1, 2, 3, 4};
        Integer[] exp = new Integer[] {1, 4, 9, 16};
        Assert.assertArrayEquals(exp, ArrayUtils.map(in, ele -> ele * ele));
        Assert.assertArrayEquals(inCopy, in);
    }

    @Test
    public void mergeTest() {
        Double[] in1 = new Double[] {1.0, 2.0, 3.0};
        Double[] in2 = new Double[] {2.0, 3.0, 4.0};
        System.out.println(Arrays.asList(ArrayUtils.merge(in1, in2, (e1, e2) -> Double.sum(e1, e2))));
    }
}
