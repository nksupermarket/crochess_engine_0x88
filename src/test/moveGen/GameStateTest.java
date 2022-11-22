package test.moveGen;

import jdk.jfr.Description;
import main.moveGen.*;
import main.utils.Utils;
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

            final Square[] wPieceList = new Square[51];
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
            Square[] bPieceList = new Square[51];
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

            MatcherAssert.assertThat(GameState.pieceList.get(Color.W),
                    is(wPieceList));
            MatcherAssert.assertThat(GameState.pieceList.get(Color.B),
                    is(bPieceList));
        }
    }

    private boolean isPieceOnSquareInPieceList(Piece piece, Color color, Square square) {
        Square[] list = GameState.pieceList.get(color);

        int startIdx = (piece.id - 1) * 10;
        // only one slot for king
        for (int i = 0; i < (piece == Piece.KING ? 1 : 10); i++) {
            int nextIdx = startIdx + i;
            if (list[nextIdx] == square) {
                return true;
            }
        }
        return false;
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
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.E4), is(true));
        }

        @Test
        public void capturesWork() {
            Move wMove = new Move(Square.E2, Square.E4);
            GameState.makeMove(wMove, unmakeDetails);

            Move bMove = new Move(Square.D7, Square.D5);
            GameState.makeMove(bMove, unmakeDetails);

            GameState.makeMove(new Move(Square.E4, Square.D5), unmakeDetails);
            MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.D5), is(true));
        }

        @Test
        public void changesTurn() {
            Move wMove = new Move(Square.E2, Square.E4);
            GameState.makeMove(wMove, unmakeDetails);

            MatcherAssert.assertThat(GameState.activeColor, is(Color.B));
        }

        @Nested
        class promotion {
            @Test
            public void promotionWorks() {
                GameState.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");

                GameState.makeMove(new Move(Square.B7, Square.A8, Piece.QUEEN), unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.A8.idx], is(Color.W.id | Piece.QUEEN.id));
                MatcherAssert.assertThat(GameState.board[Square.B7.idx], is(0));
                MatcherAssert.assertThat(GameState.pieceList.get(Color.W)[41], is(Square.A8));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.B7), is(false));
            }

            @Test
            public void resetsCorrectlyOnIllegalMove() {
                GameState.loadFen("rq2k2r/pPp2ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1KPQ1PPP/R6R w KQkq - 0 1");

                GameState.makeMove(new Move(Square.B7, Square.A8, Piece.QUEEN), unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.A8.idx], is(Color.B.id | Piece.ROOK.id));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.B, Square.A8), is(true));

                MatcherAssert.assertThat(GameState.board[Square.B7.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.B7), is(true));
            }
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
                    MatcherAssert.assertThat(GameState.castleRights, is((15 ^ Castle.W_Q.value) ^ Castle.W_K.value));

                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.W, castle.square), is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.W, castle.rSquare), is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.W, Square.E1), is(false));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.W, Square.H1),
                            is(false));
                }

                @Test
                void blackside() {
                    GameState.loadFen("rnbqk2r/pppp1ppp/5n2/2b1p3/8/8/PPPPPPPP/RNBQKBNR B KQkq - 0 1");
                    Castle castle = Castle.B_k;
                    GameState.makeMove(new Move(Square.E8, castle), unmakeDetails);

                    MatcherAssert.assertThat(GameState.board[castle.square.idx], is(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[castle.rSquare.idx], is(Color.B.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(GameState.castleRights, is((15 ^ Castle.B_q.value) ^ Castle.B_k.value));

                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.B, castle.square), is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.B, castle.rSquare), is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.B, Square.E8), is(false));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.B, Square.H8),
                            is(false));
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
                    MatcherAssert.assertThat(GameState.castleRights, is((15 ^ Castle.W_Q.value) ^ Castle.W_K.value));

                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.W, castle.square), is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.W, castle.rSquare), is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.W, Square.E1), is(false));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.W, Square.A1),
                            is(false));
                }

                @Test
                void blackside() {
                    GameState.loadFen("r3kbnr/pppqpppp/2n5/3p1b2/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
                    Castle castle = Castle.B_q;
                    GameState.makeMove(new Move(Square.E8, castle), unmakeDetails);

                    MatcherAssert.assertThat(GameState.board[castle.square.idx], is(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[castle.rSquare.idx], is(Color.B.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(GameState.castleRights, is((15 ^ Castle.B_q.value) ^ Castle.B_k.value));

                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.B, castle.square), is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.B, castle.rSquare), is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.B, Square.E8), is(false));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.B, Square.A8),
                            is(false));
                }
            }
        }

        @Nested
        @DisplayName("moving rooks adjusts castleRights")
        class rooks {
            @BeforeEach
            public void init() {
                GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");
            }

            @Nested
            class kingside {
                @Test
                public void whiteside() {
                    GameState.makeMove(new Move(Square.H1, Square.G1), unmakeDetails);

                    MatcherAssert.assertThat(GameState.castleRights, is(15 ^ Castle.W_K.value));
                }

                @Test
                public void blackside() {
                    GameState.activeColor = Color.B;
                    GameState.makeMove(new Move(Square.H8, Square.G8), unmakeDetails);

                    MatcherAssert.assertThat(GameState.castleRights, is(15 ^ Castle.B_k.value));
                }
            }

            @Nested
            class queenside {
                @Test
                public void whiteside() {
                    GameState.makeMove(new Move(Square.A1, Square.B1), unmakeDetails);

                    MatcherAssert.assertThat(GameState.castleRights, is(15 ^ Castle.W_Q.value));
                }

                @Test
                public void blackside() {
                    GameState.activeColor = Color.B;
                    GameState.makeMove(new Move(Square.A8, Square.B8), unmakeDetails);

                    MatcherAssert.assertThat(GameState.castleRights, is(15 ^ Castle.B_q.value));
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

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.D6), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.B, Square.D5), is(false));
            }

            @Test
            public void blackside() {
                GameState.loadFen("rnbqkbnr/pppp1ppp/8/8/3Pp3/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1");

                GameState.makeMove(new Move(Square.E4, Square.D3), unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.D3.idx], is(Color.B.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.D4.idx], is(0));

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.B, Square.D3), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.D4), is(false));
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

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.W, Square.E1), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.W, Square.H1), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.W, castle.square), is(false));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.W, castle.rSquare), is(false));
            }

            @Test
            public void cantMovePinnedPieceAwayFromPin() {
                GameState.loadFen("rnb1kbnr/ppp1pppp/4q3/3p4/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 0 1");

                boolean moveMade = GameState.makeMove(new Move(Square.E4, Square.D5), unmakeDetails);

                MatcherAssert.assertThat(moveMade, is(false));
                MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(Color.B.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.E4), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.B, Square.D5), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.D5), is(false));
            }

            @Test
            public void cantMoveKingIntoCheck() {
                GameState.loadFen("rnb1kbnr/pppp1ppp/8/4p3/4PP1q/8/PPPPK1PP/RNBQ1BNR w KQkq - 0 1");

                boolean moveMade = GameState.makeMove(new Move(Square.E2, Square.F2), unmakeDetails);

                MatcherAssert.assertThat(moveMade, is(false));
                MatcherAssert.assertThat(GameState.board[Square.E2.idx], is(Color.W.id | Piece.KING.id));

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.W, Square.E2), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.KING, Color.W, Square.F2), is(false));
            }

            @Test
            public void cantMakeMoveIfItDoesntBlockOrCaptureCheck() {
                GameState.loadFen("rnb1kbnr/ppp1pppp/8/3p4/4q3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");

                boolean moveMade = GameState.makeMove(new Move(Square.D2, Square.D4), unmakeDetails);

                MatcherAssert.assertThat(moveMade, is(false));
                MatcherAssert.assertThat(GameState.board[Square.D2.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.D4.idx], is(0));
            }
        }

        @Test
        public void enPassantCapturesResetCorrectlyOnIllegalMove() {
            GameState.loadFen("rq2k2r/2p2ppp/2npbn2/pPb1p3/2B1P3/2NPBN2/1KPQ1PPP/R6R w KQkq - 0 1");

            GameState.makeMove(new Move(Square.B5, Square.A6), unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.A5.idx], is(Color.B.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.board[Square.B5.idx], is(Color.W.id | Piece.PAWN.id));
        }

        @Nested
        class canDealWithChecks {
            @Test
            public void byCapturing() {
                GameState.loadFen("rnb1kbnr/ppp1pppp/8/3p4/4q3/3P4/PPP2PPP/RNBQKBNR w KQkq - 0 1");

                boolean moveMade = GameState.makeMove(new Move(Square.D3, Square.E4), unmakeDetails);

                MatcherAssert.assertThat(moveMade, is(true));
                MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.D3.idx], is(0));
            }

            @Test
            public void byBlocking() {
                GameState.loadFen("rnb1kbnr/pppp1ppp/8/4p3/4PP1q/8/PPPP2PP/RNBQKBNR w KQkq - 0 1");

                boolean moveMade = GameState.makeMove(new Move(Square.G2, Square.G3), unmakeDetails);

                MatcherAssert.assertThat(moveMade, is(true));
                MatcherAssert.assertThat(GameState.board[Square.G3.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.G2.idx], is(0));
            }
        }

        @Nested
        class handlesEnPassant {
            @Test
            public void togglesAfterPawnMovesTwoSpaces() {
                Move wMove = new Move(Square.E2, Square.E4);
                GameState.makeMove(wMove, unmakeDetails);

                MatcherAssert.assertThat(GameState.enPassant, is(Square.E3));

                Move bMove = new Move(Square.E7, Square.E5);
                GameState.makeMove(bMove, unmakeDetails);

                MatcherAssert.assertThat(GameState.enPassant, is(Square.E6));

                GameState.makeMove(new Move(Square.D2, Square.D3), unmakeDetails);
                MatcherAssert.assertThat(GameState.enPassant, nullValue());
            }

            @Test
            public void captureWorks() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 1");

                GameState.makeMove(new Move(Square.E5, Square.D6), unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(0));
                MatcherAssert.assertThat(GameState.board[Square.D6.idx], is(Color.W.id | Piece.PAWN.id));
            }
        }

        @Nested
        class halfmoves {
            @Test
            public void incrementsWhenPieceMoves() {
                GameState.makeMove(new Move(Square.B1, Square.C3), unmakeDetails);

                MatcherAssert.assertThat(GameState.halfmoves, is(1));
            }

            @Test
            public void resetsOnCapture() {
                GameState.loadFen("rnbqk1nr/ppp2ppp/4p3/3pP3/1b6/2N5/PPPP1PPP/R1BQKBNR b KQkq - 0 1");

                GameState.makeMove(new Move(Square.B4, Square.C3), unmakeDetails);

                MatcherAssert.assertThat(GameState.halfmoves, is(0));
            }

            @Test
            public void resetsWhenPawnMoves() {
                GameState.makeMove(new Move(Square.E2, Square.E4), unmakeDetails);

                MatcherAssert.assertThat(GameState.halfmoves, is(0));
            }
        }

        @Nested
        class unmakeDetails {
            @Test
            public void hasCorrectFromToSquares() {
                GameState.makeMove(new Move(Square.E2, Square.E4), unmakeDetails);

                UnmakeDetails expected = new UnmakeDetails();
                expected.from = Square.E2;
                expected.to = Square.E4;

                MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
            }

            @Test
            public void includesCapture() {
                GameState.loadFen("rnbqk1nr/ppp2ppp/4p3/3p4/1b2P3/2N5/PPPP1PPP/R1BQKBNR w KQkq - 0 1");
                GameState.makeMove(new Move(Square.E4, Square.D5), unmakeDetails);

                UnmakeDetails expected = new UnmakeDetails();
                expected.from = Square.E4;
                expected.to = Square.D5;
                expected.capturedPiece = Color.B.id | Piece.PAWN.id;
                expected.capturePieceSquare = Square.D5;

                MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
            }

            @Test
            public void worksWithEnPassant() {
                GameState.loadFen("rnbqk1nr/ppp3pp/4p3/3pPp2/1b6/2N5/PPPP1PPP/R1BQKBNR w KQkq f6 0 1");
                GameState.makeMove(new Move(Square.E5, Square.F6), unmakeDetails);

                UnmakeDetails expected = new UnmakeDetails();
                expected.from = Square.E5;
                expected.to = Square.F6;
                expected.capturedPiece = Color.B.id | Piece.PAWN.id;
                expected.capturePieceSquare = Square.F5;
                expected.prevEnPassant = Square.F6;

                MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
            }

            @Nested
            class castle {
                @Nested
                class kingside {
                    @Test
                    public void whiteside() {
                        GameState.loadFen("rnbqkbnr/pppppppp/8/8/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 1");

                        GameState.makeMove(new Move(Square.E1, Castle.W_K), unmakeDetails);

                        UnmakeDetails expected = new UnmakeDetails();
                        expected.from = Square.E1;
                        expected.castle = Castle.W_K;

                        MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
                    }

                    @Test
                    public void blackside() {
                        GameState.loadFen("rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 1");

                        GameState.makeMove(new Move(Square.E8, Castle.B_k), unmakeDetails);

                        UnmakeDetails expected = new UnmakeDetails();
                        expected.from = Square.E8;
                        expected.castle = Castle.B_k;

                        MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
                    }
                }

                @Nested
                class queenside {
                    @Test
                    public void whiteside() {
                        GameState.loadFen("rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");

                        GameState.makeMove(new Move(Square.E1, Castle.W_Q), unmakeDetails);

                        UnmakeDetails expected = new UnmakeDetails();
                        expected.from = Square.E1;
                        expected.castle = Castle.W_Q;

                        MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
                    }

                    @Test
                    public void blackside() {
                        GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R b KQkq - 0 1");

                        GameState.makeMove(new Move(Square.E8, Castle.B_q), unmakeDetails);

                        UnmakeDetails expected = new UnmakeDetails();
                        expected.from = Square.E8;
                        expected.castle = Castle.B_q;

                        MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
                    }
                }
            }

            @Test
            public void promotionWorks() {
                GameState.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");

                GameState.makeMove(new Move(Square.B7, Square.A8, Piece.QUEEN), unmakeDetails);

                UnmakeDetails expected = new UnmakeDetails();
                expected.from = Square.B7;
                expected.to = Square.A8;
                expected.capturedPiece = Color.B.id | Piece.ROOK.id;
                expected.capturePieceSquare = Square.A8;
                expected.isPromote = true;

                MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
            }
        }
    }

    @Nested
    class unmakeDetails {
        UnmakeDetails unmakeDetails = new UnmakeDetails();

        @BeforeEach
        public void init() {
            unmakeDetails.reset();
        }

        @Test
        public void regularMove() {
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

            GameState.makeMove(new Move(Square.E2, Square.E4), unmakeDetails);
            GameState.unmakeMove(unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.E2.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.board[Square.E4.idx], not(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(Utils.findIndexOf(Square.E4, GameState.pieceList.get(Color.W)), is(-1));
            MatcherAssert.assertThat(Utils.findIndexOf(Square.E2, GameState.pieceList.get(Color.W)), not(-1));
        }

        @Test
        public void capture() {
            GameState.loadFen("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
            GameState.makeMove(new Move(Square.E4, Square.D5), unmakeDetails);
            GameState.unmakeMove(unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(Color.B.id | Piece.PAWN.id));
            MatcherAssert.assertThat(Utils.findIndexOf(Square.E4, GameState.pieceList.get(Color.W)), not(-1));
            MatcherAssert.assertThat(Utils.findIndexOf(Square.D5, GameState.pieceList.get(Color.B)), not(-1));
        }

        @Test
        public void promotion() {
            GameState.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");

            GameState.makeMove(new Move(Square.B7, Square.A8, Piece.QUEEN), unmakeDetails);
            GameState.unmakeMove(unmakeDetails);
            System.out.println(unmakeDetails);
            MatcherAssert.assertThat(GameState.board[Square.A8.idx], is(Color.B.id | Piece.ROOK.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.B, Square.A8), is(true));

            MatcherAssert.assertThat(GameState.board[Square.B7.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.B7), is(true));
        }

        @Nested
        class castleMoves {
            @BeforeEach
            public void init() {
                GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");
            }

            @Nested
            class kingside {
                @Test
                public void whiteside() {
                    GameState.makeMove(new Move(Square.E1, Castle.W_K), unmakeDetails);
                    GameState.unmakeMove(unmakeDetails);
                    MatcherAssert.assertThat(GameState.board[Square.E1.idx], is(Color.W.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[Square.G1.idx], not(Color.W.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[Square.H1.idx], is(Color.W.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(GameState.board[Square.F1.idx], not(Color.W.id | Piece.ROOK.id));

                    MatcherAssert.assertThat(GameState.pieceList.get(Color.W)[50], is(Square.E1));
                    MatcherAssert.assertThat(Utils.findIndexOf(Square.H1, GameState.pieceList.get(Color.W)),
                            not(-1));
                }

                @Test
                public void blackside() {
                    GameState.activeColor = Color.B;
                    GameState.makeMove(new Move(Square.E8, Castle.B_k), unmakeDetails);
                    GameState.unmakeMove(unmakeDetails);
                    MatcherAssert.assertThat(GameState.board[Square.E8.idx], is(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[Square.G8.idx], not(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[Square.H8.idx], is(Color.B.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(GameState.board[Square.F8.idx], not(Color.B.id | Piece.ROOK.id));

                    MatcherAssert.assertThat(GameState.pieceList.get(Color.B)[50], is(Square.E8));
                    MatcherAssert.assertThat(Utils.findIndexOf(Square.H8, GameState.pieceList.get(Color.B)),
                            not(-1));
                }
            }

            @Nested
            class queenside {
                @Test
                public void whiteside() {
                    GameState.makeMove(new Move(Square.E1, Castle.W_Q), unmakeDetails);
                    GameState.unmakeMove(unmakeDetails);
                    MatcherAssert.assertThat(GameState.board[Square.E1.idx], is(Color.W.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[Square.D1.idx], not(Color.W.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[Square.A1.idx], is(Color.W.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(GameState.board[Square.C1.idx], not(Color.W.id | Piece.ROOK.id));

                    MatcherAssert.assertThat(GameState.pieceList.get(Color.W)[50], is(Square.E1));
                    MatcherAssert.assertThat(Utils.findIndexOf(Square.A1, GameState.pieceList.get(Color.W)),
                            not(-1));
                }

                @Test
                public void blackside() {
                    GameState.activeColor = Color.B;
                    GameState.makeMove(new Move(Square.E8, Castle.B_q), unmakeDetails);
                    GameState.unmakeMove(unmakeDetails);
                    MatcherAssert.assertThat(GameState.board[Square.E8.idx], is(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[Square.C8.idx], not(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(GameState.board[Square.A8.idx], is(Color.B.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(GameState.board[Square.D8.idx], not(Color.B.id | Piece.ROOK.id));

                    MatcherAssert.assertThat(GameState.pieceList.get(Color.B)[50], is(Square.E8));
                    MatcherAssert.assertThat(Utils.findIndexOf(Square.A8, GameState.pieceList.get(Color.B)),
                            not(-1));
                }
            }
        }
    }

    @Nested
    class isDrawByInsufficientMaterial {
        @Test
        public void kingvking() {
            GameState.loadFen("3k4/8/8/8/8/8/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(GameState.isDrawByInsufficientMaterial(), is(true));
        }

        @Test
        public void bkvbk() {
            GameState.loadFen("3k4/8/4b3/8/8/5B2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(GameState.isDrawByInsufficientMaterial(), is(true));
        }

        @Test
        public void kvbk() {
            GameState.loadFen("3k4/8/8/8/8/5B2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(GameState.isDrawByInsufficientMaterial(), is(true));
        }

        @Test
        public void kvnk() {
            GameState.loadFen("3k4/8/8/8/8/5N2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(GameState.isDrawByInsufficientMaterial(), is(true));
        }

        @Test
        public void twoPiecesReturnsFalse() {
            GameState.loadFen("3k4/8/8/8/8/4NN2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(GameState.isDrawByInsufficientMaterial(), is(false));
        }

        @Test
        public void returnsFalseIfPawnRookOrQueenIsOnBoard() {
            GameState.loadFen("3k4/8/8/8/8/4NN2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(GameState.isDrawByInsufficientMaterial(), is(false));

            GameState.loadFen("3k4/8/8/8/8/3QN3/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(GameState.isDrawByInsufficientMaterial(), is(false));

            GameState.loadFen("3k4/8/8/8/8/2R1N3/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(GameState.isDrawByInsufficientMaterial(), is(false));
        }
    }
}