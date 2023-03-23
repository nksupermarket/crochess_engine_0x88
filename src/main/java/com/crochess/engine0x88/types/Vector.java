package com.crochess.engine0x88.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Vector {
  UP(16),
  UP_RIGHT(17),
  UP_LEFT(15),
  LEFT(-1),
  RIGHT(1),
  DOWN(-16),
  DOWN_RIGHT(-15),
  DOWN_LEFT(-17);

  public final int offset;

  Vector(int offset) {
    this.offset = offset;
  }

  public static final List<Vector> LIST = List.copyOf(Arrays.asList(Vector.values()));
  // public static final Vector[] LIST = Vector.values();
  public static final List<Vector> XY_VECTORS =
      List.copyOf(Arrays.asList(Vector.UP, Vector.DOWN, Vector.LEFT, Vector.RIGHT));
  // public static final Vector[] XY_VECTORS = {Vector.UP, Vector.DOWN, Vector.LEFT, Vector.RIGHT};
  public static final List<Vector> DIAGONAL_VECTORS =
      List.copyOf(
          Arrays.asList(Vector.UP_RIGHT, Vector.UP_LEFT, Vector.DOWN_LEFT, Vector.DOWN_RIGHT));
  // public static final Vector[] DIAGONAL_VECTORS = {
  //   Vector.UP_RIGHT, Vector.UP_LEFT, Vector.DOWN_LEFT, Vector.DOWN_RIGHT
  // };
  public static final int[] KNIGHT_JUMPS = {33, 31, 18, 14, -31, -33, -18, -14};
  public static final List<List<Vector>> PAWN_CAPTURES =
      List.of(
          List.copyOf(Arrays.asList(Vector.UP_RIGHT, Vector.UP_LEFT)),
          List.copyOf(Arrays.asList(Vector.DOWN_RIGHT, Vector.DOWN_LEFT)));

  // public static final Vector[][] PAWN_CAPTURES = {
  //   {Vector.UP_RIGHT, Vector.UP_LEFT}, {Vector.DOWN_RIGHT, Vector.DOWN_LEFT}
  // };
  private static final Map<Integer, Vector> lookup = new HashMap<>();

  static {
    Arrays.stream(Vector.values()).forEach(s -> lookup.put(s.offset, s));
  }

  public static Vector of(Integer value) {
    return lookup.get(value);
  }
}
