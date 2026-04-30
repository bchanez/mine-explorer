package com.mineexplorer.write.application.usecases;

import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameConfiguration;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.domain.ports.GameRepository;

import java.util.Set;

public class StartGameCommandHandler {

    private final GameRepository gameRepository;

    public StartGameCommandHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void handle(StartGameCommand command) {
        var config = new GameConfiguration(
                new Position(0, 0),
                3,
                Set.of(),
                new Position(4, 4),
                Set.of()
        );
        var game = Game.create(config);
        gameRepository.save(game);
    }
}
