package main;

import main.moveGen.MoveGen;
import main.moveGen.UnmakeDetails;
import main.moveGen.Vector;
import main.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

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

    public static Map<Color, Square[]> pieceList = new HashMap<>();
    public static Map<Color, Integer> pieceCount = new HashMap<>();
    public static long zobristHash;
    public static Map<Long, Integer> totalRepititions = new HashMap<>();

    public static Color activeColor = Color.W;
    public static Square enPassant = Square.NULL;
    public static int halfmoves;
    public static int castleRights =
            Castle.W_K.value | Castle.W_Q.value | Castle.B_k.value | Castle.B_q.value;

    // init board + pieceList + get zobrish hash
    static {
        pieceList.put(Color.W,
                new Square[51]);
        pieceList.put(Color.B,
                new Square[51]);
        pieceCount.put(Color.W, 0);
        pieceCount.put(Color.B, 0);

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

        final Square[] wPieceList = pieceList.get(Color.W);
        Arrays.fill(wPieceList,
                Square.NULL);
        putPiece(Color.W, Piece.PAWN, Square.A2);
        putPiece(Color.W, Piece.PAWN, Square.B2);
        putPiece(Color.W, Piece.PAWN, Square.C2);
        putPiece(Color.W, Piece.PAWN, Square.D2);
        putPiece(Color.W, Piece.PAWN, Square.E2);
        putPiece(Color.W, Piece.PAWN, Square.F2);
        putPiece(Color.W, Piece.PAWN, Square.G2);
        putPiece(Color.W, Piece.PAWN, Square.H2);
        putPiece(Color.W, Piece.ROOK, Square.A1);
        putPiece(Color.W, Piece.KNIGHT, Square.B1);
        putPiece(Color.W, Piece.BISHOP, Square.C1);
        putPiece(Color.W, Piece.QUEEN, Square.D1);
        putPiece(Color.W, Piece.KING, Square.E1);
        putPiece(Color.W, Piece.BISHOP, Square.F1);
        putPiece(Color.W, Piece.KNIGHT, Square.G1);
        putPiece(Color.W, Piece.ROOK, Square.H1);

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

        final Square[] bPieceList = pieceList.get(Color.B);
        Arrays.fill(bPieceList,
                Square.NULL);
        putPiece(Color.B, Piece.PAWN, Square.A7);
        putPiece(Color.B, Piece.PAWN, Square.B7);
        putPiece(Color.B, Piece.PAWN, Square.C7);
        putPiece(Color.B, Piece.PAWN, Square.D7);
        putPiece(Color.B, Piece.PAWN, Square.E7);
        putPiece(Color.B, Piece.PAWN, Square.F7);
        putPiece(Color.B, Piece.PAWN, Square.G7);
        putPiece(Color.B, Piece.PAWN, Square.H7);
        putPiece(Color.B, Piece.ROOK, Square.A8);
        putPiece(Color.B, Piece.KNIGHT, Square.B8);
        putPiece(Color.B, Piece.BISHOP, Square.C8);
        putPiece(Color.B, Piece.QUEEN, Square.D8);
        putPiece(Color.B, Piece.KING, Square.E8);
        putPiece(Color.B, Piece.BISHOP, Square.F8);
        putPiece(Color.B, Piece.KNIGHT, Square.G8);
        putPiece(Color.B, Piece.ROOK, Square.H8);

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
        System.out.println(pieceCount);
        System.out.println(Arrays.toString(pieceList.get(Color.W)));
        System.out.println(Arrays.toString(pieceList.get(Color.B)));
        System.out.println(castleRights);
        System.out.println(enPassant);
    }

    private static void putPiece(Color color,
                                 Piece piece,
                                 Square square) {
        pieceCount.merge(color, piece.value, Integer::sum);

        Square[] list = pieceList.get(color);

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

    private static void removePiece(Color color, Piece piece, Square square) {
        pieceCount.merge(color, -piece.value, Integer::sum);

        pieceList.get(color)[Utils.findIndexOf(square, pieceList.get(color))] = Square.NULL;
    }

    private static void moveInPieceList(Color color, Square from, Square to) {
        pieceList.get(color)[Utils.findIndexOf(from,
                pieceList.get(color))] = to;
    }

    private static void resetState() {
        board = new int[128];

        Arrays.fill(pieceList.get(Color.W),
                Square.NULL);
        Arrays.fill(pieceList.get(Color.B),
                Square.NULL);

        pieceCount.put(Color.W, 0);
        pieceCount.put(Color.B, 0);
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
                    Square.lookup.get(idx));
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
    }

    public static boolean inCheck(Color color) {

        Color oppColor = Color.getOppColor(color);
        return MoveGen.isAttacked(pieceList.get(color)[50],
                oppColor,
                pieceList.get(oppColor),
                board);
    }

    public static boolean isCheckmate(Color color) {
        if (inCheck(color)) return getValidMoves(color, false, false).size() == 0;
        return false;
    }

    public static List<Integer> getValidMoves(Color color, boolean onlyCaptures, boolean onlyChecks) {
        Square[] list = pieceList.get(color);

        List<Integer> moves = new ArrayList<>();

        for (int idx = 0; idx < list.length; idx++) {
            if (list[idx] == Square.NULL) continue;

            if (idx == 50) {
                moves.addAll(MoveGen.pseudoLegalForKing(board, list[50], color, castleRights,
                        pieceList.get(Color.getOppColor(color))));
            } else if (idx < 10) {
                moves.addAll(
                        MoveGen.pseudoLegalForPawn(board, list[idx], color, enPassant));
            } else {
                moves.addAll(MoveGen.pseudoLegal(board, list[idx], Piece.lookup.get(
                        (int) Math.floor((float) idx / 10) + 1
                ), color));
            }
        }
        return filterOutValidMoves(moves, onlyCaptures, onlyChecks);
    }

    public static List<Integer> filterOutValidMoves(List<Integer> moves, boolean onlyCaptures, boolean onlyChecks) {
        return moves.stream()
                    .filter((move) -> {
                        Castle castle = Castle.lookup.get((move >> 14) & 15);
                        Square from = Square.lookup.get((move >> 7) & 127);
                        Square to = Square.lookup.get(move & 127);

                        Color color = Color.extractColor(board[from.idx]);
                        assert color != null;

                        Piece pieceType = Piece.extractPieceType(board[from.idx]);

                        if (onlyCaptures && !onlyChecks) {
                            if (board[to.idx] == 0 || (pieceType == Piece.PAWN && to != enPassant)) return false;
                        }

                        Square kingPos = pieceList.get(color)[50];
                        Color oppColor = Color.getOppColor(color);

                        if (castle != null) {
                            board[castle.square.idx] = board[from.idx];
                            board[from.idx] = 0;
                            board[castle.rSquare.idx] = board[castle.rInitSquare.idx];
                            board[castle.rInitSquare.idx] = 0;

                            boolean valid = !MoveGen.isAttacked(to,
                                    oppColor,
                                    pieceList.get(oppColor),
                                    board);
                            if (onlyChecks && !inCheck(oppColor)) {
                                pieceList.get(activeColor)[50] = castle.square;
                                moveInPieceList(activeColor, castle.rInitSquare, castle.rSquare);

                                valid = false;

                                pieceList.get(activeColor)[50] = from;
                                moveInPieceList(activeColor, castle.rSquare, castle.rInitSquare);
                            }

                            board[from.idx] = board[castle.square.idx];
                            board[castle.square.idx] = 0;
                            board[castle.rInitSquare.idx] = board[castle.rSquare.idx];
                            board[castle.rSquare.idx] = 0;

                            return valid;
                        } else if (pieceType == Piece.PAWN && to == enPassant) {

                            Square enPassantCaptureSquare = Square.lookup.get(
                                    to.idx + (color == Color.W ? main.moveGen.Vector.DOWN.offset :
                                            main.moveGen.Vector.UP.offset)
                            );

                            board[to.idx] = board[from.idx];
                            board[from.idx] = 0;
                            board[enPassantCaptureSquare.idx] = 0;

                            boolean valid = !inCheck(color);
                            if (onlyChecks) {
                                moveInPieceList(color, from, to);
                                removePiece(oppColor, Piece.PAWN, enPassantCaptureSquare);

                                if (!inCheck(oppColor)) valid = false;

                                moveInPieceList(color, to, from);
                                putPiece(oppColor, Piece.PAWN, enPassantCaptureSquare);
                            }

                            board[from.idx] = color.id | Piece.PAWN.id;
                            board[to.idx] = 0;
                            board[enPassantCaptureSquare.idx] = oppColor.id | Piece.PAWN.id;

                            return valid;
                        } else {

                            int capturedPiece = board[to.idx];
                            board[to.idx] = board[from.idx];
                            board[from.idx] = 0;

                            boolean valid = !MoveGen.isAttacked(pieceType == Piece.KING ? to : kingPos,
                                    oppColor,
                                    pieceList.get(oppColor),
                                    board);
                            if (onlyChecks) {
                                int promote = move >> 18;
                                if (promote != 0) {
                                    board[to.idx] = color.id | promote;

                                    removePiece(color, Piece.PAWN, from);
                                    putPiece(color, Piece.extractPieceType(promote), to);
                                } else {
                                    if (capturedPiece != 0) removePiece(oppColor, Piece.extractPieceType(capturedPiece),
                                            to);
                                    moveInPieceList(color, from, to);
                                }
                                if (onlyCaptures) {
                                    if (capturedPiece == 0 && !inCheck(oppColor)) valid = false;
                                } else if (!inCheck(oppColor)) valid = false;

                                if (promote != 0) {
                                    removePiece(color, Piece.extractPieceType(board[to.idx]), to);
                                    putPiece(color, Piece.PAWN, from);
                                } else {
                                    if (capturedPiece != 0) putPiece(oppColor, Piece.extractPieceType(capturedPiece),
                                            to);
                                    moveInPieceList(color, to, from);
                                }
                            }

                            board[from.idx] = color.id | pieceType.id;
                            board[to.idx] = capturedPiece;

                            return valid;
                        }
                    })
                    .collect(Collectors.toList());
    }


    public static void makeMove(Integer move, UnmakeDetails moveDetails) {
        moveDetails.prevCastleRights = castleRights;
        moveDetails.prevEnPassant = enPassant;
        moveDetails.prevHalfmoves = halfmoves;

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

        moveDetails.from = from;
        moveDetails.to = to;

        Color oppColor = Color.getOppColor(activeColor);

        Piece pieceType = Piece.extractPieceType(board[from.idx]);

        if (castle != null) {
            board[castle.square.idx] = board[from.idx];
            board[from.idx] = 0;
            board[castle.rSquare.idx] = board[castle.rInitSquare.idx];
            board[castle.rInitSquare.idx] = 0;

            moveDetails.castle = castle;
            pieceList.get(activeColor)[50] = castle.square;
            moveInPieceList(activeColor, castle.rInitSquare, castle.rSquare);

            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.KING.id - 1][from.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.ROOK.id - 1][castle.rInitSquare.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.KING.id - 1][castle.square.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.ROOK.id - 1][castle.rSquare.idx];
        } else if (pieceType == Piece.PAWN && to == enPassant) {
            Square enPassantCaptureSquare = Square.lookup.get(
                    to.idx + (activeColor == Color.W ? main.moveGen.Vector.DOWN.offset : main.moveGen.Vector.UP.offset)
            );
            moveDetails.capturedPiece = board[enPassantCaptureSquare.idx];

            board[to.idx] = board[from.idx];
            board[from.idx] = 0;
            board[enPassantCaptureSquare.idx] = 0;

            moveDetails.capturePieceSquare = enPassantCaptureSquare;
            // capture is handled later

            moveInPieceList(activeColor, from, to);

            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.PAWN.id - 1][from.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.PAWN.id - 1][to.idx];
        } else {
            if (board[to.idx] != 0) {
                moveDetails.capturedPiece = board[to.idx];
                moveDetails.capturePieceSquare = to;
            }

            board[to.idx] = board[from.idx];
            board[from.idx] = 0;

            if (promote != 0) {
                moveDetails.isPromote = true;
                board[to.idx] = activeColor.id | promote;

                removePiece(activeColor, Piece.PAWN, from);
                putPiece(activeColor, Piece.extractPieceType(promote), to);

                zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][0][from.idx];
                zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][promote - 1][to.idx];
            } else {
                moveInPieceList(activeColor, from, to);

                zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][pieceType.id - 1][from.idx];
                zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][pieceType.id - 1][to.idx];
            }
        }

        if (moveDetails.capturedPiece != 0) {
            if (Utils.findIndexOf(moveDetails.capturePieceSquare, pieceList.get(oppColor)) == -1) {
                System.out.println(Arrays.toString(board));
                GameState.printBoard();
            }
            removePiece(oppColor, Piece.extractPieceType(moveDetails.capturedPiece), moveDetails.capturePieceSquare);

            zobristHash ^= ZobristKey.PIECES[oppColor.ordinal()][(moveDetails.capturedPiece & 7) -
                    1][moveDetails.capturePieceSquare.idx];
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
            if (pieceType == Piece.PAWN && Math.abs(from.idx - to.idx) == 2 * (main.moveGen.Vector.UP.offset)) {
                enPassant = Square.lookup.get(
                        from.idx + (activeColor == Color.W ? main.moveGen.Vector.UP.offset : Vector.DOWN.offset));
                zobristHash ^= ZobristKey.EN_PASSANT[enPassant.idx];
            }

            // checking if rooks moved or have been captured to see if I need to toggle castleRights
            if ((castleRights & Castle.W_Q.value) != 0 && ((board[Square.A1.idx] ^ Piece.ROOK.id) ^ Color.W.id) != 0) {
                zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
                castleRights ^= Castle.W_Q.value;
                zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
            }
            if ((castleRights & Castle.W_K.value) != 0 && ((board[Square.H1.idx] ^ Piece.ROOK.id) ^ Color.W.id) != 0) {
                zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
                castleRights ^= Castle.W_K.value;
                zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
            }
            if ((castleRights & Castle.B_q.value) != 0 && ((board[Square.A8.idx] ^ Piece.ROOK.id) ^ Color.B.id) != 0) {
                zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
                castleRights ^= Castle.B_q.value;
                zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
            }
            if ((castleRights & Castle.B_k.value) != 0 && ((board[Square.H8.idx] ^ Piece.ROOK.id) ^ Color.B.id) != 0) {
                zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
                castleRights ^= Castle.B_k.value;
                zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
            }
        }

        if (moveDetails.capturedPiece == 0 && pieceType != Piece.PAWN) halfmoves++;
        else halfmoves = 0;

        activeColor = Color.getOppColor(activeColor);
        if (activeColor == Color.B) zobristHash ^= ZobristKey.SIDE;

        totalRepititions.merge(zobristHash, 1, Integer::sum);
    }

    public static void unmakeMove(UnmakeDetails move) {

        if (enPassant != Square.NULL) zobristHash ^= ZobristKey.EN_PASSANT[enPassant.idx];
        enPassant = move.prevEnPassant;
        if (enPassant != Square.NULL) zobristHash ^= ZobristKey.EN_PASSANT[enPassant.idx];

        if (activeColor == Color.B) zobristHash ^= ZobristKey.SIDE;
        activeColor = Color.getOppColor(activeColor);

        zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
        zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];
        castleRights = move.prevCastleRights;
        zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.W.ordinal()][castleRights >> 2];
        zobristHash ^= ZobristKey.CASTLING_RIGHTS[Color.B.ordinal()][castleRights & 3];

        halfmoves = move.prevHalfmoves;

        Color oppColor = Color.getOppColor(activeColor);

        if (move.castle != null) {
            board[move.from.idx] = board[move.castle.square.idx];
            board[move.castle.square.idx] = 0;
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.KING.id - 1][move.castle.square.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.KING.id - 1][move.from.idx];

            board[move.castle.rInitSquare.idx] = board[move.castle.rSquare.idx];
            board[move.castle.rSquare.idx] = 0;
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.ROOK.id - 1][move.castle.rInitSquare.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][Piece.ROOK.id - 1][move.castle.rSquare.idx];

            moveInPieceList(activeColor, move.castle.rSquare, move.castle.rInitSquare);
            pieceList.get(activeColor)[50] = move.from;

            return;
        }

        if (move.isPromote) {
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][(board[move.to.idx] & 7) - 1][move.to.idx];
            removePiece(activeColor, Piece.extractPieceType(board[move.to.idx]), move.to);
            board[move.to.idx] = 0;

            board[move.from.idx] = activeColor.id | Piece.PAWN.id;
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][0][move.from.idx];
            putPiece(activeColor, Piece.PAWN, move.from);
        } else {
            board[move.from.idx] = board[move.to.idx];
            board[move.to.idx] = 0;
            moveInPieceList(activeColor, move.to, move.from);

            int pieceId = board[move.from.idx] & 7;
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][pieceId - 1][move.to.idx];
            zobristHash ^= ZobristKey.PIECES[activeColor.ordinal()][pieceId - 1][move.from.idx];
        }

        if (move.capturedPiece != 0) {
            board[move.capturePieceSquare.idx] = move.capturedPiece;
            putPiece(oppColor, Piece.extractPieceType(move.capturedPiece),
                    move.capturePieceSquare);

            zobristHash ^=
                    ZobristKey.PIECES[oppColor.ordinal()][(move.capturedPiece & 7) - 1][move.capturePieceSquare.idx];
        }

        totalRepititions.merge(zobristHash, -1, Integer::sum);
        move.reset();
    }

    public static boolean isDrawByInsufficientMaterial() {
        /*
        insufficientMaterial represents possible draw combinations, each int representing the sum of the piece ids

        if there is more than two of one kind of piece return false
        if there is a pawn, rook, or queen on the board return false

        else add the piece id to the wPieces/bPieces
        check if the combination exists in insufficientMaterial
         */
        Map<Integer, Integer[]> insufficientMaterial = new HashMap<>();
        insufficientMaterial.put(Piece.KING.id, new Integer[]{
                Piece.BISHOP.id + Piece.KING.id,
                Piece.KNIGHT.id + Piece.KING.id, Piece.KING.id
        });
        insufficientMaterial.put(Piece.BISHOP.id + Piece.KING.id, new Integer[]{Piece.BISHOP.id + Piece.KING.id});

        int wPieces = Piece.KING.id;
        int bPieces = Piece.KING.id;

        for (Piece pieceType : Piece.values()) {
            if (pieceType == Piece.NULL || pieceType == Piece.KING) continue;
            int startIdx = (pieceType.id - 1) * 10;
            for (int i = 0; i < 10; i++) {
                switch (pieceType) {

                    case KNIGHT, BISHOP -> {
                        if (pieceList.get(Color.W)[startIdx + i] != Square.NULL) {
                            // if there are two pieces of the same type it's not a draw
                            wPieces += pieceType.id;
                            if (wPieces > 9) return false;
                        }
                        if (pieceList.get(Color.B)[startIdx + i] != Square.NULL) {
                            bPieces += pieceType.id;
                            if (bPieces > 9) return false;
                        }
                    }

                    default -> {
                        if (pieceList.get(Color.W)[startIdx + i] != Square.NULL ||
                                pieceList.get(Color.B)[startIdx + i] != Square.NULL) return false;
                    }
                }
            }
        }

        return Utils.findValue(wPieces, insufficientMaterial.get(bPieces)) || Utils.findValue(bPieces,
                insufficientMaterial.get(wPieces));
    }

    public static boolean isDrawByRepitition(long mostRecentHash) {
        return totalRepititions.get(mostRecentHash) != null && totalRepititions.get(mostRecentHash) >= 3;
    }

    public static int countNumOfPositions(int depth, boolean print) {

        if (depth == 0) return 1;
        int count = 0;
        UnmakeDetails moveDetails = new UnmakeDetails();
        List<Integer> moves = getValidMoves(activeColor, false, false);
        for (Integer move : moves) {
            int moveCount = 0;
            makeMove(move, moveDetails);
            moveCount += countNumOfPositions(depth - 1);
            count += moveCount;
            if (print) {
                System.out.printf("%s%s: %d; depth: %d %n", Square.lookup.get((move >> 7) & 127),
                        Square.lookup.get(move & 127),
                        moveCount, depth);
            }
            unmakeMove(moveDetails);
            moveDetails.reset();
        }
        return count;
    }

    public static int countNumOfPositions(int depth) {
        if (depth == 0) return 1;
        int count = 0;
        UnmakeDetails moveDetails = new UnmakeDetails();
        List<Integer> moves = getValidMoves(activeColor, false, false);
        for (Integer move : moves) {
            makeMove(move, moveDetails);
            count += countNumOfPositions(depth - 1);
            unmakeMove(moveDetails);
            moveDetails.reset();
        }
        return count;
    }
}