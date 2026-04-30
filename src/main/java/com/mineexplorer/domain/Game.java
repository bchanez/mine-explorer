package com.mineexplorer.domain;

import java.util.HashSet;
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

    public Game move(Direction direction) {
        var newPosition = new Position(playerPosition.x(), playerPosition.y() + 1);
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(newPosition);
        return new Game(newPosition, grenadeCount, Set.copyOf(newVisibleCells));
    }
}
