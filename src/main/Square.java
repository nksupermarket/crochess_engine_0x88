package main;

import java.util.*;

public enum Square {
    A1(0), B1(1), C1(2), D1(3), E1(4), F1(5), G1(6), H1(7),
    A2(16), B2(17), C2(18), D2(19), E2(20), F2(21), G2(22), H2(23),
    A3(32), B3(33), C3(34), D3(35), E3(36), F3(37), G3(38), H3(39),
    A4(48), B4(49), C4(50), D4(51), E4(52), F4(53), G4(54), H4(55),
    A5(64), B5(65), C5(66), D5(67), E5(68), F5(69), G5(70), H5(71),
    A6(80), B6(81), C6(82), D6(83), E6(84), F6(85), G6(86), H6(87),
    A7(96), B7(97), C7(98), D7(99), E7(100), F7(101), G7(102), H7(103),
    A8(112), B8(113), C8(114), D8(115), E8(116), F8(117), G8(118), H8(119), NULL(-1);

    public final int idx;

    Square(int idx) {
        this.idx = idx;
    }

    public static final Map<Integer, Square> lookup = new HashMap<>();

    static {
        for (Square square : Square.values()) {
            lookup.put(square.idx,
                    square);
        }
    }

    public static boolean isValid(int idx) {
        return (idx & 0x88) == 0;
    }

    private static int getRank(Square square) {
        return square.idx >> 4;
    }

    private static int getFile(Square square) {
        return square.idx & 7;
    }

    public static Color getColor(Square square) {
        return (getFile(square) + getRank(square)) % 2 == 0 ? Color.B : Color.W;
    }

    public static boolean isPromotion(Square square,
                                      Color color) {
        int rank = getRank(square);
        return rank == 7 && color == Color.W || rank == 0 && color == Color.B;
    }
}
