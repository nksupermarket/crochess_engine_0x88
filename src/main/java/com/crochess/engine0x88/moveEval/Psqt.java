package com.crochess.engine0x88.moveEval;

import com.crochess.engine0x88.types.Color;
import com.crochess.engine0x88.types.Piece;
import com.crochess.engine0x88.types.Square;
import com.crochess.engine0x88.utils.Score;

public class Psqt {
    private Psqt() {
    }

    private static long S(int bg, int mg, int eg) {
        return Score.make(bg, mg, eg);
    }

    private static final long[][][] PieceBonus = {
            {},
            {},
            { // Knight
              {S(-50, -50, -40), S(-50, -50, -40), S(-20, -20, -10), S(-30, -30, -20)},
              {S(-20, -20, -15), S(-20, -20, -15), S(-20, -20, -15), S(0, 0, 0)},
              {S(-61, -61, -40), S(-17, -17, -27), S(6, 6, -8), S(12, 12, 29)},
              {S(-35, -35, -35), S(8, 8, -2), S(40, 40, 13), S(49, 49, 28)},
              {S(-34, -34, -45), S(13, 13, -16), S(44, 44, 9), S(51, 51, 39)},
              {S(-9, -9, -51), S(22, 22, -44), S(58, 58, -16), S(53, 53, 17)},
              {S(-67, -67, -69), S(-27, -27, -50), S(4, 4, -51), S(37, 37, 12)},
              {S(-200, -200, -100), S(-83, -83, -88), S(-56, -56, -56), S(-26, -26, -17)}
            },
            { // Bishop
              {S(-37, -37, -40), S(-4, -4, -21), S(-6, -6, -26), S(-16, -16, -8)},
              {S(-11, -11, -26), S(6, 6, -9), S(13, 13, -12), S(23, 3, 1)},
              {S(-5, -5, -11), S(15, 15, -1), S(-1, -4, -1), S(-13, 12, 7)},
              {S(-4, -4, -14), S(8, 8, -4), S(28, 18, 0), S(7, 27, 12)},
              {S(-8, -8, -12), S(20, 20, -1), S(15, 15, -10), S(22, 22, 11)},
              {S(-11, -11, -21), S(4, 4, 4), S(1, 1, 3), S(8, 8, 4)},
              {S(-12, -12, -22), S(-10, -10, -14), S(4, 4, -1), S(0, 0, 1)},
              {S(-34, -34, -32), S(1, 1, -29), S(-10, -10, -26), S(-16, -16, -17)}
            },
            { // Rook
              {S(-31, -31, -9), S(-20, -20, -13), S(-14, -14, -10), S(-5, -5, -9)},
              {S(-21, -21, -12), S(-13, -13, -9), S(-8, -8, -1), S(6, 6, -2)},
              {S(-25, -25, 6), S(-11, -11, -8), S(-1, -1, -2), S(3, 3, -6)},
              {S(-13, -13, -6), S(-5, -5, 1), S(-4, -4, -9), S(-6, -6, 7)},
              {S(-27, -27, -5), S(-15, -15, 8), S(-4, -4, 7), S(3, 3, -6)},
              {S(-22, -22, 6), S(-2, -2, 1), S(6, 6, -7), S(12, 12, 10)},
              {S(-2, -2, 4), S(12, 12, 5), S(16, 16, 20), S(18, 18, -5)},
              {S(-17, -17, 18), S(-19, -19, 0), S(-1, -1, 19), S(9, 9, 13)}
            },
            { // Queen
              {S(3, 3, -69), S(-5, -5, -57), S(-5, -5, -47), S(4, 4, -26)},
              {S(-3, -3, -54), S(5, 5, -31), S(8, 8, -22), S(12, 12, -4)},
              {S(-3, -3, -39), S(6, 6, -18), S(13, 13, -9), S(7, 7, 3)},
              {S(4, 4, -23), S(5, 5, -3), S(9, 9, 13), S(8, 8, 24)},
              {S(0, 0, -29), S(14, 14, -6), S(12, 12, 9), S(5, 5, 21)},
              {S(-4, -4, -38), S(10, 10, -18), S(6, 6, -11), S(8, 8, 1)},
              {S(-5, -5, -50), S(6, 6, -27), S(10, 10, -24), S(8, 8, -8)},
              {S(-2, -2, -74), S(-2, -2, -52), S(1, 1, -43), S(-2, -2, -34)}
            },
            { // King
              {S(271, 271, 1), S(327, 327, 45), S(271, 271, 85), S(198, 198, 76)},
              {S(278, 278, 53), S(303, 303, 100), S(234, 234, 133), S(179, 179, 135)},
              {S(195, 195, 88), S(258, 258, 130), S(169, 169, 169), S(120, 120, 175)},
              {S(164, 164, 103), S(190, 190, 156), S(138, 138, 172), S(98, 98, 172)},
              {S(154, 154, 96), S(179, 179, 166), S(105, 105, 199), S(70, 70, 199)},
              {S(123, 123, 92), S(145, 145, 172), S(81, 81, 184), S(31, 31, 191)},
              {S(88, 88, 47), S(120, 120, 121), S(65, 65, 116), S(33, 33, 131)},
              {S(59, 59, 11), S(89, 89, 59), S(45, 45, 73), S(-1, -1, 78)}
            }
    };

    private static final long[][] PawnBonus = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {
                    S(2, 2, -8),
                    S(4, 4, -6),
                    S(11, 11, 9),
                    S(18, 18, 5),
                    S(16, 16, 16),
                    S(21, 21, 6),
                    S(9, 9, -6),
                    S(-3, -3, -18)
            },
            {
                    S(5, -9, -9),
                    S(5, -15, -7),
                    S(11, 11, -10),
                    S(35, 15, 5),
                    S(41, 31, 2),
                    S(-20, 23, 3),
                    S(6, 6, -8),
                    S(5, -20, -5)
            },
            {
                    S(-3, -3, 7),
                    S(-20, -20, 1),
                    S(28, 8, -8),
                    S(53, 19, -2),
                    S(63, 39, -14),
                    S(-5, 17, -13),
                    S(2, 2, -11),
                    S(-5, -5, -6)
            },
            {
                    S(11, 11, 12),
                    S(-4, -4, 6),
                    S(-11, -11, 2),
                    S(2, 2, -6),
                    S(11, 11, -5),
                    S(0, 0, -4),
                    S(-12, -12, 14),
                    S(5, 5, 9)
            },
            {
                    S(3, 3, 27),
                    S(-11, -11, 18),
                    S(-6, -6, 19),
                    S(22, 22, 29),
                    S(-8, -8, 30),
                    S(-5, -5, 9),
                    S(-14, -14, 8),
                    S(-11, -11, 14)
            },
            {
                    S(-7, -7, -1),
                    S(6, 6, -14),
                    S(-2, -2, 13),
                    S(-11, -11, 22),
                    S(4, 4, 24),
                    S(-14, -14, 17),
                    S(10, 10, 7),
                    S(-9, -9, 7)
            },
            {0, 0, 0, 0, 0, 0, 0, 0},
            };

    public static long[][][][] table = new long[2][Piece.list.size()][8][8];

    static {
        long[][][] wTable = new long[7][8][8];
        long[][][] bTable = new long[7][8][8];
        for (Piece pieceType : Piece.list) {
            if (pieceType == Piece.NULL) {
                wTable[pieceType.id] = new long[][]{};
                bTable[pieceType.id] = new long[][]{};
                continue;
            }
            for (Square square : Square.values()) {
                int file = Square.getFile(square);
                int rank = Square.getRank(square);

                wTable[pieceType.id][rank][file] =
                        pieceType == Piece.PAWN
                                ? PawnBonus[rank][file]
                                : PieceBonus[pieceType.id][rank][Square.edgeDistance(file)];
                bTable[pieceType.id][rank][file] =
                        pieceType == Piece.PAWN
                                ? PawnBonus[Square.flipRank(rank)][file]
                                : PieceBonus[pieceType.id][Square.flipRank(rank)][Square.edgeDistance(file)];
            }
        }
        table[Color.W.ordinal()] = wTable;
        table[Color.B.ordinal()] = bTable;
    }
}
