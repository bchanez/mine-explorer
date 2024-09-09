package com.project;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.item.StaticItem;

@ExtendWith(MockitoExtension.class)
class RoomTest {

  Room room;

  @Mock
  Player player;

  @Mock
  StaticItem staticItem;

  @Mock
  Board board;

  @Test
  void getCoordinateShouldGiveCoordinateOfRoom() {
    // given
    Coordinate expected = new Coordinate(1, 1);
    room = new Room(board, expected);

    // when
    Coordinate actual = room.getCoordinate();

    // then
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void toStringShouldDisplayRoom() {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    String display = room.toString();

    // then
    String expected = "  ";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void getTopShouldReturnTopWall() {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    Wall wall = room.getTop();

    // then
    String expected = "----";
    Assertions.assertEquals(expected, wall.toString());
  }

  @Test
  void getBottomShouldReturnBottomWall() {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    Wall wall = room.getBottom();

    // then
    String expected = "----";
    Assertions.assertEquals(expected, wall.toString());
  }

  @Test
  void getLeftShouldReturnLeftWall() {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    Wall wall = room.getLeft();

    // then
    String expected = "|";
    Assertions.assertEquals(expected, wall.toString());
  }

  @Test
  void getRightShouldReturnRightWall() {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    Wall wall = room.getRight();

    // then
    String expected = "|";
    Assertions.assertEquals(expected, wall.toString());
  }

  @ParameterizedTest
  @MethodSource
  void getWallByDirectionShouldReturnCorrectWallForGivenDirection(String direction, String expectedWall) {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    Wall wall = room.getWallByDirection(direction);

    // then
    Assertions.assertEquals(expectedWall, wall.toString());
  }

  private static Stream<Arguments> getWallByDirectionShouldReturnCorrectWallForGivenDirection() {
    return Stream.of(
        Arguments.of("z", "----"),
        Arguments.of("q", "|"),
        Arguments.of("s", "----"),
        Arguments.of("d", "|"));
  }

  @Test
  void toStringShouldDisplayPlayerWhenPlayerEnterRoom() {
    // given
    Mockito.when(player.toString()).thenReturn("♛♛");
    room = new Room(board, new Coordinate(-1, -1));

    // when
    room.playerEnterRoom(player);
    String display = room.toString();

    // then
    String expected = "♛♛";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void toStringShouldDisplayGameObjectWhenThereIsGameObject() {
    // given
    room = new Room(board, new Coordinate(-1, -1));
    Mockito.when(staticItem.toString()).thenReturn("()");

    // when
    room.setStaticItem(staticItem);
    String display = room.toString();

    // then
    String expected = "()";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void toStringShouldDisplayPlayerWhenPlayerEnterRoomAndThereIsGameObject() {
    // given
    room = new Room(board, new Coordinate(-1, -1));
    Mockito.when(player.toString()).thenReturn("♛♛");

    // when
    room.setStaticItem(staticItem);
    room.playerEnterRoom(player);
    String display = room.toString();

    // then
    String expected = "♛♛";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void getPlayerShouldReturnThePlayerWhenPlayerEnterRoom() {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    room.playerEnterRoom(player);
    Player player = room.getPlayer();

    // then
    Assertions.assertNotNull(player);
  }

  @Test
  void getPlayerShouldReturnNullWhenPlayerLeaveRoom() {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    room.playerLeaveRoom();
    Player player = room.getPlayer();

    // then
    Assertions.assertNull(player);
  }

  @Test
  void getGameObjectShouldReturnTheGameObjectWhenPresent() {
    // given
    room = new Room(board, new Coordinate(-1, -1));

    // when
    room.setStaticItem(staticItem);
    StaticItem staticItem = room.getStaticItem();

    // then
    Assertions.assertNotNull(staticItem);
  }

  @Test
  void wallsShouldBeIndestructibleIfAtEdgeOfBoard() {
    // given
    Mockito.when(board.getNbColumn()).thenReturn(1);
    Mockito.when(board.getNbRow()).thenReturn(1);

    // when
    Room room = new Room(board, new Coordinate(0, 0));
    room.getTop().destroy();
    room.getBottom().destroy();
    room.getLeft().destroy();
    room.getRight().destroy();

    // then
    assertFalse(room.getTop().isDestroyed());
    assertFalse(room.getBottom().isDestroyed());
    assertFalse(room.getLeft().isDestroyed());
    assertFalse(room.getRight().isDestroyed());
  }

  @Test
  void playerEnterRoomShouldDestroyItemsAfter() {
    // given
    room = new Room(board, new Coordinate(-1, -1));
    room.setStaticItem(staticItem);

    // when
    room.playerEnterRoom(player);

    // then
    Assertions.assertNull(room.getStaticItem());
  }
}
