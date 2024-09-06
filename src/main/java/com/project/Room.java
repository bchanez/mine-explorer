package com.project;

import java.util.Optional;

import com.project.item.StaticItem;

public class Room extends Draw {

  private static final String symbol = "  ";
  private static final String topAndBottomSymbol = "----";
  private static final String leftAndRightSymbol = "|";

  private Player player;
  private StaticItem staticItem;

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
