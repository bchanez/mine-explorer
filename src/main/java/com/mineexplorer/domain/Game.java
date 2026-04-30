package com.mineexplorer.domain;

import java.util.HashSet;
import java.util.Set;

public class Game {

    private final Position playerPosition;
    private final int grenadeCount;
    private final Set<Position> visibleCells;
    private final Set<Wall> walls;

    private Game(Position playerPosition, int grenadeCount, Set<Position> visibleCells, Set<Wall> walls) {
        this.playerPosition = playerPosition;
        this.grenadeCount = grenadeCount;
        this.visibleCells = visibleCells;
        this.walls = walls;
    }

    public static Game create(GameConfiguration config) {
        return new Game(
                config.playerPosition(),
                config.grenadeCount(),
                Set.of(config.playerPosition()),
                config.walls()
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
        var newPosition = new Position(
                playerPosition.x() + direction.deltaX(),
                playerPosition.y() + direction.deltaY()
        );
        if (walls.contains(Wall.between(playerPosition, newPosition))) {
            return this;
        }
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(newPosition);
        return new Game(newPosition, grenadeCount, Set.copyOf(newVisibleCells), walls);
    }

    public Game throwGrenade(Direction direction) {
        var targetPosition = new Position(
                playerPosition.x() + direction.deltaX(),
                playerPosition.y() + direction.deltaY()
        );
        var wallToDestroy = Wall.between(playerPosition, targetPosition);
        var newWalls = new HashSet<>(walls);
        newWalls.remove(wallToDestroy);
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(targetPosition);
        return new Game(targetPosition, grenadeCount - 1, Set.copyOf(newVisibleCells), Set.copyOf(newWalls));
    }
}
