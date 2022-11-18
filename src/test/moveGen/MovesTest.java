package test.moveGen;

import static org.junit.Assert.*;

import main.moveGen.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class MovesTest {

    @Nested
    @DisplayName("Tests for pseudoLegalMoves for pieces not including king and pawn")
    class pseudoLegalMoves {
        @Test
        public void queenPseudoLegalMovesWorkWithNoObstacles() {
            GameState.loadFen("8/8/8/8/4Q3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.QUEEN,
                    Color.W);
            Integer[] expectedXY = {
                    Square.E3.idx, Square.E2.idx, Square.E1.idx, Square.E5.idx, Square.E6.idx, Square.E7.idx,
                    Square.E8.idx,
                    Square.A4.idx, Square.B4.idx,
                    Square.C4.idx, Square.D4.idx, Square.F4.idx, Square.G4.idx, Square.H4.idx
            };

            Integer[] expectDiagonal = {
                    Square.D3.idx, Square.C2.idx, Square.B1.idx, Square.F5.idx, Square.G6.idx, Square.H7.idx,
                    Square.A8.idx, Square.B7.idx,
                    Square.C6.idx, Square.D5.idx, Square.F3.idx, Square.G2.idx, Square.H1.idx
            };

            Integer[] allExpected = Stream.concat(Arrays.stream(expectedXY),
                                                  Arrays.stream(expectDiagonal))
                                          .toArray(Integer[]::new);
            List<Integer> allExpectedList = Arrays.asList(allExpected);
            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void rookPseudoLegalMovesWorkWithNoObstacles() {
            GameState.loadFen("8/8/8/8/4R3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.ROOK,
                    Color.W);
            Integer[] expectedXY = {
                    Square.E3.idx, Square.E2.idx, Square.E1.idx, Square.E5.idx, Square.E6.idx, Square.E7.idx,
                    Square.E8.idx,
                    Square.A4.idx, Square.B4.idx,
                    Square.C4.idx, Square.D4.idx, Square.F4.idx, Square.G4.idx, Square.H4.idx
            };

            List<Integer> allExpectedList = Arrays.asList(expectedXY);
            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void bishopPseudoLegalMovesWorkWithNoObstacles() {
            GameState.loadFen("8/8/8/8/4B3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.BISHOP,
                    Color.W);

            Integer[] expectDiagonal = {
                    Square.D3.idx, Square.C2.idx, Square.B1.idx, Square.F5.idx, Square.G6.idx, Square.H7.idx,
                    Square.A8.idx, Square.B7.idx,
                    Square.C6.idx, Square.D5.idx, Square.F3.idx, Square.G2.idx, Square.H1.idx
            };

            List<Integer> allExpectedList = Arrays.asList(expectDiagonal);
            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void pseudoLegalMovesWorksWithCaptures() {
            GameState.loadFen("8/8/8/3rrr2/3rQr2/3rrr2/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegal(GameState.board,
                    Square.E4,
                    Piece.QUEEN,
                    Color.W);

            Integer[] allExpected = {
                    Square.D5.idx, Square.E5.idx, Square.F5.idx, Square.D4.idx, Square.F4.idx,
                    Square.D3.idx, Square.E3.idx, Square.F3.idx
            };
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void pseudoLegalMovesWorksWithOwnPiecesBlocking() {
            GameState.loadFen("8/8/8/3RRR2/3RQR2/3RRR2/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegal(GameState.board,
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

            List<Integer> moves = Moves.pseudoLegalForPawn(GameState.board,
                    Square.E4,
                    Color.W,
                    null
            );

            Integer[] allExpected = {Square.E5.idx};
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForWhiteCaptures() {
            GameState.loadFen("8/8/8/3r1r2/4p3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForPawn(GameState.board,
                    Square.E4,
                    Color.W,
                    null
            );

            Integer[] allExpected = {Square.D5.idx, Square.F5.idx, Square.E5.idx};
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForBlackCaptures() {
            GameState.loadFen("8/8/8/4p3/3R1R2/8/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForPawn(GameState.board,
                    Square.E5,
                    Color.B,
                    null
            );

            Integer[] allExpected = {Square.D4.idx, Square.F4.idx, Square.E4.idx};
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForPiecesBlocking() {
            GameState.loadFen("8/8/8/4p3/3RrR2/8/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForPawn(GameState.board,
                    Square.E5,
                    Color.B,
                    null
            );

            Integer[] allExpected = {Square.D4.idx, Square.F4.idx};
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForEnPassant() {
            GameState.loadFen("8/8/8/8/4p3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForPawn(GameState.board,
                    Square.E4,
                    Color.B,
                    Square.D3
            );

            Integer[] allExpected = {Square.D3.idx, Square.E3.idx};
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }
    }

    @Nested
    @DisplayName("Tests for pseudoLegalMoves for king")
    class pseudoLegalMovesForKing {
        @Test
        public void kingPseudoMovesWorks() {
            GameState.loadFen("8/8/8/8/4K3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForKing(GameState.board,
                    Square.E4,
                    Color.W,
                    0,
                    GameState.bPieceList
            );

            Integer[] allExpected = {
                    Square.D5.idx, Square.E5.idx, Square.F5.idx, Square.D3.idx, Square.E3.idx,
                    Square.F3.idx, Square.D4.idx, Square.F4.idx,
                    };
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void kingPseudoMovesWorksWithPiecesBlocking() {
            GameState.loadFen("8/8/8/3RRR2/3RKR2/3RRR2/8/8 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForKing(GameState.board,
                    Square.E4,
                    Color.W,
                    0,
                    GameState.bPieceList
            );

            MatcherAssert.assertThat(moves.size(),
                    is(0));
        }

        @Test
        public void kingPseudoMovesIncludesWhiteCastleMoves() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForKing(GameState.board,
                    Square.E1,
                    Color.W,
                    12,
                    GameState.bPieceList
            );

            Integer[] allExpected = {
                    Square.D2.idx, Square.E2.idx, Square.F2.idx, Square.D1.idx,
                    Square.F1.idx, Square.G1.idx, Square.C1.idx,
                    };
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void kingPseudoMovesIncludesBlackCastleMoves() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForKing(GameState.board,
                    Square.E8,
                    Color.B,
                    3,
                    GameState.wPieceList
            );

            Integer[] allExpected = {
                    Square.D7.idx, Square.E7.idx, Square.F7.idx, Square.D8.idx,
                    Square.F8.idx, Square.G8.idx, Square.C8.idx,
                    };
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void kingPseudoMovesWorksWithKingsideOnly() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForKing(GameState.board,
                    Square.E1,
                    Color.W,
                    8,
                    GameState.bPieceList
            );

            Integer[] allExpected = {
                    Square.D2.idx, Square.E2.idx, Square.F2.idx, Square.D1.idx,
                    Square.F1.idx, Square.G1.idx,
                    };
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void kingPseudoMovesWorksWithQueensideOnly() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForKing(GameState.board,
                    Square.E8,
                    Color.B,
                    1,
                    GameState.wPieceList
            );

            Integer[] allExpected = {
                    Square.D7.idx, Square.E7.idx, Square.F7.idx, Square.D8.idx,
                    Square.F8.idx, Square.C8.idx,
                    };
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void doesntIncludeCastleMoveIfSquareInBetweenIsAttackedOnQueenside() {
            GameState.loadFen("8/8/8/8/8/3r4/8/4K3 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForKing(GameState.board,
                    Square.E1,
                    Color.W,
                    12,
                    GameState.bPieceList
            );

            Integer[] allExpected = {
                    Square.D2.idx, Square.E2.idx, Square.F2.idx, Square.D1.idx,
                    Square.F1.idx, Square.G1.idx,
                    };
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }

        @Test
        public void doesntIncludeCastleMoveIfSquareInBetweenIsAttackedOnKingside() {
            GameState.loadFen("4k3/8/8/8/8/5R2/8/4K3 w KQkq - 0 1");

            List<Integer> moves = Moves.pseudoLegalForKing(GameState.board,
                    Square.E8,
                    Color.B,
                    3,
                    GameState.wPieceList
            );

            Integer[] allExpected = {
                    Square.D7.idx, Square.E7.idx, Square.F7.idx, Square.D8.idx,
                    Square.F8.idx, Square.C8.idx,
                    };
            List<Integer> allExpectedList = Arrays.asList(allExpected);

            allExpectedList.sort((a, b) -> Integer.compare(b,
                    a));

            moves.sort((a, b) -> Integer.compare(b,
                    a));

            MatcherAssert.assertThat(moves,
                    is(allExpectedList));
        }
    }
}
