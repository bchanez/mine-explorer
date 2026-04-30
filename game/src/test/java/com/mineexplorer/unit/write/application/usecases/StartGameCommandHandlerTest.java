package com.mineexplorer.unit.write.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.mineexplorer.write.adapters.secondary.persistence.InMemoryGameRepository;
import com.mineexplorer.write.application.domain.models.GameState;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.usecases.StartGameCommand;
import com.mineexplorer.write.application.usecases.StartGameCommandHandler;

class StartGameCommandHandlerTest {

    private final InMemoryGameRepository gameRepository = new InMemoryGameRepository();
    private final StartGameCommandHandler handler = new StartGameCommandHandler(gameRepository);

    @Test
    void should_create_game_at_starting_position_with_grenades() {
        handler.handle(new StartGameCommand());

        var game = gameRepository.findCurrent().orElseThrow();
        assertThat(game.playerPosition()).isEqualTo(new Position(0, 0));
        assertThat(game.grenadeCount()).isEqualTo(5);
        assertThat(game.state()).isEqualTo(GameState.PLAYING);
    }

    @Test
    void should_initialize_fog_of_war_with_starting_position_visible() {
        handler.handle(new StartGameCommand());

        var game = gameRepository.findCurrent().orElseThrow();
        assertThat(game.visibleCells()).containsExactly(new Position(0, 0));
    }
}
