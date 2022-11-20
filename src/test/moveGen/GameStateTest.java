package test.moveGen;


import jdk.jfr.Description;
import main.moveGen.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.*;

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

    @Nested
    @Description("test makeMove")
    public class makeMove {
        UnmakeDetails unmakeDetails = new UnmakeDetails();

        @BeforeEach
        public void init() {
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            unmakeDetails.reset();
        }


        @Test
        public void movesPiece() {
            Move move = new Move(Square.E2, Square.E4);
            GameState.makeMove(move, unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
        }

        @Test
        public void togglesEnPassant() {
            Move wMove = new Move(Square.E2, Square.E4);
            GameState.makeMove(wMove, unmakeDetails);

            MatcherAssert.assertThat(GameState.enPassant, is(Square.E3));

            Move bMove = new Move(Square.E7, Square.E5);
            GameState.makeMove(bMove, unmakeDetails);

            MatcherAssert.assertThat(GameState.enPassant, is(Square.E6));
        }

        @Test
        public void capturesWork() {
            Move wMove = new Move(Square.E2, Square.E4);
            GameState.makeMove(wMove, unmakeDetails);

            Move bMove = new Move(Square.D7, Square.D5);
            GameState.makeMove(bMove, unmakeDetails);

            GameState.makeMove(new Move(Square.E4, Square.D5), unmakeDetails);
            MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(Color.W.id | Piece.PAWN.id));
        }

        @Nested
        @DisplayName("castle works")
        class castle {
            @Nested
            @Description("castle works kingside")
            class kingside {
                @Test
                public void whiteside() {
                    GameState.loadFen("rnbqkbnr/pppppppp/8/8/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 1");
                    Castle castle = Castle.W_K;
                    GameState.makeMove(new Move(Square.E1, castle), unmakeDetails);

                    MatcherAssert.assertThat(GameState.board[castle.square.idx], is(Color.W.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[castle.rSquare.idx], is(Color.W.id | Piece.ROOK.id));
                }

                @Test
                void blackside() {
                    GameState.loadFen("rnbqk2r/pppp1ppp/5n2/2b1p3/8/8/PPPPPPPP/RNBQKBNR B KQkq - 0 1");
                    Castle castle = Castle.B_k;
                    GameState.makeMove(new Move(Square.E8, castle), unmakeDetails);

                    MatcherAssert.assertThat(GameState.board[castle.square.idx], is(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[castle.rSquare.idx], is(Color.B.id | Piece.ROOK.id));
                }
            }

            @Nested
            @Description("castle works queenside")
            class queenside {
                @Test
                public void whiteside() {
                    GameState.loadFen("rnbqkbnr/pppppppp/8/8/3P1B2/2N5/PPPQPPPP/R3KBNR w KQkq - 0 1");
                    Castle castle = Castle.W_Q;
                    GameState.makeMove(new Move(Square.E1, castle), unmakeDetails);

                    MatcherAssert.assertThat(GameState.board[castle.square.idx], is(Color.W.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[castle.rSquare.idx], is(Color.W.id | Piece.ROOK.id));
                }

                @Test
                void blackside() {
                    GameState.loadFen("r3kbnr/pppqpppp/2n5/3p1b2/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                    Castle castle = Castle.B_q;
                    GameState.makeMove(new Move(Square.E8, castle), unmakeDetails);

                    MatcherAssert.assertThat(GameState.board[castle.square.idx], is(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[castle.rSquare.idx], is(Color.B.id | Piece.ROOK.id));
                }
            }
        }

        @Nested
        class captureByEnPassantWorks {
            @Test
            public void whiteside() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 1");

                GameState.makeMove(new Move(Square.E5, Square.D6), unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.D6.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(0));
            }

            @Test
            public void blackside() {
                GameState.loadFen("rnbqkbnr/pppp1ppp/8/8/3Pp3/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1");

                GameState.makeMove(new Move(Square.E4, Square.D3), unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.D3.idx], is(Color.B.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.D4.idx], is(0));
            }
        }

        @Nested
        class cantMakeIllegalMove {
            @Test
            public void cantCastleIntoCheck() {
                GameState.loadFen("rnbqkbn1/pppppppp/6r1/8/8/5N2/PPPPPP1P/RNBQK2R w KQkq - 0 1");

                Castle castle = Castle.W_K;
                boolean moveMade = GameState.makeMove(new Move(Square.E1, castle), unmakeDetails);

                MatcherAssert.assertThat(moveMade, is(false));
                MatcherAssert.assertThat(GameState.board[Square.E1.idx], is(Color.W.id | Piece.KING.id));
                MatcherAssert.assertThat(GameState.board[Square.H1.idx], is(Color.W.id | Piece.ROOK.id));
            }
        }
    }
}