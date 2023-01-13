package com.crochess.moveValidator;

import com.crochess.engine0x88.GameState;
import com.crochess.engine0x88.MoveGen;
import com.crochess.engine0x88.ZobristKey;
import com.crochess.engine0x88.moveEval.Psqt;
import com.crochess.engine0x88.types.Castle;
import com.crochess.engine0x88.types.Color;
import com.crochess.engine0x88.types.Piece;
import com.crochess.engine0x88.types.Square;
import com.crochess.engine0x88.utils.Score;
import com.crochess.engine0x88.utils.Utils;
import jdk.jfr.Description;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class GameTest {
    Game game = new Game();

    @Nested
    @Description("game.loadFen works")
    public class loadFen {
        @Test
        public void loadFenWorks() {
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

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
                    game.board);
            Assertions.assertEquals(15,
                    game.castleRights);
        }

        @Test
        public void castleRightsAreCorrect() {
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

            Assertions.assertEquals(15,
                    game.castleRights);
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 0 1");

            Assertions.assertEquals(12,
                    game.castleRights);
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq - 0 1");

            Assertions.assertEquals(3,
                    game.castleRights);
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kk - 0 1");

            Assertions.assertEquals(10,
                    game.castleRights);
        }

        @Test
        public void enPassantWorks() {
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq e2 0 1");

            Assertions.assertEquals(Square.E2,
                    game.enPassant);
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ h5 0 1");

            Assertions.assertEquals(Square.H5,
                    game.enPassant);
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq b3 0 1");

            Assertions.assertEquals(Square.B3,
                    game.enPassant);
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kk c6 0 1");

            Assertions.assertEquals(Square.C6,
                    game.enPassant);
        }

        @Test
        public void pieceListIsCorrect() {
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

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

            MatcherAssert.assertThat(game.pieceList[Color.W.ordinal()],
                    is(wPieceList));
            MatcherAssert.assertThat(game.pieceList[Color.B.ordinal()],
                    is(bPieceList));
        }
    }

    private boolean isPieceOnSquareInPieceList(Game game, Piece piece, Color color, Square square) {
        Square[] list = game.pieceList[color.ordinal()];

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
            game.loadFen("rnbqkbn1/pppppppp/6r1/8/8/5N2/PPPPPP1P/RNBQK2R w KQkq - 0 1");

            List<Integer> validMoves = MoveGen.pseudoLegalForKing(game, game.pieceList[Color.W.ordinal()][50], Color.W,
                    game.pieceList[Color.B.ordinal()]);
            game.filterOutValidMoves(validMoves);

            MatcherAssert.assertThat(game.board[Square.E1.idx], is(Color.W.id | Piece.KING.id));
            MatcherAssert.assertThat(game.board[Square.H1.idx], is(Color.W.id | Piece.ROOK.id));

            MatcherAssert.assertThat(validMoves.size(), is(1));
            MatcherAssert.assertThat(validMoves.get(0), is((Square.E1.idx << 7) | Square.F1.idx));
        }

        @Test
        public void cantMovePinnedPieceAwayFromPin() {
            game.loadFen("rnb1kbnr/ppp1pppp/4q3/3p4/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 0 1");

            List<Integer> validMoves = MoveGen.pseudoLegalForPawn(game, Square.E4, Color.W);
            game.filterOutValidMoves(validMoves);

            MatcherAssert.assertThat(game.board[Square.D5.idx], is(Color.B.id | Piece.PAWN.id));
            MatcherAssert.assertThat(game.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));

            MatcherAssert.assertThat(validMoves.size(), is(1));
            MatcherAssert.assertThat(validMoves.get(0), is((Square.E4.idx << 7) | Square.E5.idx));
        }

        @Test
        public void cantMoveKingIntoCheck() {
            game.loadFen("rnb1kbnr/pppp1ppp/8/4p3/4PP1q/8/PPPPK1PP/RNBQ1BNR w KQkq - 0 1");

            List<Integer> validMoves = MoveGen.pseudoLegalForKing(game, game.pieceList[Color.W.ordinal()][50], Color.W,
                    game.pieceList[Color.B.ordinal()]);
            game.filterOutValidMoves(validMoves);

            MatcherAssert.assertThat(game.board[Square.E2.idx], is(Color.W.id | Piece.KING.id));

            List<Integer> expected = Arrays.asList((Square.E2.idx << 7) | Square.F3.idx,
                    (Square.E2.idx << 7) | Square.E3.idx, (Square.E2.idx << 7) | Square.D3.idx);

            MatcherAssert.assertThat(validMoves.size(), is(3));
            MatcherAssert.assertThat(validMoves.containsAll(expected), is(true));
        }
    }

    @Nested
    @Description("test makeMove")
    public class makeMove {


        @BeforeEach
        public void init() {
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }

        @Test
        public void movesPiece() {
            game.makeMove((Square.E2.idx << 7) | Square.E4.idx);

            MatcherAssert.assertThat(game.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.PAWN, Color.W, Square.E4), is(true));
        }

        @Test
        public void capturesWork() {
            game.makeMove((Square.E2.idx << 7) | Square.E4.idx);

            game.makeMove((Square.D7.idx << 7) | Square.D5.idx);

            game.makeMove((Square.E4.idx << 7) | Square.D5.idx);
            MatcherAssert.assertThat(game.board[Square.D5.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.PAWN, Color.W, Square.D5), is(true));
        }

        @Test
        public void changesTurn() {
            game.makeMove((Square.E2.idx << 7) | Square.E4.idx);

            MatcherAssert.assertThat(game.activeColor, is(Color.B));
        }

        @Nested
        class promotion {
            @Test
            public void promotionWorks() {
                game.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");

                game.makeMove(((Piece.QUEEN.id << 18) | Square.B7.idx << 7) | Square.A8.idx);

                MatcherAssert.assertThat(game.board[Square.A8.idx], is(Color.W.id | Piece.QUEEN.id));
                MatcherAssert.assertThat(game.board[Square.B7.idx], is(0));
                MatcherAssert.assertThat(game.pieceList[Color.W.ordinal()][41], is(Square.A8));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.PAWN, Color.W, Square.B7), is(false));
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
                    game.loadFen("rnbqkbnr/pppppppp/8/8/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 1");
                    Castle castle = Castle.W_K;
                    game.makeMove((castle.value << 14) | Square.E1.idx << 7);

                    MatcherAssert.assertThat(game.board[castle.square.idx], is(Color.W.id | Piece.KING.id));
                    MatcherAssert.assertThat(game.board[castle.rSquare.idx], is(Color.W.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(game.castleRights, is((15 ^ Castle.W_Q.value) ^ Castle.W_K.value));

                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.KING, Color.W, castle.square),
                            is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.ROOK, Color.W, castle.rSquare),
                            is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.KING, Color.W, Square.E1),
                            is(false));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.ROOK, Color.W, Square.H1),
                            is(false));
                }

                @Test
                void blackside() {
                    game.loadFen("rnbqk2r/pppp1ppp/5n2/2b1p3/8/8/PPPPPPPP/RNBQKBNR B KQkq - 0 1");
                    Castle castle = Castle.B_k;
                    game.makeMove((castle.value << 14) | Square.E8.idx << 7);

                    MatcherAssert.assertThat(game.board[castle.square.idx], is(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(game.board[castle.rSquare.idx], is(Color.B.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(game.castleRights, is((15 ^ Castle.B_q.value) ^ Castle.B_k.value));

                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.KING, Color.B, castle.square),
                            is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.ROOK, Color.B, castle.rSquare),
                            is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.KING, Color.B, Square.E8),
                            is(false));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.ROOK, Color.B, Square.H8),
                            is(false));
                }
            }

            @Nested
            @Description("castle works queenside")
            class queenside {
                @Test
                public void whiteside() {
                    game.loadFen("rnbqkbnr/pppppppp/8/8/3P1B2/2N5/PPPQPPPP/R3KBNR w KQkq - 0 1");
                    Castle castle = Castle.W_Q;
                    game.makeMove((castle.value << 14) | Square.E1.idx << 7);

                    MatcherAssert.assertThat(game.board[castle.square.idx], is(Color.W.id | Piece.KING.id));
                    MatcherAssert.assertThat(game.board[castle.rSquare.idx], is(Color.W.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(game.castleRights, is((15 ^ Castle.W_Q.value) ^ Castle.W_K.value));

                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.KING, Color.W, castle.square),
                            is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.ROOK, Color.W, castle.rSquare),
                            is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.KING, Color.W, Square.E1),
                            is(false));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.ROOK, Color.W, Square.A1),
                            is(false));
                }

                @Test
                void blackside() {
                    game.loadFen("r3kbnr/pppqpppp/2n5/3p1b2/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
                    Castle castle = Castle.B_q;
                    game.makeMove((castle.value << 14) | Square.E8.idx << 7);

                    MatcherAssert.assertThat(game.board[castle.square.idx], is(Color.B.id | Piece.KING.id));
                    MatcherAssert.assertThat(game.board[castle.rSquare.idx], is(Color.B.id | Piece.ROOK.id));
                    MatcherAssert.assertThat(game.castleRights, is((15 ^ Castle.B_q.value) ^ Castle.B_k.value));

                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.KING, Color.B, castle.square),
                            is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.ROOK, Color.B, castle.rSquare),
                            is(true));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.KING, Color.B, Square.E8),
                            is(false));
                    MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.ROOK, Color.B, Square.A8),
                            is(false));
                }
            }
        }

        @Nested
        @DisplayName("moving/capturing rooks adjusts castleRights")
        class rooks {
            @BeforeEach
            public void init() {
                game.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");
            }

            @Nested
            class kingside {
                @Test
                public void whiteside() {
                    game.makeMove((Square.H1.idx << 7) | Square.G1.idx);

                    MatcherAssert.assertThat(game.castleRights, is(15 ^ Castle.W_K.value));
                }

                @Test
                public void blackside() {
                    game.activeColor = Color.B;
                    game.makeMove((Square.H8.idx << 7) | Square.G8.idx);

                    MatcherAssert.assertThat(game.castleRights, is(15 ^ Castle.B_k.value));
                }
            }

            @Nested
            class queenside {
                @Test
                public void whiteside() {
                    game.makeMove((Square.A1.idx << 7) | Square.B1.idx);

                    MatcherAssert.assertThat(game.castleRights, is(15 ^ Castle.W_Q.value));
                }

                @Test
                public void blackside() {
                    game.activeColor = Color.B;
                    game.makeMove((Square.A8.idx << 7) | Square.B8.idx);

                    MatcherAssert.assertThat(game.castleRights, is(15 ^ Castle.B_q.value));
                }
            }

            @Test
            public void captures() {
                game.loadFen("rnb1k2r/2pp3K/1p2pp2/p4n1p/P7/1PP5/3PP2P/RNBQ1BNR w kq - 0 1");
                game.makeMove((Square.H7.idx << 7) | Square.H8.idx);

                MatcherAssert.assertThat(game.castleRights, is(3 ^ Castle.B_k.value));
            }
        }

        @Nested
        class captureByEnPassantWorks {
            @Test
            public void whiteside() {
                game.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 1");

                game.makeMove((Square.E5.idx << 7) | Square.D6.idx);

                MatcherAssert.assertThat(game.board[Square.D6.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(game.board[Square.D5.idx], is(0));

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.PAWN, Color.W, Square.D6), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.PAWN, Color.B, Square.D5), is(false));
            }

            @Test
            public void blackside() {
                game.loadFen("rnbqkbnr/pppp1ppp/8/8/3Pp3/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1");

                game.makeMove((Square.E4.idx << 7) | Square.D3.idx);

                MatcherAssert.assertThat(game.board[Square.D3.idx], is(Color.B.id | Piece.PAWN.id));
                MatcherAssert.assertThat(game.board[Square.D4.idx], is(0));

                MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.PAWN, Color.B, Square.D3), is(true));
                MatcherAssert.assertThat(isPieceOnSquareInPieceList(game, Piece.PAWN, Color.W, Square.D4), is(false));
            }
        }

        @Nested
        class canDealWithChecks {
            @Test
            public void byCapturing() {
                game.loadFen("rnb1kbnr/ppp1pppp/8/3p4/4q3/3P4/PPP2PPP/RNBQKBNR w KQkq - 0 1");

                game.makeMove((Square.D3.idx << 7) | Square.E4.idx);

                MatcherAssert.assertThat(game.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(game.board[Square.D3.idx], is(0));
            }

            @Test
            public void byBlocking() {
                game.loadFen("rnb1kbnr/pppp1ppp/8/4p3/4PP1q/8/PPPP2PP/RNBQKBNR w KQkq - 0 1");

                game.makeMove((Square.G2.idx << 7) | Square.G3.idx);

                MatcherAssert.assertThat(game.board[Square.G3.idx], is(Color.W.id | Piece.PAWN.id));
                MatcherAssert.assertThat(game.board[Square.G2.idx], is(0));
            }
        }

        @Nested
        class handlesEnPassant {
            @Test
            public void togglesAfterPawnMovesTwoSpaces() {
                game.makeMove((Square.E2.idx << 7) | Square.E4.idx);

                MatcherAssert.assertThat(game.enPassant, is(Square.E3));

                game.makeMove((Square.E7.idx << 7) | Square.E5.idx);

                MatcherAssert.assertThat(game.enPassant, is(Square.E6));

                game.makeMove((Square.D2.idx << 7) | Square.D3.idx);
                MatcherAssert.assertThat(game.enPassant, is(Square.NULL));
            }

            @Test
            public void captureWorks() {
                game.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 1");

                game.makeMove((Square.E5.idx << 7) | Square.D6.idx);

                MatcherAssert.assertThat(game.board[Square.D5.idx], is(0));
                MatcherAssert.assertThat(game.board[Square.D6.idx], is(Color.W.id | Piece.PAWN.id));
            }
        }

        @Nested
        class halfmoves {
            @Test
            public void incrementsWhenPieceMoves() {
                game.makeMove((Square.B1.idx << 7) | Square.C3.idx);

                MatcherAssert.assertThat(game.halfmoves, is(1));
            }

            @Test
            public void resetsOnCapture() {
                game.loadFen("rnbqk1nr/ppp2ppp/4p3/3pP3/1b6/2N5/PPPP1PPP/R1BQKBNR b KQkq - 0 1");

                game.makeMove((Square.B4.idx << 7) | Square.C3.idx);

                MatcherAssert.assertThat(game.halfmoves, is(0));
            }

            @Test
            public void resetsWhenPawnMoves() {
                game.makeMove((Square.E2.idx << 7) | Square.E4.idx);

                MatcherAssert.assertThat(game.halfmoves, is(0));
            }
        }

        @Nested
        class zobristHash {
            @Test
            public void worksForBasicMoves() {
                game.makeMove((Square.E2.idx << 7) | Square.E4.idx);
                long hash = game.zobristHash;

                game.loadFen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
                MatcherAssert.assertThat(hash, is(game.zobristHash));
            }

            @Test
            public void worksForCaptures() {
                game.loadFen("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
                game.makeMove((Square.E4.idx << 7) | Square.D5.idx);
                long hash = game.zobristHash;

                game.loadFen("rnbqkbnr/ppp1pppp/8/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
                MatcherAssert.assertThat(hash, is(game.zobristHash));
            }

            @Test
            public void worksForEnPassant() {
                game.loadFen("1r2k2r/1ppq1ppp/2npbn2/p1b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq a6 0 1");
                game.makeMove((Square.A2.idx << 7) | Square.A4.idx);
                long hash = game.zobristHash;

                game.loadFen("1r2k2r/1ppq1ppp/2npbn2/p1b1p3/P1B1P3/2NPBN2/1PPQ1PPP/R3K2R b KQkq a3 0 1");
                MatcherAssert.assertThat(hash, CoreMatchers.not(game.zobristHash));
            }

            @Test
            public void worksForPromotion() {
                game.loadFen("r3k2r/pPpq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R w KQkq - 0 1");
                game.makeMove((Piece.QUEEN.id << 18) | Square.B7.idx << 7 | Square.A8.idx);
                long hash = game.zobristHash;

                game.loadFen("Q3k2r/p1pq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/1PPQ1PPP/R3K2R b KQk - 0 1");
                MatcherAssert.assertThat(hash, is(game.zobristHash));
            }

            @Nested
            class castleRightsAreHashed {
                @BeforeEach
                public void init() {
                    game.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");
                }

                @Test
                public void whiteside() {
                    game.makeMove((Square.E1.idx << 7) | Square.F1.idx);
                    long hash = game.zobristHash;

                    game.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R4K1R b kq - 0 1");
                    MatcherAssert.assertThat(hash, is(game.zobristHash));
                }

                @Nested
                class whenRooksAreMoved {
                    @Nested
                    class kingside {
                        @Test
                        public void whiteside() {
                            game.makeMove((Square.H1.idx << 7) | Square.G1.idx);
                            long hash = game.zobristHash;

                            game.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K1R1 b Qkq - 0 1");
                            MatcherAssert.assertThat(hash, is(game.zobristHash));
                        }
                    }

                    @Nested
                    class queenside {
                        @Test
                        public void whiteside() {
                            game.makeMove((Square.A1.idx << 7) | Square.B1.idx);
                            long hash = game.zobristHash;

                            game.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/1R2K2R b Kkq - 0 1");
                            MatcherAssert.assertThat(hash, is(game.zobristHash));
                        }
                    }
                }
            }

            @Nested
            class castlePiecesAreHashCorrectly {
                @BeforeEach
                public void init() {
                    game.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R3K2R w KQkq - 0 1");
                }

                @Nested
                @Description("castle works kingside")
                class kingside {
                    @Test
                    public void whiteside() {
                        Castle castle = Castle.W_K;
                        game.makeMove((castle.value << 14) | Square.E1.idx << 7);
                        long hash = game.zobristHash;

                        game.loadFen("r3k2r/pppq1ppp/2npbn2/2b1p3/2B1P3/2NPBN2/PPPQ1PPP/R4RK1 b kq - 0 1");
                        MatcherAssert.assertThat(hash, is(game.zobristHash));
                    }
                }
            }
        }
    }


    @Nested
    class isDrawByInsufficientMaterial {


        @Test
        public void kingvking() {
            game.loadFen("3k4/8/8/8/8/8/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(true));
        }

        @Test
        public void kvbk() {
            game.loadFen("3k4/8/8/8/8/5B2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(true));
        }

        @Test
        public void kvnk() {
            game.loadFen("3k4/8/8/8/8/5N2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(true));
        }

        @Test
        public void twoPiecesReturnsFalse() {
            game.loadFen("3k4/8/8/8/8/4NN2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(false));
        }

        @Test
        public void returnsFalseIfPawnRookOrQueenIsOnBoard() {
            game.loadFen("3k4/8/8/8/8/4NN2/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(false));

            game.loadFen("3k4/8/8/8/8/3QN3/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(false));

            game.loadFen("3k4/8/8/8/8/2R1N3/8/4K3 w - - 0 1");

            MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(false));
        }

        @Test
        public void oppositeColorBishops() {
            game.loadFen("8/8/5k2/6b1/8/3B4/2K5/8 w - - 0 1");

            boolean draw = game.isDrawByInsufficientMaterial();
            MatcherAssert.assertThat(draw, is(true));
        }

        @Nested
        class nonDrawPositions {
            @Test
            public void bkvnk() {
                game.loadFen("8/8/5k2/5n2/8/3B4/2K5/8 w - - 0 1");
                MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(false));
            }

            @Test
            public void twoBishops() {
                game.loadFen("8/8/5k2/8/8/3BB3/2K5/8 w - - 0 1");

                MatcherAssert.assertThat(game.isDrawByInsufficientMaterial(), is(false));
            }
        }
    }

    @Nested
    class isDrawByRepetition {
        @Test
        public void test() {
            game.loadFen("8/8/5k2/6b1/8/3B4/2K5/8 w - - 0 1");
            game.makeMove((Square.D3.idx << 7) | Square.C4.idx);
            game.makeMove((Square.G5.idx << 7) | Square.H6.idx);
            game.makeMove((Square.C4.idx << 7) | Square.D3.idx);
            game.makeMove((Square.H6.idx << 7) | Square.G5.idx);
            MatcherAssert.assertThat(game.isDrawByRepitition(game.zobristHash, false), is(false));
            game.makeMove((Square.D3.idx << 7) | Square.C4.idx);
            game.makeMove((Square.G5.idx << 7) | Square.H6.idx);
            game.makeMove((Square.C4.idx << 7) | Square.D3.idx);
            game.makeMove((Square.H6.idx << 7) | Square.G5.idx);
            MatcherAssert.assertThat(game.isDrawByRepitition(game.zobristHash, false), is(true));
        }
    }

    @Nested
    class moveGenTest {

        @BeforeEach
        public void init() {
            game.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }

        @Nested
        class getValidMoves {
            @Test
            public void includesB7ToB5() {
                game.loadFen("rnbqkbnr/ppp1pppp/8/P2p4/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
                MatcherAssert.assertThat(game.getValidMoves(Color.B)
                                             .contains((Square.B7.idx << 7) | Square.B5.idx), is(true));
            }

            @Test
            public void includesB5ToC6() {
                game.loadFen("rnbqkbnr/pp1pp1pp/5p2/1Pp5/8/8/P1PPPPPP/RNBQKBNR w KQkq c6 0 1");
                MatcherAssert.assertThat(game.getValidMoves(Color.W)
                                             .contains((Square.B5.idx << 7) | Square.C6.idx), is(true));
            }

            @Test
            public void kingMovesDontIncludeSquaresProtectedByPawn() {
                game.loadFen("rnbqkbnr/ppp1pppp/8/8/3p4/3P4/PPPKPPPP/RNBQ1BNR w KQkq - 0 1");
                List<Integer> validMoves = game.getValidMoves(Color.W);

                MatcherAssert.assertThat(validMoves.contains((Square.D2.idx << 7) | Square.C3.idx), is(false));
                MatcherAssert.assertThat(validMoves.contains((Square.D2.idx << 7) | Square.E3.idx), is(false));
            }

            @Test
            public void missingEnPassantMove() {
                game.loadFen("rnbqkbnr/2pppppp/p7/Pp6/8/8/1PPPPPPP/RNBQKBNR w KQkq b6 0 1");
                List<Integer> validMoves = game.getValidMoves(Color.W);

                MatcherAssert.assertThat(validMoves.contains((Square.A5.idx << 7) | Square.B6.idx), is(true));
            }

            @Test
            public void getCaptureMoves() {
                game.loadFen("rnbqkbnr/5ppp/1p1p4/p3N3/P1Pp4/1P6/4PPPP/RNBQKB1R b KQkq - 0 1");
                List<Integer> validMoves = game.getValidMoves(game.activeColor);
                MatcherAssert.assertThat(validMoves.contains((Square.D6.idx << 7) | Square.E5.idx), is(true));
            }
        }

        @Nested
        class createMoveNotation {
            @Test
            public void basicPawnMove() {
                int move = game.encodeMove(Square.E2, Square.E4);
                int captureDetails = game.makeMove(move);

                MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("e4"));
            }

            @Test
            public void pawnCapture() {
                game.loadFen("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
                int move = game.encodeMove(Square.E4, Square.D5);
                int captureDetails = game.makeMove(move);

                MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("exd5"));
            }

            @Test
            public void enPassantCapture() {
                game.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 1");
                int move = game.encodeMove(Square.E5, Square.D6);
                int captureDetails = game.makeMove(move);

                MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("exd6"));
            }

            @Nested
            class multiplePiecesCanGoToTheSameSquare {
                @Test
                public void diffRankAndFile() {
                    game.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/2N5/PPPP1PPP/R1BQKBNR w KQkq - 0 1");
                    int move = game.encodeMove(Square.C3, Square.E2);
                    int captureDetails = game.makeMove(move);

                    MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("Nce2"));
                }

                @Test
                public void diffRank() {
                    game.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/4N3/8/PPPPNPPP/R1BQKBNR w KQkq - 0 1");
                    int move = game.encodeMove(Square.E4, Square.C3);
                    int captureDetails = game.makeMove(move);

                    MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("N4c3"));
                }

                @Test
                public void diffFile() {
                    game.loadFen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/R1NQKBNR w KQkq - 0 1");
                    int move = game.encodeMove(Square.C1, Square.E2);
                    int captureDetails = game.makeMove(move);

                    MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("Nce2"));
                }
            }

            @Test
            public void checksWork() {
                game.loadFen("rnbqkbnr/ppp2ppp/4p3/3pP3/5P2/8/PPPP2PP/R1NQKBNR b KQkq - 0 1");
                int move = game.encodeMove(Square.D8, Square.H4);
                int captureDetails = game.makeMove(move);

                MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("Qh4+"));
            }

            @Test
            public void checkmateWorks() {
                game.loadFen("rnbqkbnr/ppp2ppp/4p3/3pP3/5P2/8/PPPP2PP/R1NQKBNR b KQkq - 0 1");
                int move = game.encodeMove(Square.D8, Square.H4);
                int captureDetails = game.makeMove(move);

                MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, true), is("Qh4#"));
            }

            @Nested
            class promotion {
                @BeforeEach
                public void init() {
                    game.loadFen("6k1/P7/8/5K2/8/8/8/8 w - - 0 1");
                }

                @Test
                public void queen() {
                    int move = GameState.encodeMove(Square.A7, Square.A8, Piece.QUEEN);
                    int captureDetails = game.makeMove(move);

                    MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("a8=Q+"));
                }

                @Test
                public void knight() {
                    int move = GameState.encodeMove(Square.A7, Square.A8, Piece.KNIGHT);
                    int captureDetails = game.makeMove(move);

                    MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("a8=N"));
                }

                @Test
                public void capture() {
                    game.loadFen("1r4k1/P7/8/5K2/8/8/8/8 w - - 0 1");
                    int move = GameState.encodeMove(Square.A7, Square.B8, Piece.KNIGHT);
                    int captureDetails = game.makeMove(move);

                    MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("axb8=N"));
                }
            }

            @Nested
            class castling {
                @BeforeEach
                public void init() {
                    game.loadFen("1r4k1/8/8/8/8/8/8/R3K2R w KQ - 0 1");
                }

                @Test
                public void kingside() {
                    int move = GameState.encodeMove(Square.E1, Square.G1, Castle.W_K);
                    int captureDetails = game.makeMove(move);

                    MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("0-0"));
                }

                @Test
                public void queenside() {
                    int move = GameState.encodeMove(Square.E1, Square.C1, Castle.W_Q);
                    int captureDetails = game.makeMove(move);

                    MatcherAssert.assertThat(game.createMoveNotation(move, captureDetails, false), is("0-0-0"));
                }
            }
        }

        @Nested
        class convertToFen {
            @Test
            public void pos1() {
                game.loadFen("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");
                MatcherAssert.assertThat(game.getFen(),
                        is("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1"));
            }

            @Test
            public void enPassant() {
                game.loadFen("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2");
                MatcherAssert.assertThat(game.getFen(),
                        is("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0"));
            }

            @Test
            public void castleRights() {
                game.loadFen("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w - c6 0 2");
                MatcherAssert.assertThat(game.getFen(),
                        is("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w - c6 0"));
            }
        }
    }
}
