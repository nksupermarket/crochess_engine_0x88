package main.moveGen;

import java.util.HashMap;
import java.util.Map;

public enum Piece {
    NULL, PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;
    /*
    p 1
    n 10
    b 11
    r 100
    q 101
    k 110

     */
    final public int id;

    Piece() {
        this.id = ordinal();
    }

    public static Piece extractPieceType(int boardVal) {
        return Piece.values()[boardVal & 7];
    }
}

