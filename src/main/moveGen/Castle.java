package main.moveGen;

public enum Castle {
    W_K(8,
            Square.G1,
            Square.F1), W_Q(4,
            Square.C1,
            Square.D1), B_k(2,
            Square.G8,
            Square.F8), B_q(1,
            Square.C8,
            Square.D8);


    final public int value;
    final public Square square;
    final public Square rSquare;

    Castle(int value,
           Square square,
           Square rSquare) {
        this.value = value;
        this.square = square;
        this.rSquare = rSquare;
    }

    public Square getSquareOfRook() {
        switch (this) {
            case W_K -> {
                return Square.H1;
            }
            case W_Q -> {
                return Square.A1;
            }
            case B_k -> {
                return Square.H8;
            }
            case B_q -> {
                return Square.A8;
            }
        }
        return Square.NULL;
    }
}
