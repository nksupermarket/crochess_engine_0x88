
package test;

import main.types.Color;
import main.GameState;
import main.types.Piece;
import main.ZobristKey;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import static org.hamcrest.CoreMatchers.*;

public class ZobristKeyTest {
    @BeforeEach
    public void init() {
        GameState.board[0] = Color.W.id | Piece.ROOK.id;
        GameState.board[1] = Color.W.id | Piece.KNIGHT.id;
        GameState.board[2] = Color.W.id | Piece.BISHOP.id;
        GameState.board[3] = Color.W.id | Piece.QUEEN.id;
        GameState.board[4] = Color.W.id | Piece.KING.id;
        GameState.board[5] = Color.W.id | Piece.BISHOP.id;
        GameState.board[6] = Color.W.id | Piece.KNIGHT.id;
        GameState.board[7] = Color.W.id | Piece.ROOK.id;
        GameState.board[16] = Color.W.id | Piece.PAWN.id;
        GameState.board[17] = Color.W.id | Piece.PAWN.id;
        GameState.board[18] = Color.W.id | Piece.PAWN.id;
        GameState.board[19] = Color.W.id | Piece.PAWN.id;
        GameState.board[20] = Color.W.id | Piece.PAWN.id;
        GameState.board[21] = Color.W.id | Piece.PAWN.id;
        GameState.board[22] = Color.W.id | Piece.PAWN.id;
        GameState.board[23] = Color.W.id | Piece.PAWN.id;

        GameState.board[112] = Color.B.id | Piece.ROOK.id;
        GameState.board[113] = Color.B.id | Piece.KNIGHT.id;
        GameState.board[114] = Color.B.id | Piece.BISHOP.id;
        GameState.board[115] = Color.B.id | Piece.QUEEN.id;
        GameState.board[116] = Color.B.id | Piece.KING.id;
        GameState.board[117] = Color.B.id | Piece.BISHOP.id;
        GameState.board[118] = Color.B.id | Piece.KNIGHT.id;
        GameState.board[119] = Color.B.id | Piece.ROOK.id;
        GameState.board[96] = Color.B.id | Piece.PAWN.id;
        GameState.board[97] = Color.B.id | Piece.PAWN.id;
        GameState.board[98] = Color.B.id | Piece.PAWN.id;
        GameState.board[99] = Color.B.id | Piece.PAWN.id;
        GameState.board[100] = Color.B.id | Piece.PAWN.id;
        GameState.board[101] = Color.B.id | Piece.PAWN.id;
        GameState.board[102] = Color.B.id | Piece.PAWN.id;
        GameState.board[103] = Color.B.id | Piece.PAWN.id;

        GameState.zobristHash = ZobristKey.hash();
    }

    @Test
    public void hashIsConsistent() {
        long ogHash = GameState.zobristHash;

        GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        MatcherAssert.assertThat(GameState.zobristHash, is(ogHash));
    }

    @Nested
    class hashIsUnique {
        @Test
        public void differentPosition() {
            long ogHash = GameState.zobristHash;

            GameState.loadFen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");

            MatcherAssert.assertThat(GameState.zobristHash, not(ogHash));
        }

        @Test
        public void differentCastlingRights() {
            long ogHash = GameState.zobristHash;

            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");

            MatcherAssert.assertThat(GameState.zobristHash, not(ogHash));
        }

        @Test
        public void differentActiveColor() {
            long ogHash = GameState.zobristHash;

            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");

            MatcherAssert.assertThat(GameState.zobristHash, not(ogHash));
        }
    }
}
