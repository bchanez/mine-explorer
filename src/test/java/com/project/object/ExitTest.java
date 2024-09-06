package com.project.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Board;
import com.project.Coordinate;
import com.project.Player;
import com.project.PlayerState;
import com.project.Room;
import com.project.item.Exit;
import com.project.util.FixedRandom;
import com.project.util.RandomUtil;

@ExtendWith(MockitoExtension.class)
class ExitTest {

  Exit exit;

  @Mock
  Board board;

  @Mock
  Room room;

  @Mock
  Player player;

  @BeforeEach
  void setUp() {
    RandomUtil.setRandom(new FixedRandom(0));
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
  void setPositionShouldSetPositionRandomlyToBoardCorner(int cornerBoard, Coordinate expected) {
    // given
    RandomUtil.setRandom(new FixedRandom(cornerBoard));
    Mockito.when(board.getNbRow()).thenReturn(4);
    Mockito.when(board.getNbColumn()).thenReturn(4);

    ArgumentCaptor<Coordinate> coordinateCaptor = ArgumentCaptor.forClass(Coordinate.class);
    when(board.getRoomByCoordinate(coordinateCaptor.capture())).thenReturn(Optional.of(room));

    doAnswer(invocation -> {
      Exit exit = invocation.getArgument(0);
      exit.setCoordinate(coordinateCaptor.getValue());
      return null;
    }).when(room).setStaticItem(any(Exit.class));

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
