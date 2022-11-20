package main.moveGen;

import main.utils.Utils;

import java.util.*;

final public class GameState {
    private GameState() {
    }

    public static int[] board = new int[128];
    //start index for each piece is equal to Piece.id - 1 * 10 (need to multiply by 10 bc that is max amount of possible
    // pieces
    // need to subtract 1 from Piece.id bc Piece enum starts with NULL

    public static Map<Color, Square[]> pieceList = new HashMap<>();

    // init board + pieceList
    static {
        pieceList.put(Color.W,
                new Square[51]);
        pieceList.put(Color.B,
                new Square[51]);

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
        wPieceList[0] = Square.A2;
        wPieceList[1] = Square.B2;
        wPieceList[2] = Square.C2;
        wPieceList[3] = Square.D2;
        wPieceList[4] = Square.E2;
        wPieceList[5] = Square.F2;
        wPieceList[6] = Square.G2;
        wPieceList[7] = Square.H2;
        wPieceList[10] = Square.B1;
        wPieceList[11] = Square.G1;
        wPieceList[20] = Square.C1;
        wPieceList[21] = Square.F1;
        wPieceList[30] = Square.A1;
        wPieceList[31] = Square.H1;
        wPieceList[40] = Square.D1;
        wPieceList[50] = Square.E1;

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
        bPieceList[0] = Square.A7;
        bPieceList[1] = Square.B7;
        bPieceList[2] = Square.C7;
        bPieceList[3] = Square.D7;
        bPieceList[4] = Square.E7;
        bPieceList[5] = Square.F7;
        bPieceList[6] = Square.G7;
        bPieceList[7] = Square.H7;
        bPieceList[10] = Square.B8;
        bPieceList[11] = Square.G8;
        bPieceList[20] = Square.C8;
        bPieceList[21] = Square.F8;
        bPieceList[30] = Square.A8;
        bPieceList[31] = Square.H8;
        bPieceList[40] = Square.D8;
        bPieceList[50] = Square.E8;
    }

    public static Color activeColor = Color.W;
    public static Square enPassant;
    public static int halfmoves;
    public static int castleRights =
            Castle.W_K.value & Castle.W_Q.value & Castle.B_k.value & Castle.B_q.value;

    private static void pushToPieceList(Color color,
                                        int pieceId,
                                        Square square) {
        Square[] list = pieceList.get(color);

        int startIdx = (pieceId - 1) * 10;
        for (int i = 0; i < 10; i++) {
            int nextIdx = startIdx + i;
            if (nextIdx > 50) return;
            if (list[nextIdx] == Square.NULL) {
                list[nextIdx] = square;
                return;
            }
        }
    }

    public static void loadFen(String fen) {
        board = new int[128];

        Arrays.fill(pieceList.get(Color.W),
                Square.NULL);
        Arrays.fill(pieceList.get(Color.B),
                Square.NULL);

        Map<Character, Integer> pieceIdMap = new HashMap<>();
        pieceIdMap.put('k',
                Piece.KING.id);
        pieceIdMap.put('q',
                Piece.QUEEN.id);
        pieceIdMap.put('r',
                Piece.ROOK.id);
        pieceIdMap.put('n',
                Piece.KNIGHT.id);
        pieceIdMap.put('b',
                Piece.BISHOP.id);
        pieceIdMap.put('p',
                Piece.PAWN.id);

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
            int pieceId = pieceIdMap.get(type);
            board[idx] = color.id | pieceId;
            pushToPieceList(color,
                    pieceId,
                    Square.lookup.get(idx));
            file++;
        }

        String fenCastle = fenState[2];
        castleRights = 0;
        for (int i = 0; i < fenCastle.length(); i++) {
            castleRights |= castleMap.get(fenCastle.charAt(i));
        }

        activeColor = Objects.equals(fenState[1],
                "w") ?
                Color.W :
                Color.B;

        enPassant = !Objects.equals(fenState[3],
                "-") ?
                Square.valueOf(fenState[3].toUpperCase()) :
                null;

        halfmoves = Integer.parseInt(fenState[4]);
    }

    public static boolean makeMove(Move move, UnmakeDetails moveDetails) {
        moveDetails.prevCastleRights = castleRights;
        moveDetails.prevEnPassant = enPassant;
        moveDetails.prevHalfmoves = halfmoves;
        // assume to square is a valid pseudolegal move
        Square kingPos = pieceList.get(activeColor)[50];
        Color oppColor = Color.getOppColor(activeColor);

        Piece pieceType = Piece.extractPieceType(board[move.from.idx]);

        if (move.castle != null) {
            board[move.castle.square.idx] = board[move.from.idx];
            board[move.from.idx] = 0;
            Square rookPos = move.castle.getSquareOfRook();
            board[move.castle.rSquare.idx] = board[rookPos.idx];
            board[rookPos.idx] = 0;

            boolean valid = !MoveGen.isAttacked(move.castle.square,
                    oppColor,
                    pieceList.get(oppColor),
                    board);

            if (!valid) {
                board[move.from.idx] = board[move.castle.square.idx];
                board[move.castle.square.idx] = 0;
                board[rookPos.idx] = board[move.castle.rSquare.idx];
                board[move.castle.rSquare.idx] = 0;
                return false;
            } else {
                moveDetails.castle = move.castle;
                pieceList.get(activeColor)[50] = move.castle.square;
                pieceList.get(activeColor)[Utils.findIndexOf(rookPos,
                        pieceList.get(activeColor))] =
                        move.castle.rSquare;
            }
        } else if (pieceType == Piece.PAWN && move.to == enPassant) {
            Square enPassantCaptureSquare = Square.lookup.get(
                    move.to.idx + (activeColor == Color.W ? Vector.DOWN.offset : Vector.UP.offset)
            );
            moveDetails.capturedPiece = board[enPassantCaptureSquare.idx];

            board[move.to.idx] = board[move.from.idx];
            board[enPassantCaptureSquare.idx] = 0;

            boolean valid = !MoveGen.isAttacked(kingPos,
                    oppColor,
                    pieceList.get(oppColor),
                    board);

            if (!valid) {
                board[move.from.idx] = activeColor.id | Piece.PAWN.id;
                board[enPassantCaptureSquare.idx] = moveDetails.capturedPiece;
                return false;
            } else {
                board[enPassantCaptureSquare.idx] = moveDetails.capturedPiece;
                moveDetails.capturePieceSquare = enPassantCaptureSquare;

                pieceList.get(activeColor)[Utils.findIndexOf(move.from,
                        pieceList.get(activeColor))] = move.to;
                Square[] oppList = pieceList.get(Color.getOppColor(activeColor));
                oppList[Utils.findIndexOf(enPassantCaptureSquare, oppList)] = Square.NULL;
            }
        } else {
            if (board[move.to.idx] != 0) {
                moveDetails.capturedPiece = board[move.to.idx];
                moveDetails.capturePieceSquare = move.to;
            }

            board[move.to.idx] = board[move.from.idx];
            board[move.from.idx] = 0;

            boolean valid = !MoveGen.isAttacked(pieceType == Piece.KING ? move.to : kingPos,
                    oppColor,
                    pieceList.get(oppColor),
                    board);

            if (!valid) {
                board[move.from.idx] = activeColor.id | pieceType.id;
                board[move.to.idx] = moveDetails.capturedPiece;
                return false;
            }

            if (move.promote != null) {
                moveDetails.isPromote = true;
                board[move.to.idx] = activeColor.id | move.promote.id;

                Square[] list = pieceList.get(activeColor);
                list[Utils.findIndexOf(move.from, list)] =
                        Square.NULL;
                for (int i = 0; i < 10; i++) {
                    // look for the first open slot
                    int idx = (move.promote.id - 1) * 10 + i;
                    if (list[idx] == Square.NULL) list[idx] = move.to;
                }
            } else {
                pieceList.get(activeColor)[Utils.findIndexOf(move.from,
                        pieceList.get(activeColor))] = move.to;
                if (moveDetails.capturedPiece != 0) {
                    Square[] oppList = pieceList.get(Color.getOppColor(activeColor));
                    oppList[Utils.findIndexOf(move.to, oppList)] = Square.NULL;
                }
            }
        }

        enPassant = null;
        switch (pieceType) {
            case KING -> {
                if (activeColor == Color.W) {
                    castleRights ^= 8;
                    castleRights ^= 4;
                } else {
                    castleRights ^= 2;
                    castleRights ^= 1;
                }
            }

            case PAWN -> {
                if (Math.abs(move.from.idx - move.to.idx) == 2 * (Vector.UP.offset)) enPassant = Square.lookup.get(
                        move.from.idx + (activeColor == Color.W ? Vector.UP.offset : Vector.DOWN.offset)
                );
            }
        }

        // checking if rooks are on their home squares to see if i need to toggle castleRights
        if (((board[Square.A1.idx] ^ Piece.ROOK.id) ^ Color.W.id) != 0) castleRights ^= Castle.W_Q.value;
        if (((board[Square.H1.idx] ^ Piece.ROOK.id) ^ Color.W.id) != 0) castleRights ^= Castle.W_K.value;
        if (((board[Square.A8.idx] ^ Piece.ROOK.id) ^ Color.B.id) != 0) castleRights ^= Castle.B_q.value;
        if (((board[Square.H8.idx] ^ Piece.ROOK.id) ^ Color.B.id) != 0) castleRights ^= Castle.B_k.value;

        moveDetails.from = move.from;
        moveDetails.to = move.to;

        if (moveDetails.capturedPiece == 0 && pieceType != Piece.PAWN) halfmoves++;
        else halfmoves = 0;

        activeColor = Color.getOppColor(activeColor);

        return true;
    }
}
