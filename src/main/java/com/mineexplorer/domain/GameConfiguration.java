package com.mineexplorer.domain;

import java.util.Set;

public record GameConfiguration(Position playerPosition, int grenadeCount, Set<Wall> walls) {

    public GameConfiguration(Position playerPosition, int grenadeCount) {
        this(playerPosition, grenadeCount, Set.of());
    }
}
