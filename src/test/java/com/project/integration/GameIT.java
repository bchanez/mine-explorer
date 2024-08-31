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

    RandomUtil.setRandom(new FixedRandom(0));
    Mockito.when(menu.getNbRow()).thenReturn(3);
    Mockito.when(menu.getNbColumn()).thenReturn(3);

    Game game = new Game(menu, player);

    // when
    Mockito.when(menu.chooseDirection()).thenReturn("q").thenReturn("z");
    Mockito.when(menu.doAction(ArgumentMatchers.any())).thenReturn(1).thenReturn(1).thenReturn(0);
    game.loop();

    // then
    assertEquals(PlayerState.WON, player.getState());
  }
}
