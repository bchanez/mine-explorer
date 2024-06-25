package com.project;

import java.util.ArrayList;
import java.util.List;

import com.project.object.Grenade;

public class Player extends Draw {

  private static final double PERCENTAGE_GRENADE = 0.10;

  private PlayerState state;
  private Board board;
  private List<Grenade> grenades;

  public Player() {
    super("♛♛");
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

  public void throwGrenadeInDirection(String direction) {
    // todo
  }

  public PlayerState getState() {
    return state;
  }

  public void setState(PlayerState state) {
    this.state = state;
  }

  private void setupGrenades() {
    int totalNumberOfRooms = board.getNbRow() * board.getNbColumn();
    int totalNumberOfGrenades = (int) (totalNumberOfRooms * PERCENTAGE_GRENADE);

    for (int i = 0; i < totalNumberOfGrenades; i++) {
      grenades.add(new Grenade());
    }
  }

  public List<Grenade> getGrenades() {
    return grenades;
  }
}
