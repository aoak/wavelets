package aoak.projects.hobby.dsp.image;

import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Test;
import static aoak.projects.hobby.dsp.image.ImageProcessor.*;

public class ImageProcessorTest {

    @Test
    public void histTest() {

        double[][] i = {{1, 2, 4}, {3, 4, 2}, {3, 5, 2}};
        SortedMap<Double, Integer> exp = new TreeMap<>();
        exp.put(0.0, 0);
        exp.put(1.0, 1);
        exp.put(2.0, 3);
        exp.put(3.0, 2);
        exp.put(4.0, 2);
        exp.put(5.0, 1);
        exp.put(6.0, 0);
        exp.put(7.0, 0);

        Assert.assertEquals(exp, histogram(i, 0, 7, 1));
    }
}
