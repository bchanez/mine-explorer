package com.mineexplorer.unit;

import com.mineexplorer.domain.Direction;
import com.mineexplorer.domain.Game;
import com.mineexplorer.domain.GameConfiguration;
import com.mineexplorer.domain.GameState;
import com.mineexplorer.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DefeatTest {

    @Test
    void should_lose_when_player_steps_on_mine() {
        // Given
        var playerPosition = new Position(1, 1);
        var minePosition = new Position(2, 1);
        var config = new GameConfiguration(
                playerPosition,
                0,
                Set.of(),
                null,
                Set.of(minePosition)
        );
        var game = Game.create(config);

        // When
        var newGame = game.move(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(minePosition);
        assertThat(newGame.visibleCells()).contains(minePosition);
        assertThat(newGame.state()).isEqualTo(GameState.LOST);
    }
}
