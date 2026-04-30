package com.mineexplorer.write.application.usecases;

import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.ports.GameRepository;

public class MovePlayerCommandHandler {

    private final GameRepository gameRepository;

    public MovePlayerCommandHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game handle(MovePlayerCommand command) {
        var game = gameRepository.findCurrent()
                .orElseThrow(() -> new NoGameInProgressException());
        var newGame = game.move(command.direction());
        gameRepository.save(newGame);
        return newGame;
    }
}
