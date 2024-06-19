package com.project;

public class Player {
  private final String symbol = "♛♛";

  private boolean hasWon;
  private Coordinate coordinate;
  private Board board;

  public void setBoard(Board board) {
    this.board = board;
  }

  public void setCoordinate(Coordinate coordinate) {
    if (!board.isRoomExist(coordinate))
      return;

    if (this.coordinate != null)
      board.getRoomByCoordinate(this.coordinate).playerLeaveRoom();

    this.coordinate = coordinate;
    board.getRoomByCoordinate(this.coordinate).playerEnterRoom(this);
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public void moveToDirection(String direction) {
    switch (direction) {
      case "z":
        setCoordinate(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
        break;
      case "q":
        setCoordinate(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
        break;
      case "s":
        setCoordinate(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
        break;
      case "d":
        setCoordinate(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
        break;
      default:
        break;
    }
  }

  public boolean getHasWon() {
    return hasWon;
  }

  public void setHasWon(boolean hasWon) {
    this.hasWon = hasWon;
  }

  @Override
  public String toString() {
    return symbol;
  }
}
