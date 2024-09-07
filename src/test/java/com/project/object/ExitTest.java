package com.project.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Board;
import com.project.Coordinate;
import com.project.Player;
import com.project.PlayerState;
import com.project.item.Exit;
import com.project.util.FixedRandom;
import com.project.util.RandomUtil;

@ExtendWith(MockitoExtension.class)
class ExitTest {

  Exit exit;

  Board board;

  @Mock
  Player player;

  @BeforeEach
  void setUp() throws Exception {
    RandomUtil.setRandom(new FixedRandom(0));
    board = new Board(3, 3, player);
    exit = new Exit(board);
  }

  @Test
  void toStringShouldDisplayExit() {
    // given
    // when
    String display = exit.toString();

    // then
    String expected = "()";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void performActionShouldSetPlayerStateToWon() {
    // given
    // when
    exit.performAction(player);

    // then
    ArgumentCaptor<PlayerState> stateCaptor = ArgumentCaptor.forClass(PlayerState.class);
    verify(player).setState(stateCaptor.capture());

    assertEquals(PlayerState.WON, stateCaptor.getValue());
  }

  @ParameterizedTest
  @MethodSource
  void setPositionShouldSetPositionRandomlyToBoardCorner(int cornerBoard, Coordinate expected) throws Exception {
    // given
    RandomUtil.setRandom(new FixedRandom(cornerBoard));
    board = new Board(4, 4, player);
    exit = new Exit(board);

    // when
    exit = new Exit(board);

    // then
    Assertions.assertEquals(expected, exit.getCoordinate());
  }

  private static Stream<Arguments> setPositionShouldSetPositionRandomlyToBoardCorner() {
    return Stream.of(
        Arguments.of(0, new Coordinate(0, 0)),
        Arguments.of(1, new Coordinate(3, 0)),
        Arguments.of(2, new Coordinate(0, 3)),
        Arguments.of(3, new Coordinate(3, 3)));
  }
}
