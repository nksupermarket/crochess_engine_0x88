package main.moveGen;

import java.util.EnumSet;

public enum Piece {
    NULL, PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;
    final public int id;

    Piece() {
        this.id = ordinal();
    }

    public static Piece extractPieceType(int boardVal) {
        return Piece.values()[boardVal & 7];
    }

    public static EnumSet<Piece> getPromoteTypes() {
        return EnumSet.of(KNIGHT,
                BISHOP,
                ROOK,
                QUEEN);
    }
}

