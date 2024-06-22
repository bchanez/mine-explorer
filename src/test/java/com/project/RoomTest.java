package com.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.object.Exit;

@ExtendWith(MockitoExtension.class)
class RoomTest {

  Room room;

  @Mock
  Player player;

  @Test
  void coordinate() {
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
    room = new Room();

    // when
    String display = room.toString();

    // then
    String expected = "  ";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void playerEntersInRoomShouldDisplayPlayer() {
    // given
    Mockito.when(player.toString()).thenReturn("♛♛");
    room = new Room();

    // when
    room.playerEnterRoom(player);
    String display = room.toString();

    // then
    String expected = "♛♛";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void playerLeavesRoomShouldDisplayRoom() {
    // given
    room = new Room();
    room.playerEnterRoom(player);

    // when
    room.playerLeaveRoom();
    String display = room.toString();

    // then
    String expected = "  ";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void roomIsExitShouldDisplayExit() {
    // given
    room = new Room();
    room.playerEnterRoom(player);

    // when
    room.setGameObject(new Exit());
    room.playerLeaveRoom();
    String display = room.toString();

    // then
    String expected = "()";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void playerIsOnExitRoomShouldDisplayPlayer() {
    // given
    Mockito.when(player.toString()).thenReturn("♛♛");
    room = new Room();

    // when
    room.setGameObject(new Exit());
    room.playerEnterRoom(player);
    String display = room.toString();

    // then
    String expected = "♛♛";
    Assertions.assertEquals(expected, display);
  }
}
