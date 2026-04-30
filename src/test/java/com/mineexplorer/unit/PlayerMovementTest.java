package com.mineexplorer.unit;

import com.mineexplorer.domain.Direction;
import com.mineexplorer.domain.Game;
import com.mineexplorer.domain.GameConfiguration;
import com.mineexplorer.domain.Position;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerMovementTest {

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
