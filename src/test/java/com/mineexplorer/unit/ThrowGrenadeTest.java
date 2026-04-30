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
    void should_blast_through_wall_when_throwing_grenade() {
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
    void should_propel_player_through_destroyed_wall() {
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
    void should_consume_grenade_when_blasting_wall() {
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
    void should_reveal_cell_after_blast_propulsion() {
        // Given
        var playerPosition = new Position(1, 1);
        var targetPosition = new Position(2, 1);
        var wallToDestroy = Wall.between(playerPosition, targetPosition);
        var config = new GameConfiguration(playerPosition, 3, Set.of(wallToDestroy));
        var game = Game.create(config);

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then
        assertThat(newGame.visibleCells()).containsExactlyInAnyOrder(playerPosition, targetPosition);
    }

    @Test
    void should_continue_playing_after_safe_blast() {
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
}
