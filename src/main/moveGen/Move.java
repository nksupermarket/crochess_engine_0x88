package main.moveGen;

import lombok.ToString;

import java.util.Objects;

@ToString()
public class Move {
    public Square from;
    public Square to;
    public Castle castle;
    public Piece promote;

    public Move(Square from,
                Square to) {
        this.from = from;
        this.to = to;
    }

    public Move(Square from,
                Square to,
                Piece promote) {
        this.from = from;
        this.to = to;
        this.promote = promote;
    }

    public Move(Square from,
                Castle castle) {
        this.from = from;
        this.castle = castle;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return from == move.from && to == move.to && castle == move.castle && promote == move.promote;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, castle, promote);
    }
}
