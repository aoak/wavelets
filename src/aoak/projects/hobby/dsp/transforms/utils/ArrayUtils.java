package aoak.projects.hobby.dsp.transforms.utils;

import java.util.Arrays;
import java.util.function.Function;

public class ArrayUtils {

    public static <T> T[] mapInPlace(T[] array, Function<T, T> function) {
        for (int i = 0; i < array.length; i++) {
            array[i] = function.apply(array[i]);
        }
        return array;
    }

    public static <T> T[] map(T[] array, Function<T, T> function) {
        T[] result = Arrays.copyOf(array, array.length);
        for (int i = 0; i < array.length; i++) {
            result[i] = function.apply(array[i]);
        }
        return result;
    }
}
