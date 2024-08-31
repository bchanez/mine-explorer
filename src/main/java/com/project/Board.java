package com.project;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.project.object.Exit;
import com.project.object.Mine;

public class Board {

  private static final double PERCENTAGE_MINE = 0.03;

  private Room[][] matrix;
  private int nbRow;
  private int nbColumn;

  public Board(int nbRow, int nbColumn, Player player) throws Exception {
    this.nbRow = nbRow;
    this.nbColumn = nbColumn;

    initMatrix();
    player.setBoard(this);
    new Exit(this);

    int totalNumberOfRooms = nbRow * nbColumn;
    setUpGameObject(Mine.class, (int) (totalNumberOfRooms * PERCENTAGE_MINE));
  }

  private void initMatrix() {
    matrix = new Room[nbRow][nbColumn];
    for (int y = 0; y < nbRow; y++) {
      for (int x = 0; x < nbColumn; x++) {
        matrix[y][x] = new Room(new Coordinate(x, y));
      }
    }
  }

  public int getNbRow() {
    return nbRow;
  }

  public int getNbColumn() {
    return nbColumn;
  }

  private void setUpGameObject(Class<?> clazz, int totalNumberOfGameObjects) throws Exception {
    Constructor<?> constructor = clazz.getConstructor(Board.class);
    for (int i = 0; i < totalNumberOfGameObjects; i++) {
      constructor.newInstance(this);
    }
  }

  public boolean isRoomExist(Coordinate coordinate) {
    return coordinate != null && 0 <= coordinate.getX() && coordinate.getX() < nbColumn && 0 <= coordinate.getY()
        && coordinate.getY() < nbRow;
  }

  public Optional<Room> getRoomByCoordinate(Coordinate coordinate) {
    if (isRoomExist(coordinate)) {
      return Optional.ofNullable(matrix[coordinate.getY()][coordinate.getX()]);
    }

    return Optional.empty();
  }

  public List<Room> getRoomsWithoutGameObjectAndPlayer() {
    List<Room> rooms = new ArrayList<>();
    Room room = null;

    for (int y = 0; y < nbRow; y++) {
      for (int x = 0; x < nbColumn; x++) {
        room = matrix[y][x];
        if (room.getGameObject() == null && room.getPlayer() == null) {
          rooms.add(room);
        }
      }
    }

    return rooms;
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
