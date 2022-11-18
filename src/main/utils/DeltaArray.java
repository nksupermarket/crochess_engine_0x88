package main.utils;

import main.moveGen.*;

import java.util.Arrays;
import java.util.List;

public class DeltaArray {
    private DeltaArray() {
    }

    public static void main(String[] args) {

        createAttackArray();
        System.out.println();
        createOffsetArray();
    }

    public static void createAttackArray() {
        int[] board = new int[128];
        int[] bPieceList = new int[51];
        int castleRights = 0;
        Square enPassant = null;
        Arrays.fill(board,
                0);
        Arrays.fill(bPieceList,
                -1);

        int[] Attack_array = new int[239];
        for (int i = 0; i < 120; i++) {
            for (int j = 0; j < 120; j++) {
                if (!Square.isValid(j)) continue;
                Arrays.fill(board,
                        0);
                List<Integer> queenMoves = Moves.pseudoLegal(board,
                        Square.lookup.get(j),
                        Piece.QUEEN,
                        Color.W);
                List<Integer> bishopMoves = Moves.pseudoLegal(board,
                        Square.lookup.get(j),
                        Piece.BISHOP,
                        Color.W);
                List<Integer> rookMoves = Moves.pseudoLegal(board,
                        Square.lookup.get(j),
                        Piece.ROOK,
                        Color.W);
                List<Integer> knightMoves = Moves.pseudoLegal(board,
                        Square.lookup.get(j),
                        Piece.KNIGHT,
                        Color.W);
                List<Integer> kingMoves = Moves.pseudoLegalForKing(board,
                        Square.lookup.get(j),
                        Color.W,
                        castleRights,
                        bPieceList);
                Arrays.fill(board,
                        Color.B.id | Piece.PAWN.id);
                List<Integer> wPawnMoves = Moves.pseudoLegalForPawn(board,
                        Square.lookup.get(j),
                        Color.W,
                        enPassant);
                Arrays.fill(board,
                        Color.W.id | Piece.PAWN.id);
                List<Integer> bPawnMoves = Moves.pseudoLegalForPawn(board,
                        Square.lookup.get(j),
                        Color.B,
                        enPassant);

                if (kingMoves.contains(i) && queenMoves.contains(i) && bishopMoves.contains(i) &&
                        bPawnMoves.contains(i)) Attack_array[j - i + 119] = 4;
                else if (kingMoves.contains(i) && queenMoves.contains(i) && bishopMoves.contains(i) &&
                        wPawnMoves.contains(i)) Attack_array[j - i + 119] = 3;
                else if (kingMoves.contains(i) && queenMoves.contains(i) && rookMoves.contains(i))
                    Attack_array[j - i + 119] = 1;
                else if (queenMoves.contains(i) && bishopMoves.contains(i)) Attack_array[j - i + 119] = 5;
                else if (queenMoves.contains(i) && rookMoves.contains(i)) Attack_array[j - i + 119] = 2;
                else if (knightMoves.contains(i))
                    Attack_array[j - i + 119] = 6;
            }
        }

        // display in console
        int[] list = new int[20];
        // fill with -1 bc Attack_array isnt cleanly divisible by 20
        // a value of -1 means its safe to delete
        Arrays.fill(list,
                -1);
        int count = 0;
        for (int i = 0; i < Attack_array.length; i++) {
            list[count] = Attack_array[i];
            count++;
            if (count == 20) {
                System.out.println(Arrays.toString(list));
                count = 0;
                Arrays.fill(list,
                        -1);
            }
            if (i == Attack_array.length - 1) System.out.println(Arrays.toString(list));
        }
    }

    public static void createOffsetArray() {
        int[] board = new int[128];
        Square enPassant = null;
        Arrays.fill(board,
                0);

        int[] Delta_array = new int[239];
        for (int i = 0; i < 120; i++) {
            for (int j = 0; j < 120; j++) {
                if (!Square.isValid(j)) continue;
                Arrays.fill(board,
                        0);

                List<Integer> bishopMoves = Moves.pseudoLegal(board,
                        Square.lookup.get(j),
                        Piece.BISHOP,
                        Color.W);
                List<Integer> rookMoves = Moves.pseudoLegal(board,
                        Square.lookup.get(j),
                        Piece.ROOK,
                        Color.W);
                List<Integer> knightMoves = Moves.pseudoLegal(board,
                        Square.lookup.get(j),
                        Piece.KNIGHT,
                        Color.W);
                List<Integer> wPawnMoves = Moves.pseudoLegalForPawn(board,
                        Square.lookup.get(j),
                        Color.W,
                        enPassant);
                Arrays.fill(board,
                        Color.W.id | Piece.PAWN.id);
                List<Integer> bPawnMoves = Moves.pseudoLegalForPawn(board,
                        Square.lookup.get(j),
                        Color.B,
                        enPassant);

                if (bishopMoves.contains(i) &&
                        bPawnMoves.contains(i)) {
                    int offset =
                            (j - i) % Vector.DOWN_RIGHT.offset == 0 ? Vector.DOWN_RIGHT.offset :
                                    Vector.DOWN_LEFT.offset;
                    Delta_array[j - i + 119] = offset;
                } else if (bishopMoves.contains(i) &&
                        wPawnMoves.contains(i)) {
                    int offset =
                            (j - i) % Vector.UP_RIGHT.offset == 0 ? Vector.UP_RIGHT.offset : Vector.UP_LEFT.offset;
                    Delta_array[j - i + 119] = offset;
                } else if (rookMoves.contains(i)) {
                    boolean isOnY = (j - i) % Vector.UP.offset == 0;
                    int offset;
                    final boolean negativeOffset = Math.abs(j - i) != j - i;
                    if (isOnY) offset = negativeOffset ? Vector.UP.offset : Vector.DOWN.offset;
                    else offset = negativeOffset ? Vector.RIGHT.offset : Vector.LEFT.offset;
                    Delta_array[j - i + 119] = offset;
                } else if (bishopMoves.contains(i)) {
                    boolean isOnUpRightDiagonal = (j - i) % Vector.UP_RIGHT.offset == 0;
                    int offset;
                    final boolean negativeOffset = Math.abs(j - i) != j - i;
                    if (isOnUpRightDiagonal) offset = negativeOffset ? Vector.UP_RIGHT.offset : Vector.DOWN_LEFT.offset;
                    else offset = negativeOffset ? Vector.UP_LEFT.offset : Vector.DOWN_RIGHT.offset;
                    Delta_array[j - i + 119] = offset;
                } else if (knightMoves.contains(i))
                    Delta_array[j - i + 119] = j - i;
            }
        }

        // display in console
        int[] list = new int[20];
        // fill with -3 bc Attack_array isnt cleanly divisible by 20
        // a value of -3 means its safe to delete
        Arrays.fill(list,
                -3);
        int count = 0;
        for (int i = 0; i < Delta_array.length; i++) {
            list[count] = Delta_array[i];
            count++;
            if (count == 20) {
                System.out.println(Arrays.toString(list));
                count = 0;
                Arrays.fill(list,
                        -3);
            }
            if (i == Delta_array.length - 1) System.out.println(Arrays.toString(list));
        }
    }
}
