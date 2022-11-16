package main.moveGen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final public class GameState {

    private GameState() {
    }

    public static int[] board = new int[128];
    //start index for each piece is equal to Piece.id - 1 * 10 (need to multiply by 10 bc that is max amount of possible
    // pieces
    // need to subtract 1 from Piece.id bc Piece enum starts with NULL
    public static int[] wPieceList = new int[51];
    public static int[] bPieceList = new int[51];

    // init board + pieceList
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

        Arrays.fill(wPieceList,
                    -1);
        wPieceList[0] = 16;
        wPieceList[1] = 17;
        wPieceList[2] = 18;
        wPieceList[3] = 19;
        wPieceList[4] = 20;
        wPieceList[5] = 21;
        wPieceList[6] = 22;
        wPieceList[7] = 23;
        wPieceList[10] = 1;
        wPieceList[11] = 6;
        wPieceList[20] = 2;
        wPieceList[21] = 5;
        wPieceList[30] = 0;
        wPieceList[31] = 7;
        wPieceList[40] = 3;
        wPieceList[50] = 4;

        Arrays.fill(bPieceList,
                    -1);
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

        bPieceList[0] = 96;
        bPieceList[1] = 97;
        bPieceList[2] = 98;
        bPieceList[3] = 99;
        bPieceList[4] = 100;
        bPieceList[5] = 101;
        bPieceList[6] = 102;
        bPieceList[7] = 103;
        bPieceList[10] = 113;
        bPieceList[11] = 118;
        bPieceList[20] = 114;
        bPieceList[21] = 117;
        bPieceList[30] = 112;
        bPieceList[31] = 119;
        bPieceList[40] = 115;
        bPieceList[50] = 116;
    }

    public static Color activeColor = Color.W;
    public static Square enPassant = null;
    public static int castleRights = 15;
    public static Map<Character, Integer> castleMap = new HashMap<>();

    // map fen representation of castle rights to binary
    static {
        castleMap.put('K',
                      8);
        castleMap.put('Q',
                      4);
        castleMap.put('k',
                      2);
        castleMap.put('q',
                      1);
    }

    public static void loadFen(String fen) {
        board = new int[128];
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
            board[idx] = color.id | pieceIdMap.get(type);
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
    }
}
