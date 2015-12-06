package aoak.projects.hobby.dsp.transforms.utils;

import java.util.Arrays;
import java.util.function.Function;

public class ArrayUtils {

    /**
     * Update each element of the input array by the result of application of the
     * input function on the element. Modifies the input array.
     * @param array
     * @param function
     * @return
     */
    public static <T> T[] mapInPlace(T[] array, Function<T, T> function) {
        for (int i = 0; i < array.length; i++) {
            array[i] = function.apply(array[i]);
        }
        return array;
    }

    /**
     * Return a new array whose each element is result of application of the input
     * function on the corresponding element of input array.
     * @param array
     * @param function
     * @return
     */
    public static <T> T[] map(T[] array, Function<T, T> function) {
        T[] result = Arrays.copyOf(array, array.length);
        for (int i = 0; i < array.length; i++) {
            result[i] = function.apply(array[i]);
        }
        return result;
    }

    public static Double[] pad(Double[] array, int paddingLength, double pad) {
        Double[] result = Arrays.copyOf(array, array.length + paddingLength);
        for (int i = array.length; i < result.length; i++) {
            result[i] = pad;
        }
        return result;
    }
}
