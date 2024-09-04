package com.project.object;

import com.project.Board;
import com.project.Coordinate;
import com.project.Player;
import com.project.PlayerState;
import com.project.util.RandomUtil;

public class Exit extends GameObject {

  private static final String symbol = "()";

  public Exit(Board board) {
    super(symbol);
    setPosition(board);
  }

  @Override
  protected void setPosition(Board board) {
    int cornerBoard = RandomUtil.nextInt(4);
    Coordinate cornerCoordinate = null;
    int nbColumn = board.getNbColumn();
    int nbRow = board.getNbRow();

    switch (cornerBoard) {
      case 0:
        cornerCoordinate = new Coordinate(0, 0);
        break;
      case 1:
        cornerCoordinate = new Coordinate(nbColumn - 1, 0);
        break;
      case 2:
        cornerCoordinate = new Coordinate(0, nbRow - 1);
        break;
      case 3:
        cornerCoordinate = new Coordinate(nbColumn - 1, nbRow - 1);
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
