package com.project.object;

import java.util.Random;

import com.project.Board;
import com.project.Draw;
import com.project.Player;

public abstract class GameObject extends Draw {
  public GameObject(String symbol) {
    super(symbol);
  }

  abstract protected void setPosition(Board board, Random random);

  abstract public void performAction(Player player);
}
