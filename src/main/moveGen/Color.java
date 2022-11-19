package main.moveGen;

public enum Color {
    W(8), B(16);

    final public int id;

    Color(int id) {
        this.id = id;
    }

    public static Color extractColor(int boardVal) {
        return (boardVal & 24) == 16 ? Color.B : Color.W;
    }

    public static Color getOppColor(Color color) {
        return color == Color.W ? Color.B : Color.W;
    }
}
