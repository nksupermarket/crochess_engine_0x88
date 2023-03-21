package com.crochess.engine0x88.moveEval;

import com.crochess.engine0x88.types.TT_Flag;
import java.util.HashMap;
import java.util.Map;

public class TranspositionTable {
  static final Map<Integer, TableEntry> map = new HashMap<>();
  static final int TABLE_SIZE = 0x100000 * 16;
  static final int ENTRY_SIZE = 40; // TableEntry is 40 bytes
  static final int UNKNOWN_VAL = Integer.MIN_VALUE;
  public static int age = 0;

  private static class TableEntry {
    public long zobrist;
    public int depth;
    public TT_Flag flag;
    public int eval;
    public int age;
    public int move;

    public TableEntry(long zobrist, int depth, TT_Flag flag, int eval, int move, int age) {
      this.zobrist = zobrist;
      this.depth = depth;
      this.flag = flag;
      this.eval = eval;
      this.move = move;
      this.age = age;
    }
  }

  public static void store(long zobrist, int depth, TT_Flag flag, int eval, int move) {
    int key = (int) (zobrist % TABLE_SIZE);

    if (map.get(key) != null) {
      TableEntry entry = map.get(key);
      if (age > entry.age) {
        map.put(key, new TableEntry(zobrist, depth, flag, eval, move, age));
      } else if (depth > map.get(key).depth) {
        map.put(key, new TableEntry(zobrist, depth, flag, eval, move, age));
      }
    } else map.put(key, new TableEntry(zobrist, depth, flag, eval, move, age));
  }

  public static int probeVal(long zobrist, int depth, int alpha, int beta) {
    int key = (int) (zobrist % (TABLE_SIZE / ENTRY_SIZE));
    TableEntry entry = map.get(key);

    if (entry == null || zobrist != entry.zobrist || depth > entry.depth) return UNKNOWN_VAL;

    switch (entry.flag) {
      case EXACT -> {
        return entry.eval;
      }
      case ALPHA -> {
        if (entry.eval <= alpha) return alpha;
      }

      case BETA -> {
        if (entry.eval >= beta) return beta;
      }
    }

    return UNKNOWN_VAL;
  }

  public static int probeMove(long zobrist, int depth) {
    int key = (int) (zobrist % (TABLE_SIZE / ENTRY_SIZE));
    TableEntry entry = map.get(key);

    if (entry == null
        || zobrist != entry.zobrist
        || depth > entry.depth
        || entry.flag != TT_Flag.EXACT) return 0;

    return entry.move;
  }
}
