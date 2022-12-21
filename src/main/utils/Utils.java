package main.utils;

import main.types.Square;

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

    public static void printMove(int move) {
        System.out.printf("%s%s ", Square.lookup.get((move >> 7) & 127),
                Square.lookup.get(move & 127));
    }

    public static String convertMove(int move) {
        return Square.lookup.get((move >> 7) & 127)
                            .toString() +
                Square.lookup.get(move & 127)
                             .toString();
    }

    public static <T> boolean findValue(T v, T[] collection) {
        for (T t : collection) {
            if (t.equals(v)) return true;
        }
        return false;
    }
}
