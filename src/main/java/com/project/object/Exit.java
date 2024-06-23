package com.project.object;

import java.util.Random;

import com.project.Board;
import com.project.Coordinate;
import com.project.Player;
import com.project.PlayerState;

public class Exit extends GameObject {

  public Exit(Board board, Random random) {
    super("()");
    setPosition(board, random);
  }

  @Override
  protected void setPosition(Board board, Random random) {
    int cornerBoard = random.nextInt(4);
    Coordinate cornerCoordinate = null;

    switch (cornerBoard) {
      case 0:
        cornerCoordinate = new Coordinate(0, 0);
        break;
      case 1:
        cornerCoordinate = new Coordinate(board.getNbColumn() - 1, 0);
        break;
      case 2:
        cornerCoordinate = new Coordinate(0, board.getNbRow() - 1);
        break;
      case 3:
        cornerCoordinate = new Coordinate(board.getNbColumn() - 1, board.getNbRow() - 1);
        break;

      default:
        break;
    }

    board.getRoomByCoordinate(cornerCoordinate).ifPresent(room -> room.setGameObject(this));
  }

  @Override
  public void performAction(Player player) {
    player.setState(PlayerState.WON);
  }
}
