package com.mineexplorer.domain;

import java.util.Set;

public record GameConfiguration(Position playerPosition, int grenadeCount, Set<Wall> walls, Position exitPosition) {

    public GameConfiguration(Position playerPosition, int grenadeCount) {
        this(playerPosition, grenadeCount, Set.of(), null);
    }

    public GameConfiguration(Position playerPosition, int grenadeCount, Set<Wall> walls) {
        this(playerPosition, grenadeCount, walls, null);
    }
}
