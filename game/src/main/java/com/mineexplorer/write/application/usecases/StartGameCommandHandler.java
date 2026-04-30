package com.mineexplorer.write.application.usecases;

import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.ports.GameRepository;

public class StartGameCommandHandler {

    private final GameRepository gameRepository;

    public StartGameCommandHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void handle(StartGameCommand command) {
        var game = Game.create(command.configuration());
        gameRepository.save(game);
    }
}
