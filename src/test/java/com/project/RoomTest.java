package com.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.object.GameObject;

@ExtendWith(MockitoExtension.class)
class RoomTest {

  Room room;

  @Mock
  Player player;

  @Mock
  GameObject gameObject;

  @Test
  void getCoordinateShouldGiveCoordinateOfRoom() {
    // given
    Coordinate expected = new Coordinate(1, 1);
    room = new Room(expected);

    // when
    Coordinate actual = room.getCoordinate();

    // then
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void toStringShouldDisplayRoom() {
    // given
    room = new Room(new Coordinate(-1, -1));

    // when
    String display = room.toString();

    // then
    String expected = "  ";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void toStringShouldDisplayPlayerWhenPlayerEnterRoom() {
    // given
    Mockito.when(player.toString()).thenReturn("♛♛");
    room = new Room(new Coordinate(-1, -1));

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
    room = new Room(new Coordinate(-1, -1));
    Mockito.when(gameObject.toString()).thenReturn("()");

    // when
    room.setGameObject(gameObject);
    String display = room.toString();

    // then
    String expected = "()";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void toStringShouldDisplayPlayerWhenPlayerEnterRoomAndThereIsGameObject() {
    // given
    room = new Room(new Coordinate(-1, -1));
    Mockito.when(player.toString()).thenReturn("♛♛");

    // when
    room.setGameObject(gameObject);
    room.playerEnterRoom(player);
    String display = room.toString();

    // then
    String expected = "♛♛";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void getPlayerShouldReturnThePlayerWhenPlayerEnterRoom() {
    // given
    room = new Room(new Coordinate(-1, -1));

    // when
    room.playerEnterRoom(player);
    Player player = room.getPlayer();

    // then
    Assertions.assertNotNull(player);
  }

  @Test
  void getPlayerShouldReturnNullWhenPlayerLeaveRoom() {
    // given
    room = new Room(new Coordinate(-1, -1));

    // when
    room.playerLeaveRoom();
    Player player = room.getPlayer();

    // then
    Assertions.assertNull(player);
  }

  @Test
  void getGameObjectShouldReturnTheGameObjectWhenPresent() {
    // given
    room = new Room(new Coordinate(-1, -1));

    // when
    room.setGameObject(gameObject);
    GameObject gameObject = room.getGameObject();

    // then
    Assertions.assertNotNull(gameObject);
  }
}
