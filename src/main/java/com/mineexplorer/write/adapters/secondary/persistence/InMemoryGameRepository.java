package com.mineexplorer.write.adapters.secondary.persistence;

import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.ports.GameRepository;

import java.util.Optional;

public class InMemoryGameRepository implements GameRepository {

    private Game currentGame;

    @Override
    public void save(Game game) {
        this.currentGame = game;
    }

    @Override
    public Optional<Game> findCurrent() {
        return Optional.ofNullable(currentGame);
    }
}
