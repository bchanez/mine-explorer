package com.project.object;

public abstract class GameObject {
  protected String symbol;

  public GameObject(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return symbol;
  }
}
