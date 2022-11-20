package main.moveGen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    public static List<Move> pseudoLegal(int[] board,
                                         Square start,
                                         Piece pieceType,
                                         Color color) {
        List<Move> moves = new ArrayList<>();

        Function<Square, Boolean> pushToMoves = (Square square) -> {
            if (board[square.idx] != 0 && Color.extractColor(board[square.idx]) == color) return false;
            moves.add(new Move(start,
                    square));
            // return false after capture move has been added
            return board[square.idx] == 0;
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
                final Vector[] DIAGONAL_VECTORS = {
                        Vector.UP_RIGHT, Vector.UP_LEFT, Vector.DOWN_LEFT,
                        Vector.DOWN_RIGHT
                };
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
                    int jumpIdx = start.idx + jump;
                    if (Square.isValid(jumpIdx))
                        pushToMoves.apply(Square.lookup.get(jumpIdx));
                }
                return moves;
            }

            default -> {
                return moves;
            }
        }
    }

    public static boolean isAttacked(Square square,
                                     Color oppColor,
                                     Square[] oppPieceList,
                                     int[] board) {
        final boolean[] attacked = {false};
        for (Piece piece : Piece.values()) {
            if (piece == Piece.NULL) continue;
            // iterating to 10 because each piece type gets 10 slots in the array except the king
            for (int i = 0; i < (piece == Piece.KING ? 1 : 10); i++) {
                // need to subtract 1 to account for Piece.NULL
                int idx = (piece.id - 1) * 10 + i;

                if (oppPieceList[idx] == Square.NULL) break;
                int delta = square.idx - oppPieceList[idx].idx + Square.H8.idx;
                // need to add 119 so there are no negative indices

                Function<Square, Boolean> lookForObstacles = (Square newSquare) -> {
                    if (board[newSquare.idx] == 0) return true;
                    if (Color.extractColor(board[newSquare.idx]) != oppColor) return false;
                    if (Piece.extractPieceType(board[newSquare.idx]) == piece) {
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
                        if (piece != Piece.ROOK && piece != Piece.QUEEN) continue;
                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 2 -> {
                        if (piece != Piece.ROOK && piece != Piece.QUEEN) continue;

                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 3 -> {
                        if (piece == Piece.KING) return true;
                        if (piece == Piece.PAWN && oppColor == Color.W) return true;
                        if (piece != Piece.BISHOP && piece != Piece.QUEEN) continue;

                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 4 -> {
                        if (piece == Piece.KING) return true;
                        if (piece == Piece.PAWN && oppColor == Color.B) return true;
                        if (piece != Piece.BISHOP && piece != Piece.QUEEN) continue;

                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 5 -> {
                        if (piece != Piece.BISHOP && piece != Piece.QUEEN) continue;

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

    public static List<Move> pseudoLegalForKing(int[] board,
                                                Square start,
                                                Color color,
                                                int castleRights,
                                                Square[] oppPieceMap) {
        List<Move> moves = new ArrayList<>();
        Function<Square, Boolean> pushToMoves = (Square square) -> {
            if (board[square.idx] != 0 && Color.extractColor(board[square.idx]) == color) return false;
            moves.add(new Move(start,
                    square));
            // return false after capture move has been added
            return board[square.idx] == 0;
        };
        for (Vector vector : Vector.values()) {
            traverseVectorShort(vector,
                    start,
                    pushToMoves);
        }

        // kingside castle check
        Color oppColor = color == Color.W ? Color.B : Color.W;
        if (color == Color.W && (castleRights & 8) != 0 || color == Color.B && (castleRights & 2) != 0) {
            final Castle castle = color == Color.W ? Castle.W_K : Castle.B_k;
            if (board[castle.square.idx] == 0 && board[castle.rSquare.idx] == 0 && !isAttacked(castle.rSquare,
                    oppColor,
                    oppPieceMap,
                    board)) {
                moves.add(new Move(start, castle));
            }
        }
        //queenside castle check
        if (color == Color.W && (castleRights & 4) != 0 || color == Color.B && (castleRights & 1) != 0) {
            final Castle castle = color == Color.W ? Castle.W_Q : Castle.B_q;

            if (board[castle.square.idx] == 0 && board[castle.rSquare.idx] == 0 && !isAttacked(castle.rSquare,
                    oppColor,
                    oppPieceMap,
                    board)) {
                moves.add(new Move(start, castle));
            }
        }
        return moves;
    }

    public static List<Move> pseudoLegalForPawn(int[] board,
                                                Square start,
                                                Color color,
                                                Square enPassant) {
        List<Move> moves = new ArrayList<>();
        Function<Square, Boolean> pushToMovesRegular = (Square square) -> {
            if (board[square.idx] != 0) return false;

            if (!Square.isPromotion(square,
                    color)) moves.add(new Move(start,
                    square));
            else {
                for (Piece piece : Piece.getPromoteTypes()) {
                    moves.add(new Move(start,
                            square,
                            piece));
                }
            }

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
        Function<Square, Boolean> pushToMovesCapture = (Square square) -> {
            if (square != enPassant && board[square.idx] == 0) return false;
            if (Color.extractColor(board[square.idx]) == color) return false;
            if (!Square.isPromotion(square,
                    color)) moves.add(new Move(start,
                    square));
            else {
                for (Piece piece : Piece.getPromoteTypes()) {
                    moves.add(new Move(start,
                            square,
                            piece));
                }
            }
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
                                           Function<Square, Boolean> cb) {
        int squareIdx = start.idx + vector.offset;
        while (Square.isValid(squareIdx)) {
            if (!cb.apply(Square.lookup.get(squareIdx))) break;
            squareIdx = squareIdx + vector.offset;
        }
    }

    private static void traverseVectorShort(Vector vector,
                                            Square start,
                                            Function<Square, Boolean> cb) {
        int squareIdx = start.idx + vector.offset;
        if (Square.isValid(squareIdx)) {
            cb.apply(Square.lookup.get(squareIdx));
        }
    }
}
