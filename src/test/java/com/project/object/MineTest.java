package com.project.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Board;
import com.project.Coordinate;
import com.project.Player;
import com.project.PlayerState;
import com.project.item.Mine;
import com.project.util.FixedRandom;
import com.project.util.RandomUtil;

@ExtendWith(MockitoExtension.class)
class MineTest {

  Mine mine;

  Board board;

  @Mock
  Player player;

  @BeforeEach
  void setUp() throws Exception {
    RandomUtil.setRandom(new FixedRandom(0));
    board = new Board(3, 3, player);
    mine = new Mine(board);
  }

  @Test
  void toStringShouldDisplayMine() {
    // given
    // when
    String display = mine.toString();

    // then
    String expected = "**";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void performActionShouldSetPlayerStateToLost() {
    // given
    // when
    mine.performAction(player);

    // then
    ArgumentCaptor<PlayerState> stateCaptor = ArgumentCaptor.forClass(PlayerState.class);
    verify(player).setState(stateCaptor.capture());

    assertEquals(PlayerState.LOST, stateCaptor.getValue());
  }

  @Test
  void setPositionShouldSetPositionRandomly() {
    // given
    Coordinate coordinate = new Coordinate(1, 0);

    // when

    // then
    Assertions.assertEquals(coordinate, mine.getCoordinate());
  }

}
