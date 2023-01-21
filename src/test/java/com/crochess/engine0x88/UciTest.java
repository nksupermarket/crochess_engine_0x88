package com.crochess.engine0x88;

import com.crochess.engine0x88.types.Castle;
import com.crochess.engine0x88.types.Piece;
import com.crochess.engine0x88.types.Color;
import com.crochess.engine0x88.types.Square;
import com.crochess.moveValidator.Game;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class UciTest {
    @Nested
    class algebraToMove {
        @Test
        public void spitsOutRightMove() {
            MatcherAssert.assertThat(Uci.algebraToMove("e2e4"), is((Square.E2.idx << 7) | Square.E4.idx));
        }

        @Test
        public void handlesCastleMovesCorrectly() {
            MatcherAssert.assertThat(Uci.algebraToMove("e1g1"),
                    is(Castle.W_K.value << 14 | ((Square.E1.idx << 7) | Square.G1.idx)));

            MatcherAssert.assertThat(Uci.algebraToMove("e8c8"),
                    is(Castle.B_q.value << 14 | ((Square.E8.idx << 7) | Square.C8.idx)));
        }

        @Test
        public void handlesPromotion() {
            MatcherAssert.assertThat(Uci.algebraToMove("e2e4q"), is(
                    (Piece.QUEEN.id << 18) | (Square.E2.idx << 7) | Square.E4.idx
            ));
        }
    }

    @Nested
    class inputPosition {
        @Test
        public void handlesMoves() {
            String inputStr = "position startpos moves e2e4 e7e5";

            Uci.inputPosition(inputStr);

            MatcherAssert.assertThat(GameState.board[Square.E4.idx], is(Color.W.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.board[Square.E2.idx], not(Color.W.id | Piece.PAWN.id));

            MatcherAssert.assertThat(GameState.board[Square.E5.idx], is(Color.B.id | Piece.PAWN.id));
            MatcherAssert.assertThat(GameState.board[Square.E7.idx], not(Color.B.id | Piece.PAWN.id));
        }

        @Test
        public void debug() {
            Uci.inputPosition(
                    "position fen 5bnr/3k1ppp/8/3rp3/3PP3/3K4/5PPP/R5NR b - - 0 26");

            Uci.inputGo();
        }
    }

    @Nested
    class moveToAlgebra {
        @Test
        public void basicMove() {
            MatcherAssert.assertThat(Uci.moveToAlgebra((Square.E2.idx << 7) | Square.E4.idx), is("e2e4"));
        }

        @Test
        public void promoteMove() {
            MatcherAssert.assertThat(Uci.moveToAlgebra((Piece.QUEEN.id << 18) | (Square.E2.idx << 7) | Square.E4.idx)
                    , is("e2e4q"));
        }
    }

    @Nested
    class testingPositions {
        @Test
        public void pos1() {
            GameState.loadFen("1nbqkbnr/r1pp1ppp/P7/1p2B3/8/2P5/1p1PPPPP/RN1QKBNR w KQk - 0 10");
            Uci.inputGo();
        }

        @Test
        public void series1() {
            Uci.inputUCINewGame();
            Uci.inputIsReady();
            Uci.inputGo();
            Uci.inputPosition("position startpos moves a2a3");
            Uci.inputGo();
            Uci.inputPosition("position startpos moves a2a3 a7a6");
            Uci.inputGo();
            Uci.inputPosition("position startpos moves a2a3 a7a6 a3a4");
            Uci.inputGo();
            Uci.inputPosition("position startpos moves a2a3 a7a6 a3a4 a6a5");
            Uci.inputGo();
            Uci.inputPosition("position startpos moves a2a3 a7a6 a3a4 a6a5 b2b3");
            Uci.inputGo();
            Uci.inputPosition("position startpos moves a2a3 a7a6 a3a4 a6a5 b2b3 b7b6");
            Uci.inputGo();
            Uci.inputPosition("position startpos moves a2a3 a7a6 a3a4 a6a5 b2b3 b7b6 c2c3");
            Uci.inputGo();
        }

        @Test
        public void series2() {
            Game moveValidator = new Game();

            Uci.inputPosition(
                    "position startpos moves e2e4 b8c6 d2d4 d7d5 e4e5 g8h6 c1h6 g7h6 h2h4 f8g7 g1f3 c8g4 c2c3 e8g8 " +
                            "b1d2 f7f6",
                    moveValidator);

            Game mv2 = new Game();
            Uci.inputPosition(
                    "position startpos moves e2e4 b8c6 d2d4 d7d5 e4e5 g8h6 c1h6 g7h6 h2h4 f8g7 g1f3 c8g4 c2c3 e8g8 " +
                            "b1d2 f7f6 e5f6 f8f6",
                    mv2);
        }
    }
}
