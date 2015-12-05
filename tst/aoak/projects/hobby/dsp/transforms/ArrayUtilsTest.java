package aoak.projects.hobby.dsp.transforms;

import org.junit.Assert;
import org.junit.Test;

import aoak.projects.hobby.dsp.transforms.utils.ArrayUtils;

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
}
