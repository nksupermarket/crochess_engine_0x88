package main.moveGen;

public class UnmakeDetails {
    int capturedPiece;
    Square capturePieceSquare;
    Square from;
    Square to;
    Castle castle;
    boolean isPromote;
    int prevCastleRights;
    Square prevEnPassant;
    int prevHalfmoves;

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
}
