package com.crochess.engine0x88.moveEval;

import static org.hamcrest.CoreMatchers.*;

import com.crochess.engine0x88.*;
import com.crochess.engine0x88.Uci;
import com.crochess.engine0x88.types.Square;
import com.crochess.engine0x88.utils.Utils;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.jupiter.api.*;

public class MoveEvalTest {
    @Test
    public void givesSameMoveOnRepeatCalls() {
        GameState.loadFen("rnbqkbnr/2pppppp/1p6/p7/P7/1P6/2PPPPPP/RNBQKBNR w KQkq - 0 4");

        int bestMove = MoveEval.getBestMove(5);
        int repeat = MoveEval.getBestMove(5);
        Utils.printMove(bestMove);
        Utils.printMove(repeat);

        MatcherAssert.assertThat(bestMove, is(repeat));
    }

    @Test
    public void firstMove() {
        GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int bestMove = MoveEval.getBestMove(5);
        Utils.printMove(bestMove);
    }

    @Nested
    class tactics {
        @Test
        public void pos1() {
            GameState.loadFen("r3rk2/pb4p1/4QbBp/1p1q4/2pP4/2P5/PP3PPP/R3R1K1 w - - 0 21");
            int wBestMove = MoveEval.getBestMove(5);
            Utils.printMove(wBestMove);

            MatcherAssert.assertThat(wBestMove, is((Square.E6.idx << 7) | Square.E8.idx));
            GameState.makeMove((Square.E6.idx << 7) | Square.E8.idx);
            int bBestMove = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(bBestMove, is((Square.A8.idx << 7) | Square.E8.idx));
        }

        @Test
        public void pos2() {
            GameState.loadFen("5rk1/ppq3p1/2p3Qp/8/3P4/2P3nP/PP1N2PK/R1B5 b - - 0 28");
            int bestMove = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(bestMove, is((Square.G3.idx << 7) | Square.F1.idx));
        }

        @Test
        @Ignore
        public void mateIn4() {
            GameState.loadFen("r1bqr2k/ppp3bp/2np2p1/8/2BnPQ2/2N2N2/PPPB1PP1/2KR3R w - - 0 0");

            int move1 = MoveEval.getBestMove(5);
            MoveEval.getBestMove(5);
            MoveEval.getBestMove(5);
            MoveEval.getBestMove(5);
            MoveEval.getBestMove(5);
            MoveEval.getBestMove(5);
            MoveEval.getBestMove(5);
            MatcherAssert.assertThat(move1, is((Square.H1.idx << 7) | Square.H7.idx));
            GameState.makeMove((Square.H1.idx << 7) | Square.H7.idx);

            int move2 = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(move2, is((Square.H8.idx << 7) | Square.H7.idx));
            GameState.makeMove(move2);

            int move3 = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(move3, is((Square.D1.idx << 7) | Square.H1.idx));
            GameState.makeMove(move3);

            int move4 = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(move4, is((Square.C8.idx << 7) | Square.H3.idx));
            GameState.makeMove(move4);

            GameState.makeMove((Square.H1.idx << 7) | Square.H3.idx);

            int move5 = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(move5, is((Square.D8.idx << 7) | Square.H4.idx));
            GameState.makeMove(move5);

            int move6 = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(move6, is((Square.H3.idx << 7) | Square.H4.idx));
            GameState.makeMove(move6);

            int move7 = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(move7, is((Square.G7.idx << 7) | Square.H6.idx));
            GameState.makeMove(move7);

            int move8 = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(move8, is((Square.F4.idx << 7) | Square.H6.idx));
        }
    }

    @Nested
    class engineDebug {
        @Test
        public void whyIsItBeingStupid() {
            GameState.loadFen("5bnr/3k1ppp/8/3rp3/3PP3/3K4/5PPP/R5NR b - - 0 26");
            int bestMove = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(bestMove, is((Square.D5.idx << 7) | Square.D4.idx));
        }

        @Test
        public void blunderPos2() {
            GameState.loadFen("rnb1kb1r/pppp1ppp/5q2/8/4n3/5N2/PPPPBPPP/RNBQ1RK1 b kq - 0 1");
            int bestMove = MoveEval.getBestMove(5);
            Utils.printMove(bestMove);
        }

        @Test
        public void posDebug() {
            GameState.loadFen("4k3/8/r2p1n2/P3p3/3pP2R/R7/4K3/8 w - - 19 52 \u0000");
            int bestMove = MoveEval.getBestMove(5);
            Utils.printMove(bestMove);
        }

        @Test
        public void seriesDebug() {
            Uci.inputPosition(
                    "position startpos moves a2a3 a7a6 a3a4 a6a5 b2b3 b7b6 c2c3 c7c6 b3b4 a5b4 c3b4 c6c5 b4c5"
                            + " b6c5 d2d3 d7d6 d3d4 c5d4 d1d4 e7e6 e2e4");
            MoveEval.getBestMove(5);
            Utils.printMove(MoveEval.getBestMove(5));
            Uci.inputPosition(
                    "position startpos moves a2a3 a7a6 a3a4 a6a5 b2b3 b7b6 c2c3 c7c6 b3b4 a5b4 c3b4 c6c5 b4c5"
                            + " b6c5 d2d3 d7d6 d3d4 c5d4 d1d4 e7e6 e2e4 e6e5");
            MoveEval.getBestMove(5);
            Utils.printMove(MoveEval.getBestMove(5));
        }

        @Test
        public void blunder() {
            Uci.inputPosition("position startpos moves b1c3 e7e5 g1f3 b8c6 e2e3 a7a6 f1d3 d7d5");
            int bestMove = MoveEval.getBestMove(5);
            Utils.printMove(bestMove);
        }
    }
}
