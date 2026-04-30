package com.mineexplorer.write.application.usecases;

import com.mineexplorer.write.application.domain.models.GameConfiguration;
import com.mineexplorer.write.application.domain.models.Position;

import java.util.Set;

public record StartGameCommand(GameConfiguration configuration) {

    public StartGameCommand() {
        this(new GameConfiguration(
                new Position(0, 0),
                5,
                Set.of(),
                new Position(4, 4),
                Set.of()));
    }
}
