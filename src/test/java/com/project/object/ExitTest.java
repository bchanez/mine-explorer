package com.project.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Board;
import com.project.Player;
import com.project.PlayerState;

@ExtendWith(MockitoExtension.class)
class ExitTest {

  Exit exit;

  Random random = new Random(1);

  @Mock
  Board board;

  @Mock
  Player player;

  @BeforeEach
  void setUp() {
    exit = new Exit(board, random);
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
  void performAction() {
    // given
    // when
    exit.performAction(player);

    // then
    ArgumentCaptor<PlayerState> stateCaptor = ArgumentCaptor.forClass(PlayerState.class);
    verify(player).setState(stateCaptor.capture());

    assertEquals(PlayerState.WON, stateCaptor.getValue());
  }
}
