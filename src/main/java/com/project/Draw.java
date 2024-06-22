package com.project;

public abstract class Draw {
  protected String symbol;

  public Draw(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return symbol;
  }
}
