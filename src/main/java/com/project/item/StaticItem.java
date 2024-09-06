package com.project.item;

import com.project.Board;
import com.project.Draw;
import com.project.Player;

public abstract class StaticItem extends Draw {

  public StaticItem(String symbol) {
    super(symbol);
  }

  abstract protected void setPosition(Board board);

  abstract public void performAction(Player player);
}
