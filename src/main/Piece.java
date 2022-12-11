package main;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Piece {
    NULL(0), PAWN(100), KNIGHT(300), BISHOP(310), ROOK(500), QUEEN(900), KING(10000);
    final public int id;
    final public int value;


    Piece(int value) {
        this.id = ordinal();
        this.value = value;
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

