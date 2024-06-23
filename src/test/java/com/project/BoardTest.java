package com.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Random;
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

@ExtendWith(MockitoExtension.class)
class BoardTest {

  Board board;

  Random random = new Random(1);

  @Mock
  Player player;

  @Test
  void getRoom() {
    // given
    Coordinate coordinate = new Coordinate(1, 1);
    board = new Board(2, 3, player, random);

    // when
    Optional<Room> optionalRoom = board.getRoomByCoordinate(coordinate);

    // then
    assertTrue(optionalRoom.isPresent(), "Room not found at coordinate: " + coordinate);
    optionalRoom.ifPresent(room -> assertEquals(coordinate, room.getCoordinate()));
  }

  @ParameterizedTest
  @MethodSource
  void isRoomExist(Boolean expected, Coordinate coordinate) {
    // given
    board = new Board(1, 1, player, random);

    // when
    boolean roomExist = board.isRoomExist(coordinate);

    // then
    Assertions.assertEquals(expected, roomExist);
  }

  private static Stream<Arguments> isRoomExist() {
    return Stream.of(
        Arguments.of(true, new Coordinate(0, 0)),
        Arguments.of(false, new Coordinate(-1, 0)),
        Arguments.of(false, new Coordinate(0, -1)),
        Arguments.of(false, new Coordinate(1, 0)),
        Arguments.of(false, new Coordinate(0, 1)));
  }

  @Test
  void toStringShouldDisplayBoardOnInit() {
    // given
    Mockito.when(player.toString()).thenReturn("♛♛");

    // when
    board = new Board(3, 3, player, random);
    Optional<Room> optionalRoom = board
        .getRoomByCoordinate(new Coordinate(board.getNbColumn() / 2, board.getNbRow() / 2));
    optionalRoom.ifPresent(room -> room.playerEnterRoom(player));
    String result = board.toString();

    // then
    String expected = "      \n  ♛♛  \n()    ";
    Assertions.assertEquals(expected, result);
  }
}
