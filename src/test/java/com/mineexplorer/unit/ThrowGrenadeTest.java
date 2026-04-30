package com.mineexplorer.unit;

import com.mineexplorer.domain.Direction;
import com.mineexplorer.domain.Game;
import com.mineexplorer.domain.GameConfiguration;
import com.mineexplorer.domain.GameState;
import com.mineexplorer.domain.Position;
import com.mineexplorer.domain.Wall;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ThrowGrenadeTest {

    @Test
    void should_destroy_wall_move_player_and_use_grenade_when_throwing_grenade() {
        // Given
        var playerPosition = new Position(1, 1);
        var targetPosition = new Position(2, 1);
        var wall = Wall.between(playerPosition, targetPosition);
        var game = Game.create(new GameConfiguration(playerPosition, 3, Set.of(wall)));

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(targetPosition);
        assertThat(newGame.grenadeCount()).isEqualTo(2);
        assertThat(newGame.state()).isEqualTo(GameState.PLAYING);
    }

    @Test
    void should_refuse_grenade_throw_when_no_grenades_remaining() {
        // Given
        var playerPosition = new Position(1, 1);
        var targetPosition = new Position(2, 1);
        var wallBetween = Wall.between(playerPosition, targetPosition);
        var config = new GameConfiguration(playerPosition, 0, Set.of(wallBetween));
        var game = Game.create(config);

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(playerPosition);
        assertThat(newGame.grenadeCount()).isEqualTo(0);
        // Verify wall is still intact by trying to move through it
        var afterMoveAttempt = newGame.move(Direction.EAST);
        assertThat(afterMoveAttempt.playerPosition()).isEqualTo(playerPosition);
    }

    @Test
    void should_waste_grenade_without_blast_when_throwing_toward_open_passage() {
        // Given
        var playerPosition = new Position(1, 1);
        var game = Game.create(new GameConfiguration(playerPosition, 2, Set.of()));

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(playerPosition);
        assertThat(newGame.grenadeCount()).isEqualTo(1);
        assertThat(newGame.state()).isEqualTo(GameState.PLAYING);
    }
}
