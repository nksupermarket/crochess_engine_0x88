package test.moveGen;

import main.moveGen.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MoveGenTest {

    @Nested
    @DisplayName("Tests for pseudoLegalMoves for pieces not including king and pawn")
    class pseudoLegalMoves {
        @Test
        public void queenPseudoLegalMovesWorkWithNoObstacles() {
            GameState.loadFen("8/8/8/8/4Q3/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.QUEEN,
                    Color.W);
            Square[] expectedXY = {
                    Square.E3, Square.E2, Square.E1, Square.E5, Square.E6, Square.E7,
                    Square.E8,
                    Square.A4, Square.B4,
                    Square.C4, Square.D4, Square.F4, Square.G4, Square.H4
            };

            Square[] expectDiagonal = {
                    Square.D3, Square.C2, Square.B1, Square.F5, Square.G6, Square.H7,
                    Square.A8, Square.B7,
                    Square.C6, Square.D5, Square.F3, Square.G2, Square.H1
            };

            Square[] allExpected = Stream.concat(Arrays.stream(expectedXY),
                                                 Arrays.stream(expectDiagonal))
                                         .toArray(Square[]::new);
            List<Square> allExpectedList = Arrays.asList(allExpected);

            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E4, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void rookPseudoLegalMovesWorkWithNoObstacles() {
            GameState.loadFen("8/8/8/8/4R3/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.ROOK,
                    Color.W);
            Square[] expectedXY = {
                    Square.E3, Square.E2, Square.E1, Square.E5, Square.E6, Square.E7,
                    Square.E8,
                    Square.A4, Square.B4,
                    Square.C4, Square.D4, Square.F4, Square.G4, Square.H4
            };

            List<Square> allExpectedList = Arrays.asList(expectedXY);

            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E4, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void bishopPseudoLegalMovesWorkWithNoObstacles() {
            GameState.loadFen("8/8/8/8/4B3/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.BISHOP,
                    Color.W);

            Square[] expectDiagonal = {
                    Square.D3, Square.C2, Square.B1, Square.F5, Square.G6, Square.H7,
                    Square.A8, Square.B7,
                    Square.C6, Square.D5, Square.F3, Square.G2, Square.H1
            };

            List<Square> allExpectedList = Arrays.asList(expectDiagonal);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E4, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pseudoLegalMovesWorksWithCaptures() {
            GameState.loadFen("8/8/8/3rrr2/3rQr2/3rrr2/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.QUEEN,
                    Color.W);

            Square[] allExpected = {
                    Square.D5, Square.E5, Square.F5, Square.D4, Square.F4,
                    Square.D3, Square.E3, Square.F3
            };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E4, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pseudoLegalMovesWorksWithOwnPiecesBlocking() {
            GameState.loadFen("8/8/8/3RRR2/3RQR2/3RRR2/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.QUEEN,
                    Color.W);

            MatcherAssert.assertThat(moves.size(),
                    is(0));
        }
    }

    @Nested
    @DisplayName("Tests for pseudoLegalMoves for pawn")
    class pseudoLegalMovesForPawn {
        @Test
        public void pawnPseudoLegalMovesWorksForRegularMoves() {
            GameState.loadFen("8/8/8/8/4p3/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForPawn(GameState.board,
                    Square.E4,
                    Color.W,
                    null
            );

            Square[] allExpected = {Square.E5};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E4, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForWhiteCaptures() {
            GameState.loadFen("8/8/8/3r1r2/4p3/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForPawn(GameState.board,
                    Square.E4,
                    Color.W,
                    null
            );

            Square[] allExpected = {Square.D5, Square.F5, Square.E5};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E4, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForBlackCaptures() {
            GameState.loadFen("8/8/8/4p3/3R1R2/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForPawn(GameState.board,
                    Square.E5,
                    Color.B,
                    null
            );

            Square[] allExpected = {Square.D4, Square.F4, Square.E4};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E5, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForPiecesBlocking() {
            GameState.loadFen("8/8/8/4p3/3RrR2/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForPawn(GameState.board,
                    Square.E5,
                    Color.B,
                    null
            );

            Square[] allExpected = {Square.D4, Square.F4};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E5, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForEnPassant() {
            GameState.loadFen("8/8/8/8/4p3/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForPawn(GameState.board,
                    Square.E4,
                    Color.B,
                    Square.D3
            );

            Square[] allExpected = {Square.D3, Square.E3};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E4, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }
    }

    @Nested
    @DisplayName("Tests for pseudoLegalMoves for king")
    class pseudoLegalMovesForKing {
        @Test
        public void kingPseudoMovesWorks() {
            GameState.loadFen("8/8/8/8/4K3/8/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForKing(GameState.board,
                    Square.E4,
                    Color.W,
                    0,
                    GameState.pieceList.get(Color.B)
            );

            Square[] allExpected = {
                    Square.D5, Square.E5, Square.F5, Square.D3, Square.E3,
                    Square.F3, Square.D4, Square.F4,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> new Move(Square.E4, v))
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void kingPseudoMovesWorksWithPiecesBlocking() {
            GameState.loadFen("8/8/8/3RRR2/3RKR2/3RRR2/8/8 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForKing(GameState.board,
                    Square.E4,
                    Color.W,
                    0,
                    GameState.pieceList.get(Color.B)
            );

            MatcherAssert.assertThat(moves.size(),
                    is(0));
        }

        @Test
        public void kingPseudoMovesIncludesWhiteCastleMoves() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForKing(GameState.board,
                    Square.E1,
                    Color.W,
                    12,
                    GameState.pieceList.get(Color.B)
            );

            Square[] allExpected = {
                    Square.D2, Square.E2, Square.F2, Square.D1,
                    Square.F1, Square.G1, Square.C1,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> {
                                                             if (v == Square.C1) return new Move(Square.E1, Castle.W_Q);
                                                             if (v == Square.G1) return new Move(Square.E1, Castle.W_K);

                                                             return new Move(Square.E1, v);
                                                         })
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void kingPseudoMovesIncludesBlackCastleMoves() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForKing(GameState.board,
                    Square.E8,
                    Color.B,
                    3,
                    GameState.pieceList.get(Color.W)
            );

            Square[] allExpected = {
                    Square.D7, Square.E7, Square.F7, Square.D8,
                    Square.F8, Square.G8, Square.C8,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> {
                                                             if (v == Square.C8) return new Move(Square.E8, Castle.B_q);
                                                             if (v == Square.G8) return new Move(Square.E8, Castle.B_k);
                                                             return new Move(Square.E8, v);
                                                         })
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void kingPseudoMovesWorksWithKingsideOnly() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForKing(GameState.board,
                    Square.E1,
                    Color.W,
                    8,
                    GameState.pieceList.get(Color.B)
            );

            Square[] allExpected = {
                    Square.D2, Square.E2, Square.F2, Square.D1,
                    Square.F1, Square.G1,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> {
                                                             if (v == Square.C1) return new Move(Square.E1, Castle.W_Q);
                                                             if (v == Square.G1) return new Move(Square.E1, Castle.W_K);

                                                             return new Move(Square.E1, v);
                                                         })
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void kingPseudoMovesWorksWithQueensideOnly() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForKing(GameState.board,
                    Square.E8,
                    Color.B,
                    1,
                    GameState.pieceList.get(Color.W)
            );

            Square[] allExpected = {
                    Square.D7, Square.E7, Square.F7, Square.D8,
                    Square.F8, Square.C8,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> {
                                                             if (v == Square.C8) return new Move(Square.E8, Castle.B_q);
                                                             if (v == Square.G8) return new Move(Square.E8, Castle.B_k);
                                                             return new Move(Square.E8, v);
                                                         })
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void doesntIncludeCastleMoveIfSquareInBetweenIsAttackedOnQueenside() {
            GameState.loadFen("8/8/8/8/8/3r4/8/4K3 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForKing(GameState.board,
                    Square.E1,
                    Color.W,
                    12,
                    GameState.pieceList.get(Color.B)
            );

            Square[] allExpected = {
                    Square.D2, Square.E2, Square.F2, Square.D1,
                    Square.F1, Square.G1,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> {
                                                             if (v == Square.C1) return new Move(Square.E1, Castle.W_Q);
                                                             if (v == Square.G1) return new Move(Square.E1, Castle.W_K);

                                                             return new Move(Square.E1, v);
                                                         })
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void doesntIncludeCastleMoveIfSquareInBetweenIsAttackedOnKingside() {
            GameState.loadFen("4k3/8/8/8/8/5R2/8/4K3 w KQkq - 0 1");

            List<Move> moves = MoveGen.pseudoLegalForKing(GameState.board,
                    Square.E8,
                    Color.B,
                    3,
                    GameState.pieceList.get(Color.W)
            );

            Square[] allExpected = {
                    Square.D7, Square.E7, Square.F7, Square.D8,
                    Square.F8, Square.C8,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Move> allExpectedMoves = allExpectedList.stream()
                                                         .map((v) -> {
                                                             if (v == Square.C8) return new Move(Square.E8, Castle.B_q);
                                                             if (v == Square.G8) return new Move(Square.E8, Castle.B_k);
                                                             return new Move(Square.E8, v);
                                                         })
                                                         .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }
    }
}
