package com.project.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Board;
import com.project.Player;
import com.project.PlayerState;
import com.project.Room;

@ExtendWith(MockitoExtension.class)
class MineTest {

  Mine mine;

  Random random = new Random(1);

  @Mock
  Board board;

  @Mock
  Player player;

  @BeforeEach
  void setUp() {
    Mockito.when(board.getRoomsWithoutGameObjectAndPlayer()).thenReturn(Arrays.asList(new Room()));
    mine = new Mine(board, random);
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
  void performAction() {
    // given
    // when
    mine.performAction(player);

    // then
    ArgumentCaptor<PlayerState> stateCaptor = ArgumentCaptor.forClass(PlayerState.class);
    verify(player).setState(stateCaptor.capture());

    assertEquals(PlayerState.LOST, stateCaptor.getValue());
  }
}
