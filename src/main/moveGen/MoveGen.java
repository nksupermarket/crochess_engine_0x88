package main.moveGen;

import main.*;
import main.types.Castle;
import main.types.Color;
import main.types.Piece;
import main.types.Square;

import java.util.ArrayList;
import java.util.List;

final public class MoveGen {
    private MoveGen() {
    }

    /*
    ATTACK_NONE = 0;
    ATTACK_KQR = 1;
    ATTACK_QR = 2;
    ATTACK_KQBwP = 3;
    ATTACK_KQBbP = 4;
    ATTACK_QB = 5;
    ATTACK_N = 6;
     */
    final private static int[] ATTACK_TABLE = {
            5, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0,
            0, 0, 0, 2, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 2, 0, 0, 0, 5,
            0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 2, 0, 0, 5, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 5, 6, 2, 6, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 6, 3, 1, 3, 6, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 1, 0,
            1, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 6, 4, 1, 4, 6, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6, 2, 6, 5, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 5, 0, 0, 2, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5,
            0, 0, 0, 2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
            0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 5
    };

    final private static int[] DELTA_ARRAY = {
            17, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 15, 0, 0, 17, 0, 0,
            0, 0, 0, 16, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 17, 0, 0, 0, 0, 16,
            0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 16, 0, 0, 0, 15,
            0, 0, 0, 0, 0, 0, 0, 0, 17, 0, 0, 16, 0, 0, 15, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 17, -33, 16, -31, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, -18, 17, 16, 15, -14, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0,
            -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 14, -15, -16, -17, 18, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, -15, 31, -16, 33, -17, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, -15, 0, 0, -16, 0, 0, -17, 0, 0, 0, 0, 0, 0, 0, 0, -15,
            0, 0, 0, -16, 0, 0, 0, -17, 0, 0, 0, 0, 0, 0, -15, 0, 0, 0, 0, -16,
            0, 0, 0, 0, -17, 0, 0, 0, 0, -15, 0, 0, 0, 0, 0, -16, 0, 0, 0, 0,
            0, -17, 0, 0, -15, 0, 0, 0, 0, 0, 0, -16, 0, 0, 0, 0, 0, 0, -17
    };

    private static void pushSlidingPieceMoves(Square start, Color color, List<Integer> moveList, Vector[] vectors) {
        for (Vector vector : vectors) {
            int squareIdx = start.idx + vector.offset;
            while (Square.isValid(squareIdx)) {
                Square square = Square.lookup.get(squareIdx);
                if ((GameState.board[square.idx] & color.id) == 0) moveList.add((start.idx << 7) | square.idx);
                if (GameState.board[square.idx] != 0) break;
                squareIdx += vector.offset;
            }
        }
    }

    public static List<Integer> pseudoLegal(
            Square start,
            Piece pieceType,
            Color color) {
        /*
        21 bit int to represent move
        first 7 bits refer to the "to" square idx
        next 7 bits refer to the "from" square idx
        next 4 bits refer to castling
        next 3 bits refer to promotion piece type
         */
        List<Integer> moves = new ArrayList<>();

        switch (pieceType) {
            case QUEEN -> {
                pushSlidingPieceMoves(start, color, moves, Vector.values());
                return moves;
            }

            case ROOK -> {
                final Vector[] XY_VECTORS = {Vector.UP, Vector.DOWN, Vector.LEFT, Vector.RIGHT};
                pushSlidingPieceMoves(start, color, moves, XY_VECTORS);
                return moves;
            }

            case BISHOP -> {
                final Vector[] DIAGONAL_VECTORS = {
                        Vector.UP_RIGHT, Vector.UP_LEFT, Vector.DOWN_LEFT,
                        Vector.DOWN_RIGHT
                };
                pushSlidingPieceMoves(start, color, moves, DIAGONAL_VECTORS);
                return moves;
            }

            case KNIGHT -> {
                final int[] KNIGHT_JUMPS = {33, 31, 18, 14, -31, -33, -18, -14};
                for (int jump : KNIGHT_JUMPS) {
                    int jumpIdx = start.idx + jump;
                    if (Square.isValid(jumpIdx) && (GameState.board[jumpIdx] & color.id) == 0)
                        moves.add((start.idx << 7) | jumpIdx);
                }
                return moves;
            }

            default -> {
                return moves;
            }
        }
    }

    private static boolean isNoObstacles(Vector vector, Square start, int attackingPiece) {
        int squareIdx = start.idx + vector.offset;
        while (Square.isValid(squareIdx)) {
            if (GameState.board[squareIdx] != 0) {
                return GameState.board[squareIdx] == attackingPiece;
            }
            squareIdx += vector.offset;
        }
        return false;
    }

    public static boolean isAttacked(Square square,
                                     Color oppColor,
                                     Square[] oppPieceList
    ) {
        for (Piece piece : Piece.values()) {
            if (piece == Piece.NULL) continue;
            // iterating to 10 because each piece type gets 10 slots in the array except the king
            for (int i = 0; i < (piece == Piece.KING ? 1 : 10); i++) {
                // need to subtract 1 to account for Piece.NULL
                int idx = (piece.id - 1) * 10 + i;

                if (oppPieceList[idx] == Square.NULL ||
                        GameState.board[oppPieceList[idx].idx] != (oppColor.id | piece.id))
                    continue;

                int delta = square.idx - oppPieceList[idx].idx + Square.H8.idx;
                // need to add 119 so there are no negative indices

                if (delta == 239) {
                    GameState.printState();
                    GameState.printBoard();
                }
                switch (ATTACK_TABLE[delta]) {
                   /*   ATTACK_NONE : 0;
                        ATTACK_KQR : 1;
                        ATTACK_QR : 2;
                        ATTACK_KQBwP : 3;
                        ATTACK_KQBbP : 4;
                        ATTACK_QB : 5;
                        ATTACK_N : 6;
                    */
                    case 1 -> {
                        if (piece == Piece.KING) return true;
                        if (piece != Piece.ROOK && piece != Piece.QUEEN) continue;

                        if (isNoObstacles(Vector.of(DELTA_ARRAY[delta]), square,
                                oppColor.id | piece.id)) return true;
                    }

                    case 2 -> {
                        if (piece != Piece.ROOK && piece != Piece.QUEEN) continue;

                        if (isNoObstacles(Vector.of(DELTA_ARRAY[delta]), square,
                                oppColor.id | piece.id)) return true;
                    }

                    case 3 -> {
                        if (piece == Piece.KING) return true;
                        if (piece == Piece.PAWN && oppColor == Color.B) return true;
                        if (piece != Piece.BISHOP && piece != Piece.QUEEN) continue;

                        if (isNoObstacles(Vector.of(DELTA_ARRAY[delta]), square,
                                oppColor.id | piece.id)) return true;
                    }

                    case 4 -> {
                        if (piece == Piece.KING) return true;
                        if (piece == Piece.PAWN && oppColor == Color.W) return true;
                        if (piece != Piece.BISHOP && piece != Piece.QUEEN) continue;

                        if (isNoObstacles(Vector.of(DELTA_ARRAY[delta]), square,
                                oppColor.id | piece.id)) return true;
                    }

                    case 5 -> {
                        if (piece != Piece.BISHOP && piece != Piece.QUEEN) continue;

                        if (isNoObstacles(Vector.of(DELTA_ARRAY[delta]), square,
                                oppColor.id | piece.id)) return true;
                    }

                    case 6 -> {
                        if (piece == Piece.KNIGHT) return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<Integer> pseudoLegalForKing(
            Square start,
            Color color,
            Square[] oppPieceMap) {
        // refer to pseudoLegal for breakdown of move representation
        List<Integer> moves = new ArrayList<>();

        for (Vector vector : Vector.values()) {
            int squareIdx = start.idx + vector.offset;
            if (!Square.isValid(squareIdx)) continue;
            if ((GameState.board[squareIdx] & color.id) == 0) moves.add((start.idx << 7) | squareIdx);
        }

        // kingside castle check
        Color oppColor = color == Color.W ? Color.B : Color.W;
        if (color == Color.W && (GameState.castleRights & 8) != 0 ||
                color == Color.B && (GameState.castleRights & 2) != 0) {
            final Castle castle = color == Color.W ? Castle.W_K : Castle.B_k;
            if (GameState.board[castle.square.idx] == 0 && GameState.board[castle.rSquare.idx] == 0 &&
                    !isAttacked(castle.rSquare,
                            oppColor,
                            oppPieceMap) && !isAttacked(start, oppColor, oppPieceMap)) {
                moves.add(((castle.value << 14) | start.idx << 7) | castle.square.idx);
            }
        }
        //queenside castle check
        if (color == Color.W && (GameState.castleRights & 4) != 0 ||
                color == Color.B && (GameState.castleRights & 1) != 0) {
            final Castle castle = color == Color.W ? Castle.W_Q : Castle.B_q;

            if (GameState.board[castle.square.idx] == 0 && GameState.board[castle.rSquare.idx] == 0 &&
                    GameState.board[castle.rInitSquare.idx + 1] == 0 && !isAttacked(castle.rSquare,
                    oppColor,
                    oppPieceMap
            ) && !isAttacked(start, oppColor, oppPieceMap)) {
                moves.add(((castle.value << 14) | start.idx << 7) | castle.square.idx);
            }
        }
        return moves;
    }

    private static void pushRegularPawnMoves(List<Integer> moveList, Square start, Color color, boolean jumpTwo) {
        Vector vector = color == Color.W ? Vector.UP : Vector.DOWN;
        int squareIdx = start.idx + vector.offset;
        Square square = Square.lookup.get(squareIdx);

        if (GameState.board[squareIdx] != 0) return;

        if (!Square.isPromotion(square,
                color)) moveList.add((start.idx << 7) | squareIdx);
        else {
            for (Piece pieceType : Piece.getPromoteTypes()) {
                moveList.add(((pieceType.id << 18) | (start.idx << 7)) | squareIdx);
            }
        }

        if (jumpTwo) {
            squareIdx += vector.offset;

            if (GameState.board[squareIdx] != 0) return;
            moveList.add((start.idx << 7) | squareIdx);
        }
    }

    private static void pushCapturePawnMoves(List<Integer> moveList, Vector[] captureVectors, Square start,
                                             Color color) {
        for (Vector vector : captureVectors) {
            int squareIdx = start.idx + vector.offset;

            if (!Square.isValid(squareIdx)) continue;

            Square square = Square.lookup.get(squareIdx);
            if (square != GameState.enPassant && GameState.board[square.idx] == 0) continue;
            if ((GameState.board[square.idx] & color.id) != 0) continue;
            if (!Square.isPromotion(square,
                    color)) moveList.add((start.idx << 7) | square.idx);
            else {
                for (Piece pieceType : Piece.getPromoteTypes()) {
                    moveList.add(((pieceType.id << 18) | (start.idx << 7)) | square.idx);
                }
            }
        }
    }

    public static List<Integer> pseudoLegalForPawn(
            Square start,
            Color color) {
        // refer to pseudoLegal for breakdown of move representation
        List<Integer> moves = new ArrayList<>();

        boolean onStartSquare = start.toString()
                                     .contains(color == Color.W ? "2" : "7");
        pushRegularPawnMoves(moves, start, color, onStartSquare);

        final Vector[] captureVectors = color == Color.W ?
                new Vector[]{Vector.UP_RIGHT, Vector.UP_LEFT} :
                new Vector[]{
                        Vector.DOWN_RIGHT,
                        Vector.DOWN_LEFT
                };
        pushCapturePawnMoves(moves, captureVectors, start, color);
        return moves;
    }
}