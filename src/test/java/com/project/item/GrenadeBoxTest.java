package com.project.item;

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
import com.project.util.FixedRandom;
import com.project.util.RandomUtil;

@ExtendWith(MockitoExtension.class)
class GrenadeBoxTest {

  GrenadeBox grenadeBox;

  Board board;

  @Mock
  Player player;

  @BeforeEach
  void setUp() throws Exception {
    RandomUtil.setRandom(new FixedRandom(0));
    board = new Board(3, 3, player);
    grenadeBox = new GrenadeBox(board);
  }

  @Test
  void toStringShouldDisplayGrenadeBoxWithQuantity() {
    // given
    // when
    String display = grenadeBox.toString();

    // then
    String expected = "☌1";
    Assertions.assertEquals(expected, display);
  }

  @Test
  void performActionShouldMakePlayerCollectGrenade() {
    // given
    // when
    grenadeBox.performAction(player);

    // then
    ArgumentCaptor<Integer> quantityCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(player).collectGrenades(quantityCaptor.capture());

    assertEquals(1, quantityCaptor.getValue());
  }

  @Test
  void setPositionShouldSetPositionRandomly() {
    // given
    Coordinate coordinate = new Coordinate(2, 0);

    // when
    grenadeBox = new GrenadeBox(board);

    // then
    Assertions.assertEquals(coordinate, grenadeBox.getCoordinate());
  }
}
