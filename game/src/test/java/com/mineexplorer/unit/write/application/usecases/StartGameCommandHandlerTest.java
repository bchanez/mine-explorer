package com.mineexplorer.unit.write.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mineexplorer.write.adapters.secondary.persistence.InMemoryGameRepository;
import com.mineexplorer.write.application.domain.models.GameConfiguration;
import com.mineexplorer.write.application.domain.models.GameState;
import com.mineexplorer.write.application.domain.models.InvalidGameConfigurationException;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.usecases.StartGameCommand;
import com.mineexplorer.write.application.usecases.StartGameCommandHandler;

class StartGameCommandHandlerTest {

    private final InMemoryGameRepository gameRepository = new InMemoryGameRepository();
    private final StartGameCommandHandler handler = new StartGameCommandHandler(gameRepository);

    @Test
    void should_place_player_at_start_with_grenades_when_creating_game() {
        handler.handle(new StartGameCommand());

        var game = gameRepository.findCurrent().orElseThrow();
        assertThat(game.playerPosition()).isEqualTo(new Position(0, 0));
        assertThat(game.grenadeCount()).isEqualTo(5);
        assertThat(game.state()).isEqualTo(GameState.PLAYING);
    }

    @Test
    void should_reveal_only_starting_cell_when_creating_game() {
        handler.handle(new StartGameCommand());

        var game = gameRepository.findCurrent().orElseThrow();
        assertThat(game.visibleCells()).containsExactly(new Position(0, 0));
    }

    @Nested
    class ConfigurationValidation {

        @Test
        void should_reject_configuration_when_mine_is_on_exit() {
            var exitPosition = new Position(4, 4);
            var mineOnExit = new Position(4, 4);
            var config = new GameConfiguration(
                    new Position(0, 0),
                    3,
                    Set.of(),
                    exitPosition,
                    Set.of(mineOnExit));

            assertThatThrownBy(() -> handler.handle(new StartGameCommand(config)))
                    .isInstanceOf(InvalidGameConfigurationException.class)
                    .hasMessage("Mine and exit cannot be on the same cell");
        }

        @Test
        void should_reject_configuration_when_player_starts_on_mine() {
            var playerPosition = new Position(0, 0);
            var mineOnPlayer = new Position(0, 0);
            var config = new GameConfiguration(
                    playerPosition,
                    3,
                    Set.of(),
                    new Position(4, 4),
                    Set.of(mineOnPlayer));

            assertThatThrownBy(() -> handler.handle(new StartGameCommand(config)))
                    .isInstanceOf(InvalidGameConfigurationException.class)
                    .hasMessage("Player cannot start on a mine");
        }

        @Test
        void should_reject_configuration_when_player_starts_on_exit() {
            var playerPosition = new Position(4, 4);
            var exitPosition = new Position(4, 4);
            var config = new GameConfiguration(
                    playerPosition,
                    3,
                    Set.of(),
                    exitPosition,
                    Set.of());

            assertThatThrownBy(() -> handler.handle(new StartGameCommand(config)))
                    .isInstanceOf(InvalidGameConfigurationException.class)
                    .hasMessage("Player cannot start on the exit");
        }
    }
}
