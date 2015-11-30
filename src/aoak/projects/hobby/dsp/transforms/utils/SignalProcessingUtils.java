package aoak.projects.hobby.dsp.transforms.utils;

import java.util.List;
import com.google.common.collect.Iterables;

public class SignalProcessingUtils {

    /**
     * Given a list of doubles, calculate it's norm (L2-norm or magnitude)
     * @param vector
     * @return
     */
    public static double getNorm(List<Double> vector) {
        if (vector == null || vector.isEmpty()) {
            throw new IllegalArgumentException("Input list has to be non null, non empty");
        }
        if (vector.size() == 1) {
            return Iterables.getOnlyElement(vector);
        }
        return vector.
                parallelStream().
                map(ele -> ele * ele).
                reduce((ele1, ele2) -> ele1 + ele2).
                map(o -> Math.sqrt(o)).
                get();
    }
}
