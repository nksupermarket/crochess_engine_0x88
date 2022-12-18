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
    private MoveEval() {
    }

    public static void main(String[] args) {

    }

    final static int CHECKMATE_VAL = Integer.MAX_VALUE - 1;
    final static int[][] MVV_LVA = {
            {0, 0, 0, 0, 0, 0, 0},       // victim None, attacker none, p, n , b, r, q, k
            {0, 15, 14, 13, 12, 11, 10}, // victim P, attacker none, p, n , b, r, q, k
            {0, 25, 24, 23, 22, 21, 20}, // victim N, attacker none, p, n , b, r, q, k
            {0, 35, 34, 33, 32, 31, 30}, // victim B, attacker none, p, n , b, r, q, k
            {0, 45, 44, 43, 42, 41, 40}, // victim R, attacker none, p, n , b, r, q, k
            {0, 55, 54, 53, 52, 51, 50}, // victim Q, attacker none, p, n , b, r, q, k
            {0, 0, 0, 0, 0, 0, 0},       // victim K, attacker none, p, n , b, r, q, k
    };
    final static int TTMOVE_SORT_VAL = 60;

    private static int[] scoreMoves(List<Integer> moves, int ttMove) {
        int[] scores = new int[moves.size()];

        for (int i = 0; i < moves.size(); i++) {
            int from = (moves.get(i) >> 7) & 127;
            int to = moves.get(i) & 127;

            int attacker = GameState.board[from] & 7;
            int capture = GameState.board[to] & 7;
            if (attacker == Piece.PAWN.id && to == GameState.enPassant.idx) capture = 1;

            scores[i] = MVV_LVA[capture][attacker];
            if (moves.get(i) == ttMove) scores[i] += TTMOVE_SORT_VAL;
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

    private static int evaluate(int levelsSearched) {
        if (GameState.isDraw()) return 0;
        if (GameState.isCheckmate(GameState.activeColor)) return -CHECKMATE_VAL + levelsSearched;
        if (GameState.isCheckmate(Color.getOppColor(GameState.activeColor))) return CHECKMATE_VAL - levelsSearched;
        return GameState.pieceCount.get(GameState.activeColor) -
                GameState.pieceCount.get(Color.getOppColor(GameState.activeColor));
    }

    private static int quiescence(int alpha, int beta, int levelsSearched) {
        if (levelsSearched == 12) return evaluate(levelsSearched);
        if (GameState.inCheck(GameState.activeColor)) {
            int ttVal = TranspositionTable.probeVal(GameState.zobristHash, 1, alpha, beta);
            return ttVal != TranspositionTable.UNKNOWN_VAL ? ttVal : alphaBeta(1, alpha, beta, levelsSearched);
        }

        int standPat = evaluate(levelsSearched);
        if (standPat >= beta) return beta;
        if (standPat > alpha) alpha = standPat;

        List<Integer> forcingMoves = GameState.getValidMoves(GameState.activeColor, true, true);
        int ttMove = TranspositionTable.probeMove(GameState.zobristHash, 1);
        int[] scores = scoreMoves(forcingMoves, ttMove);

        UnmakeDetails moveDetails = new UnmakeDetails();

        for (int i = 0; i < scores.length; i++) {
            pickMove(forcingMoves, scores, i);
            GameState.makeMove(forcingMoves.get(i), moveDetails);

            int ttVal = TranspositionTable.probeVal(GameState.zobristHash, 0, alpha, beta);
            int eval =
                    ttVal != TranspositionTable.UNKNOWN_VAL ? ttVal : -quiescence(-beta, -alpha, levelsSearched + 1);

            if (eval >= beta) {
                TranspositionTable.store(GameState.zobristHash, 0, TT_Flag.BETA, beta, 0);
                GameState.unmakeMove(moveDetails);
                return beta;
            }
            if (eval > alpha) {
                TranspositionTable.store(GameState.zobristHash, 0, TT_Flag.EXACT, eval, forcingMoves.get(i));
                alpha = eval;
            } else TranspositionTable.store(GameState.zobristHash, 0, TT_Flag.ALPHA, alpha, 0);

            GameState.unmakeMove(moveDetails);
        }

        return alpha;
    }

    private static int alphaBeta(int depth, int alpha, int beta, int levelsSearched) {
        if (depth == 0) {
            int ttVal = TranspositionTable.probeVal(GameState.zobristHash, 0, alpha, beta);
            return ttVal != TranspositionTable.UNKNOWN_VAL ? ttVal : quiescence(alpha, beta, levelsSearched);
        }

        List<Integer> legalMoves = GameState.getValidMoves(GameState.activeColor, false, false);
        if (legalMoves.size() == 0) return -CHECKMATE_VAL + levelsSearched;

        int ttMove = TranspositionTable.probeMove(GameState.zobristHash, depth);
        int[] scores = scoreMoves(legalMoves, ttMove);

        UnmakeDetails moveDetails = new UnmakeDetails();

        for (int i = 0; i < scores.length; i++) {
            pickMove(legalMoves, scores, i);
            GameState.makeMove(legalMoves.get(i), moveDetails);

            int eval = 0;
            if (!GameState.isDraw()) {
                int ttVal = TranspositionTable.probeVal(GameState.zobristHash, depth, alpha, beta);
                eval =
                        ttVal != TranspositionTable.UNKNOWN_VAL ? ttVal : -alphaBeta(depth - 1,
                                -beta, -alpha, levelsSearched + 1);
            }

            if (eval >= beta) {
                TranspositionTable.store(GameState.zobristHash, depth, TT_Flag.BETA, beta, 0);
                GameState.unmakeMove(moveDetails);
                return beta;
            }
            if (eval > alpha) {
                TranspositionTable.store(GameState.zobristHash, depth, TT_Flag.EXACT, eval, legalMoves.get(i));
                alpha = eval;
            } else TranspositionTable.store(GameState.zobristHash, depth, TT_Flag.ALPHA, alpha, 0);

            GameState.unmakeMove(moveDetails);

            if (eval == CHECKMATE_VAL - (levelsSearched + 1)) return eval;
        }
        return alpha;
    }

    public static int getBestMove(int depth) {
        TranspositionTable.age = GameState.halfmoves;

        int bestMove = 0;
        int alpha = Integer.MIN_VALUE + 1;

        List<Integer> legalMoves = GameState.getValidMoves(GameState.activeColor, false, false);
        if (legalMoves.size() == 1) return legalMoves.get(0);

        int ttMove = TranspositionTable.probeMove(GameState.zobristHash, depth);
        int[] scores = scoreMoves(legalMoves, ttMove);

        UnmakeDetails moveDetails = new UnmakeDetails();

        for (int i = 0; i < legalMoves.size(); i++) {
            pickMove(legalMoves, scores, i);
            GameState.makeMove(legalMoves.get(i), moveDetails);

            int eval = 0;
            if (!GameState.isDraw()) {
                int ttVal = TranspositionTable.probeVal(GameState.zobristHash, depth, alpha, Integer.MAX_VALUE);
                eval = ttVal != TranspositionTable.UNKNOWN_VAL ? ttVal : -alphaBeta(depth - 1,
                        Integer.MIN_VALUE + 1, -alpha, 1);
            }
            if (eval > alpha) {
                alpha = eval;
                bestMove = legalMoves.get(i);
                TranspositionTable.store(GameState.zobristHash, depth, TT_Flag.EXACT, eval, bestMove);
            } else TranspositionTable.store(GameState.zobristHash, depth, TT_Flag.ALPHA, alpha, 0);

            GameState.unmakeMove(moveDetails);
        }
        TranspositionTable.store(GameState.zobristHash, depth, TT_Flag.EXACT, alpha, bestMove);
        return bestMove;
    }
}
