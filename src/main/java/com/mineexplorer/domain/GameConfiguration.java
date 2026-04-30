package com.mineexplorer.domain;

import java.util.Set;

public record GameConfiguration(Position playerPosition, int grenadeCount, Set<Wall> walls, Position exitPosition, Set<Position> minePositions) {

    public GameConfiguration(Position playerPosition, int grenadeCount) {
        this(playerPosition, grenadeCount, Set.of(), null, Set.of());
    }

    public GameConfiguration(Position playerPosition, int grenadeCount, Set<Wall> walls) {
        this(playerPosition, grenadeCount, walls, null, Set.of());
    }

    public GameConfiguration(Position playerPosition, int grenadeCount, Set<Wall> walls, Position exitPosition) {
        this(playerPosition, grenadeCount, walls, exitPosition, Set.of());
    }
}
