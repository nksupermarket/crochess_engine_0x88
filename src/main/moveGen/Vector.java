package main.moveGen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Vector {
    UP(16), UP_RIGHT(17), UP_LEFT(15), LEFT(-1), RIGHT(1), DOWN(-16), DOWN_RIGHT(-15), DOWN_LEFT(-17);

    public final int offset;

    Vector(int offset) {
        this.offset = offset;
    }

    private static final Map<Integer, Vector> lookup = new HashMap<>();

    static {
        Arrays.stream(Vector.values())
              .forEach(s -> lookup.put(s.offset,
                      s));
    }

    public static Vector of(Integer value) {
        return lookup.get(value);
    }
}
