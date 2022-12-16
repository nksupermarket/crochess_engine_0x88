package main.moveGen;

import main.*;

import java.util.ArrayList;
import java.util.Iterator;
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

    private static void traverseVectorShort(Vector vector,
                                            Square start,
                                            Function<Square, Boolean> cb, int range) {
        int squareIdx = start.idx + vector.offset;
        while (range != 0) {
            if (!cb.apply(Square.lookup.get(squareIdx))) break;
            squareIdx += vector.offset;
            range--;
        }
    }

    public static List<Integer> pseudoLegal(int[] board,
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

        Function<Square, Boolean> pushToMoves = (Square square) -> {
            if ((board[square.idx] & color.id) == 0) moves.add((start.idx << 7) | square.idx);
            // return false after capture move has been added
            return board[square.idx] == 0;
        };

        switch (pieceType) {
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
                    if (Square.isValid(jumpIdx) && (board[jumpIdx] & color.id) == 0)
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

                if (oppPieceList[idx] == Square.NULL || board[oppPieceList[idx].idx] != (oppColor.id | piece.id))
                    continue;

                int delta = square.idx - oppPieceList[idx].idx + Square.H8.idx;
                // need to add 119 so there are no negative indices

                Function<Square, Boolean> lookForObstacles = (Square newSquare) -> {
                    if (board[newSquare.idx] == 0) return true;
                    if (board[newSquare.idx] == (oppColor.id | piece.id)) {
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
                        if (piece == Piece.PAWN && oppColor == Color.B) return true;
                        if (piece != Piece.BISHOP && piece != Piece.QUEEN) continue;

                        traverseVectorLong(Vector.of(DELTA_ARRAY[delta]),
                                square,
                                lookForObstacles);
                        if (attacked[0]) return true;
                    }

                    case 4 -> {
                        if (piece == Piece.KING) return true;
                        if (piece == Piece.PAWN && oppColor == Color.W) return true;
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

    public static List<Integer> pseudoLegalForKing(int[] board,
                                                   Square start,
                                                   Color color,
                                                   int castleRights,
                                                   Square[] oppPieceMap) {
        // refer to pseudoLegal for breakdown of move representation
        List<Integer> moves = new ArrayList<>();
        Function<Square, Boolean> pushToMoves = (Square square) -> {
            if ((board[square.idx] & color.id) == 0) moves.add((start.idx << 7) | square.idx);
            return false;
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
                    board) && !isAttacked(start, oppColor, oppPieceMap, board)) {
                moves.add(((castle.value << 14) | start.idx << 7) | castle.square.idx);
            }
        }
        //queenside castle check
        if (color == Color.W && (castleRights & 4) != 0 || color == Color.B && (castleRights & 1) != 0) {
            final Castle castle = color == Color.W ? Castle.W_Q : Castle.B_q;

            if (board[castle.square.idx] == 0 && board[castle.rSquare.idx] == 0 &&
                    board[castle.rInitSquare.idx + 1] == 0 && !isAttacked(castle.rSquare,
                    oppColor,
                    oppPieceMap,
                    board) && !isAttacked(start, oppColor, oppPieceMap, board)) {
                moves.add(((castle.value << 14) | start.idx << 7) | castle.square.idx);
            }
        }
        return moves;
    }

    public static List<Integer> pseudoLegalForPawn(int[] board,
                                                   Square start,
                                                   Color color,
                                                   Square enPassant) {
        // refer to pseudoLegal for breakdown of move representation
        List<Integer> moves = new ArrayList<>();
        Function<Square, Boolean> pushToMovesRegular = (Square square) -> {
            if (board[square.idx] != 0) return false;

            if (!Square.isPromotion(square,
                    color)) moves.add((start.idx << 7) | square.idx);
            else {
                for (Piece pieceType : Piece.getPromoteTypes()) {
                    moves.add(((pieceType.id << 18) | (start.idx << 7)) | square.idx);
                }
            }

            return true;
        };
        boolean onStartSquare = start.toString()
                                     .contains(color == Color.W ? "2" : "7");
        traverseVectorShort(color == Color.W ?
                        Vector.UP :
                        Vector.DOWN,
                start,
                pushToMovesRegular,
                onStartSquare ? 2 : 1
        );

        final Vector[] captureVectors = color == Color.W ?
                new Vector[]{Vector.UP_RIGHT, Vector.UP_LEFT} :
                new Vector[]{
                        Vector.DOWN_RIGHT,
                        Vector.DOWN_LEFT
                };
        Function<Square, Boolean> pushToMovesCapture = (Square square) -> {
            if (square != enPassant && board[square.idx] == 0) return false;
            if ((board[square.idx] & color.id) != 0) return false;
            if (!Square.isPromotion(square,
                    color)) moves.add((start.idx << 7) | square.idx);
            else {
                for (Piece pieceType : Piece.getPromoteTypes()) {
                    moves.add(((pieceType.id << 18) | (start.idx << 7)) | square.idx);
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

    private static boolean moveIsCheck(int move) {
        Castle castle = Castle.lookup.get((move >> 14) & 15);
        Square from = Square.lookup.get((move >> 7) & 127);
        Square to = Square.lookup.get(move & 127);

        Color color = Color.extractColor(GameState.board[from.idx]);
        assert color != null;

        Piece pieceType = Piece.extractPieceType(GameState.board[from.idx]);
        Color oppColor = Color.getOppColor(color);

        if (castle != null) {
            GameState.board[castle.square.idx] = GameState.board[from.idx];
            GameState.board[from.idx] = 0;
            GameState.board[castle.rSquare.idx] = GameState.board[castle.rInitSquare.idx];
            GameState.board[castle.rInitSquare.idx] = 0;

            GameState.pieceList.get(GameState.activeColor)[50] = castle.square;
            GameState.moveInPieceList(GameState.activeColor, castle.rInitSquare, castle.rSquare);

            boolean valid = GameState.inCheck(oppColor);

            GameState.pieceList.get(GameState.activeColor)[50] = from;
            GameState.moveInPieceList(GameState.activeColor, castle.rSquare, castle.rInitSquare);

            GameState.board[from.idx] = GameState.board[castle.square.idx];
            GameState.board[castle.square.idx] = 0;
            GameState.board[castle.rInitSquare.idx] = GameState.board[castle.rSquare.idx];
            GameState.board[castle.rSquare.idx] = 0;

            return valid;
        } else if (pieceType == Piece.PAWN && to == GameState.enPassant) {

            Square enPassantCaptureSquare = Square.lookup.get(
                    to.idx + (color == Color.W ? Vector.DOWN.offset :
                            Vector.UP.offset)
            );

            GameState.board[to.idx] = GameState.board[from.idx];
            GameState.board[from.idx] = 0;
            GameState.board[enPassantCaptureSquare.idx] = 0;

            boolean valid = !GameState.inCheck(oppColor);
            GameState.moveInPieceList(color, from, to);
            GameState.removePiece(oppColor, Piece.PAWN, enPassantCaptureSquare);

            GameState.moveInPieceList(color, to, from);
            GameState.putPiece(oppColor, Piece.PAWN, enPassantCaptureSquare);

            GameState.board[from.idx] = color.id | Piece.PAWN.id;
            GameState.board[to.idx] = 0;
            GameState.board[enPassantCaptureSquare.idx] = oppColor.id | Piece.PAWN.id;

            return valid;
        } else {

            int capturedPiece = GameState.board[to.idx];
            GameState.board[to.idx] = GameState.board[from.idx];
            GameState.board[from.idx] = 0;

            int promote = move >> 18;
            if (promote != 0) {
                GameState.board[to.idx] = color.id | promote;

                GameState.removePiece(color, Piece.PAWN, from);
                GameState.putPiece(color, Piece.extractPieceType(promote), to);
            } else {
                if (capturedPiece != 0) GameState.removePiece(oppColor, Piece.extractPieceType(capturedPiece),
                        to);
                GameState.moveInPieceList(color, from, to);
            }

            boolean valid = GameState.inCheck(oppColor);

            if (promote != 0) {
                GameState.removePiece(color, Piece.extractPieceType(GameState.board[to.idx]), to);
                GameState.putPiece(color, Piece.PAWN, from);
            } else {
                if (capturedPiece != 0) GameState.putPiece(oppColor, Piece.extractPieceType(capturedPiece),
                        to);
                GameState.moveInPieceList(color, to, from);
            }

            GameState.board[from.idx] = color.id | pieceType.id;
            GameState.board[to.idx] = capturedPiece;

            return valid;
        }
    }

    public static void filterOut(List<Integer> moveList, boolean captures, boolean checks) {
        Iterator<Integer> moveIterator = moveList.iterator();
        while (moveIterator.hasNext()) {
            int move = moveIterator.next();
            Square from = Square.lookup.get((move >> 7) & 127);
            Square to = Square.lookup.get(move & 127);
            Piece pieceType = Piece.extractPieceType(GameState.board[from.idx]);

            boolean moveIsCapture =
                    GameState.board[to.idx] == 0 || (pieceType == Piece.PAWN && to != GameState.enPassant);

            if ((captures && !moveIsCapture) && (checks && !moveIsCheck(move))) moveIterator.remove();
        }
    }
}
