package com.project.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.Game;
import com.project.Menu;
import com.project.Player;
import com.project.PlayerState;
import com.project.util.FixedRandom;
import com.project.util.RandomUtil;

@ExtendWith(MockitoExtension.class)
class GameIT {

  @Mock
  Menu menu;

  Player player;

  @Test
  void playerShouldWinWhenReachingExit() throws Exception {
    // given
    player = new Player();

    RandomUtil.setRandom(new FixedRandom(3));
    Mockito.when(menu.getNbRow()).thenReturn(8);
    Mockito.when(menu.getNbColumn()).thenReturn(8);

    Game game = new Game(menu, player);

    // when
    Mockito.when(menu.chooseDirection()).thenReturn("s").thenReturn("s").thenReturn("s").thenReturn("d").thenReturn("d")
        .thenReturn("d");
    Mockito.when(menu.doAction(ArgumentMatchers.any())).thenReturn(2).thenReturn(2).thenReturn(2).thenReturn(2)
        .thenReturn(2).thenReturn(2).thenReturn(0);
    game.loop();

    // then
    assertEquals(PlayerState.WON, player.getState());
  }

  @Test
  void playerShouldLoseWhenReachingMine() throws Exception {
    // given
    player = new Player();

    RandomUtil.setRandom(new FixedRandom(3));
    Mockito.when(menu.getNbRow()).thenReturn(6);
    Mockito.when(menu.getNbColumn()).thenReturn(6);

    Game game = new Game(menu, player);

    // when
    Mockito.when(menu.chooseDirection()).thenReturn("z").thenReturn("z").thenReturn("z");
    Mockito.when(menu.doAction(ArgumentMatchers.any())).thenReturn(2).thenReturn(2).thenReturn(2).thenReturn(0);
    game.loop();

    // then
    assertEquals(PlayerState.LOST, player.getState());
  }
}
