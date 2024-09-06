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

    switch (direction) {
      case "z":
        playerRoom.getTop().destroy();
        nextRoom.getBottom().destroy();
        break;
      case "q":
        playerRoom.getLeft().destroy();
        nextRoom.getRight().destroy();
        break;
      case "s":
        playerRoom.getBottom().destroy();
        nextRoom.getTop().destroy();
        break;
      case "d":
        playerRoom.getRight().destroy();
        nextRoom.getLeft().destroy();
        break;
    }
  }
}
