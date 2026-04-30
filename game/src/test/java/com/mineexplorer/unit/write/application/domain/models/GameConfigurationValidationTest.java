package com.mineexplorer.unit.write.application.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.mineexplorer.write.application.domain.models.Direction;
import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameConfiguration;
import com.mineexplorer.write.application.domain.models.InvalidGameConfigurationException;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.domain.models.Wall;

class GameConfigurationValidationTest {

    @Test
    void should_reject_configuration_when_mine_is_on_exit() {
        // Given
        var exitPosition = new Position(4, 4);
        var mineOnExit = new Position(4, 4);
        var config = new GameConfiguration(
                new Position(0, 0),
                3,
                Set.of(),
                exitPosition,
                Set.of(mineOnExit)
        );

        // When / Then
        assertThatThrownBy(() -> Game.create(config))
                .isInstanceOf(InvalidGameConfigurationException.class)
                .hasMessage("Mine and exit cannot be on the same cell");
    }

    @Test
    void should_reject_configuration_when_player_starts_on_mine() {
        // Given
        var playerPosition = new Position(0, 0);
        var mineOnPlayer = new Position(0, 0);
        var config = new GameConfiguration(
                playerPosition,
                3,
                Set.of(),
                new Position(4, 4),
                Set.of(mineOnPlayer)
        );

        // When / Then
        assertThatThrownBy(() -> Game.create(config))
                .isInstanceOf(InvalidGameConfigurationException.class)
                .hasMessage("Player cannot start on a mine");
    }

    @Test
    void should_reject_configuration_when_player_starts_on_exit() {
        // Given
        var playerPosition = new Position(4, 4);
        var exitPosition = new Position(4, 4);
        var config = new GameConfiguration(
                playerPosition,
                3,
                Set.of(),
                exitPosition,
                Set.of()
        );

        // When / Then
        assertThatThrownBy(() -> Game.create(config))
                .isInstanceOf(InvalidGameConfigurationException.class)
                .hasMessage("Player cannot start on the exit");
    }

    @Test
    void should_allow_grenade_throws_when_surrounded_by_walls() {
        // Given: player at (0,0) with 2 grenades, surrounded by walls east and south
        var playerPosition = new Position(0, 0);
        var wallEast = Wall.between(playerPosition, new Position(1, 0));
        var wallSouth = Wall.between(playerPosition, new Position(0, 1));
        var config = new GameConfiguration(
                playerPosition,
                2,
                Set.of(wallEast, wallSouth),
                new Position(4, 4),
                Set.of()
        );
        var game = Game.create(config);

        // When: throw grenade east (D)
        var gameAfterEast = game.throwGrenade(Direction.EAST);

        // Then: player propelled east, wall destroyed, grenade consumed
        assertThat(gameAfterEast.playerPosition()).isEqualTo(new Position(1, 0));
        assertThat(gameAfterEast.grenadeCount()).isEqualTo(1);
        assertThat(gameAfterEast.walls()).doesNotContain(wallEast);

        // When: throw grenade south (S) from original position (simulate separate throw)
        var gameAfterSouth = game.throwGrenade(Direction.SOUTH);

        // Then: player propelled south, wall destroyed, grenade consumed
        assertThat(gameAfterSouth.playerPosition()).isEqualTo(new Position(0, 1));
        assertThat(gameAfterSouth.grenadeCount()).isEqualTo(1);
        assertThat(gameAfterSouth.walls()).doesNotContain(wallSouth);
    }
}
