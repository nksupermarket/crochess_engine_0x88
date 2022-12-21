package main.utils;

import main.GameState;
import main.types.Phase;

public class Score {
    public static int make(int mg, int eg) {
        return (eg << 16) | mg;
    }

    public static int get(int score) {
        return GameState.phase == Phase.MG ? score & 65535 : score >> 16;
    }
}
