package com.project;

import java.util.Optional;
import java.util.Random;

import com.project.object.Exit;

public class Board {

  private Room[][] matrix;
  private int nbRow;
  private int nbColumn;

  public Board(int nbRow, int nbColumn, Player player, Random random) {
    this.nbRow = nbRow;
    this.nbColumn = nbColumn;

    initMatrix();
    player.setBoard(this);
    new Exit(this, random);
  }

  public boolean isRoomExist(Coordinate coordinate) {
    return 0 <= coordinate.getX() && coordinate.getX() < nbColumn && 0 <= coordinate.getY()
        && coordinate.getY() < nbRow;
  }

  public Optional<Room> getRoomByCoordinate(Coordinate coordinate) {
    if (isRoomExist(coordinate)) {
      return Optional.ofNullable(matrix[coordinate.getY()][coordinate.getX()]);
    }

    return Optional.empty();
  }

  public void initMatrix() {
    matrix = new Room[nbRow][nbColumn];
    for (int y = 0; y < nbRow; y++) {
      for (int x = 0; x < nbColumn; x++) {
        matrix[y][x] = new Room(new Coordinate(x, y));
      }
    }
  }

  public Room[][] getMatrix() {
    return matrix;
  }

  public int getNbRow() {
    return nbRow;
  }

  public int getNbColumn() {
    return nbColumn;
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
