package com.project;

import java.util.ArrayList;
import java.util.List;

import com.project.item.Grenade;

public class Player extends Draw {

  private static final String SYMBOL = "♛♛";
  private static final double PERCENTAGE_GRENADE = 0.1;

  private PlayerState state;
  private Board board;
  private List<Grenade> grenades;

  public Player() {
    super(SYMBOL);
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
    if (this.coordinate != null) {
      board.getRoomByCoordinate(this.coordinate).playerLeaveRoom();
    }

    this.coordinate = coordinate;
    board.getRoomByCoordinate(this.coordinate).playerEnterRoom(this);
  }

  public void moveToDirection(String direction) {
    if (!canMoveToDirection(direction)) {
      return;
    }

    setCoordinate(getNextCoordinate(direction));
  }

  private boolean canMoveToDirection(String direction) {
    Room currentRoom = board.getRoomByCoordinate(this.coordinate);
    Wall wallByDirection = currentRoom.getWallByDirection(direction);
    return wallByDirection.isDestroyed();
  }

  public void throwGrenadeInDirection(String direction) {
    int numberOfGrenades = grenades.size();

    if (numberOfGrenades == 0)
      return;

    Grenade grenade = grenades.remove(numberOfGrenades - 1);
    grenade.use(this, direction);
    moveToDirection(direction);
  }

  private void setupGrenades() {
    int totalNumberOfRooms = board.getNbRow() * board.getNbColumn();
    int totalNumberOfGrenades = (int) (totalNumberOfRooms * PERCENTAGE_GRENADE);

    collectGrenades(totalNumberOfGrenades);
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

  public void collectGrenades(int quantity) {
    for (int i = 0; i < quantity; i++) {
      grenades.add(new Grenade(board));
    }
  }
}
