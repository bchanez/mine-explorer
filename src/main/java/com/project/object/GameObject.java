package com.project.object;

import java.util.Random;

import com.project.Board;
import com.project.Draw;

public abstract class GameObject extends Draw {
  public GameObject(String symbol) {
    super(symbol);
  }

  abstract protected void setPosition(Board board, Random random);
}
