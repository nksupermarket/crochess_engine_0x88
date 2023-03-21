package com.crochess.engine0x88.types;

public enum Phase {
  BG(27000),
  MG(15258),
  EG(3915);

  public final int limit;

  Phase(int limit) {
    this.limit = limit;
  }
}
