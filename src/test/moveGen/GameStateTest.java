package test.moveGen;


import jdk.jfr.Description;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.*;

import main.moveGen.GameState;
import main.moveGen.Color;
import main.moveGen.Piece;
import main.moveGen.Square;

import java.util.Arrays;

public class GameStateTest {
    @Nested
    @Description("GameState.loadFen works")
    public class loadFen {
        @Test
        public void loadFenWorks() {
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

            int[] board;
            board = new int[128];

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

            Assertions.assertArrayEquals(board,
                    GameState.board);
            Assertions.assertEquals(15,
                    GameState.castleRights);
        }

        @Test
        public void castleRightsAreCorrect() {
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

            Assertions.assertEquals(15,
                    GameState.castleRights);
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 0 1");

            Assertions.assertEquals(12,
                    GameState.castleRights);
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq - 0 1");

            Assertions.assertEquals(3,
                    GameState.castleRights);
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kk - 0 1");

            Assertions.assertEquals(10,
                    GameState.castleRights);
        }

        @Test
        public void enPassantWorks() {
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq e2 0 1");

            Assertions.assertEquals(Square.E2,
                    GameState.enPassant);
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ h5 0 1");

            Assertions.assertEquals(Square.H5,
                    GameState.enPassant);
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq b3 0 1");

            Assertions.assertEquals(Square.B3,
                    GameState.enPassant);
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kk c6 0 1");

            Assertions.assertEquals(Square.C6,
                    GameState.enPassant);
        }

        @Test
        public void pieceListIsCorrect() {
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

            int[] wPieceList = new int[51];
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
            int[] bPieceList = new int[51];
            Arrays.fill(bPieceList,
                    -1);
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

            MatcherAssert.assertThat(GameState.pieceList.get(Color.W),
                    is(wPieceList));
            MatcherAssert.assertThat(GameState.pieceList.get(Color.B),
                    is(bPieceList));
        }
    }
}