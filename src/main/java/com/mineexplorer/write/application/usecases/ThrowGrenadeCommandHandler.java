package com.mineexplorer.write.application.usecases;

import com.mineexplorer.write.application.domain.ports.GameRepository;

public class ThrowGrenadeCommandHandler {

    private final GameRepository gameRepository;

    public ThrowGrenadeCommandHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void handle(ThrowGrenadeCommand command) {
        var game = gameRepository.findCurrent()
                .orElseThrow(() -> new NoGameInProgressException());
        var newGame = game.throwGrenade(command.direction());
        gameRepository.save(newGame);
    }
}
