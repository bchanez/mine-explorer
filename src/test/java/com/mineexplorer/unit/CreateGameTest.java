package com.mineexplorer.unit;

import com.mineexplorer.domain.Game;
import com.mineexplorer.domain.GameConfiguration;
import com.mineexplorer.domain.GameState;
import com.mineexplorer.domain.Position;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateGameTest {

    @Test
    void should_be_in_progress_when_a_new_game_starts() {
        // Given
        var config = new GameConfiguration(
                new Position(0, 0),
                3
        );

        // When
        var game = Game.create(config);

        // Then
        assertThat(game.state()).isEqualTo(GameState.PLAYING);
    }

    @Test
    void should_place_player_at_starting_position_when_a_new_game_starts() {
        // Given
        var startingPosition = new Position(2, 3);
        var config = new GameConfiguration(startingPosition, 3);

        // When
        var game = Game.create(config);

        // Then
        assertThat(game.playerPosition()).isEqualTo(startingPosition);
    }

    @Test
    void should_give_player_initial_grenades_when_a_new_game_starts() {
        // Given
        var config = new GameConfiguration(new Position(0, 0), 5);

        // When
        var game = Game.create(config);

        // Then
        assertThat(game.grenadeCount()).isEqualTo(5);
    }

    @Test
    void should_reveal_only_starting_cell_when_a_new_game_starts() {
        // Given
        var startingPosition = new Position(1, 2);
        var config = new GameConfiguration(startingPosition, 3);

        // When
        var game = Game.create(config);

        // Then
        assertThat(game.visibleCells()).containsExactly(startingPosition);
    }
}
