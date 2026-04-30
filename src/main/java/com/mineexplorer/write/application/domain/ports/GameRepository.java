package com.mineexplorer.write.application.domain.ports;

import com.mineexplorer.write.application.domain.models.Game;

import java.util.Optional;

public interface GameRepository {
    void save(Game game);
    Optional<Game> findCurrent();
}
