package com.project;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.project.item.Exit;
import com.project.item.GrenadeBox;
import com.project.item.Mine;

public class Board {

  private static final int EXIT_QUANTITY = 1;
  private static final double PERCENTAGE_MINE = 0.03;
  private static final double PERCENTAGE_GRENADE_BOX = 0.05;

  private Room[][] matrix;
  private int nbRow;
  private int nbColumn;

  public Board(int nbRow, int nbColumn, Player player) throws Exception {
    this.nbRow = nbRow;
    this.nbColumn = nbColumn;

    initMatrix();
    player.setBoard(this);

    int totalNumberOfRooms = nbRow * nbColumn;
    setUpGameObject(Exit.class, EXIT_QUANTITY);
    setUpGameObject(Mine.class, (int) (totalNumberOfRooms * PERCENTAGE_MINE));
    setUpGameObject(GrenadeBox.class, (int) (totalNumberOfRooms * PERCENTAGE_GRENADE_BOX));
  }

  private void initMatrix() {
    matrix = new Room[nbRow][nbColumn];
    for (int y = 0; y < nbRow; y++) {
      for (int x = 0; x < nbColumn; x++) {
        matrix[y][x] = new Room(this, new Coordinate(x, y));
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

  public Room getRoomByCoordinate(Coordinate coordinate) {
    return matrix[coordinate.getY()][coordinate.getX()];
  }

  public List<Room> getRoomsWithoutGameObjectAndPlayer() {
    List<Room> rooms = new ArrayList<>();
    Room room = null;

    for (int y = 0; y < nbRow; y++) {
      for (int x = 0; x < nbColumn; x++) {
        room = matrix[y][x];
        if (room.getStaticItem() == null && room.getPlayer() == null) {
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

      display += "+";
      for (int x = 0; x < nbColumn; x++) {
        display += matrix[y][x].getTop() + "+";
      }
      display += "\n";

      display += "|";
      for (int x = 0; x < nbColumn; x++) {
        display += " " + matrix[y][x] + " " + matrix[y][x].getRight();
      }
      display += "\n";
    }

    display += "+";
    for (int x = 0; x < nbColumn; x++) {
      display += matrix[nbRow - 1][x].getBottom() + "+";
    }
    display += "\n";

    return display;
  }
}
