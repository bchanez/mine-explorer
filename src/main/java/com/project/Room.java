package com.project;

import java.util.Optional;

import com.project.object.GameObject;

public class Room extends Draw {

  private static final String symbol = "  ";
  private static final String topAndBottomSymbol = "----";
  private static final String leftAndRightSymbol = "|";

  private Player player;
  private GameObject gameObject;

  private Wall top;
  private Wall bottom;
  private Wall left;
  private Wall right;

  public Room(Coordinate coordinate) {
    super(symbol, coordinate);
    top = new Wall(topAndBottomSymbol);
    bottom = new Wall(topAndBottomSymbol);
    left = new Wall(leftAndRightSymbol);
    right = new Wall(leftAndRightSymbol);
  }

  public Wall getTop() {
    return top;
  }

  public Wall getBottom() {
    return bottom;
  }

  public Wall getLeft() {
    return left;
  }

  public Wall getRight() {
    return right;
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
