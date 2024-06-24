package com.project;

public abstract class Draw {

  protected String symbol;
  protected Coordinate coordinate;

  public Draw(String symbol) {
    this.symbol = symbol;
  }

  public Draw(String symbol, Coordinate coordinate) {
    this.symbol = symbol;
    this.coordinate = coordinate;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }

  @Override
  public String toString() {
    return symbol;
  }
}
