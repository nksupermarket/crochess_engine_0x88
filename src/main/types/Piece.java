package main.types;

import main.utils.Score;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Piece {
    NULL(0), PAWN(S(126, 208)), KNIGHT(S(781, 854)), BISHOP(S(825, 915)), ROOK(S(1276, 1380)), QUEEN(S(2538, 2682)),
    KING(10000);
    final public int id;
    final public int value;


    Piece(int value) {
        this.id = ordinal();
        this.value = value;
    }

    private static int S(int mg, int eg) {
        return Score.make(mg, eg);
    }

    public static final Map<Integer, Piece> lookup = new HashMap<>();

    static {
        for (Piece pieceType : Piece.values()) {
            lookup.put(pieceType.id,
                    pieceType);
        }
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

