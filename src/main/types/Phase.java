package main.types;

public enum Phase {

    OPEN(19404), MG(15258), EG(3915);

    final public int limit;

    Phase(int limit) {
        this.limit = limit;
    }
}
