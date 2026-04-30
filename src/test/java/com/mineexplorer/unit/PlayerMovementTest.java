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
    void should_keep_player_at_current_position_when_moving_east_and_there_is_a_wall_between_current_position_and_target_position() {
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
    void should_move_player_to_target_position_when_moving_east_and_there_is_no_wall_between_current_position_and_target_position() {
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
    void should_keep_player_at_current_position_when_moving_south_and_there_is_a_wall_between_current_position_and_target_position() {
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
    void should_place_the_player_at_position_0_1_when_the_player_moves_south_from_position_0_0() {
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
    void should_make_the_destination_cell_visible_when_the_player_moves_south_into_an_unvisited_cell() {
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
