package com.project;

public class Room {
  private final String symbol = "  ";
  private final String exitSymbol = "()";

  private Coordinate coordinate;
  private Player player;
  private boolean exit;

  public Room() {
    this.coordinate = new Coordinate(-1, -1);
  }

  public Room(Coordinate coordinate) {
    this.coordinate = coordinate;
  }

  public void playerEnterRoom(Player player) {
    this.player = player;
    player.setHasWon(exit);
  }

  public void playerLeaveRoom() {
    this.player = null;
  }

  public void setExit(boolean exit) {
    this.exit = exit;
  }

  @Override
  public String toString() {
    if (player != null) {
      return player.toString();
    }
    if (exit) {
      return exitSymbol;
    }
    return symbol;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }
}
