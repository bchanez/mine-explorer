package com.project;

import java.util.Random;

public class Board {

  private Room[][] matrix;
  private int nbRow;
  private int nbColumn;

  private Random random;
  private Player player;

  public Board(int nbRow, int nbColumn, Player player, Random random) {
    this.nbRow = nbRow;
    this.nbColumn = nbColumn;
    this.player = player;
    this.random = random;
  }

  public boolean isRoomExist(Coordinate coordinate) {
    return 0 <= coordinate.getX() && coordinate.getX() < nbColumn && 0 <= coordinate.getY()
        && coordinate.getY() < nbRow;
  }

  public Room getRoomByCoordinate(Coordinate coordinate) {
    return matrix[coordinate.getY()][coordinate.getX()];
  }

  private void setPositionPlayer() {
    player.setCoordinate(new Coordinate(nbColumn / 2, nbRow / 2));
  }

  private void setPositionExit() {
    int cornerBoard = random.nextInt(4);
    Coordinate cornerCoordinate = null;

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

    getRoomByCoordinate(cornerCoordinate).setExit(true);
  }

  public void initMatrix() {
    matrix = new Room[nbRow][nbColumn];
    for (int y = 0; y < nbRow; y++) {
      for (int x = 0; x < nbColumn; x++) {
        matrix[y][x] = new Room(new Coordinate(x, y));
      }
    }

    setPositionPlayer();
    setPositionExit();
  }

  public Room[][] getMatrix() {
    return matrix;
  }

  @Override
  public String toString() {
    String display = "";

    for (int y = 0; y < nbRow; y++) {
      for (int x = 0; x < nbColumn; x++) {
        display += matrix[y][x];
      }
      if (y < nbRow - 1) {
        display += "\n";
      }
    }

    return display;
  }
}
