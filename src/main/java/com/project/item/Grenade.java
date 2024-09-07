package com.project.item;

import java.util.Optional;

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
    Optional<Room> playerRoomOptional = board.getRoomByCoordinate(playerCoordinate);

    if (!playerRoomOptional.isPresent()) {
      return;
    }
    Room playerRoom = playerRoomOptional.get();

    Coordinate nexCoordinate = playerRoom.getNextCoordinate(direction);
    Optional<Room> nextRoomOptional = board.getRoomByCoordinate(nexCoordinate);

    if (!nextRoomOptional.isPresent()) {
      return;
    }
    Room nextRoom = nextRoomOptional.get();

    playerRoom.getWallByDirection(direction).destroy();
    nextRoom.getWallByDirection(getReverseDirection(direction)).destroy();
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
