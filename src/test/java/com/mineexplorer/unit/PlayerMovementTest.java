package com.mineexplorer.unit;

import com.mineexplorer.domain.Direction;
import com.mineexplorer.domain.Game;
import com.mineexplorer.domain.GameConfiguration;
import com.mineexplorer.domain.Position;
import com.mineexplorer.domain.Wall;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerMovementTest {

    @Test
    void should_not_cross_intact_wall_when_moving_east() {
        // Given
        var startingPosition = new Position(1, 1);
        var wallBetweenPositions = new Wall(new Position(1, 1), new Position(2, 1));
        var config = new GameConfiguration(startingPosition, 3, Set.of(wallBetweenPositions));
        var game = Game.create(config);

        // When
        var newGame = game.move(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(new Position(1, 1));
    }

    @Test
    void should_enter_adjacent_cell_when_moving_east_through_clear_passage() {
        // Given
        var startingPosition = new Position(1, 1);
        var config = new GameConfiguration(startingPosition, 3, Set.of());
        var game = Game.create(config);

        // When
        var newGame = game.move(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(new Position(2, 1));
    }

    @Test
    void should_not_cross_intact_wall_when_moving_south() {
        // Given
        var startingPosition = new Position(1, 1);
        var wallBetweenPositions = new Wall(new Position(1, 1), new Position(1, 2));
        var config = new GameConfiguration(startingPosition, 3, Set.of(wallBetweenPositions));
        var game = Game.create(config);

        // When
        var newGame = game.move(Direction.SOUTH);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(new Position(1, 1));
    }

    @Test
    void should_move_south_into_accessible_cell() {
        // Given
        var startingPosition = new Position(0, 0);
        var config = new GameConfiguration(startingPosition, 3);
        var game = Game.create(config);

        // When
        var newGame = game.move(Direction.SOUTH);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(new Position(0, 1));
    }

    @Test
    void should_reveal_cell_when_player_enters_it() {
        // Given
        var startingPosition = new Position(0, 0);
        var config = new GameConfiguration(startingPosition, 3);
        var game = Game.create(config);

        // When
        var newGame = game.move(Direction.SOUTH);

        // Then
        assertThat(newGame.visibleCells()).containsExactlyInAnyOrder(
                new Position(0, 0),
                new Position(0, 1)
        );
    }
}
