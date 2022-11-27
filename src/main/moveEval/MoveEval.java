package main.moveEval;

import main.moveGen.Color;
import main.moveGen.GameState;
import main.moveGen.UnmakeDetails;

import java.util.List;

final public class MoveEval {
    private MoveEval() {

    }

//    private int alphaBeta(int depth, int alpha, int beta) {
//        if (depth == 0) return positionEvaluation;
//
//        List<Integer> legalMoves = GameState.getValidMoves(Color.W);
//        if (legalMoves.size() == 0) return Infinity;
//
//        UnmakeDetails moveDetails = new UnmakeDetails();
//        for (Integer legalMove : legalMoves) {
//            GameState.makeMove(legalMove, moveDetails);
//            int eval = -alphaBeta(depth - 1, -beta, -alpha);
//            GameState.unmakeMove(moveDetails);
//            if (eval >= beta)
//                return beta;
//            if (eval > alpha)
//                alpha = eval;
//        }
//        return alpha;
//    }
}
