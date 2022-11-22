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
    public static long zobristHash;

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

        zobristHash = ZobristKey.hash();
    }

    private static void pushToPieceList(Color color,
                                        Piece piece,
                                        Square square) {
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

    private static void moveInPieceList(Color color, Square from, Square to) {
        pieceList.get(color)[Utils.findIndexOf(from,
                pieceList.get(color))] = to;
    }

    public static void loadFen(String fen) {
        board = new int[128];

        Arrays.fill(pieceList.get(Color.W),
                Square.NULL);
        Arrays.fill(pieceList.get(Color.B),
                Square.NULL);

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
            pushToPieceList(color,
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

    public static boolean makeMove(Integer move, UnmakeDetails moveDetails) {
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

        Square kingPos = pieceList.get(activeColor)[50];
        Color oppColor = Color.getOppColor(activeColor);

        Piece pieceType = Piece.extractPieceType(board[from.idx]);

        if (castle != null) {
            board[castle.square.idx] = board[from.idx];
            board[from.idx] = 0;
            board[castle.rSquare.idx] = board[castle.rInitSquare.idx];
            board[castle.rInitSquare.idx] = 0;

            boolean valid = !MoveGen.isAttacked(castle.square,
                    oppColor,
                    pieceList.get(oppColor),
                    board);

            if (!valid) {
                board[from.idx] = board[castle.square.idx];
                board[castle.square.idx] = 0;
                board[castle.rInitSquare.idx] = board[castle.rSquare.idx];
                board[castle.rSquare.idx] = 0;
                return false;
            } else {
                moveDetails.castle = castle;
                pieceList.get(activeColor)[50] = castle.square;
                moveInPieceList(activeColor, castle.rInitSquare, castle.rSquare);
            }
        } else if (pieceType == Piece.PAWN && to == enPassant) {
            Square enPassantCaptureSquare = Square.lookup.get(
                    to.idx + (activeColor == Color.W ? Vector.DOWN.offset : Vector.UP.offset)
            );
            moveDetails.capturedPiece = board[enPassantCaptureSquare.idx];

            board[to.idx] = board[from.idx];
            board[enPassantCaptureSquare.idx] = 0;

            boolean valid = !MoveGen.isAttacked(kingPos,
                    oppColor,
                    pieceList.get(oppColor),
                    board);

            if (!valid) {
                board[from.idx] = activeColor.id | Piece.PAWN.id;
                board[enPassantCaptureSquare.idx] = moveDetails.capturedPiece;
                return false;
            } else {
                board[enPassantCaptureSquare.idx] = 0;
                moveDetails.capturePieceSquare = enPassantCaptureSquare;

                moveInPieceList(activeColor, from, to);
                moveInPieceList(Color.getOppColor(activeColor), enPassantCaptureSquare, Square.NULL);
            }
        } else {
            if (board[to.idx] != 0) {
                moveDetails.capturedPiece = board[to.idx];
                moveDetails.capturePieceSquare = to;
            }

            board[to.idx] = board[from.idx];
            board[from.idx] = 0;

            boolean valid = !MoveGen.isAttacked(pieceType == Piece.KING ? to : kingPos,
                    oppColor,
                    pieceList.get(oppColor),
                    board);

            if (!valid) {
                board[from.idx] = activeColor.id | pieceType.id;
                board[to.idx] = moveDetails.capturedPiece;
                return false;
            }

            if (promote != 0) {
                moveDetails.isPromote = true;
                board[to.idx] = activeColor.id | promote;

                Square[] list = pieceList.get(activeColor);
                list[Utils.findIndexOf(from, list)] =
                        Square.NULL;
                for (int i = 0; i < 10; i++) {
                    // look for the first open slot
                    int idx = (promote - 1) * 10 + i;
                    if (list[idx] == Square.NULL) list[idx] = to;
                }
            } else {
                moveInPieceList(activeColor, from, to);
                if (moveDetails.capturedPiece != 0) {
                    moveInPieceList(Color.getOppColor(activeColor), to, Square.NULL);
                }
            }
        }

        enPassant = Square.NULL;

        if (pieceType == Piece.KING) {
            if (activeColor == Color.W) {
                castleRights = (castleRights ^ Castle.W_K.value) ^ Castle.W_Q.value;
            } else {
                castleRights = (castleRights ^ Castle.B_k.value) ^ Castle.B_q.value;
            }
        } else {
            if (pieceType == Piece.PAWN) {
                if (Math.abs(from.idx - to.idx) == 2 * (Vector.UP.offset)) enPassant = Square.lookup.get(
                        from.idx + (activeColor == Color.W ? Vector.UP.offset : Vector.DOWN.offset)
                );
            }

            // checking if rooks are on their home squares to see if I need to toggle castleRights
            if (((board[Square.A1.idx] ^ Piece.ROOK.id) ^ Color.W.id) != 0) castleRights ^= Castle.W_Q.value;
            if (((board[Square.H1.idx] ^ Piece.ROOK.id) ^ Color.W.id) != 0) castleRights ^= Castle.W_K.value;
            if (((board[Square.A8.idx] ^ Piece.ROOK.id) ^ Color.B.id) != 0) castleRights ^= Castle.B_q.value;
            if (((board[Square.H8.idx] ^ Piece.ROOK.id) ^ Color.B.id) != 0) castleRights ^= Castle.B_k.value;
        }

        moveDetails.from = from;
        moveDetails.to = to;

        if (moveDetails.capturedPiece == 0 && pieceType != Piece.PAWN) halfmoves++;
        else halfmoves = 0;

        activeColor = Color.getOppColor(activeColor);

        return true;
    }

    public static void unmakeMove(UnmakeDetails move) {
        enPassant = move.prevEnPassant;
        castleRights = move.prevCastleRights;
        activeColor = Color.getOppColor(activeColor);
        halfmoves = move.prevHalfmoves;
        Color oppColor = Color.getOppColor(activeColor);
        if (move.castle != null) {
            board[move.from.idx] = board[move.castle.square.idx];
            board[move.castle.square.idx] = 0;

            Square rookPos = Square.NULL;
            switch (move.castle) {
                case W_K -> rookPos = Square.H1;
                case W_Q -> rookPos = Square.A1;
                case B_k -> rookPos = Square.H8;
                case B_q -> rookPos = Square.A8;
            }

            board[rookPos.idx] = board[move.castle.rSquare.idx];
            board[move.castle.rSquare.idx] = 0;
            moveInPieceList(activeColor, move.castle.rSquare, rookPos);
            pieceList.get(activeColor)[50] = move.from;
        } else {
            board[move.from.idx] = board[move.to.idx];
            board[move.to.idx] = 0;
            moveInPieceList(activeColor, move.to, move.from);

            if (move.capturedPiece != 0) {
                board[move.capturePieceSquare.idx] = move.capturedPiece;
                pushToPieceList(oppColor, Piece.extractPieceType(move.capturedPiece),
                        move.capturePieceSquare);
            }

            if (move.isPromote) {
                board[move.from.idx] = activeColor.id | Piece.PAWN.id;
                pushToPieceList(activeColor, Piece.PAWN, move.from);
            }
        }
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
}
