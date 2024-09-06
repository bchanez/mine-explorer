package com.project;

import java.util.ArrayList;
import java.util.List;

import com.project.item.Grenade;

public class Player extends Draw {

  private static final String symbol = "♛♛";
  private static final double PERCENTAGE_GRENADE = 0.1;

  private PlayerState state;
  private Board board;
  private List<Grenade> grenades;

  public Player() {
    super(symbol);
    state = PlayerState.PLAYING;
    grenades = new ArrayList<>();
  }

  public void setBoard(Board board) {
    this.board = board;
    setCoordinate(new Coordinate(board.getNbColumn() / 2, board.getNbRow() / 2));
    setupGrenades();
  }

  @Override
  public void setCoordinate(Coordinate coordinate) {
    if (!board.isRoomExist(coordinate))
      return;

    if (this.coordinate != null) {
      board.getRoomByCoordinate(this.coordinate).ifPresent(room -> room.playerLeaveRoom());
    }

    this.coordinate = coordinate;
    board.getRoomByCoordinate(this.coordinate).ifPresent(room -> room.playerEnterRoom(this));
  }

  private Coordinate getNextCoordinate(String direction) {
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

  public void moveToDirection(String direction) {
    setCoordinate(getNextCoordinate(direction));
  }

  public void throwGrenadeInDirection(String direction) {
    int numberOfGrenades = grenades.size();

    if (numberOfGrenades == 0)
      return;

    grenades.remove(numberOfGrenades - 1);
    moveToDirection(direction);
  }

  private void setupGrenades() {
    int totalNumberOfRooms = board.getNbRow() * board.getNbColumn();
    int totalNumberOfGrenades = (int) (totalNumberOfRooms * PERCENTAGE_GRENADE);

    for (int i = 0; i < totalNumberOfGrenades; i++) {
      grenades.add(new Grenade());
    }
  }

  public PlayerState getState() {
    return state;
  }

  public void setState(PlayerState state) {
    this.state = state;
  }

  public List<Grenade> getGrenades() {
    return grenades;
  }
}
