package com.project;

import java.util.Random;

public class Game {

  private Menu menu;
  private Board board;
  private Player player;

  private boolean gameRunning;

  public Game(Menu menu, Player player, Random random) {
    this.menu = menu;
    this.player = player;

    board = new Board(menu.getNbRow(), menu.getNbColumn(), player, random);
    player.setBoard(board);

    board.initMatrix();
    gameRunning = true;
  }

  public void loop() {
    do {
      System.out.println(board);
      handleAction(menu.doAction(player));
    } while (gameRunning);
  }

  private void handleAction(int action) {
    switch (action) {
      case 0:
        gameRunning = false;
        break;
      case 1:
        String direction = menu.chooseDirectionToMovePlayer();
        player.moveToDirection(direction);
        break;

      default:
        break;
    }
  }

  public boolean isGameRunning() {
    return gameRunning;
  }
}
