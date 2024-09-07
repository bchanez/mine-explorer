package com.project;

import java.util.Optional;

import com.project.item.StaticItem;

public class Room extends Draw {

  private static final String symbol = "  ";

  private static final String topAndBottomSymbol = "----";
  private String topAndBottomDestroySymbol = "    ";
  private static final String leftAndRightSymbol = "|";
  private String leftAndRightDestroySymbol = " ";

  private Player player;
  private StaticItem staticItem;

  private Wall top;
  private Wall bottom;
  private Wall left;
  private Wall right;

  public Room(Board board, Coordinate coordinate) {
    super(symbol, coordinate);
    setupWalls(board, coordinate);
  }

  private void setupWalls(Board board, Coordinate coordinate) {
    int maxY = board.getNbRow();
    int maxX = board.getNbColumn();

    boolean topIsIndestructible = 0 == coordinate.getY();
    boolean bottomIsIndestructible = 0 == maxY - 1;
    boolean leftIsIndestructible = 0 == coordinate.getX();
    boolean rightIsIndestructible = 0 == maxX - 1;

    top = new Wall(topIsIndestructible, topAndBottomSymbol, topAndBottomDestroySymbol);
    bottom = new Wall(bottomIsIndestructible, topAndBottomSymbol, topAndBottomDestroySymbol);
    left = new Wall(leftIsIndestructible, leftAndRightSymbol, leftAndRightDestroySymbol);
    right = new Wall(rightIsIndestructible, leftAndRightSymbol, leftAndRightDestroySymbol);
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

  public Wall getWallByDirection(String direction) {
    Wall wall = null;

    switch (direction) {
      case "z":
        wall = top;
        break;
      case "q":
        wall = left;
        break;
      case "s":
        wall = bottom;
        break;
      case "d":
        wall = right;
        break;
    }

    return wall;
  }

  public void playerEnterRoom(Player player) {
    this.player = player;
    if (staticItem != null) {
      staticItem.performAction(player);
    }
  }

  public void playerLeaveRoom() {
    this.player = null;
  }

  public void setStaticItem(StaticItem staticItem) {
    this.staticItem = staticItem;
    Optional.ofNullable(this.staticItem).ifPresent(o -> o.setCoordinate(coordinate));
  }

  public StaticItem getStaticItem() {
    return staticItem;
  }

  public Player getPlayer() {
    return player;
  }

  @Override
  public String toString() {
    if (player != null) {
      return player.toString();
    }
    if (staticItem != null) {
      return staticItem.toString();
    }
    return symbol;
  }
}
