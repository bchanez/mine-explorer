package com.project.object;

import com.project.Board;
import com.project.Coordinate;
import com.project.Player;
import com.project.PlayerState;
import com.project.util.RandomUtil;

public class Exit extends GameObject {

  public Exit(Board board) {
    super("()");
    setPosition(board);
  }

  @Override
  protected void setPosition(Board board) {
    int cornerBoard = RandomUtil.nextInt(4);
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
