package com.project.object;

import java.util.List;
import java.util.Random;

import com.project.Board;
import com.project.Player;
import com.project.PlayerState;
import com.project.Room;

public class Mine extends GameObject {

  public Mine(Board board, Random random) {
    super("**");
    setPosition(board, random);
  }

  @Override
  protected void setPosition(Board board, Random random) {
    List<Room> rooms = board.getRoomsWithoutGameObjectAndPlayer();
    int positionRoom = random.nextInt(rooms.size());
    rooms.get(positionRoom).setGameObject(this);
  }

  @Override
  public void performAction(Player player) {
    player.setState(PlayerState.LOST);
  }
}
