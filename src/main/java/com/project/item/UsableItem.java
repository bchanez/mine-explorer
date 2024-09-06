package com.project.item;

import com.project.Board;
import com.project.Player;

public abstract class UsableItem {

  Board board;

  UsableItem(Board board) {
    this.board = board;
  }

  public abstract void use(Player player, String direction);
}
