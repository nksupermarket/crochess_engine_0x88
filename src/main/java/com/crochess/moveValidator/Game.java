package com.crochess.moveValidator;

import com.crochess.engine0x88.GameState;
import com.crochess.engine0x88.MoveGen;
import com.crochess.engine0x88.ZobristKey;
import com.crochess.engine0x88.moveEval.Psqt;
import com.crochess.engine0x88.types.*;
import com.crochess.engine0x88.types.Vector;
import com.crochess.engine0x88.utils.Score;
import com.crochess.engine0x88.utils.Utils;

import java.util.*;

public class Game {
    public int[] board = new int[128];
    public Square[][] pieceList = new Square[2][51];
    public long zobristHash;

    public Map<Long, Integer> totalRepititions = new HashMap<>();

    public Color activeColor = Color.W;
    public Square enPassant = Square.NULL;
    public int halfmoves;
    public int castleRights =
            Castle.W_K.value | Castle.W_Q.value | Castle.B_k.value | Castle.B_q.value;

    public Game() {

    }

    public void putPiece(Color color,
                         Piece piece,
                         Square square) {

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

    public void removePiece(Color color, Piece piece, Square square) {
        pieceList[color.ordinal()][Utils.findIndexOf(square, pieceList[color.ordinal()])] = Square.NULL;
    }

    public void moveInPieceList(Color color, Square from, Square to) {
        pieceList[color.ordinal()][Utils.findIndexOf(from, pieceList[color.ordinal()])] = to;
    }

    private void resetState() {
        board = new int[128];

        enPassant = Square.NULL;
        halfmoves = 0;

        Arrays.fill(pieceList[Color.W.ordinal()],
                Square.NULL);
        Arrays.fill(pieceList[Color.B.ordinal()],
                Square.NULL);

        totalRepititions.clear();
    }

    public void loadFen(String fen) {
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
        zobristHash = ZobristKey.hash(this);
        totalRepititions.put(zobristHash, 1);
    }

    public String getFen() {
        StringBuilder fen = new StringBuilder();
        int emptyFileCount = 0;

        for (int rank = 7; rank >= 0; rank--) {
            if (rank != 7) fen.append("/");

            for (int file = 0; file < 8; file++) {
                int idx = (rank * 16) + file;
                int boardVal = board[idx];
                if (boardVal == 0) {
                    emptyFileCount++;
                    if (file == 7) {
                        fen.append(emptyFileCount);
                        emptyFileCount = 0;
                    }
                    continue;
                }

                if (emptyFileCount != 0) {
                    fen.append(emptyFileCount);
                    emptyFileCount = 0;
                }
                Piece pType = Piece.extractPieceType(boardVal);
                Color color = Color.extractColor(boardVal);

                String piece = pType == Piece.KNIGHT ? "N" : String.valueOf(pType.name()
                                                                                 .charAt(0));
                String notation = color == Color.W ? piece : piece.toLowerCase();
                fen.append(notation);
            }
        }

        String color = " " + activeColor.name()
                                        .toLowerCase();
        fen.append(color);

        String castle = " ";
        if ((castleRights & 15) == 0) castle += "-";
        else {
            if ((castleRights & 8) != 0) castle += "K";
            if ((castleRights & 4) != 0) castle += "Q";
            if ((castleRights & 2) != 0) castle += "k";
            if ((castleRights & 1) != 0) castle += "q";
        }
        fen.append(castle);

        String enPassantStr = enPassant != Square.NULL ? " " + enPassant.name()
                                                                        .toLowerCase() : " -";
        fen.append(enPassantStr);
        fen.append(" ");
        fen.append(halfmoves);

        return fen.toString();
    }


    public boolean isMoveValid(int move) {
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
                if (!MoveGen.pseudoLegalForPawn(this, from, color)
                            .contains(move)) return false;
            }
            case KING -> {
                if (!MoveGen.pseudoLegalForKing(this, from, color, pieceList[oppColor.ordinal()])
                            .contains(move)) return false;
            }
            default -> {
                if (!MoveGen.pseudoLegal(this, from, pieceType, color)
                            .contains(move)) return false;
            }
        }
        boolean valid;

        if (castle != null) {
            board[castle.square.idx] = board[from.idx];
            board[from.idx] = 0;
            board[castle.rSquare.idx] = board[castle.rInitSquare.idx];
            board[castle.rInitSquare.idx] = 0;

            valid = !MoveGen.isAttacked(this, to,
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

            valid = !MoveGen.isAttacked(this, pieceType == Piece.KING ? to : kingPos,
                    oppColor,
                    pieceList[oppColor.ordinal()]);

            board[from.idx] = color.id | pieceType.id;
            board[to.idx] = capturedPiece;
        }

        return valid;
    }

    public int makeMove(Integer move) {
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

            moveInPieceList(activeColor, from, castle.square);
            moveInPieceList(activeColor, castle.rInitSquare, castle.rSquare);

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

            moveInPieceList(activeColor, from, to);

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

        if (capturedPiece != 0) {
            if (Utils.findIndexOf(capturePieceSquare, pieceList[oppColor.ordinal()]) == -1) {
                System.out.println(Arrays.toString(board));
                GameState.printBoard();
            }
            removePiece(oppColor, Piece.extractPieceType(capturedPiece), capturePieceSquare);

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

        return capturePieceSquare != null ? (capturePieceSquare.idx << 5) | capturedPiece : 0;
    }

    public int encodeMove(Square from, Square to) {
        return (from.idx << 7) | to.idx;
    }

    public boolean inCheck(Color color) {
        Color oppColor = Color.getOppColor(color);
        return MoveGen.isAttacked(this, pieceList[color.ordinal()][50],
                oppColor,
                pieceList[oppColor.ordinal()]
        );
    }

    public List<Integer> getValidMoves(Color color) {
        Square[] list = pieceList[color.ordinal()];

        List<Integer> moves = new ArrayList<>();

        for (int idx = 0; idx < list.length; idx++) {
            if (list[idx] == Square.NULL) continue;

            if (idx == 50) {
                moves.addAll(MoveGen.pseudoLegalForKing(this, list[50], color, pieceList[Color.getOppColor(color)
                                                                                              .ordinal()]));
            } else if (idx < 10) {
                moves.addAll(
                        MoveGen.pseudoLegalForPawn(this, list[idx], color));
            } else {
                moves.addAll(MoveGen.pseudoLegal(this, list[idx], Piece.lookup.get(
                        (int) Math.floor((float) idx / 10) + 1
                ), color));
            }
        }
        filterOutValidMoves(moves);
        return moves;
    }

    public void filterOutValidMoves(List<Integer> moves) {
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

            Square kingPos = pieceList[color.ordinal()][50];
            Color oppColor = Color.getOppColor(color);

            if (castle != null) {
                board[castle.square.idx] = board[from.idx];
                board[from.idx] = 0;
                board[castle.rSquare.idx] = board[castle.rInitSquare.idx];
                board[castle.rInitSquare.idx] = 0;

                valid = !MoveGen.isAttacked(this, to,
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

                valid = !MoveGen.isAttacked(this, pieceType == Piece.KING ? to : kingPos,
                        oppColor,
                        pieceList[oppColor.ordinal()]);

                board[from.idx] = color.id | pieceType.id;
                board[to.idx] = capturedPiece;
            }

            if (!valid) moveIterator.remove();
        }
    }

    public boolean isCheckmate(Color color) {
        return inCheck(color) && getValidMoves(color).size() == 0;
    }

    public String createMoveNotation(int move, int captureDetails, boolean checkmate) {
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

    public boolean isDrawByRepitition(long mostRecentHash, boolean forced) {
        return forced ? totalRepititions.get(mostRecentHash) == 5 : totalRepititions.get(mostRecentHash) == 3;
    }

    public boolean isDrawByMoveRule(boolean forced) {
        int MAX_MOVES = forced ? 150 : 100;
        return halfmoves == MAX_MOVES;
    }

    public boolean isDrawByInsufficientMaterial() {
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

    public boolean isUnforcedDraw() {
        return isDrawByMoveRule(false) || isDrawByRepitition(GameState.zobristHash, false);
    }

    public boolean isForcedDraw() {
        return isDrawByMoveRule(false) || isDrawByRepitition(GameState.zobristHash, false) ||
                isDrawByInsufficientMaterial();
    }
}
