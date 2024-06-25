package com.project;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameTest {
  Game game;

  @Mock
  Menu menu;

  @Mock
  Player player;

  @BeforeEach
  void setUp() throws Exception {
    game = new Game(menu, player);
  }

  @Test
  void loopShouldStopWhenAction0() {
    // given
    Mockito.when(menu.doAction(ArgumentMatchers.any())).thenReturn(-1).thenReturn(0);

    // when
    game.loop();

    // then
    Assertions.assertEquals(false, game.isGameRunning());
  }

  @Test
  void playerMoveWhenAction1() {
    // given
    String direction = "q";
    Mockito.when(menu.chooseDirection()).thenReturn(direction);
    Mockito.when(menu.doAction(ArgumentMatchers.any())).thenReturn(1).thenReturn(0);

    // when
    game.loop();

    // then
    verify(player).moveToDirection(direction);
  }

  @Test
  void playerthrowGrenadeWhenAction2() {
    // given
    String direction = "z";
    Mockito.when(menu.chooseDirection()).thenReturn(direction);
    Mockito.when(menu.doAction(ArgumentMatchers.any())).thenReturn(2).thenReturn(0);

    // when
    game.loop();

    // then
    verify(player).throwGrenadeInDirection(direction);
  }
}
