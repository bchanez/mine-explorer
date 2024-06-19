package com.project;

import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardTest {

  Board board;

  @Mock
  Player player;

  @Test
  void getRoom() {
    // given
    Coordinate coordinate = new Coordinate(1, 1);
    board = new Board(2, 3, player, new Random(1));
    board.initMatrix();

    // when
    Room room = board.getRoomByCoordinate(coordinate);

    // then
    Assertions.assertEquals(coordinate, room.getCoordinate());
  }

  @ParameterizedTest
  @MethodSource
  void isRoomExist(Boolean expected, Coordinate coordinate) {
    // given
    board = new Board(1, 1, player, new Random(1));
    board.initMatrix();

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
    Mockito.doAnswer(invocation -> {
      Coordinate coordinate = invocation.getArgument(0);
      board.getMatrix()[coordinate.getY()][coordinate.getX()].playerEnterRoom(player);
      return null;
    }).when(player).setCoordinate(ArgumentMatchers.any(Coordinate.class));
    Mockito.when(player.toString()).thenReturn("♛♛");

    // when
    board = new Board(3, 3, player, new Random(1));
    board.initMatrix();
    String result = board.toString();

    // then
    String expected = "      \n  ♛♛  \n()    ";
    Assertions.assertEquals(expected, result);
  }
}
