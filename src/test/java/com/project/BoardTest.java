package com.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.object.Exit;
import com.project.object.GameObject;
import com.project.object.Mine;
import com.project.util.FixedRandom;
import com.project.util.RandomUtil;

@ExtendWith(MockitoExtension.class)
class BoardTest {

  Board board;

  @Mock
  Player player;

  @Mock
  GameObject gameObject;

  @BeforeEach
  void setUp() {
    RandomUtil.setRandom(new FixedRandom(2));
  }

  @Test
  void testBoard() throws Exception {
    // given
    int nbRow = 1;
    int nbColumn = 2;

    // when
    board = new Board(nbRow, nbColumn, player);

    // then
    assertEquals(nbRow, board.getNbRow());
    assertEquals(nbColumn, board.getNbColumn());

  }

  @Test
  void boardHasOneExit() throws Exception {
    // given
    // when
    board = new Board(10, 10, player);

    // then
    int numberOfExit = countObjects(Exit.class);
    Assertions.assertEquals(1, numberOfExit);
  }

  @Test
  void boardHas3PercentageMine() throws Exception {
    // given
    // when
    board = new Board(10, 10, player);

    // then
    int numberOfMine = countObjects(Mine.class);
    Assertions.assertEquals(3, numberOfMine);
  }

  @ParameterizedTest
  @MethodSource
  void isRoomExistShouldReturnTrueIfRoomExists(Boolean expected, Coordinate coordinate) throws Exception {
    // given
    board = new Board(1, 1, player);

    // when
    boolean roomExist = board.isRoomExist(coordinate);

    // then
    Assertions.assertEquals(expected, roomExist);
  }

  private static Stream<Arguments> isRoomExistShouldReturnTrueIfRoomExists() {
    return Stream.of(
        Arguments.of(true, new Coordinate(0, 0)),
        Arguments.of(false, new Coordinate(-1, 0)),
        Arguments.of(false, new Coordinate(0, -1)),
        Arguments.of(false, new Coordinate(1, 0)),
        Arguments.of(false, new Coordinate(0, 1)),
        Arguments.of(false, null));
  }

  @Test
  void getRoomByCoordinateShouldReturnRoom() throws Exception {
    // given
    Coordinate coordinate = new Coordinate(0, 0);
    board = new Board(1, 1, player);

    // when
    Optional<Room> optionalRoom = board.getRoomByCoordinate(coordinate);

    // then
    assertTrue(optionalRoom.isPresent());
    optionalRoom.ifPresent(room -> assertEquals(coordinate, room.getCoordinate()));

  }

  @Test
  void getRoomByCoordinateShouldReturnEmpty() throws Exception {
    // given
    Coordinate coordinate = new Coordinate(-1, -1);
    board = new Board(1, 1, player);

    // when
    Optional<Room> room = board.getRoomByCoordinate(coordinate);

    // then
    assertFalse(room.isPresent());
  }

  @Test
  void getRoomsWithoutGameObjectOrPlayerShouldReturnsRoomsWithoutObjects() throws Exception {
    // given
    board = new Board(3, 3, player);
    for (int y = 0; y < board.getNbRow(); y++) {
      for (int x = 0; x < board.getNbColumn(); x++) {
        board.getRoomByCoordinate(new Coordinate(x, y))
            .ifPresent(room -> room.setGameObject(null));
      }
    }

    board.getRoomByCoordinate(new Coordinate(0, 0)).ifPresent(room -> room.setGameObject(gameObject));
    board.getRoomByCoordinate(new Coordinate(0, 1)).ifPresent(room -> room.setGameObject(gameObject));
    board.getRoomByCoordinate(new Coordinate(0, 2)).ifPresent(room -> room.setGameObject(gameObject));

    // when
    List<Room> rooms = board.getRoomsWithoutGameObjectAndPlayer();

    // then
    Assertions.assertEquals(6, rooms.size());
  }

  @Test
  void toStringShouldDisplayBoard() throws Exception {
    // given
    Mockito.when(player.toString()).thenReturn("♛♛");
    board = new Board(3, 3, player);
    Optional<Room> optionalRoom = board
        .getRoomByCoordinate(new Coordinate(board.getNbColumn() / 2, board.getNbRow() / 2));
    optionalRoom.ifPresent(room -> room.playerEnterRoom(player));

    // when
    String result = board.toString();

    // then
    String expected = ""
        + "+----+----+----+\n"
        + "|    |    |    |\n"
        + "+----+----+----+\n"
        + "|    | ♛♛ |    |\n"
        + "+----+----+----+\n"
        + "| () |    |    |\n"
        + "+----+----+----+\n";
    Assertions.assertEquals(expected, result);
  }

  private int countObjects(Class<?> clazz) {
    int count = 0;
    for (int y = 0; y < board.getNbRow(); y++) {
      for (int x = 0; x < board.getNbColumn(); x++) {
        Optional<Room> roomOptional = board.getRoomByCoordinate(new Coordinate(x, y));
        if (roomOptional.isPresent() && clazz.isInstance(roomOptional.get().getGameObject())) {
          count++;
        }
      }
    }
    return count;
  }
}
