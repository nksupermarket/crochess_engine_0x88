package com.crochess.engine0x88;

import com.crochess.engine0x88.types.Color;
import com.crochess.engine0x88.types.Piece;
import com.crochess.engine0x88.types.Square;
import com.crochess.moveValidator.Game;

import java.util.Random;

final public class ZobristKey {
    private ZobristKey() {
    }

    public static long[][][] PIECES = new long[2][6][120];
    /*
    0 index for no castle rights
    1 index for queenside only
    2 for kingside only
    3 for both sides
     */
    public static long[][] CASTLING_RIGHTS = new long[2][4];
    public static long[] EN_PASSANT = new long[120];
    public static long SIDE;

    static {
        Random rdm = new Random();

        SIDE = rdm.nextLong();
        for (Color color : Color.values()) {
            for (Piece pieceType : Piece.values()) {
                if (pieceType == Piece.NULL) continue;
                int pieceIdx = pieceType.id - 1;
                for (int i = 0; i < 120; i++) {
                    PIECES[color.ordinal()][pieceIdx][i] = rdm.nextLong();
                    EN_PASSANT[i] = rdm.nextLong();
                }
            }

            for (int i = 0; i < CASTLING_RIGHTS[0].length; i++) {
                CASTLING_RIGHTS[color.ordinal()][i] = rdm.nextLong();
            }
        }
    }

    public static long hash() {
        long hash = 0;
        // get zobrish hash
        for (int i = 0; i < 120; i++) {
            if (!Square.isValid(i)) continue;

            int piece = GameState.board[i];
            if (piece != 0) {
                hash ^= PIECES[Color.extractColor(piece)
                                    .ordinal()][Piece.extractPieceType(piece).id - 1][i];
            }
        }

        hash ^= CASTLING_RIGHTS[Color.W.ordinal()][GameState.castleRights >> 2];
        hash ^= CASTLING_RIGHTS[Color.B.ordinal()][GameState.castleRights & 3];

        if (GameState.enPassant != Square.NULL)
            hash ^= EN_PASSANT[GameState.enPassant.idx];

        if (GameState.activeColor == Color.B) hash ^= SIDE;

        return hash;
    }

    public static long hash(Game game) {
        long hash = 0;
        // get zobrish hash
        for (int i = 0; i < 120; i++) {
            if (!Square.isValid(i)) continue;

            int piece = game.board[i];
            if (piece != 0) {
                hash ^= PIECES[Color.extractColor(piece)
                                    .ordinal()][Piece.extractPieceType(piece).id - 1][i];
            }
        }

        hash ^= CASTLING_RIGHTS[Color.W.ordinal()][game.castleRights >> 2];
        hash ^= CASTLING_RIGHTS[Color.B.ordinal()][game.castleRights & 3];

        if (game.enPassant != Square.NULL)
            hash ^= EN_PASSANT[game.enPassant.idx];

        if (game.activeColor == Color.B) hash ^= SIDE;

        return hash;
    }
}
