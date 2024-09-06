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

  public Coordinate getNextCoordinate(String direction) {
    Coordinate nextCoordinate = getCoordinate();

    switch (direction) {
      case "z":
        nextCoordinate = new Coordinate(coordinate.getX(), coordinate.getY() - 1);
        break;
      case "q":
        nextCoordinate = new Coordinate(coordinate.getX() - 1, coordinate.getY());
        break;
      case "s":
        nextCoordinate = new Coordinate(coordinate.getX(), coordinate.getY() + 1);
        break;
      case "d":
        nextCoordinate = new Coordinate(coordinate.getX() + 1, coordinate.getY());
        break;
    }

    return nextCoordinate;
  }

  @Override
  public String toString() {
    return symbol;
  }
}
