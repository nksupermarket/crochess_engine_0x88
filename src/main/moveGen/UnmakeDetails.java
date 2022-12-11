package main.moveGen;

import main.Castle;
import main.Square;

import java.util.Objects;

public class UnmakeDetails {
    public int capturedPiece;
    public Square capturePieceSquare;
    public Square from;
    public Square to;
    public Castle castle;
    public boolean isPromote;
    public int prevCastleRights = 15;
    public Square prevEnPassant = Square.NULL;
    public int prevHalfmoves;

    public void reset() {
        capturedPiece = 0;
        capturePieceSquare = null;
        from = null;
        to = null;
        castle = null;
        isPromote = false;
        prevCastleRights = 0;
        prevEnPassant = null;
        prevHalfmoves = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnmakeDetails that = (UnmakeDetails) o;
        return capturedPiece == that.capturedPiece && isPromote == that.isPromote &&
                prevCastleRights == that.prevCastleRights && prevHalfmoves == that.prevHalfmoves &&
                capturePieceSquare == that.capturePieceSquare && from == that.from && to == that.to &&
                castle == that.castle && prevEnPassant == that.prevEnPassant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(capturedPiece, capturePieceSquare, from, to, castle, isPromote, prevCastleRights,
                prevEnPassant,
                prevHalfmoves);
    }
}
