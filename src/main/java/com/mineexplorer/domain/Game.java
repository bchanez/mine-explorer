package com.mineexplorer.domain;

import java.util.HashSet;
import java.util.Set;

public class Game {

    private final Position playerPosition;
    private final int grenadeCount;
    private final Set<Position> visibleCells;
    private final Set<Wall> walls;
    private final Position exitPosition;

    private Game(Position playerPosition, int grenadeCount, Set<Position> visibleCells, Set<Wall> walls, Position exitPosition) {
        this.playerPosition = playerPosition;
        this.grenadeCount = grenadeCount;
        this.visibleCells = visibleCells;
        this.walls = walls;
        this.exitPosition = exitPosition;
    }

    public static Game create(GameConfiguration config) {
        return new Game(
                config.playerPosition(),
                config.grenadeCount(),
                Set.of(config.playerPosition()),
                config.walls(),
                config.exitPosition()
        );
    }

    public GameState state() {
        if (playerPosition.equals(exitPosition)) {
            return GameState.WON;
        }
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
        var newPosition = new Position(
                playerPosition.x() + direction.deltaX(),
                playerPosition.y() + direction.deltaY()
        );
        if (walls.contains(Wall.between(playerPosition, newPosition))) {
            return this;
        }
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(newPosition);
        return new Game(newPosition, grenadeCount, Set.copyOf(newVisibleCells), walls, exitPosition);
    }

    public Game throwGrenade(Direction direction) {
        if (grenadeCount == 0) {
            return this;
        }
        var targetPosition = new Position(
                playerPosition.x() + direction.deltaX(),
                playerPosition.y() + direction.deltaY()
        );
        var wallToDestroy = Wall.between(playerPosition, targetPosition);
        var wallExists = walls.contains(wallToDestroy);
        if (!wallExists) {
            return new Game(playerPosition, grenadeCount - 1, visibleCells, walls, exitPosition);
        }
        var newWalls = new HashSet<>(walls);
        newWalls.remove(wallToDestroy);
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(targetPosition);
        return new Game(targetPosition, grenadeCount - 1, Set.copyOf(newVisibleCells), Set.copyOf(newWalls), exitPosition);
    }
}
