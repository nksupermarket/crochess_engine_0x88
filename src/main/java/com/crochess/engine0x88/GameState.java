package com.crochess.engine0x88;

import com.crochess.engine0x88.moveEval.Psqt;
import com.crochess.engine0x88.types.*;
import com.crochess.engine0x88.types.Vector;

import com.crochess.engine0x88.utils.Score;
import com.crochess.engine0x88.utils.Utils;

import java.util.*;

final public class GameState {
    private GameState() {
    }

    public static void main(String[] args) {
        loadFen(args[1]);
        System.out.println(countNumOfPositions(Integer.parseInt(args[0]), true));
    }

    public static int[] board = new int[128];
    //start index for each piece is equal to Piece.id - 1 * 10 (need to multiply by 10 bc that is max amount of possible
    // pieces
    // need to subtract 1 from Piece.id bc Piece enum starts with NULL
    public static Phase phase = Phase.MG;
    public static Square[][] pieceList = new Square[2][51];
    public static int[] pawnCount = new int[2];
    public static int[] pieceCount = new int[2];
    public static int[] pieceBonus = new int[2];
    public static long zobristHash;
    public static Map<Long, Integer> totalRepititions = new HashMap<>();

    public static Color activeColor = Color.W;
    public static Square enPassant = Square.NULL;
    public static int halfmoves;
    public static int castleRights =
            Castle.W_K.value | Castle.W_Q.value | Castle.B_k.value | Castle.B_q.value;

    // init board + pieceList + get zobrish hash
    static {
        board[0] = Color.W.id | Piece.ROOK.id;
        board[1] = Color.W.id | Piece.KNIGHT.id;
        board[2] = Color.W.id | Piece.BISHOP.id;
        board[3] = Color.W.id | Piece.QUEEN.id;
        board[4] = Color.W.id | Piece.KING.id;
        board[5] = Color.W.id | Piece.BISHOP.id;
        board[6] = Color.W.id | Piece.KNIGHT.id;
        board[7] = Color.W.id | Piece.ROOK.id;
        board[16] = Color.W.id | Piece.PAWN.id;
        board[17] = Color.W.id | Piece.PAWN.id;
        board[18] = Color.W.id | Piece.PAWN.id;
        board[19] = Color.W.id | Piece.PAWN.id;
        board[20] = Color.W.id | Piece.PAWN.id;
        board[21] = Color.W.id | Piece.PAWN.id;
        board[22] = Color.W.id | Piece.PAWN.id;
        board[23] = Color.W.id | Piece.PAWN.id;

        final Square[] wPieceList = pieceList[Color.W.ordinal()];
        Arrays.fill(wPieceList,
                Square.NULL);
        putPiece(Color.W, Piece.PAWN, Square.A2, false);
        putPiece(Color.W, Piece.PAWN, Square.B2, false);
        putPiece(Color.W, Piece.PAWN, Square.C2, false);
        putPiece(Color.W, Piece.PAWN, Square.D2, false);
        putPiece(Color.W, Piece.PAWN, Square.E2, false);
        putPiece(Color.W, Piece.PAWN, Square.F2, false);
        putPiece(Color.W, Piece.PAWN, Square.G2, false);
        putPiece(Color.W, Piece.PAWN, Square.H2, false);
        putPiece(Color.W, Piece.ROOK, Square.A1, false);
        putPiece(Color.W, Piece.KNIGHT, Square.B1, false);
        putPiece(Color.W, Piece.BISHOP, Square.C1, false);
        putPiece(Color.W, Piece.QUEEN, Square.D1, false);
        putPiece(Color.W, Piece.KING, Square.E1, false);
        putPiece(Color.W, Piece.BISHOP, Square.F1, false);
        putPiece(Color.W, Piece.KNIGHT, Square.G1, false);
        putPiece(Color.W, Piece.ROOK, Square.H1, false);

        board[112] = Color.B.id | Piece.ROOK.id;
        board[113] = Color.B.id | Piece.KNIGHT.id;
        board[114] = Color.B.id | Piece.BISHOP.id;
        board[115] = Color.B.id | Piece.QUEEN.id;
        board[116] = Color.B.id | Piece.KING.id;
        board[117] = Color.B.id | Piece.BISHOP.id;
        board[118] = Color.B.id | Piece.KNIGHT.id;
        board[119] = Color.B.id | Piece.ROOK.id;
        board[96] = Color.B.id | Piece.PAWN.id;
        board[97] = Color.B.id | Piece.PAWN.id;
        board[98] = Color.B.id | Piece.PAWN.id;
        board[99] = Color.B.id | Piece.PAWN.id;
        board[100] = Color.B.id | Piece.PAWN.id;
        board[101] = Color.B.id | Piece.PAWN.id;
        board[102] = Color.B.id | Piece.PAWN.id;
        board[103] = Color.B.id | Piece.PAWN.id;

        final Square[] bPieceList = pieceList[Color.B.ordinal()];
        Arrays.fill(bPieceList,
                Square.NULL);
        putPiece(Color.B, Piece.PAWN, Square.A7, false);
        putPiece(Color.B, Piece.PAWN, Square.B7, false);
        putPiece(Color.B, Piece.PAWN, Square.C7, false);
        putPiece(Color.B, Piece.PAWN, Square.D7, false);
        putPiece(Color.B, Piece.PAWN, Square.E7, false);
        putPiece(Color.B, Piece.PAWN, Square.F7, false);
        putPiece(Color.B, Piece.PAWN, Square.G7, false);
        putPiece(Color.B, Piece.PAWN, Square.H7, false);
        putPiece(Color.B, Piece.ROOK, Square.A8, false);
        putPiece(Color.B, Piece.KNIGHT, Square.B8, false);
        putPiece(Color.B, Piece.BISHOP, Square.C8, false);
        putPiece(Color.B, Piece.QUEEN, Square.D8, false);
        putPiece(Color.B, Piece.KING, Square.E8, false);
        putPiece(Color.B, Piece.BISHOP, Square.F8, false);
        putPiece(Color.B, Piece.KNIGHT, Square.G8, false);
        putPiece(Color.B, Piece.ROOK, Square.H8, false);

        zobristHash = ZobristKey.hash();
    }

    public static void printBoard() {
        System.out.println();
        Map<Color, String[]> unicode_pieces = new HashMap<>();

        unicode_pieces.put(Color.W, new String[]{".", "♟︎", "♞", "♝", "♜", "♛", "♚"});
        unicode_pieces.put(Color.B, new String[]{".", "♙", "♘", "♗", "♖", "♕", "♔"})
        ;
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 16; file++) {
                // init square
                int square = rank * 16 + file;

                // print ranks
                if (file == 0)
                    System.out.printf(" %d  ", rank + 1);

                // if square is on board
                if ((square & 0x88) == 0)
                    System.out.printf("%s ",
                            GameState.board[square] == 0 ? "." : unicode_pieces.get(
                                    Color.extractColor(GameState.board[square]))[GameState.board[square] &
                                    7]);
            }

            // print new line every time new rank is encountered
            System.out.print("\n");
        }
        System.out.println();
    }

    public static void printState() {
        System.out.println(activeColor);
        System.out.println(Arrays.toString(pieceCount));
        System.out.println(Arrays.toString(pieceList[Color.W.ordinal()]));
        System.out.println(Arrays.toString(pieceList[Color.B.ordinal()]));
        System.out.println(castleRights);
        System.out.println(enPassant);
    }

    public static void putPiece(Color color,
                                Piece piece,
                                Square square, boolean listOnly) {
        if (!listOnly) {
            switch (piece) {
                case PAWN -> pawnCount[color.ordinal()] += Score.get(piece.value);
                case KING -> {
                }
                default -> pieceCount[color.ordinal()] += Score.get(piece.value);
            }
            pieceBonus[color.ordinal()] +=
                    Score.get(Psqt.table[color.ordinal()][piece.id][Square.getRank(square)][Square.getFile(square)]);
        }
        Square[] list = pieceList[color.ordinal()];

        int startIdx = (piece.id - 1) * 10;
        // only one slot for king
        for (int i = 0; i < (piece == Piece.KING ? 1 : 10); i++) {
            int nextIdx = startIdx + i;
            if (list[nextIdx] == Square.NULL) {
                list[nextIdx] = square;
                return;
            }
        }
    }

    public static void removePiece(Color color, Piece piece, Square square, boolean listOnly) {
        if (!listOnly) {
            if (piece == Piece.PAWN) pawnCount[color.ordinal()] -= Score.get(piece.value);
            else pieceCount[color.ordinal()] -= Score.get(piece.value);

            pieceBonus[color.ordinal()] -=
                    Score.get(Psqt.table[color.ordinal()][piece.id][Square.getRank(square)][Square.getFile(square)]);
        }
        pieceList[color.ordinal()][Utils.findIndexOf(square, pieceList[color.ordinal()])] = Square.NULL;
    }

    public static void moveInPieceList(Color color, Square from, Square to) {
        pieceList[color.ordinal()][Utils.findIndexOf(from, pieceList[color.ordinal()])] = to;
    }

    public static void moveInPieceList(Color color, Piece piece, Square from, Square to) {
        pieceBonus[color.ordinal()] -=
                Score.get(Psqt.table[color.ordinal()][piece.id][Square.getRank(from)][Square.getFile(from)]);
        pieceBonus[color.ordinal()] +=
                Score.get(Psqt.table[color.ordinal()][piece.id][Square.getRank(to)][Square.getFile(to)]);

        pieceList[color.ordinal()][Utils.findIndexOf(from, pieceList[color.ordinal()])] = to;
    }

    public static int encodePrevState() {
        return (((halfmoves << 12) | castleRights << 8) | enPassant.idx << 1) | phase.ordinal();
    }

    private static void resetState() {
        board = new int[128];

        phase = Phase.MG;
        enPassant = Square.NULL;
        halfmoves = 0;

        Arrays.fill(pieceList[Color.W.ordinal()],
                Square.NULL);
        Arrays.fill(pieceList[Color.B.ordinal()],
                Square.NULL);

        pieceCount[Color.W.ordinal()] = 0;
        pieceCount[Color.B.ordinal()] = 0;

        pawnCount[Color.W.ordinal()] = 0;
        pawnCount[Color.B.ordinal()] = 0;

        pieceBonus[Color.W.ordinal()] = 0;
        pieceBonus[Color.B.ordinal()] = 0;

        totalRepititions.clear();
    }

    public static void loadFen(String fen) {
        resetState();

        Map<Character, Piece> pieceMap = new HashMap<>();
        pieceMap.put('k',
                Piece.KING);
        pieceMap.put('q',
                Piece.QUEEN);
        pieceMap.put('r',
                Piece.ROOK);
        pieceMap.put('n',
                Piece.KNIGHT);
        pieceMap.put('b',
                Piece.BISHOP);
        pieceMap.put('p',
                Piece.PAWN);

        Map<Character, Integer> castleMap = new HashMap<>();
        castleMap.put('K',
                8);
        castleMap.put('Q',
                4);
        castleMap.put('k',
                2);
        castleMap.put('q',
                1);

        String[] fenState = fen.split(" ");

        String fenBoard = fenState[0];
        int rank = 7, file = 0;

        for (int i = 0; i < fenBoard.length(); i++) {
            char c = fenBoard.charAt(i);

            if (c == '/') {
                file = 0;
                rank--;
                continue;
            }

            if (Character.isDigit(c)) {
                file += Character.getNumericValue(c);
                continue;
            }

            // c represents a piece
            char type = Character.toLowerCase(c);
            Color color = type == c ?
                    Color.B :
                    Color.W;
            int idx = rank * 16 + file;
            Piece piece = pieceMap.get(type);
            board[idx] = color.id | piece.id;
            putPiece(color,
                    piece,
                    Square.lookup.get(idx), false);
            file++;
        }

        String fenCastle = fenState[2];
        castleRights = 0;
        if (!fenCastle.equals("-")) {
            for (int i = 0; i < fenCastle.length(); i++) {
                castleRights |= castleMap.get(fenCastle.charAt(i));
            }
        }

        activeColor = Objects.equals(fenState[1],
                "w") ?
                Color.W :
                Color.B;

        enPassant = !Objects.equals(fenState[3],
                "-") ?
                Square.valueOf(fenState[3].toUpperCase()) :
                Square.NULL;

        halfmoves = Integer.parseInt(fenState[4]);
        zobristHash = ZobristKey.hash();
        totalRepititions.put(zobristHash, 1);
    }

    public static boolean inCheck(Color color) {
        Color oppColor = Color.getOppColor(color);
        return MoveGen.isAttacked(pieceList[color.ordinal()][50],
                oppColor,
                pieceList[oppColor.ordinal()]
        );
    }

    public static boolean isCheckmate(Color color) {
        return inCheck(color) && getValidMoves(color, false, false).size() == 0;
    }

    public static int encodeMove(Square from, Square to) {
        return (from.idx << 7) | to.idx;
    }

    public static int encodeMove(Square from, Square to, Castle castle) {
        return ((castle.value << 14) | from.idx << 7) | to.idx;
    }

    public static int encodeMove(Square from, Square to, Piece promote) {
        return (promote.id << 18 | from.idx << 7) | to.idx;
    }

    public static boolean isMoveValid(int move) {
        Castle castle = Castle.lookup.get((move >> 14) & 15);
        Square from = Square.lookup.get((move >> 7) & 127);
        Square to = Square.lookup.get(move & 127);

        Color color = Color.extractColor(board[from.idx]);
        assert color != null;

        Piece pieceType = Piece.extractPieceType(board[from.idx]);
        Square kingPos = pieceList[color.ordinal()][50];
        Color oppColor = Color.getOppColor(color);

        switch (pieceType) {
            case PAWN -> {
                if (!MoveGen.pseudoLegalForPawn(from, color)
                            .contains(move)) return false;
            }
            case KING -> {
                if (!MoveGen.pseudoLegalForKing(from, color, pieceList[oppColor.ordinal()])
                            .contains(move)) return false;
            }
            default -> {
                if (!MoveGen.pseudoLegal(from, pieceType, color)
                            .contains(move)) return false;
            }
        }
        boolean valid;

        if (castle != null) {
            if (Utils.findIndexOf(castle.rInitSquare, pieceList[color.ordinal()]) == -1) {
                Utils.printMove(move);
                printBoard();
            }
            board[castle.square.idx] = board[from.idx];
            board[from.idx] = 0;
            board[castle.rSquare.idx] = board[castle.rInitSquare.idx];
            board[castle.rInitSquare.idx] = 0;

            valid = !MoveGen.isAttacked(to,
                    oppColor,
                    pieceList[oppColor.ordinal()]);

            board[from.idx] = board[castle.square.idx];
            board[castle.square.idx] = 0;
            board[castle.rInitSquare.idx] = board[castle.rSquare.idx];
            board[castle.rSquare.idx] = 0;
        } else if (pieceType == Piece.PAWN && to == enPassant) {

            Square enPassantCaptureSquare = Square.lookup.get(
                    to.idx + (color == Color.W ? Vector.DOWN.offset :
                            Vector.UP.offset)
            );

            board[to.idx] = board[from.idx];
            board[from.idx] = 0;
            board[enPassantCaptureSquare.idx] = 0;

            valid = !inCheck(color);

            board[from.idx] = color.id | Piece.PAWN.id;
            board[to.idx] = 0;
            board[enPassantCaptureSquare.idx] = oppColor.id | Piece.PAWN.id;
        } else {

            int capturedPiece = board[to.idx];
            board[to.idx] = board[from.idx];
            board[from.idx] = 0;

            valid = !MoveGen.isAttacked(pieceType == Piece.KING ? to : kingPos,
                    oppColor,
                    pieceList[oppColor.ordinal()]);

            board[from.idx] = color.id | pieceType.id;
            board[to.idx] = capturedPiece;
        }

        return valid;
    }

    public static List<Integer> getValidMoves(Color color, boolean onlyCaptures, boolean onlyChecks) {
        Square[] list = pieceList[color.ordinal()];

        List<Integer> moves = new ArrayList<>();

        for (int idx = 0; idx < list.length; idx++) {
            if (list[idx] == Square.NULL) continue;

            if (idx == 50) {
                moves.addAll(MoveGen.pseudoLegalForKing(list[50], color, pieceList[Color.getOppColor(color)
                                                                                        .ordinal()]));
            } else if (idx < 10) {
                moves.addAll(
                        MoveGen.pseudoLegalForPawn(list[idx], color));
            } else {
                moves.addAll(MoveGen.pseudoLegal(list[idx], Piece.lookup.get(
                        (int) Math.floor((float) idx / 10) + 1
                ), color));
            }
        }
        filterOutValidMoves(moves, onlyCaptures, onlyChecks);
        return moves;
    }

    public static void filterOutValidMoves(List<Integer> moves, boolean onlyCaptures, boolean onlyChecks) {
        Iterator<Integer> moveIterator = moves.iterator();

        while (moveIterator.hasNext()) {
            int move = moveIterator.next();
            Castle castle = Castle.lookup.get((move >> 14) & 15);
            Square from = Square.lookup.get((move >> 7) & 127);
            Square to = Square.lookup.get(move & 127);

            Color color = Color.extractColor(board[from.idx]);
            assert color != null;

            Piece pieceType = Piece.extractPieceType(board[from.idx]);

            boolean valid;
            boolean capture = board[to.idx] != 0 || (pieceType == Piece.PAWN && to == enPassant);
            boolean check = false;

            Square kingPos = pieceList[color.ordinal()][50];
            Color oppColor = Color.getOppColor(color);

            if (castle != null) {
                if (Utils.findIndexOf(castle.rInitSquare, pieceList[color.ordinal()]) == -1) {
                    Utils.printMove(move);
                    printBoard();
                }
                board[castle.square.idx] = board[from.idx];
                board[from.idx] = 0;
                board[castle.rSquare.idx] = board[castle.rInitSquare.idx];
                board[castle.rInitSquare.idx] = 0;

                valid = !MoveGen.isAttacked(to,
                        oppColor,
                        pieceList[oppColor.ordinal()]);
                if (onlyChecks) {
                    pieceList[activeColor.ordinal()][50] = castle.square;

                    moveInPieceList(activeColor, castle.rInitSquare, castle.rSquare);

                    check = inCheck(oppColor);

                    pieceList[activeColor.ordinal()][50] = from;
                    moveInPieceList(activeColor, castle.rSquare, castle.rInitSquare);
                }

                board[from.idx] = board[castle.square.idx];
                board[castle.square.idx] = 0;
                board[castle.rInitSquare.idx] = board[castle.rSquare.idx];
                board[castle.rSquare.idx] = 0;
            } else if (pieceType == Piece.PAWN && to == enPassant) {

                Square enPassantCaptureSquare = Square.lookup.get(
                        to.idx + (color == Color.W ? Vector.DOWN.offset :
                                Vector.UP.offset)
                );

                board[to.idx] = board[from.idx];
                board[from.idx] = 0;
                board[enPassantCaptureSquare.idx] = 0;

                valid = !inCheck(color);
                if (onlyChecks) {
                    moveInPieceList(color, from, to);
                    removePiece(oppColor, Piece.PAWN, enPassantCaptureSquare, true);

                    check = !inCheck(oppColor);

                    moveInPieceList(color, to, from);
                    putPiece(oppColor, Piece.PAWN, enPassantCaptureSquare, true);
                }

                board[from.idx] = color.id | Piece.PAWN.id;
                board[to.idx] = 0;
                board[enPassantCaptureSquare.idx] = oppColor.id | Piece.PAWN.id;
            } else {

                int capturedPiece = board[to.idx];
                board[to.idx] = board[from.idx];
                board[from.idx] = 0;

                valid = !MoveGen.isAttacked(pieceType == Piece.KING ? to : kingPos,
                        oppColor,
                        pieceList[oppColor.ordinal()]);
                if (onlyChecks) {
                    int promote = move >> 18;
                    if (promote != 0) {
                        board[to.idx] = color.id | promote;

                        removePiece(color, Piece.PAWN, from, true);
                        putPiece(color, Piece.extractPieceType(promote), to, true);
                    } else moveInPieceList(color, from, to);
                    if (capturedPiece != 0) removePiece(oppColor, Piece.extractPieceType(capturedPiece),
                            to, true);

                    check = inCheck(oppColor);

                    if (promote != 0) {
                        removePiece(color, Piece.extractPieceType(board[to.idx]), to, true);
                        putPiece(color, Piece.PAWN, from, true);
                    } else moveInPieceList(color, to, from);

                    if (capturedPiece != 0) putPiece(oppColor, Piece.extractPieceType(capturedPiece),
                            to, true);
                }

                board[from.idx] = color.id | pieceType.id;
                board[to.idx] = capturedPiece;
            }

            if (!valid || ((onlyCaptures && !capture) && (onlyChecks && !check))) moveIterator.remove();
        }
    }

    private static void checkPhase() {
        if (phase == Phase.MG && pieceCount[Color.W.ordinal()] + pieceCount[Color.B.ordinal()] <= Phase.EG.limit) {
            phase = Phase.EG;
            recalibrateCount();
        }
    }

    private static void recalibrateCount() {
        pawnCount[Color.W.ordinal()] = 0;
        pawnCount[Color.B.ordinal()] = 0;

        pieceCount[Color.W.ordinal()] = 0;
        pieceCount[Color.B.ordinal()] = 0;

        // iterate over every piece type execept the king
        for (int i = 0; i < pieceList[Color.W.ordinal()].length - 1; i++) {
            int pieceId = (i / 10) + 1;
            Piece piece = Piece.lookup.get(pieceId);
            int[] countMap;

            if (pieceId == 1) {
                countMap = pawnCount;
            } else countMap = pieceCount;

            if (pieceList[Color.W.ordinal()][i] != Square.NULL)
                countMap[Color.W.ordinal()] += Score.get(piece.value);
            if (pieceList[Color.B.ordinal()][i] != Square.NULL)
                countMap[Color.B.ordinal()] += Score.get(piece.value);
        }
    }

    public static int makeMove(Integer move) {
        // assume to square is a valid pseudo legal move

        /*
        move is 21 bit int
        first 7 bits refer to the "to" square idx
        next 7 bits refer to the "from" square idx
        next 4 bits refer to castling
        next 3 bits refer to promotion piece type
         */
        Castle castle = Castle.lookup.get((move >> 14) & 15);
        Square from = Square.lookup.get((move >> 7) & 127);
        Square to = Square.lookup.get(move & 127);
        int promote = move >> 18;

        int capturedPiece = 0;
        Square capturePieceSquare = null;

        Color oppColor = Color.getOppColor(activeColor);

        Piece pieceType = Piece.extractPieceType(board[from.idx]);

        if (castle != null) {
            board[castle.square.idx] = board[from.idx];
            board[from.idx] = 0;
            board[castle.rSquare.idx] = board[castle.rInitSquare.idx];
            board[castle.rInitSquare.idx] = 0;

            moveInPieceList(activeColor, Piece.KING, from, castle.square);
            moveInPieceList(activeColor, Piece.ROOK, castle.rInitSquare, castle.rSquare);

            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.KING.id - 1][from.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.ROOK.id - 1][castle.rInitSquare.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.KING.id - 1][castle.square.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.ROOK.id - 1][castle.rSquare.idx];
        } else if (pieceType == Piece.PAWN && to == enPassant) {
            Square enPassantCaptureSquare = Square.lookup.get(
                    to.idx + (activeColor == Color.W ? Vector.DOWN.offset :
                            Vector.UP.offset)
            );
            capturedPiece = board[enPassantCaptureSquare.idx];

            board[to.idx] = board[from.idx];
            board[from.idx] = 0;
            board[enPassantCaptureSquare.idx] = 0;

            capturePieceSquare = enPassantCaptureSquare;
            // capture is handled later

            moveInPieceList(activeColor, Piece.PAWN, from, to);

            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.PAWN.id - 1][from.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.PAWN.id - 1][to.idx];
        } else {
            if (board[to.idx] != 0) {
                capturedPiece = board[to.idx];
                capturePieceSquare = to;
            }

            board[to.idx] = board[from.idx];
            board[from.idx] = 0;

            if (promote != 0) {
                board[to.idx] = activeColor.id | promote;

                removePiece(activeColor, Piece.PAWN, from, false);
                putPiece(activeColor, Piece.extractPieceType(promote), to, false);

                zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][0][from.idx];
                zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][promote - 1][to.idx];
            } else {
                moveInPieceList(activeColor, pieceType, from, to);

                zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][pieceType.id - 1][from.idx];
                zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][pieceType.id - 1][to.idx];
            }
        }

        if (capturedPiece != 0) {
            if (Utils.findIndexOf(capturePieceSquare, pieceList[oppColor.ordinal()]) == -1) {
                System.out.println(Arrays.toString(board));
                GameState.printBoard();
            }
            removePiece(oppColor, Piece.extractPieceType(capturedPiece), capturePieceSquare, false);

            zobristHash ^= ZobristKey.PIECES[oppColor.ordinal()][(capturedPiece & 7) -
                    1][capturePieceSquare.idx];
        }

        if (enPassant != Square.NULL) zobristHash ^= ZobristKey.EN_PASSANT[enPassant.idx];
        enPassant = Square.NULL;

        if (pieceType == Piece.KING && castleRights != 0) {
            switch (activeColor) {
                case W -> {
                    zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];

                    if ((castleRights & Castle.W_K.value) != 0) castleRights ^= Castle.W_K.value;
                    if ((castleRights & Castle.W_Q.value) != 0) castleRights ^= Castle.W_Q.value;
                }

                case B -> {
                    zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];

                    if ((castleRights & Castle.B_k.value) != 0) castleRights ^= Castle.B_k.value;
                    if ((castleRights & Castle.B_q.value) != 0) castleRights ^= Castle.B_q.value;
                }
            }

            zobristHash ^= ZobristKey.CASTLING_RIGHTS[activeColor.ordinal()][0];
        } else {
            if (pieceType == Piece.PAWN &&
                    Math.abs(from.idx - to.idx) == 2 * (Vector.UP.offset)) {
                enPassant = Square.lookup.get(
                        from.idx + (activeColor == Color.W ? Vector.UP.offset :
                                Vector.DOWN.offset));
                zobristHash ^= ZobristKey.EN_PASSANT[enPassant.idx];
            }
        }
        // checking if rooks moved or have been captured to see if I need to toggle castleRights
        if ((castleRights & Castle.W_Q.value) != 0 &&
                (board[Square.A1.idx] ^ (Color.W.id | Piece.ROOK.id)) != 0) {
            zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
            castleRights ^= Castle.W_Q.value;
            zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
        }
        if ((castleRights & Castle.W_K.value) != 0 && (board[Square.H1.idx] ^ (Color.W.id | Piece.ROOK.id)) != 0) {
            zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
            castleRights ^= Castle.W_K.value;
            zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
        }
        if ((castleRights & Castle.B_q.value) != 0 && (board[Square.A8.idx] ^ (Color.B.id | Piece.ROOK.id)) != 0) {
            zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
            castleRights ^= Castle.B_q.value;
            zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
        }
        if ((castleRights & Castle.B_k.value) != 0 && (board[Square.H8.idx] ^ (Color.B.id | Piece.ROOK.id)) != 0) {
            zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
            castleRights ^= Castle.B_k.value;
            zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
        }

        if (capturedPiece == 0 && pieceType != Piece.PAWN) halfmoves++;
        else halfmoves = 0;

        activeColor = Color.getOppColor(activeColor);
        if (activeColor == Color.B) zobristHash ^= ZobristKey.SIDE;

        totalRepititions.merge(zobristHash, 1, Integer::sum);

        checkPhase();

        return capturePieceSquare != null ? (capturePieceSquare.idx << 5) | capturedPiece : 0;
    }

    public static void unmakeMove(int move, int prevState, int captureDetails) {

        Castle castle = Castle.lookup.get((move >> 14) & 15);
        Square from = Square.lookup.get((move >> 7) & 127);
        Square to = Square.lookup.get(move & 127);
        int promote = move >> 18;

        Phase prevPhase = (prevState & 1) == 0 ? Phase.MG : Phase.EG;
        if (prevPhase != phase) recalibrateCount();
        Square prevEnPassant = Square.lookup.get((prevState >> 1) & 127);
        int prevCastleRights = (prevState >> 8) & 15;
        int prevHalfmoves = prevState >> 12;

        totalRepititions.merge(zobristHash, -1, Integer::sum);
        if (totalRepititions.get(zobristHash) == 0) totalRepititions.remove(zobristHash);

        if (enPassant != Square.NULL) zobristHash ^= ZobristKey.EN_PASSANT[enPassant.idx];
        enPassant = prevEnPassant;
        if (enPassant != Square.NULL) zobristHash ^= ZobristKey.EN_PASSANT[enPassant.idx];

        if (activeColor == Color.B) zobristHash ^= ZobristKey.SIDE;
        activeColor = Color.getOppColor(activeColor);

        if (Utils.findIndexOf(to, pieceList[activeColor.ordinal()]) == -1) {
            Utils.printMove((from.idx << 7) | to.idx);
            printBoard();
            printState();
        }

        zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
        zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
        castleRights = prevCastleRights;
        zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
        zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];

        halfmoves = prevHalfmoves;

        Color oppColor = Color.getOppColor(activeColor);

        if (castle != null) {
            board[from.idx] = board[castle.square.idx];
            board[castle.square.idx] = 0;
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.KING.id - 1][castle.square.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.KING.id - 1][from.idx];

            board[castle.rInitSquare.idx] = board[castle.rSquare.idx];
            board[castle.rSquare.idx] = 0;
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.ROOK.id - 1][castle.rInitSquare.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.ROOK.id - 1][castle.rSquare.idx];

            moveInPieceList(activeColor, Piece.ROOK, castle.rSquare, castle.rInitSquare);
            moveInPieceList(activeColor, Piece.KING, castle.square, from);
            return;
        }

        if (promote != 0) {
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][(board[to.idx] & 7) - 1][to.idx];
            removePiece(activeColor, Piece.extractPieceType(board[to.idx]), to, false);
            board[to.idx] = 0;

            board[from.idx] = activeColor.id | Piece.PAWN.id;
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][0][from.idx];
            putPiece(activeColor, Piece.PAWN, from, false);
        } else {
            board[from.idx] = board[to.idx];
            board[to.idx] = 0;
            Piece pieceType = Piece.extractPieceType(board[from.idx]);
            moveInPieceList(activeColor, pieceType, to, from);

            int pieceId = board[from.idx] & 7;
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][pieceId - 1][to.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][pieceId - 1][from.idx];
        }

        int capturedPiece = captureDetails & 31;
        if (capturedPiece != 0) {
            Square capturePieceSquare = Square.lookup.get(captureDetails >> 5);
            board[capturePieceSquare.idx] = capturedPiece;
            putPiece(oppColor, Piece.extractPieceType(capturedPiece),
                    capturePieceSquare, false);

            zobristHash ^=
                    ZobristKey.PIECES[oppColor.ordinal()][(capturedPiece & 7) - 1][capturePieceSquare.idx];
        }
    }

    public static String createMoveNotation(int move, int captureDetails, boolean checkmate) {
        Castle castle = Castle.lookup.get((move >> 14) & 15);
        Square from = Square.lookup.get((move >> 7) & 127);
        String fromNotation = from.name()
                                  .toLowerCase();
        Square to = Square.lookup.get(move & 127);
        String toNotation = to.name()
                              .toLowerCase();
        Piece promote = Piece.extractPieceType(move >> 18);
        Piece pieceType = Piece.extractPieceType(board[to.idx]);
        char pieceNotation = pieceType != Piece.KNIGHT ? pieceType.name()
                                                                  .charAt(0) : 'N';
        Color color = Color.extractColor(board[to.idx]);

        int capturedPiece = captureDetails & 31;
        boolean capture = capturedPiece != 0;

        String notation = "";

        if (castle != null) {
            switch (castle) {
                case B_k, W_K -> notation = "0-0";
                case B_q, W_Q -> notation = "0-0-0";
            }
        } else if (promote != Piece.NULL) {
            String promoteNotation = promote == Piece.KNIGHT ? "=N" : "=" + promote.name()
                                                                                   .charAt(0);
            notation = capture ? fromNotation.charAt(0) + "x" + toNotation + promoteNotation :
                    toNotation + promoteNotation;
        } else {
            switch (pieceType) {
                case PAWN -> {
                    notation = capture ? fromNotation.charAt(0) + "x" + toNotation : toNotation;
                }

                case KING -> {
                    notation = capture ? pieceNotation + "x" + toNotation : pieceNotation + toNotation;
                }

                default -> {
                    int pieceId = pieceType.id;
                    Square[] pList = pieceList[color.ordinal()];
                    board[to.idx] = 0;
                    board[from.idx] = color.id | pieceType.id;
                    moveInPieceList(color, to, from);

                    List<Square> list = new ArrayList<>();
                    // see how many pieces of the same piece type can move to the "to" square
                    for (int i = 0; i < 10; i++) {
                        int pieceListIdx = ((pieceId - 1) * 10) + i;
                        Square square = pList[pieceListIdx];
                        if (square == Square.NULL || square == from) continue;
                        // if another piece can go to the same square
                        if (isMoveValid(encodeMove(square, to))) {
                            list.add(square);
                        }
                    }

                    moveInPieceList(color, from, to);
                    board[to.idx] = color.id | pieceType.id;
                    board[from.idx] = 0;

                    if (list.size() == 2) notation = capture ? pieceNotation + fromNotation + "x" + toNotation :
                            pieceNotation + fromNotation + toNotation;
                    else if (list.size() == 1) {
                        // need to check if the piece is on the same rank or file as the move that moved
                        // if same rank, differentiate the move using the file and vice-versa for rank
                        char differentation;
                        if (Square.getFile(from) != Square.getFile(list.get(0)))
                            differentation = fromNotation.charAt(0);
                        else differentation = fromNotation.charAt(1);

                        notation = capture ? "" + pieceNotation + differentation + "x" + toNotation :
                                "" + pieceNotation + differentation + toNotation;
                    } else {
                        notation = capture ? pieceNotation + "x" + toNotation :
                                pieceNotation + toNotation;
                    }
                }
            }
        }

        if (checkmate) notation += "#";
        else if (inCheck(Color.getOppColor(color))) notation += "+";

        return notation;
    }

    public static boolean isDrawByInsufficientMaterial() {
        int wBishopCount = 0;
        Color wBishopColor = null;
        int bBishopCount = 0;
        Color bBishopColor = null;
        int wKnightCount = 0;
        int bKnightCount = 0;

        for (int i = 0; i < pieceList[activeColor.ordinal()].length; i++) {
            int pieceId = (i / 10) + 1;
            Piece pieceType = Piece.lookup.get(pieceId);

            switch (pieceType) {
                case BISHOP -> {
                    if (pieceList[Color.W.ordinal()][i] != Square.NULL) {
                        if (bKnightCount != 0) return false;
                        if (wBishopCount == 1) return false;
                        wBishopCount++;

                        wBishopColor = Square.getColor(pieceList[Color.W.ordinal()][i]);
                        if (wBishopColor == bBishopColor) return false;
                    }
                    if (pieceList[Color.B.ordinal()][i] != Square.NULL) {
                        if (wKnightCount != 0) return false;
                        if (bBishopCount == 1) return false;
                        bBishopCount++;

                        bBishopColor = Square.getColor(pieceList[Color.B.ordinal()][i]);
                        if (wBishopColor == bBishopColor) return false;
                    }
                }

                case KNIGHT -> {
                    if (pieceList[Color.W.ordinal()][i] != Square.NULL) {
                        wKnightCount++;
                    }
                    if (pieceList[Color.B.ordinal()][i] != Square.NULL) {
                        bKnightCount++;
                    }
                    if (wKnightCount + bKnightCount == 2) return false;
                }

                case KING -> {
                }

                default -> {
                    if (pieceList[Color.W.ordinal()][i] != Square.NULL ||
                            pieceList[Color.B.ordinal()][i] != Square.NULL) return false;
                }
            }
        }

        return true;
    }

    public static boolean isDrawByRepitition(long mostRecentHash, boolean forced) {
        return forced ? totalRepititions.get(mostRecentHash) == 5 : totalRepititions.get(mostRecentHash) == 3;
    }

    public static boolean isDrawByMoveRule(boolean forced) {
        int MAX_MOVES = forced ? 150 : 100;
        return halfmoves == MAX_MOVES;
    }

    // for engine use
    public static boolean isDraw() {
        return isDrawByMoveRule(false) || isDrawByRepitition(GameState.zobristHash, false) ||
                isDrawByInsufficientMaterial();
    }

    // for vali
    public static boolean isUnforcedDraw() {
        return isDrawByMoveRule(false) || isDrawByRepitition(GameState.zobristHash, false);
    }

    public static boolean isForcedDraw() {
        return isDrawByMoveRule(false) || isDrawByRepitition(GameState.zobristHash, false) ||
                isDrawByInsufficientMaterial();
    }

    public static int countNumOfPositions(int depth, boolean print) {

        if (depth == 0) return 1;
        int count = 0;
        List<Integer> moves = getValidMoves(activeColor, false, false);
        for (Integer move : moves) {
            int moveCount = 0;
            int prevState = ((halfmoves << 11) | castleRights << 7) | enPassant.idx;
            int captureDetails = makeMove(move);

            moveCount += countNumOfPositions(depth - 1);
            count += moveCount;
            if (print) {
                System.out.printf("%s%s: %d; depth: %d %n", Square.lookup.get((move >> 7) & 127),
                        Square.lookup.get(move & 127),
                        moveCount, depth);
            }
            unmakeMove(move, prevState, captureDetails);
        }
        return count;
    }

    public static int countNumOfPositions(int depth) {
        if (depth == 0) return 1;
        int count = 0;
        List<Integer> moves = getValidMoves(activeColor, false, false);
        for (Integer move : moves) {
            int prevState = ((halfmoves << 11) | castleRights << 7) | enPassant.idx;
            int captureDetails = makeMove(move);

            count += countNumOfPositions(depth - 1);

            unmakeMove(move, prevState, captureDetails);
        }
        return count;
    }
}
