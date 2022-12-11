package main.moveEval;

import main.Color;
import main.GameState;
import main.Piece;
import main.Square;
import main.moveGen.*;
import main.utils.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final public class MoveEval {
    private static final Map<Long, Integer> transpositionTable = new HashMap<>();

    private MoveEval() {
    }

    final static int[][] MVV_LVA = {
            {0, 0, 0, 0, 0, 0, 0},       // victim None, attacker none, p, n , b, r, q, k
            {0, 15, 14, 13, 12, 11, 10}, // victim P, attacker none, p, n , b, r, q, k
            {0, 25, 24, 23, 22, 21, 20}, // victim N, attacker none, p, n , b, r, q, k
            {0, 35, 34, 33, 32, 31, 30}, // victim B, attacker none, p, n , b, r, q, k
            {0, 45, 44, 43, 42, 41, 40}, // victim R, attacker none, p, n , b, r, q, k
            {0, 55, 54, 53, 52, 51, 50}, // victim Q, attacker none, p, n , b, r, q, k
            {0, 0, 0, 0, 0, 0, 0},       // victim K, attacker none, p, n , b, r, q, k
    };

    private static int[] scoreMoves(List<Integer> moves) {
        int[] scores = new int[moves.size()];

        for (int i = 0; i < moves.size(); i++) {
            int from = (moves.get(i) >> 7) & 127;
            int to = moves.get(i) & 127;

            int attacker = GameState.board[from] & 7;
            int capture = GameState.board[to] & 7;
            if (attacker == Piece.PAWN.id && to == GameState.enPassant.idx) capture = 1;

            scores[i] = MVV_LVA[capture][attacker];
        }

        return scores;
    }

    private static void pickMove(List<Integer> moveList, int[] scores, int startIdx) {
        int bestScore = scores[startIdx];
        int bestScoreIdx = startIdx;
        for (int i = startIdx; i < scores.length; i++) {
            if (scores[i] > bestScore) {
                bestScore = scores[i];
                bestScoreIdx = i;
            }
        }
        int tmp = scores[startIdx];
        scores[startIdx] = scores[bestScoreIdx];
        scores[bestScoreIdx] = tmp;
        Collections.swap(moveList, startIdx, bestScoreIdx);
    }

    public static int getBestMove() {
        UnmakeDetails moveDetails = new UnmakeDetails();
        int bestMove = 0;
        int alpha = Integer.MIN_VALUE + 1;
        List<Integer> legalMoves = GameState.getValidMoves(GameState.activeColor, false, false);
        int[] scores = scoreMoves(legalMoves);

        for (int i = 0; i < legalMoves.size(); i++) {
            pickMove(legalMoves, scores, i);
            GameState.makeMove(legalMoves.get(i), moveDetails);
            int eval = transpositionTable.get(GameState.zobristHash) != null ?
                    transpositionTable.get(GameState.zobristHash) : -alphaBeta(1, Integer.MIN_VALUE + 1, -alpha);

            transpositionTable.putIfAbsent(GameState.zobristHash, eval);
            GameState.unmakeMove(moveDetails);
            if (eval > alpha) {
                alpha = eval;
                bestMove = legalMoves.get(i);
            }
        }
        return bestMove;
    }

    private static int evaluate() {
        if (GameState.isCheckmate(GameState.activeColor)) return -10000;
        if (GameState.isCheckmate(Color.getOppColor(GameState.activeColor))) return 10000;
        return GameState.pieceCount.get(GameState.activeColor) -
                GameState.pieceCount.get(Color.getOppColor(GameState.activeColor));
    }

    private static int quiescence(int alpha, int beta) {
        int standPat = evaluate();

        if (standPat >= beta) return beta;
        if (standPat > alpha) alpha = standPat;

        UnmakeDetails moveDetails = new UnmakeDetails();
        List<Integer> forcingMoves = GameState.getValidMoves(GameState.activeColor, true, true);
        for (int move : forcingMoves) {
            GameState.makeMove(move, moveDetails);
            int eval = transpositionTable.get(GameState.zobristHash) != null ?
                    transpositionTable.get(GameState.zobristHash) : -quiescence(-beta, -alpha);
            GameState.unmakeMove(moveDetails);

            if (eval >= beta)
                return beta;
            if (eval > alpha)
                alpha = eval;
        }

        return alpha;
    }

    private static int alphaBeta(int depth, int alpha, int beta) {
        if (depth == 0) return quiescence(alpha, beta);

        List<Integer> legalMoves = GameState.getValidMoves(GameState.activeColor, false, false);
        if (legalMoves.size() == 0) return -100000;
        UnmakeDetails moveDetails = new UnmakeDetails();
        int[] scores = scoreMoves(legalMoves);

        for (int i = 0; i < scores.length; i++) {
            pickMove(legalMoves, scores, i);
            GameState.makeMove(legalMoves.get(i), moveDetails);
            int eval = transpositionTable.get(GameState.zobristHash) != null ?
                    transpositionTable.get(GameState.zobristHash) : -alphaBeta(depth - 1, -beta, -alpha);

            transpositionTable.putIfAbsent(GameState.zobristHash, eval);
            GameState.unmakeMove(moveDetails);

            if (eval >= beta)
                return beta;
            if (eval > alpha) {
                alpha = eval;
            }
        }

        return alpha;
    }
}
