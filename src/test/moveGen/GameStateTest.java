package test.moveGen;

import jdk.jfr.Description;
import main.moveGen.*;
import main.utils.Utils;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;

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
    class filterOutIllegalMoves {
        @Test
        public void cantCastleIntoCheck() {
            GameState.loadFen("rnbqkbn1/pppppppp/6r1/8/8/5N2/PPPPPP1P/RNBQK2R w KQkq - 0 1");

            List<Integer> validMoves = GameState.filterOutValidMoves(MoveGen.pseudoLegalForKing(GameState.board,
                    GameState.pieceList.get(Color.W)[50], Color.W, GameState.castleRights,
                    GameState.pieceList.get(Color.B)));

            MatcherAssert.assertThat(GameState.board[Square.E1.idx], is(Color.W.id | Piece.KING.id));
            MatcherAssert.assertThat(GameState.board[Square.H1.idx], is(Color.W.id | Piece.ROOK.id));

            MatcherAssert.assertThat(validMoves.size(), is(1));
            MatcherAssert.assertThat(validMoves.get(0), is((Square.E1.idx << 7) | Square.F1.idx));
        }

        @Test
        public void cantMovePinnedPieceAwayFromPin() {
            GameState.loadFen("rnb1kbnr/ppp1pppp/4q3/3p4/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 0 1");

            List<Integer> validMoves = GameState.filterOutValidMoves(MoveGen.pseudoLegalForPawn(GameState.board,
                    Square.E4, Color.W, GameState.enPassant));

            MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(Color.B.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));

            MatcherAssert.assertThat(validMoves.size(), is(1));
            MatcherAssert.assertThat(validMoves.get(0), is((Square.E4.idx << 7) | Square.E5.idx));
        }

        @Test
        public void cantMoveKingIntoCheck() {
            GameState.loadFen("rnb1kbnr/pppp1ppp/8/4p3/4PP1q/8/PPPPK1PP/RNBQ1BNR w KQkq - 0 1");

            List<Integer> validMoves = GameState.filterOutValidMoves(MoveGen.pseudoLegalForKing(GameState.board,
                    GameState.pieceList.get(Color.W)[50], Color.W, GameState.castleRights,
                    GameState.pieceList.get(Color.B)));

            MatcherAssert.assertThat(GameState.board[Square.E2.idx], is(Color.W.id | Piece.KING.id));

            List<Integer> expected = Arrays.asList((Square.E2.idx << 7) | Square.F3.idx,
                    (Square.E2.idx << 7) | Square.E3.idx, (Square.E2.idx << 7) | Square.D3.idx);

            MatcherAssert.assertThat(validMoves.size(), is(3));
            MatcherAssert.assertThat(validMoves.containsAll(expected), is(true));
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
            GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.E4), is(true));
        }

        @Test
        public void capturesWork() {
            GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);

            GameState.makeMove((Square.D7.idx << 7) | Square.D5.idx, unmakeDetails);

            GameState.makeMove((Square.E4.idx << 7) | Square.D5.idx, unmakeDetails);
            MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.D5), is(true));
        }

        @Test
        public void changesTurn() {
            GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);

            MatcherAssert.assertThat(GameState.activeColor, is(Color.B));
        }

        @Nested
        class promotion {
            @Test
            public void promotionWorks() {
                GameState.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");

                GameState.makeMove(((Piece.QUEEN.id << 18) | Square.B7.idx << 7) | Square.A8.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.A8.idx], is(Color.W.id | Piece.QUEEN.id));
                MatcherAssert.assertThat(GameState.board[Square.B7.idx], is(0));
                MatcherAssert.assertThat(GameState.pieceList.get(Color.W)[41], is(Square.A8));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.B7), is(false));
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
                    GameState.makeMove((castle.value << 14) | Square.E1.idx << 7, unmakeDetails);

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
                    GameState.makeMove((castle.value << 14) | Square.E8.idx << 7, unmakeDetails);

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
                    GameState.makeMove((castle.value << 14) | Square.E1.idx << 7, unmakeDetails);

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
                    GameState.makeMove((castle.value << 14) | Square.E8.idx << 7, unmakeDetails);

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
                    GameState.makeMove((Square.H1.idx << 7) | Square.G1.idx, unmakeDetails);

                    MatcherAssert.assertThat(GameState.castleRights, is(15 ^ Castle.W_K.value));
                }

                @Test
                public void blackside() {
                    GameState.activeColor = Color.B;
                    GameState.makeMove((Square.H8.idx << 7) | Square.G8.idx, unmakeDetails);

                    MatcherAssert.assertThat(GameState.castleRights, is(15 ^ Castle.B_k.value));
                }
            }

            @Nested
            class queenside {
                @Test
                public void whiteside() {
                    GameState.makeMove((Square.A1.idx << 7) | Square.B1.idx, unmakeDetails);

                    MatcherAssert.assertThat(GameState.castleRights, is(15 ^ Castle.W_Q.value));
                }

                @Test
                public void blackside() {
                    GameState.activeColor = Color.B;
                    GameState.makeMove((Square.A8.idx << 7) | Square.B8.idx, unmakeDetails);

                    MatcherAssert.assertThat(GameState.castleRights, is(15 ^ Castle.B_q.value));
                }
            }
        }

        @Nested
        class captureByEnPassantWorks {
            @Test
            public void whiteside() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 1");

                GameState.makeMove((Square.E5.idx << 7) | Square.D6.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.D6.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(0));

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.D6), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.B, Square.D5), is(false));
            }

            @Test
            public void blackside() {
                GameState.loadFen("rnbqkbnr/pppp1ppp/8/8/3Pp3/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1");

                GameState.makeMove((Square.E4.idx << 7) | Square.D3.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.D3.idx], is(Color.B.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.D4.idx], is(0));

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.B, Square.D3), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.D4), is(false));
            }
        }

        @Nested
        class canDealWithChecks {
            @Test
            public void byCapturing() {
                GameState.loadFen("rnb1kbnr/ppp1pppp/8/3p4/4q3/3P4/PPP2PPP/RNBQKBNR w KQkq - 0 1");

                GameState.makeMove((Square.D3.idx << 7) | Square.E4.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.D3.idx], is(0));
            }

            @Test
            public void byBlocking() {
                GameState.loadFen("rnb1kbnr/pppp1ppp/8/4p3/4PP1q/8/PPPP2PP/RNBQKBNR w KQkq - 0 1");

                GameState.makeMove((Square.G2.idx << 7) | Square.G3.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.G3.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(GameState.board[Square.G2.idx], is(0));
            }
        }

        @Nested
        class handlesEnPassant {
            @Test
            public void togglesAfterPawnMovesTwoSpaces() {
                GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.enPassant, is(Square.E3));

                GameState.makeMove((Square.E7.idx << 7) | Square.E5.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.enPassant, is(Square.E6));

                GameState.makeMove((Square.D2.idx << 7) | Square.D3.idx, unmakeDetails);
                MatcherAssert.assertThat(GameState.enPassant, is(Square.NULL));
            }

            @Test
            public void captureWorks() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 1");

                GameState.makeMove((Square.E5.idx << 7) | Square.D6.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(0));
                MatcherAssert.assertThat(GameState.board[Square.D6.idx], is(Color.W.id | Piece.PAWN.id));
            }
        }

        @Nested
        class halfmoves {
            @Test
            public void incrementsWhenPieceMoves() {
                GameState.makeMove((Square.B1.idx << 7) | Square.C3.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.halfmoves, is(1));
            }

            @Test
            public void resetsOnCapture() {
                GameState.loadFen("rnbqk1nr/ppp2ppp/4p3/3pP3/1b6/2N5/PPPP1PPP/R1BQKBNR b KQkq - 0 1");

                GameState.makeMove((Square.B4.idx << 7) | Square.C3.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.halfmoves, is(0));
            }

            @Test
            public void resetsWhenPawnMoves() {
                GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);

                MatcherAssert.assertThat(GameState.halfmoves, is(0));
            }
        }

        @Nested
        class unmakeDetails {
            @Test
            public void hasCorrectFromToSquares() {
                GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);

                UnmakeDetails expected = new UnmakeDetails();
                expected.from = Square.E2;
                expected.to = Square.E4;

                MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
            }

            @Test
            public void includesCapture() {
                GameState.loadFen("rnbqk1nr/ppp2ppp/4p3/3p4/1b2P3/2N5/PPPP1PPP/R1BQKBNR w KQkq - 0 1");
                GameState.makeMove((Square.E4.idx << 7) | Square.D5.idx, unmakeDetails);

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
                GameState.makeMove((Square.E5.idx << 7) | Square.F6.idx, unmakeDetails);

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

                        GameState.makeMove(((Castle.W_K.value << 14) | Square.E1.idx << 7) | Castle.W_K.square.idx,
                                unmakeDetails);

                        UnmakeDetails expected = new UnmakeDetails();
                        expected.from = Square.E1;
                        expected.to = Castle.W_K.square;
                        expected.castle = Castle.W_K;

                        MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
                    }

                    @Test
                    public void blackside() {
                        GameState.loadFen("rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 1");

                        GameState.makeMove(((Castle.B_k.value << 14) | Square.E8.idx << 7) | Castle.B_k.square.idx,
                                unmakeDetails);

                        UnmakeDetails expected = new UnmakeDetails();
                        expected.from = Square.E8;
                        expected.to = Castle.B_k.square;
                        expected.castle = Castle.B_k;

                        MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
                    }
                }

                @Nested
                class queenside {
                    @Test
                    public void whiteside() {
                        GameState.loadFen("rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");

                        GameState.makeMove(((Castle.W_Q.value << 14) | Square.E1.idx << 7) | Castle.W_Q.square.idx,
                                unmakeDetails);

                        UnmakeDetails expected = new UnmakeDetails();
                        expected.from = Square.E1;
                        expected.to = Castle.W_Q.square;
                        expected.castle = Castle.W_Q;

                        MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
                    }

                    @Test
                    public void blackside() {
                        GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R b KQkq - 0 1");

                        GameState.makeMove(((Castle.B_q.value << 14) | Square.E8.idx << 7) | Castle.B_q.square.idx,
                                unmakeDetails);

                        UnmakeDetails expected = new UnmakeDetails();
                        expected.from = Square.E8;
                        expected.to = Castle.B_q.square;
                        expected.castle = Castle.B_q;

                        MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
                    }
                }
            }

            @Test
            public void promotionWorks() {
                GameState.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");

                GameState.makeMove(((Piece.QUEEN.id << 18) | Square.B7.idx << 7) | Square.A8.idx, unmakeDetails);

                UnmakeDetails expected = new UnmakeDetails();
                expected.from = Square.B7;
                expected.to = Square.A8;
                expected.capturedPiece = Color.B.id | Piece.ROOK.id;
                expected.capturePieceSquare = Square.A8;
                expected.isPromote = true;

                MatcherAssert.assertThat(unmakeDetails.equals(expected), is(true));
            }
        }

        @Nested
        class zobristHash {
            @Test
            public void worksForBasicMoves() {
                GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);
                long hash = GameState.zobristHash;

                GameState.loadFen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
                MatcherAssert.assertThat(hash, is(GameState.zobristHash));
            }

            @Test
            public void worksForCaptures() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
                GameState.makeMove((Square.E4.idx << 7) | Square.D5.idx, unmakeDetails);
                long hash = GameState.zobristHash;

                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
                MatcherAssert.assertThat(hash, is(GameState.zobristHash));
            }

            @Test
            public void worksForBlackMoves() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
                GameState.makeMove((Square.E7.idx << 7) | Square.E6.idx, unmakeDetails);

                long hash = GameState.zobristHash;

                GameState.loadFen("rnbqkbnr/ppp2ppp/4p3/3P4/8/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");

                MatcherAssert.assertThat(hash, is(GameState.zobristHash));
            }

            @Test
            public void differentHashKeysForDifferentActiveColor() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
                GameState.makeMove((Square.E7.idx << 7) | Square.E6.idx, unmakeDetails);

                long hash = GameState.zobristHash;

                GameState.loadFen("rnbqkbnr/ppp2ppp/4p3/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");

                MatcherAssert.assertThat(hash, not(GameState.zobristHash));
            }

            @Test
            public void worksForEnPassant() {
                GameState.loadFen("1r2k2r/1ppq1ppp/2npbn2/p1b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq a6 0 1");
                GameState.makeMove((Square.A2.idx << 7) | Square.A4.idx, unmakeDetails);
                long hash = GameState.zobristHash;

                GameState.loadFen("1r2k2r/1ppq1ppp/2npbn2/p1b1p3/P1B1P3/2NPBN2/1PPQ1PPP/R3K2R b KQkq a3 0 1");
                MatcherAssert.assertThat(hash, not(GameState.zobristHash));
            }

            @Test
            public void worksForPromotion() {
                GameState.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");
                GameState.makeMove((Piece.QUEEN.id << 18) | Square.B7.idx << 7 | Square.A8.idx, unmakeDetails);
                long hash = GameState.zobristHash;

                GameState.loadFen("Q3k2r/p1pq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R b KQk - 0 1");
                MatcherAssert.assertThat(hash, is(GameState.zobristHash));
            }

            @Nested
            class castleRightsAreHashed {
                @BeforeEach
                public void init() {
                    GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");
                }

                @Test
                public void whiteside() {
                    GameState.makeMove((Square.E1.idx << 7) | Square.F1.idx, unmakeDetails);
                    long hash = GameState.zobristHash;

                    GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R4K1R b kq - 0 1");
                    MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                }

                @Test
                public void blackside() {
                    GameState.activeColor = Color.B;
                    GameState.zobristHash ^= ZobristKey.SIDE;
                    GameState.makeMove((Square.E8.idx << 7) | Square.F8.idx, unmakeDetails);
                    long hash = GameState.zobristHash;

                    GameState.loadFen("r4k1r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQ - 0 1");
                    MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                }

                @Nested
                class whenRooksAreMoved {
                    @Nested
                    class kingside {
                        @Test
                        public void whiteside() {
                            GameState.makeMove((Square.H1.idx << 7) | Square.G1.idx, unmakeDetails);
                            long hash = GameState.zobristHash;

                            GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K1R1 b Qkq - 0 1");
                            MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                        }

                        @Test
                        public void blackside() {
                            GameState.activeColor = Color.B;
                            GameState.zobristHash ^= ZobristKey.SIDE;
                            GameState.makeMove((Square.H8.idx << 7) | Square.F8.idx, unmakeDetails);
                            long hash = GameState.zobristHash;

                            GameState.loadFen("r3kr2/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQq - 0 1");
                            MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                        }
                    }

                    @Nested
                    class queenside {
                        @Test
                        public void whiteside() {
                            GameState.makeMove((Square.A1.idx << 7) | Square.B1.idx, unmakeDetails);
                            long hash = GameState.zobristHash;

                            GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/1R2K2R b Kkq - 0 1");
                            MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                        }

                        @Test
                        public void blackside() {
                            GameState.activeColor = Color.B;
                            GameState.zobristHash ^= ZobristKey.SIDE;
                            GameState.makeMove((Square.A8.idx << 7) | Square.B8.idx, unmakeDetails);
                            long hash = GameState.zobristHash;

                            GameState.loadFen("1r2k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQk - 0 1");
                            MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                        }
                    }
                }
            }

            @Nested
            class castlePiecesAreHashCorrectly {
                @BeforeEach
                public void init() {
                    GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");
                }

                @Nested
                @Description("castle works kingside")
                class kingside {
                    @Test
                    public void whiteside() {
                        Castle castle = Castle.W_K;
                        GameState.makeMove((castle.value << 14) | Square.E1.idx << 7, unmakeDetails);
                        long hash = GameState.zobristHash;

                        GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R4RK1 b kq - 0 1");
                        MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                    }

                    @Test
                    void blackside() {
                        GameState.activeColor = Color.B;
                        GameState.zobristHash ^= ZobristKey.SIDE;
                        Castle castle = Castle.B_k;
                        GameState.makeMove((castle.value << 14) | Square.E8.idx << 7, unmakeDetails);
                        long hash = GameState.zobristHash;

                        GameState.loadFen("r4rk1/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQ - 0 1");
                        MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                    }
                }
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
        public void test() {
            GameState.loadFen("rnbqkbnr/p1pppppp/8/Pp6/8/8/1PPPPPPP/RNBQKBNR w KQkq b6 0 1");
            GameState.makeMove((Square.A5.idx << 7) | Square.B6.idx, unmakeDetails);
            GameState.unmakeMove(unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.B6.idx], is(0));

            unmakeDetails.reset();
            GameState.loadFen("rnbqkbnr/pppppppp/8/1P6/8/8/P1PPPPPP/RNBQKBNR w KQkq - 0 1");
            GameState.makeMove((Square.B5.idx << 7) | Square.B6.idx, unmakeDetails);
            GameState.unmakeMove(unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.B6.idx], is(0));
        }

        @Test
        public void regularMove() {
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

            GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);
            GameState.unmakeMove(unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.E2.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.board[Square.E4.idx], not(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(Utils.findIndexOf(Square.E4, GameState.pieceList.get(Color.W)), is(-1));
            MatcherAssert.assertThat(Utils.findIndexOf(Square.E2, GameState.pieceList.get(Color.W)), not(-1));
        }

        @Test
        public void capture() {
            GameState.loadFen("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
            GameState.makeMove((Square.E4.idx << 7) | Square.D5.idx, unmakeDetails);
            GameState.unmakeMove(unmakeDetails);

            MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.board[Square.D5.idx], is(Color.B.id | Piece.PAWN.id));
            MatcherAssert.assertThat(Utils.findIndexOf(Square.E4, GameState.pieceList.get(Color.W)), not(-1));
            MatcherAssert.assertThat(Utils.findIndexOf(Square.D5, GameState.pieceList.get(Color.B)), not(-1));
        }

        @Test
        public void promotion() {
            GameState.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");

            GameState.makeMove(((Piece.QUEEN.id << 18) | Square.B7.idx << 7) | Square.A8.idx, unmakeDetails);
            GameState.unmakeMove(unmakeDetails);
            MatcherAssert.assertThat(GameState.board[Square.A8.idx], is(Color.B.id | Piece.ROOK.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.ROOK, Color.B, Square.A8), is(true));

            MatcherAssert.assertThat(GameState.board[Square.B7.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.PAWN, Color.W, Square.B7), is(true));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(Piece.QUEEN, Color.W, Square.B7), is(false));
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
                    GameState.makeMove((Castle.W_K.value << 14) | Square.E1.idx << 7, unmakeDetails);
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
                    GameState.makeMove((Castle.B_k.value << 14) | Square.E8.idx << 7, unmakeDetails);
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
                    GameState.makeMove((Castle.W_Q.value << 14) | Square.E1.idx << 7, unmakeDetails);
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
                    GameState.makeMove((Castle.B_q.value << 14) | Square.E8.idx << 7, unmakeDetails);
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

        @Nested
        class undoZobristHash {
            @Test
            public void worksForBasicMoves() {
                GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                long hash = GameState.zobristHash;

                GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);
                GameState.unmakeMove(unmakeDetails);

                MatcherAssert.assertThat(GameState.zobristHash, is(hash));
            }

            @Test
            public void worksForCaptures() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
                long hash = GameState.zobristHash;

                GameState.makeMove((Square.E4.idx << 7) | Square.D5.idx, unmakeDetails);
                GameState.unmakeMove(unmakeDetails);

                MatcherAssert.assertThat(GameState.zobristHash, is(hash));
            }

            @Test
            public void worksForWhenBlackIsActiveColor() {
                GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
                long hash = GameState.zobristHash;

                GameState.makeMove((Square.E7.idx << 7) | Square.E5.idx, unmakeDetails);
                GameState.unmakeMove(unmakeDetails);

                MatcherAssert.assertThat(GameState.zobristHash, is(hash));
            }

            @Test
            public void restoresPrevEnPassant() {
                GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq d3 0 1");
                long hash = GameState.zobristHash;

                GameState.makeMove((Square.E2.idx << 7) | Square.E4.idx, unmakeDetails);
                GameState.unmakeMove(unmakeDetails);

                MatcherAssert.assertThat(GameState.zobristHash, is(hash));
            }

            @Nested
            class retoresPrevCastleRights {
                @BeforeEach
                public void init() {
                    GameState.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");
                }

                @Nested
                class whenKingMoves {
                    @Test
                    public void whiteside() {
                        long hash = GameState.zobristHash;
                        GameState.makeMove((Square.E1.idx << 7) | Square.F1.idx, unmakeDetails);
                        GameState.unmakeMove(unmakeDetails);
                        MatcherAssert.assertThat(GameState.zobristHash, is(hash));
                    }

                    @Test
                    public void blackside() {
                        GameState.activeColor = Color.B;
                        GameState.zobristHash ^= ZobristKey.SIDE;
                        long hash = GameState.zobristHash;
                        GameState.makeMove((Square.E8.idx << 7) | Square.F8.idx, unmakeDetails);
                        GameState.unmakeMove(unmakeDetails);
                        MatcherAssert.assertThat(GameState.zobristHash, is(hash));
                    }
                }

                @Nested
                class whenRooksAreMoved {
                    @Nested
                    class kingside {
                        @Test
                        public void whiteside() {
                            long hash = GameState.zobristHash;
                            GameState.makeMove((Square.H1.idx << 7) | Square.G1.idx, unmakeDetails);
                            GameState.unmakeMove(unmakeDetails);

                            MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                        }

                        @Test
                        public void blackside() {
                            GameState.activeColor = Color.B;
                            GameState.zobristHash ^= ZobristKey.SIDE;
                            long hash = GameState.zobristHash;

                            GameState.makeMove((Square.H8.idx << 7) | Square.F8.idx, unmakeDetails);
                            GameState.unmakeMove(unmakeDetails);

                            MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                        }
                    }

                    @Nested
                    class queenside {
                        @Test
                        public void whiteside() {
                            long hash = GameState.zobristHash;
                            GameState.makeMove((Square.A1.idx << 7) | Square.B1.idx, unmakeDetails);
                            GameState.unmakeMove(unmakeDetails);

                            MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                        }

                        @Test
                        public void blackside() {
                            GameState.activeColor = Color.B;
                            GameState.zobristHash ^= ZobristKey.SIDE;
                            long hash = GameState.zobristHash;

                            GameState.makeMove((Square.A8.idx << 7) | Square.B8.idx, unmakeDetails);
                            GameState.unmakeMove(unmakeDetails);

                            MatcherAssert.assertThat(hash, is(GameState.zobristHash));
                        }
                    }
                }
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
                        long hash = GameState.zobristHash;
                        GameState.makeMove((Castle.W_K.value << 14) | Square.E1.idx << 7, unmakeDetails);
                        GameState.unmakeMove(unmakeDetails);
                        MatcherAssert.assertThat(GameState.zobristHash, is(hash));
                    }

                    @Test
                    public void blackside() {
                        GameState.activeColor = Color.B;
                        GameState.zobristHash ^= ZobristKey.SIDE;
                        long hash = GameState.zobristHash;
                        GameState.makeMove((Castle.B_k.value << 14) | Square.E8.idx << 7, unmakeDetails);
                        GameState.unmakeMove(unmakeDetails);
                        MatcherAssert.assertThat(GameState.zobristHash, is(hash));
                    }
                }
            }

            @Test
            public void promotion() {
                GameState.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");
                long hash = GameState.zobristHash;

                GameState.makeMove(((Piece.QUEEN.id << 18) | Square.B7.idx << 7) | Square.A8.idx, unmakeDetails);
                GameState.unmakeMove(unmakeDetails);

                MatcherAssert.assertThat(GameState.zobristHash, is(hash));
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

    @Nested
    class moveGenTest {
        UnmakeDetails moveDetails = new UnmakeDetails();

        @BeforeEach
        public void init() {
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            moveDetails.reset();
        }

        @Nested
        class getValidMoves {
            @Test
            public void includesB7ToB5() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/P2p4/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
                MatcherAssert.assertThat(GameState.getValidMoves(Color.B)
                                                  .contains((Square.B7.idx << 7) | Square.B5.idx), is(true));
            }

            @Test
            public void includesB5ToC6() {
                GameState.loadFen("rnbqkbnr/pp1pp1pp/5p2/1Pp5/8/8/P1PPPPPP/RNBQKBNR w KQkq c6 0 1");
                MatcherAssert.assertThat(GameState.getValidMoves(Color.W)
                                                  .contains((Square.B5.idx << 7) | Square.C6.idx), is(true));
            }

            @Test
            public void kingMovesDontIncludeSquaresProtectedByPawn() {
                GameState.loadFen("rnbqkbnr/ppp1pppp/8/8/3p4/3P4/PPPKPPPP/RNBQ1BNR w KQkq - 0 1");
                List<Integer> validMoves = GameState.getValidMoves(Color.W);

                MatcherAssert.assertThat(validMoves.contains((Square.D2.idx << 7) | Square.C3.idx), is(false));
                MatcherAssert.assertThat(validMoves.contains((Square.D2.idx << 7) | Square.E3.idx), is(false));
            }

            @Test
            public void missingEnPassantMove() {
                GameState.loadFen("rnbqkbnr/2pppppp/p7/Pp6/8/8/1PPPPPPP/RNBQKBNR w KQkq b6 0 1");
                List<Integer> validMoves = GameState.getValidMoves(Color.W);

                MatcherAssert.assertThat(validMoves.contains((Square.A5.idx << 7) | Square.B6.idx), is(true));
            }
        }

        @Test
        public void onePly() {
            MatcherAssert.assertThat(GameState.countNumOfPositions(1), is(20));
        }

        @Test
        public void twoPly() {
            MatcherAssert.assertThat(GameState.countNumOfPositions(2), is(400));
        }

        @Test
        public void threePly() {
            MatcherAssert.assertThat(GameState.countNumOfPositions(3), is(8902));
        }

        @Test
        public void fourPly() {
            MatcherAssert.assertThat(GameState.countNumOfPositions(4), is(197_281));
        }

        @Test
        public void fivePly() {
            MatcherAssert.assertThat(GameState.countNumOfPositions(5), is(4_865_609));
        }

        @Test
        public void debuggingSixPly() {
            GameState.loadFen("rnbqkbnr/p1pppppp/8/1P6/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
            MatcherAssert.assertThat(GameState.board[Square.B5.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.countNumOfPositions(3, true), is(11_204));
        }

        @Test
        public void debuggingPosition() {
            GameState.loadFen("rnbqkbnr/2pppppp/p7/Pp6/8/8/1PPPPPPP/RNBQKBNR w KQkq b6 0 1");
            GameState.makeMove((Square.A5.idx << 7) | Square.B6.idx, moveDetails);

            MatcherAssert.assertThat(GameState.countNumOfPositions(1, true), is(19));
        }

        @Test
        public void knightsCanCaptureCheckGivers() {
            GameState.loadFen("r4k1r/p1pNqpb1/bn2pnp1/3P4/1p2P3/1PN2Q1p/P1PBBPPP/R3K2R b KQ - 0 1");
            List<Integer> validMoves = GameState.getValidMoves(Color.B);

            MatcherAssert.assertThat(validMoves.contains((Square.B6.idx << 7) | Square.D7.idx), is(true));
            MatcherAssert.assertThat(validMoves.contains((Square.F6.idx << 7) | Square.D7.idx), is(true));
        }

        @Test
        public void sixPly() {
            MatcherAssert.assertThat(GameState.countNumOfPositions(6, true), is(119_060_324));
        }

        @Test
        public void position2() {
            GameState.loadFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

            MatcherAssert.assertThat(GameState.countNumOfPositions(4, true), is(193_690_690));
        }

        @Test
        public void position3() {
            GameState.loadFen("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");

            MatcherAssert.assertThat(GameState.countNumOfPositions(6, true), is(11_030_083));
        }

        @Test
        public void position4() {
            GameState.loadFen("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");

            MatcherAssert.assertThat(GameState.countNumOfPositions(5, true), is(15_833_292));

            GameState.loadFen("r2q1rk1/pP1p2pp/Q4n2/bbp1p3/Np6/1B3NBn/pPPP1PPP/R3K2R b KQ - 0 1 ");

            MatcherAssert.assertThat(GameState.countNumOfPositions(5, true), is(15_833_292));
        }

        @Test
        public void position5() {
            GameState.loadFen("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");

            MatcherAssert.assertThat(GameState.countNumOfPositions(5, true), is(89_941_194));
        }

        @Test
        public void position6() {
            GameState.loadFen("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10 ");

            MatcherAssert.assertThat(GameState.countNumOfPositions(5, true), is(164_075_551));
        }
    }
}