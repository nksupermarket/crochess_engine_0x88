package test.moveGen;

import main.*;
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

            List<Integer> moves = MoveGen.pseudoLegal(
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

            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E4.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void rookPseudoLegalMovesWorkWithNoObstacles() {
            GameState.loadFen("8/8/8/8/4R3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegal(
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

            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E4.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void bishopPseudoLegalMovesWorkWithNoObstacles() {
            GameState.loadFen("8/8/8/8/4B3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegal(
                    Square.E4,
                    Piece.BISHOP,
                    Color.W);

            Square[] expectDiagonal = {
                    Square.D3, Square.C2, Square.B1, Square.F5, Square.G6, Square.H7,
                    Square.A8, Square.B7,
                    Square.C6, Square.D5, Square.F3, Square.G2, Square.H1
            };

            List<Square> allExpectedList = Arrays.asList(expectDiagonal);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E4.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pseudoLegalMovesWorksWithCaptures() {
            GameState.loadFen("8/8/8/3rrr2/3rQr2/3rrr2/8/8 w KQkq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegal(
                    Square.E4,
                    Piece.QUEEN,
                    Color.W);

            Square[] allExpected = {
                    Square.D5, Square.E5, Square.F5, Square.D4, Square.F4,
                    Square.D3, Square.E3, Square.F3
            };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E4.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pseudoLegalMovesWorksWithOwnPiecesBlocking() {
            GameState.loadFen("8/8/8/3RRR2/3RQR2/3RRR2/8/8 w KQkq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegal(
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

            List<Integer> moves = MoveGen.pseudoLegalForPawn(
                    Square.E4,
                    Color.W,
                    null
            );

            Square[] allExpected = {Square.E5};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E4.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForWhiteCaptures() {
            GameState.loadFen("8/8/8/3r1r2/4p3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForPawn(
                    Square.E4,
                    Color.W,
                    null
            );

            Square[] allExpected = {Square.D5, Square.F5, Square.E5};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E4.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForBlackCaptures() {
            GameState.loadFen("8/8/8/4p3/3R1R2/8/8/8 w KQkq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForPawn(
                    Square.E5,
                    Color.B,
                    null
            );

            Square[] allExpected = {Square.D4, Square.F4, Square.E4};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E5.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForPiecesBlocking() {
            GameState.loadFen("8/8/8/4p3/3RrR2/8/8/8 w KQkq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForPawn(
                    Square.E5,
                    Color.B,
                    null
            );

            Square[] allExpected = {Square.D4, Square.F4};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E5.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void pawnPseudoLegalMovesWorksForEnPassant() {
            GameState.loadFen("8/8/8/8/4p3/8/8/8 w KQkq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForPawn(
                    Square.E4,
                    Color.B,
                    Square.D3
            );

            Square[] allExpected = {Square.D3, Square.E3};
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E4.idx << 7) | v.idx)
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
            GameState.loadFen("8/8/8/8/4K3/8/8/8 w - - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForKing(
                    Square.E4,
                    Color.W,
                    GameState.pieceList.get(Color.B)
            );

            Square[] allExpected = {
                    Square.D5, Square.E5, Square.F5, Square.D3, Square.E3,
                    Square.F3, Square.D4, Square.F4,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> (Square.E4.idx << 7) | v.idx)
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void kingPseudoMovesWorksWithPiecesBlocking() {
            GameState.loadFen("8/8/8/3RRR2/3RKR2/3RRR2/8/8 w - - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForKing(
                    Square.E4,
                    Color.W,
                    GameState.pieceList.get(Color.B)
            );

            MatcherAssert.assertThat(moves.size(),
                    is(0));
        }

        @Test
        public void kingPseudoMovesIncludesWhiteCastleMoves() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w KQ - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForKing(
                    Square.E1,
                    Color.W,
                    GameState.pieceList.get(Color.B)
            );

            Square[] allExpected = {
                    Square.D2, Square.E2, Square.F2, Square.D1,
                    Square.F1, Square.G1, Square.C1,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> {
                                                                if (v == Square.C1)
                                                                    return (((Castle.W_Q.value << 14) |
                                                                            Square.E1.idx << 7) | Square.C1.idx);
                                                                if (v == Square.G1)
                                                                    return (((Castle.W_K.value << 14) |
                                                                            Square.E1.idx << 7) | Square.G1.idx);

                                                                return (Square.E1.idx << 7) | v.idx;
                                                            })
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void kingPseudoMovesIncludesBlackCastleMoves() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w kq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForKing(
                    Square.E8,
                    Color.B,
                    GameState.pieceList.get(Color.W)
            );

            Square[] allExpected = {
                    Square.D7, Square.E7, Square.F7, Square.D8,
                    Square.F8, Square.G8, Square.C8,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> {
                                                                if (v == Square.C8)
                                                                    return ((Castle.B_q.value << 14) |
                                                                            Square.E8.idx << 7 |
                                                                            Castle.B_q.square.idx);
                                                                if (v == Square.G8)
                                                                    return ((Castle.B_k.value << 14) |
                                                                            Square.E8.idx << 7 |
                                                                            Castle.B_k.square.idx);

                                                                return (Square.E8.idx << 7) | v.idx;
                                                            })
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void kingPseudoMovesWorksWithKingsideOnly() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w K - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForKing(
                    Square.E1,
                    Color.W,
                    GameState.pieceList.get(Color.B)
            );

            Square[] allExpected = {
                    Square.D2, Square.E2, Square.F2, Square.D1,
                    Square.F1, Square.G1,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> {
                                                                if (v == Square.C1)
                                                                    return (((Castle.W_Q.value << 14) |
                                                                            Square.E1.idx << 7) | Square.C1.idx);
                                                                if (v == Square.G1)
                                                                    return (((Castle.W_K.value << 14) |
                                                                            Square.E1.idx << 7) | Square.G1.idx);

                                                                return (Square.E1.idx << 7) | v.idx;
                                                            })
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void kingPseudoMovesWorksWithQueensideOnly() {
            GameState.loadFen("8/8/8/8/8/8/8/4K3 w q - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForKing(
                    Square.E8,
                    Color.B,
                    GameState.pieceList.get(Color.W)
            );

            Square[] allExpected = {
                    Square.D7, Square.E7, Square.F7, Square.D8,
                    Square.F8, Square.C8,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> {
                                                                if (v == Square.C8)
                                                                    return ((Castle.B_q.value << 14) |
                                                                            Square.E8.idx << 7 |
                                                                            Castle.B_q.square.idx);
                                                                if (v == Square.G8)
                                                                    return ((Castle.B_k.value << 14) |
                                                                            Square.E8.idx << 7 |
                                                                            Castle.B_k.square.idx);

                                                                return (Square.E8.idx << 7) | v.idx;
                                                            })
                                                            .toList();
            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void doesntIncludeCastleMoveIfSquareInBetweenIsAttackedOnQueenside() {
            GameState.loadFen("8/8/8/8/8/3r4/8/4K3 w KQ - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForKing(
                    Square.E1,
                    Color.W,
                    GameState.pieceList.get(Color.B)
            );

            Square[] allExpected = {
                    Square.D2, Square.E2, Square.F2, Square.D1,
                    Square.F1, Square.G1,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> {
                                                                if (v == Square.C1)
                                                                    return ((Castle.W_Q.value << 14) |
                                                                            Square.E1.idx << 7 |
                                                                            Castle.W_Q.square.idx);
                                                                if (v == Square.G1)
                                                                    return ((Castle.W_K.value << 14) |
                                                                            Square.E1.idx << 7 |
                                                                            Castle.W_K.square.idx);

                                                                return (Square.E1.idx << 7) | v.idx;
                                                            })
                                                            .toList();

            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }

        @Test
        public void doesntIncludeCastleMoveIfSquareInBetweenIsAttackedOnKingside() {
            GameState.loadFen("4k3/8/8/8/8/5R2/8/4K3 w kq - 0 1");

            List<Integer> moves = MoveGen.pseudoLegalForKing(
                    Square.E8,
                    Color.B,
                    GameState.pieceList.get(Color.W)
            );

            Square[] allExpected = {
                    Square.D7, Square.E7, Square.F7, Square.D8,
                    Square.F8, Square.C8,
                    };
            List<Square> allExpectedList = Arrays.asList(allExpected);
            List<Integer> allExpectedMoves = allExpectedList.stream()
                                                            .map((v) -> {
                                                                if (v == Square.C8)
                                                                    return ((Castle.B_q.value << 14) |
                                                                            Square.E8.idx << 7 |
                                                                            Castle.B_q.square.idx);
                                                                if (v == Square.G8)
                                                                    return ((Castle.B_k.value << 14) |
                                                                            Square.E8.idx << 7 |
                                                                            Castle.B_k.square.idx);

                                                                return (Square.E8.idx << 7) | v.idx;
                                                            })
                                                            .toList();
            
            MatcherAssert.assertThat(moves.containsAll(allExpectedMoves),
                    is(true));
        }
    }

    @Test
    public void isAttackedWorks() {
        GameState.loadFen("rnbqkbn1/pppppppp/6r1/8/8/5N2/PPPPPP1P/RNBQK2R w KQkq - 0 1");

        boolean attacked =
                MoveGen.isAttacked(Castle.W_K.square, Color.B, GameState.pieceList.get(Color.B));

        MatcherAssert.assertThat(attacked, is(true));
    }
}
