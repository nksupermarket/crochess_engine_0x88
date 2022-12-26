package com.crochess.engine0x88.types;

import java.util.HashMap;
import java.util.Map;

public enum Castle {
    W_K(8,
            Square.G1,
            Square.F1, Square.H1), W_Q(4,
            Square.C1,
            Square.D1, Square.A1), B_k(2,
            Square.G8,
            Square.F8, Square.H8), B_q(1,
            Square.C8,
            Square.D8, Square.A8);


    final public int value;
    final public Square square;
    final public Square rSquare;
    final public Square rInitSquare;

    Castle(int value,
           Square square,
           Square rSquare,
           Square rInitSquare) {
        this.value = value;
        this.square = square;
        this.rSquare = rSquare;
        this.rInitSquare = rInitSquare;
    }

    final public static Map<Integer, Castle> lookup = new HashMap<>();

    static {
        for (Castle castle : Castle.values()) {
            lookup.put(castle.value, castle);
        }
    }
}