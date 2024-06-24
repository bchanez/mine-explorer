package com.project.object;

import com.project.Board;
import com.project.Draw;
import com.project.Player;

public abstract class GameObject extends Draw {

  public GameObject(String symbol) {
    super(symbol);
  }

  abstract protected void setPosition(Board board);

  abstract public void performAction(Player player);
}
