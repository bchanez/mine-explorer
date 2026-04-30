package com.mineexplorer.domain;

import java.util.Set;

public class Game {

    private final Position playerPosition;
    private final int grenadeCount;
    private final Set<Position> visibleCells;

    private Game(Position playerPosition, int grenadeCount, Set<Position> visibleCells) {
        this.playerPosition = playerPosition;
        this.grenadeCount = grenadeCount;
        this.visibleCells = visibleCells;
    }

    public static Game create(GameConfiguration config) {
        return new Game(
                config.playerPosition(),
                config.grenadeCount(),
                Set.of(config.playerPosition())
        );
    }

    public GameState state() {
        return GameState.PLAYING;
    }

    public Position playerPosition() {
        return playerPosition;
    }

    public int grenadeCount() {
        return grenadeCount;
    }

    public Set<Position> visibleCells() {
        return visibleCells;
    }
}
