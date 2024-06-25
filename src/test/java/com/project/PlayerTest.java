package com.project;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.object.Exit;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

  Player player;

  @Mock
  Board board;

  @Mock
  Exit exit;

  @BeforeEach
  void setUp() {
    player = new Player();
  }

  @Test
  void toStringShouldDisplayPlayer() {
    // given
    // when
    String display = player.toString();

    // then
    String expected = "♛♛";
    Assertions.assertEquals(expected, display);
  }

  @ParameterizedTest
  @MethodSource
  void playerCanMove(String direction, Coordinate coordinateExpected) {
    // given
    Mockito.when(board.isRoomExist(ArgumentMatchers.any())).thenReturn(true);
    Mockito.when(board.getRoomByCoordinate(ArgumentMatchers.any())).thenReturn(Optional.of(new Room()));
    player.setBoard(board);
    player.setCoordinate(new Coordinate(1, 1));

    // when
    player.moveToDirection(direction);

    // then
    Assertions.assertEquals(coordinateExpected, player.getCoordinate());
  }

  private static Stream<Arguments> playerCanMove() {
    return Stream.of(
        Arguments.of("z", new Coordinate(1, 0)),
        Arguments.of("q", new Coordinate(0, 1)),
        Arguments.of("s", new Coordinate(1, 2)),
        Arguments.of("d", new Coordinate(2, 1)),
        Arguments.of("?", new Coordinate(1, 1)));
  }

  @ParameterizedTest
  @ValueSource(strings = { "z", "q", "s", "d" })
  void playerCantMoveOutsideBoard(String direction) {
    // given
    Mockito.when(board.isRoomExist(ArgumentMatchers.any())).thenReturn(true).thenReturn(false);
    Mockito.when(board.getRoomByCoordinate(ArgumentMatchers.any())).thenReturn(Optional.of(new Room()));
    player.setBoard(board);
    player.setCoordinate(new Coordinate(0, 0));

    // when
    player.moveToDirection(direction);

    // then
    Assertions.assertEquals(new Coordinate(0, 0), player.getCoordinate());
  }

  @Test
  void playerStateInit() {
    // given
    // when
    // then
    Assertions.assertEquals(PlayerState.PLAYING, player.getState());
  }

  @ParameterizedTest
  @EnumSource(PlayerState.class)
  void playerStateTest(PlayerState state) {
    // given
    // when
    player.setState(state);

    // then
    Assertions.assertEquals(state, player.getState());
  }

  @Test
  void playerHas10PercentageGrenadeOnInit() throws Exception {
    // given
    Mockito.when(board.getNbColumn()).thenReturn(10);
    Mockito.when(board.getNbRow()).thenReturn(10);

    // when
    player.setBoard(board);

    // then
    int numberOfGrenades = player.getGrenades().size();
    Assertions.assertEquals(10, numberOfGrenades);
  }

  @ParameterizedTest
  @MethodSource
  void playerCanThrowGrenade(String direction, Coordinate coordinateExpected) {
    // given
    Mockito.when(board.isRoomExist(ArgumentMatchers.any())).thenReturn(true);
    Mockito.when(board.getRoomByCoordinate(ArgumentMatchers.any())).thenReturn(Optional.of(new Room()));
    player.setBoard(board);
    player.setCoordinate(new Coordinate(1, 1));

    // when
    player.throwGrenadeInDirection(direction);

    // then
    Assertions.assertEquals(coordinateExpected, player.getCoordinate());
  }

  private static Stream<Arguments> playerCanThrowGrenade() {
    return Stream.of(
        Arguments.of("z", new Coordinate(1, 0)),
        Arguments.of("q", new Coordinate(0, 1)),
        Arguments.of("s", new Coordinate(1, 2)),
        Arguments.of("d", new Coordinate(2, 1)),
        Arguments.of("?", new Coordinate(1, 1)));
  }
}
