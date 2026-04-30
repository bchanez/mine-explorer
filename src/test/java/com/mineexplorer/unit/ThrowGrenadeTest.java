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
    void should_destroy_wall_when_throwing_grenade() {
        // Given
        var playerPosition = new Position(1, 1);
        var targetPosition = new Position(2, 1);
        var furtherPosition = new Position(3, 1);
        var wallToDestroy = Wall.between(playerPosition, targetPosition);
        var wallToKeep = Wall.between(targetPosition, furtherPosition);
        var config = new GameConfiguration(playerPosition, 3, Set.of(wallToDestroy, wallToKeep));
        var game = Game.create(config);

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then - player is propelled to target, but cannot move further (wall still exists)
        var afterMove = newGame.move(Direction.EAST);
        assertThat(afterMove.playerPosition()).isEqualTo(targetPosition);
    }

    @Test
    void should_move_player_to_adjacent_cell_when_wall_is_destroyed() {
        // Given
        var playerPosition = new Position(1, 1);
        var targetPosition = new Position(2, 1);
        var wallToDestroy = Wall.between(playerPosition, targetPosition);
        var config = new GameConfiguration(playerPosition, 3, Set.of(wallToDestroy));
        var game = Game.create(config);

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(targetPosition);
    }

    @Test
    void should_use_one_grenade_when_destroying_wall() {
        // Given
        var playerPosition = new Position(1, 1);
        var targetPosition = new Position(2, 1);
        var wallToDestroy = Wall.between(playerPosition, targetPosition);
        var config = new GameConfiguration(playerPosition, 3, Set.of(wallToDestroy));
        var game = Game.create(config);

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then
        assertThat(newGame.grenadeCount()).isEqualTo(2);
    }

    @Test
    void should_keep_playing_when_grenade_lands_on_empty_cell() {
        // Given
        var playerPosition = new Position(1, 1);
        var targetPosition = new Position(2, 1);
        var wallToDestroy = Wall.between(playerPosition, targetPosition);
        var config = new GameConfiguration(playerPosition, 3, Set.of(wallToDestroy));
        var game = Game.create(config);

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then
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
}
