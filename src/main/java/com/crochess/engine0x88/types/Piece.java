package com.crochess.engine0x88.types;

import com.crochess.engine0x88.utils.Score;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Piece {
  NULL(0),
  PAWN(S(126, 126, 280)),
  KNIGHT(S(781, 781, 854)),
  BISHOP(S(825, 825, 915)),
  ROOK(S(1276, 1276, 1380)),
  QUEEN(S(2538, 2538, 2682)),
  KING(10000);
  public final int id;
  public final long value;

  Piece(long value) {
    this.id = ordinal();
    this.value = value;
  }

  public static final List<Piece> list = List.copyOf(Arrays.asList(Piece.values()));

  private static long S(int bg, int mg, int eg) {
    return Score.make(bg, mg, eg);
  }

  public static final Map<Integer, Piece> lookup = new HashMap<>();

  static {
    for (Piece pieceType : list) {
      lookup.put(pieceType.id, pieceType);
    }
  }

  public static Piece extractPieceType(int boardVal) {
    return list.get(boardVal & 7);
  }

  private static final EnumSet<Piece> promoteTypes = EnumSet.of(KNIGHT, BISHOP, ROOK, QUEEN);

  public static EnumSet<Piece> getPromoteTypes() {
    return promoteTypes;
  }
}
