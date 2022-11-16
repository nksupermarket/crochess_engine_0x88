package main.moveGen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

final public class Moves {
    private Moves() {
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
            0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,     //0-19
            0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 5, 0,     //20-39
            0, 0, 0, 5, 0, 0, 0, 0, 2, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0,     //40-59
            5, 0, 0, 0, 2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0,     //60-79
            2, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6, 2, 6, 5, 0,     //80-99
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 4, 1, 4, 6, 0, 0, 0, 0, 0,     //100-119
            0, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0,     //120-139
            0, 0, 6, 3, 1, 3, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6,     //140-159
            2, 6, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 2, 0, 0, 5,     //160-179
            0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 2, 0, 0, 0, 5, 0, 0, 0,     //180-199
            0, 0, 0, 5, 0, 0, 0, 0, 2, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0,     //200-219
            0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0,     //220-239
            2, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0
    }; //240-256
    final private static int[] DELTA_ARRAY = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, -17, 0, 0, 0, 0, 0, 0, -16, 0, 0,
            0, 0, 0, 0, -15, 0, 0, -17, 0, 0, 0, 0, 0, -16, 0, 0, 0, 0, 0,
            -15, 0, 0, 0, 0, -17, 0, 0, 0, 0, -16, 0, 0, 0, 0, -15, 0, 0, 0, 0, 0,
            0, -17, 0, 0, 0, -16, 0, 0, 0, -15, 0, 0, 0, 0, 0, 0, 0, 0, -17, 0, 0, -16, 0, 0, -15, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -17, -33, -16, -31, -15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -18, -17, -16, -15, -14, 0, 0, 0, 0, 0,
            0, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 14, 15, 16, 17, 18, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 15, 31, 16, 33, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 16, 0, 0, 17, 0, 0, 0, 0, 0,
            0, 0, 0, 15, 0, 0, 0, 16, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 16, 0, 0, 0, 0, 17, 0, 0, 0, 0, 15,
            0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 17, 0, 0, 15, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0,
            0, 0, 0, 0
    };

    public static List<Integer> pseudoLegal(int[] board,
                                            Square start,
                                            Piece pieceType,
                                            Color color) {
        List<Integer> moves = new ArrayList<>();
        Function<Integer, Boolean> pushToMoves = (Integer squareIdx) -> {
            if (Color.extractColor(board[squareIdx]) == color) return false;
            moves.add(squareIdx);
            // return false after capture move has been added
            return board[squareIdx] == 0;
        };

        switch (pieceType) {
            case KING -> {
                for (Vector vector : Vector.values()) {
                    traverseVectorShort(vector,
                            start,
                            pushToMoves);
                }
                return moves;
            }

            case QUEEN -> {
                for (Vector vector : Vector.values()) {
                    traverseVectorLong(vector,
                            start,
                            pushToMoves);
                }
                return moves;
            }

            case ROOK -> {
                final Vector[] XY_VECTORS = {Vector.UP, Vector.DOWN, Vector.LEFT, Vector.RIGHT};
                for (Vector vector : XY_VECTORS) {
                    traverseVectorLong(vector,
                            start,
                            pushToMoves);
                }
                return moves;
            }

            case BISHOP -> {
                final Vector[] DIAGONAL_VECTORS = {Vector.UP_RIGHT, Vector.UP_LEFT, Vector.DOWN_LEFT, Vector.UP_RIGHT};
                for (Vector vector : DIAGONAL_VECTORS) {
                    traverseVectorLong(vector,
                            start,
                            pushToMoves);
                }
                return moves;
            }

            case KNIGHT -> {
                final int[] KNIGHT_JUMPS = {33, 31, 18, 14, -31, -33, -18, -14};
                for (int jump : KNIGHT_JUMPS) {
                    pushToMoves.apply(start.idx + jump);
                }
                return moves;
            }

            default -> {
                return moves;
            }
        }
    }

    private static boolean isAttacked(Square square,
                                      Color oppColor,
                                      int[] oppPieceList,
                                      int[] board) {
        final boolean[] attacked = {false};
        outerLoop:
        for (Piece piece : Piece.values()) {
            if (piece == Piece.NULL) continue;
            for (int i = 0; i < 10; i++) {
                if (i == oppPieceList.length) break;
                int idx = piece.id * 10 + i;
                if (oppPieceList[idx] == -1) break outerLoop;
                int delta = square.idx - oppPieceList[idx];

                Function<Integer, Boolean> lookForObstacles = (Integer squareIdx) -> {
                    if (board[squareIdx] == 0) return true;
                    if (Color.extractColor(board[squareIdx]) != oppColor) return false;
                    if (Piece.extractPieceType(board[squareIdx]) == piece) {
                        attacked[0] = true;
                    }
                    return false;
                };

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

                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 2, 5 -> {
                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 3 -> {
                        if (piece == Piece.KING) return true;
                        if (piece == Piece.PAWN && oppColor == Color.W) return true;
                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 4 -> {
                        if (piece == Piece.KING) return true;
                        if (piece == Piece.PAWN && oppColor == Color.B) return true;
                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 6 -> {
                        if (piece == Piece.KNIGHT) return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<Integer> pseudoLegalForKing(int[] board,
                                                   Square start,
                                                   Color color,
                                                   int castleRights,
                                                   int[] oppPieceMap) {
        List<Integer> moves = new ArrayList<>();
        Function<Integer, Boolean> pushToMovesRegular = (Integer squareIdx) -> {
            if (board[squareIdx] != 0) return false;
            moves.add(squareIdx);
            return false;
        };
        for (Vector vector : Vector.values()) {
            traverseVectorShort(vector,
                    start,
                    pushToMovesRegular);
        }

        // kingside castle check
        Color oppColor = color == Color.W ? Color.B : Color.W;
        if (color == Color.W && (castleRights & 8) != 0 || color == Color.B && (castleRights & 2) != 0) {
            if (!isAttacked(color == Color.W ? Square.F1 : Square.F8,
                    oppColor,
                    oppPieceMap,
                    board)) {
                moves.add(color == Color.W ? Square.G1.idx : Square.G8.idx);
            }
        }
        //queenside castle check
        if (color == Color.W && (castleRights & 4) != 0 || color == Color.B && (castleRights & 1) != 0) {
            if (!isAttacked(color == Color.W ? Square.D1 : Square.C8,
                    oppColor,
                    oppPieceMap,
                    board)) moves.add(color == Color.W ?
                    Square.C1.idx :
                    Square.C8.idx);
        }
        return moves;
    }

    public static List<Integer> pseudoLegalForPawn(int[] board,
                                                   Square start,
                                                   Color color,
                                                   Square enPassant) {
        List<Integer> moves = new ArrayList<>();
        Function<Integer, Boolean> pushToMovesRegular = (Integer squareIdx) -> {
            if (board[squareIdx] != 0) return false;
            moves.add(squareIdx);
            return false;
        };
        traverseVectorShort(color == Color.W ?
                        Vector.UP :
                        Vector.DOWN,
                start,
                pushToMovesRegular);

        final Vector[] captureVectors = color == Color.W ?
                new Vector[]{Vector.UP_RIGHT, Vector.UP_LEFT} :
                new Vector[]{
                        Vector.DOWN_RIGHT,
                        Vector.DOWN_LEFT
                };
        Function<Integer, Boolean> pushToMovesCapture = (Integer squareIdx) -> {
            if (Square.lookup.get(squareIdx) != enPassant || board[squareIdx] == 0 ||
                    Color.extractColor(board[squareIdx]) == color) return false;
            moves.add(squareIdx);
            return false;
        };
        for (Vector vector : captureVectors) {
            traverseVectorShort(vector,
                    start,
                    pushToMovesCapture);
        }
        return moves;
    }

    private static void traverseVectorLong(Vector vector,
                                           Square start,
                                           Function<Integer, Boolean> cb) {
        int squareIdx = start.idx + vector.offset;
        while (Square.isValid(squareIdx)) {
            if (!cb.apply(squareIdx)) break;
            squareIdx += vector.offset;
        }
    }

    private static void traverseVectorShort(Vector vector,
                                            Square start,
                                            Function<Integer, Boolean> cb) {
        int squareIdx = start.idx + vector.offset;
        if (Square.isValid(squareIdx)) {
            cb.apply(squareIdx);
        }
    }
}
