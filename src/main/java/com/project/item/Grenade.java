package com.project.item;

import com.project.Board;
import com.project.Coordinate;
import com.project.Player;
import com.project.Room;

public class Grenade extends UsableItem {

  public Grenade(Board board) {
    super(board);
  }

  @Override
  public void use(Player player, String direction) {
    Coordinate playerCoordinate = player.getCoordinate();
    Room playerRoom = board.getRoomByCoordinate(playerCoordinate);

    Coordinate nexCoordinate = playerRoom.getNextCoordinate(direction);
    Room nextRoom = board.getRoomByCoordinate(nexCoordinate);

    playerRoom.getWallByDirection(direction).destroy();
    nextRoom.getWallByDirection(getReverseDirection(direction)).destroy();

    player.moveToDirection(direction);
  }

  private String getReverseDirection(String direction) {
    String reverseDirection = null;

    switch (direction) {
      case "z":
        reverseDirection = "s";
        break;
      case "q":
        reverseDirection = "d";
        break;
      case "s":
        reverseDirection = "z";
        break;
      case "d":
        reverseDirection = "q";
        break;
    }

    return reverseDirection;
  }
}
