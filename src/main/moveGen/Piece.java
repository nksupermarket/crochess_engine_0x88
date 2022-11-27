package main.moveGen;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Piece {
    NULL, PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;
    final public int id;

    Piece() {
        this.id = ordinal();
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

