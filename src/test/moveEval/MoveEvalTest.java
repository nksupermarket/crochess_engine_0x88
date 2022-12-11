package test.moveEval;

import jdk.jfr.Description;
import main.*;
import main.moveEval.MoveEval;
import main.moveGen.*;
import main.uci.Uci;
import main.utils.Utils;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

public class MoveEvalTest {
    @Test
    public void givesSameMoveOnRepeatCalls() {
        GameState.loadFen("rnbqkbnr/2pppppp/1p6/p7/P7/1P6/2PPPPPP/RNBQKBNR w KQkq - 0 4");

        int bestMove = MoveEval.getBestMove(5);
        int repeat = MoveEval.getBestMove(5);
        Utils.printMove(bestMove);
        System.out.println();
        Utils.printMove(repeat);

        MatcherAssert.assertThat(bestMove, is(repeat));
    }

    @Nested
    class tactics {
        @Test
        public void pos1() {
            GameState.loadFen("r3rk2/pb4p1/4QbBp/1p1q4/2pP4/2P5/PP3PPP/R3R1K1 w - - 0 21");
            int wBestMove = MoveEval.getBestMove(5);
            Utils.printMove(wBestMove);

            MatcherAssert.assertThat(wBestMove, is((Square.E6.idx << 7) | Square.E8.idx));
            UnmakeDetails moveDetails = new UnmakeDetails();
            GameState.makeMove((Square.E6.idx << 7) | Square.E8.idx, moveDetails);
            int bBestMove = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(bBestMove, is((Square.A8.idx << 7) | Square.E8.idx));
        }

        @Test
        public void pos2() {
            GameState.loadFen("5rk1/ppq3p1/2p3Qp/8/3P4/2P3nP/PP1N2PK/R1B5 b - - 0 28");
            int bestMove = MoveEval.getBestMove(5);
            MatcherAssert.assertThat(bestMove, is((Square.G3.idx << 7) | Square.F1.idx));
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
        public void posDebug() {
            GameState.loadFen("2bqkb1r/r2n1ppp/3p4/3Qp1P1/P3P3/8/5P1P/RNB1K1NR b KQk - 0 16 \u0000");
            Utils.printMove(MoveEval.getBestMove(5));
            Utils.printMove(MoveEval.getBestMove(5));
        }

        @Test
        public void seriesDebug() {
            Uci.inputPosition(
                    "position startpos moves a2a3 a7a6 a3a4 a6a5 b2b3 b7b6 c2c3 c7c6 b3b4 a5b4");
            MoveEval.getBestMove(5);
            Utils.printMove(MoveEval.getBestMove(5));
            Uci.inputPosition(
                    "position startpos moves a2a3 a7a6 a3a4 a6a5 b2b3 b7b6 c2c3 c7c6 b3b4 a5b4 c3b4");
            MoveEval.getBestMove(5);
            Utils.printMove(MoveEval.getBestMove(5));
        }
    }
}
