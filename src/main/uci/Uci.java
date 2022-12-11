package main.uci;

import main.*;
import main.moveEval.MoveEval;
import main.moveGen.UnmakeDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Uci {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            String inputStr = input.nextLine();
            if ("uci".equals(inputStr)) {
                inputUCI();
            } else if (inputStr.startsWith("setoption")) {
                inputSetOption(inputStr);
            } else if ("isready".equals(inputStr)) {
                inputIsReady();
            } else if ("ucinewgame".equals(inputStr)) {
                inputUCINewGame();
            } else if (inputStr.startsWith("position")) {
                inputPosition(inputStr);
            } else if (inputStr.startsWith("go")) {
                inputGo();
            } else if ("quit:".equals(inputStr)) {
                inputQuit();
            } else if ("print".equals(inputStr)) {
                print();
            }
        }
    }


    public static void inputUCI() {
        System.out.println("id name " + "croChess");
        System.out.println("id author alex");
        //options go here
        System.out.println("uciok");
    }

    public static void inputSetOption(String inputString) {
        //set options
    }

    public static void inputIsReady() {
        System.out.println("readyok");
    }

    public static void inputUCINewGame() {
        //add code here
        GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static void inputQuit() {
        System.exit(0);
    }

    public static int algebraToMove(String moveNotation) {
        int from = moveNotation.charAt(0) - 'a' + (16 * (Character.getNumericValue(moveNotation.charAt(1)) - 1));
        int to = moveNotation.charAt(2) - 'a' + (16 * (Character.getNumericValue(moveNotation.charAt(3)) - 1));

        int move = (from << 7) | to;
        // handle castling
        if (Piece.extractPieceType(GameState.board[from]) == Piece.KING) {
            Color color = Color.extractColor(GameState.board[from]);
            if (from - to == 2) {
                move = color == Color.W ? (Castle.W_Q.value << 14) | move : (Castle.B_q.value << 14) | move;
            } else if (
                    from - to == -2
            ) move = color == Color.W ? (Castle.W_K.value << 14) | move : (Castle.B_k.value << 14) | move;
        }

        if (moveNotation.length() == 5) {
            Map<Character, Piece> pieceMap = new HashMap<>();
            pieceMap.put('q',
                    Piece.QUEEN);
            pieceMap.put('r',
                    Piece.ROOK);
            pieceMap.put('n',
                    Piece.KNIGHT);
            pieceMap.put('b',
                    Piece.BISHOP);

            move = (pieceMap.get(moveNotation.charAt(4)).id << 18) | move;
        }

        return move;
    }

    public static void inputPosition(String input) {
        input = input.substring(9)
                     .concat(" ");
        if (input.contains("startpos ")) {
            input = input.substring(9);
            GameState.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        } else if (input.contains("fen")) {
            input = input.substring(4);
            GameState.loadFen(input);
        }

        if (input.contains("moves")) {
            input = input.substring(input.indexOf("moves") + 6);
            //make each of the moves
            String[] moves = input.split(" ");
            UnmakeDetails moveDetails = new UnmakeDetails();
            for (String moveNotation : moves) {
                int move = algebraToMove(moveNotation);
                GameState.makeMove(move, moveDetails);
                moveDetails.reset();
            }
        }
    }

    public static String moveToAlgebra(int move) {
        Square from = Square.lookup.get((move >> 7) & 127);
        Square to = Square.lookup.get(move & 127);
        int promote = move >> 18;

        String algebra = from.toString()
                             .toLowerCase() + to.toString()
                                                .toLowerCase();
        if (promote != 0) {
            Map<Integer, Character> abbrMap = new HashMap<>();
            abbrMap.put(5, 'q');
            abbrMap.put(4, 'r');
            abbrMap.put(2, 'n');
            abbrMap.put(3, 'b');

            algebra += abbrMap.get(promote);
        }

        return algebra;
    }

    public static void inputGo() {
        //search for best move
        int bestMove = MoveEval.getBestMove();
        String algebra = moveToAlgebra(bestMove);
        System.out.println("bestmove " + algebra);
    }

    public static void print() {
        GameState.printBoard();
    }
}
