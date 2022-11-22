package main.utils;

import main.moveGen.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final public class Utils {
    private Utils() {
    }

    public static <T> int findIndexOf(T v,
                                      T[] collection) {
        for (int i = 0; i < collection.length; i++) {
            if (collection[i] == v) return i;
        }
        return -1;
    }

    public static <T> boolean findValue(T v, T[] collection) {
        for (T t : collection) {
            if (t.equals(v)) return true;
        }
        return false;
    }
}
