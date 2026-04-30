package com.mineexplorer.unit;

import com.mineexplorer.domain.Direction;
import com.mineexplorer.domain.Game;
import com.mineexplorer.domain.GameConfiguration;
import com.mineexplorer.domain.GameState;
import com.mineexplorer.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class VictoryTest {

    @Test
    void should_win_when_player_reaches_the_exit() {
        // Given
        var playerPosition = new Position(3, 4);
        var exitPosition = new Position(4, 4);
        var config = new GameConfiguration(playerPosition, 0, Set.of(), exitPosition);
        var game = Game.create(config);

        // When
        var newGame = game.move(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(exitPosition);
        assertThat(newGame.visibleCells()).contains(exitPosition);
        assertThat(newGame.state()).isEqualTo(GameState.WON);
    }
}
