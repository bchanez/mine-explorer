package com.mineexplorer.domain;

import java.util.HashSet;
import java.util.Set;

public class Game {

    private final Position playerPosition;
    private final int grenadeCount;
    private final Set<Position> visibleCells;
    private final Set<Wall> walls;
    private final Position exitPosition;
    private final Set<Position> minePositions;

    private Game(Position playerPosition, int grenadeCount, Set<Position> visibleCells, Set<Wall> walls, Position exitPosition, Set<Position> minePositions) {
        this.playerPosition = playerPosition;
        this.grenadeCount = grenadeCount;
        this.visibleCells = visibleCells;
        this.walls = walls;
        this.exitPosition = exitPosition;
        this.minePositions = minePositions;
    }

    public static Game create(GameConfiguration config) {
        return new Game(
                config.playerPosition(),
                config.grenadeCount(),
                Set.of(config.playerPosition()),
                config.walls(),
                config.exitPosition(),
                config.minePositions()
        );
    }

    public GameState state() {
        if (playerSteppedOnMine()) {
            return GameState.LOST;
        }
        if (playerReachedExit()) {
            return GameState.WON;
        }
        return GameState.PLAYING;
    }

    private boolean playerSteppedOnMine() {
        return minePositions.contains(playerPosition);
    }

    private boolean playerReachedExit() {
        return playerPosition.equals(exitPosition);
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

    public Set<Wall> walls() {
        return walls;
    }

    public Game move(Direction direction) {
        var newPosition = playerPosition.neighbor(direction);
        if (isBlockedByWall(playerPosition, newPosition)) {
            return this;
        }
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(newPosition);
        return new Game(newPosition, grenadeCount, Set.copyOf(newVisibleCells), walls, exitPosition, minePositions);
    }

    private boolean isBlockedByWall(Position from, Position to) {
        return walls.contains(Wall.between(from, to));
    }

    public Game throwGrenade(Direction direction) {
        if (hasNoGrenades()) {
            return this;
        }
        var targetPosition = playerPosition.neighbor(direction);
        var wallToDestroy = Wall.between(playerPosition, targetPosition);
        if (!hasWall(wallToDestroy)) {
            return new Game(playerPosition, grenadeCount - 1, visibleCells, walls, exitPosition, minePositions);
        }
        var newWalls = new HashSet<>(walls);
        newWalls.remove(wallToDestroy);
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(targetPosition);
        return new Game(targetPosition, grenadeCount - 1, Set.copyOf(newVisibleCells), Set.copyOf(newWalls), exitPosition, minePositions);
    }

    private boolean hasNoGrenades() {
        return grenadeCount == 0;
    }

    private boolean hasWall(Wall wall) {
        return walls.contains(wall);
    }
}
