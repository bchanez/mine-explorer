package com.project.item;

import java.util.List;

import com.project.Board;
import com.project.Player;
import com.project.Room;
import com.project.util.RandomUtil;

public class GrenadeBox extends StaticItem {

  private static final String SYMBOL = "☌";
  private static final int MAX_QUANTITY_GRENADE_BOX = 10;

  private int quantity;

  public GrenadeBox(Board board) {
    super(SYMBOL);
    this.quantity = RandomUtil.nextInt(MAX_QUANTITY_GRENADE_BOX);
    this.symbol = SYMBOL + this.quantity;
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
    player.collectGrenades(quantity);
  }
}
