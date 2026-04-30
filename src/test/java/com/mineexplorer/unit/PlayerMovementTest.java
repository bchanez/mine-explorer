package com.mineexplorer.unit;

import com.mineexplorer.domain.Direction;
import com.mineexplorer.domain.Game;
import com.mineexplorer.domain.GameConfiguration;
import com.mineexplorer.domain.Position;
import com.mineexplorer.domain.Wall;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerMovementTest {

    @Test
    void should_block_player_by_intact_wall() {
        // Given
        var startingPosition = new Position(1, 1);
        var targetPosition = new Position(2, 1);
        var wall = Wall.between(startingPosition, targetPosition);
        var game = Game.create(new GameConfiguration(startingPosition, 3, Set.of(wall)));

        // When
        var newGame = game.move(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(startingPosition);
    }

    @Test
    void should_move_player_through_open_passage() {
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
    void should_block_player_by_wall_in_any_direction() {
        // Given
        var startingPosition = new Position(1, 1);
        var targetPosition = new Position(1, 2);
        var wall = Wall.between(startingPosition, targetPosition);
        var game = Game.create(new GameConfiguration(startingPosition, 3, Set.of(wall)));

        // When
        var newGame = game.move(Direction.SOUTH);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(startingPosition);
    }

    @Test
    void should_move_player_in_any_direction_through_open_passage() {
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

    @ParameterizedTest(name = "should block player at {1} border")
    @MethodSource("borderCases")
    void should_block_player_at_grid_border(Position startingPosition, Direction direction) {
        // Given
        var game = Game.create(new GameConfiguration(startingPosition, 0));

        // When
        var newGame = game.move(direction);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(startingPosition);
    }

    private static Stream<Arguments> borderCases() {
        return Stream.of(
                Arguments.of(new Position(2, 0), Direction.NORTH),
                Arguments.of(new Position(2, 4), Direction.SOUTH),
                Arguments.of(new Position(0, 2), Direction.WEST),
                Arguments.of(new Position(4, 2), Direction.EAST)
        );
    }
}
