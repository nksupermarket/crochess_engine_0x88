package com.crochess.engine0x88.utils;

import com.crochess.engine0x88.GameState;

public class Score {
    public static int MIN_VAL = -200;

    public static long make(int bg, int mg, int eg) {
        return ((long) (eg - MIN_VAL) << 32) | (long) (mg - MIN_VAL) << 16 | (bg - MIN_VAL);
    }

    public static long get(long score) {
        switch (GameState.phase) {
            case BG -> {
                return score & 65_535;
            }
            case MG -> {
                return (score >> 16) & 65_535;
            }
            case EG -> {
                return score >> 32;
            }
            default -> {
                return score;
            }
        }
    }
}
