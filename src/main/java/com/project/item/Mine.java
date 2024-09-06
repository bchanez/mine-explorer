package com.project.item;

import java.util.List;

import com.project.Board;
import com.project.Player;
import com.project.PlayerState;
import com.project.Room;
import com.project.util.RandomUtil;

public class Mine extends StaticItem {

  public Mine(Board board) {
    super("**");
    setPosition(board);
  }

  @Override
  protected void setPosition(Board board) {
    List<Room> rooms = board.getRoomsWithoutGameObjectAndPlayer();
    int positionRoom = RandomUtil.nextInt(rooms.size());
    rooms.get(positionRoom).setStaticItem(this);
  }

  @Override
  public void performAction(Player player) {
    player.setState(PlayerState.LOST);
  }
}
