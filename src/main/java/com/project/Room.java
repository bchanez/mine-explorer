package com.project;

import java.util.Optional;

import com.project.object.GameObject;

public class Room extends Draw {

  private static final String symbol = "  ";

  private Player player;
  private GameObject gameObject;

  public Room() {
    super(symbol, new Coordinate(-1, -1));
  }

  public Room(Coordinate coordinate) {
    super(symbol, coordinate);
  }

  public void playerEnterRoom(Player player) {
    this.player = player;
    if (gameObject != null) {
      gameObject.performAction(player);
    }
  }

  public void playerLeaveRoom() {
    this.player = null;
  }

  public void setGameObject(GameObject gameObject) {
    this.gameObject = gameObject;
    Optional.ofNullable(this.gameObject).ifPresent(o -> o.setCoordinate(coordinate));
  }

  public GameObject getGameObject() {
    return gameObject;
  }

  public Player getPlayer() {
    return player;
  }

  @Override
  public String toString() {
    if (player != null) {
      return player.toString();
    }
    if (gameObject != null) {
      return gameObject.toString();
    }
    return symbol;
  }
}
