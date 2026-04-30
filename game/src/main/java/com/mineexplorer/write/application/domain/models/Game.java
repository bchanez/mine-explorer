package com.mineexplorer.write.application.domain.models;

import java.util.HashSet;
import java.util.Set;

// Aggregate Root (TODO: add GameId and extend AggregateRoot<GameId>)
public class Game {

    private final Player player;
    private final Set<Position> visibleCells;
    private final Set<Wall> walls;
    private final Position exitPosition;
    private final Set<Position> minePositions;

    private Game(Player player, Set<Position> visibleCells, Set<Wall> walls,
            Position exitPosition, Set<Position> minePositions) {
        this.player = player;
        this.visibleCells = visibleCells;
        this.walls = walls;
        this.exitPosition = exitPosition;
        this.minePositions = minePositions;
    }

    public static Game create(GameConfiguration config) {
        validateConfiguration(config);
        var player = new Player(config.playerPosition(), config.grenadeCount());
        return new Game(
                player,
                Set.of(config.playerPosition()),
                config.walls(),
                config.exitPosition(),
                config.minePositions());
    }

    private static void validateConfiguration(GameConfiguration config) {
        if (config.exitPosition() != null && config.minePositions().contains(config.exitPosition())) {
            throw new InvalidGameConfigurationException("Mine and exit cannot be on the same cell");
        }
        if (config.minePositions().contains(config.playerPosition())) {
            throw new InvalidGameConfigurationException("Player cannot start on a mine");
        }
        if (config.playerPosition().equals(config.exitPosition())) {
            throw new InvalidGameConfigurationException("Player cannot start on the exit");
        }
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
        return minePositions.contains(player.position());
    }

    private boolean playerReachedExit() {
        return player.position().equals(exitPosition);
    }

    private boolean isGameOver() {
        return state() != GameState.PLAYING;
    }

    public Position playerPosition() {
        return player.position();
    }

    public int grenadeCount() {
        return player.grenadeCount();
    }

    public Set<Position> visibleCells() {
        return visibleCells;
    }

    public Set<Wall> walls() {
        return walls;
    }

    public Game move(Direction direction) {
        if (isGameOver()) {
            return this;
        }
        var newPosition = player.position().neighbor(direction);
        if (isOutOfBounds(newPosition) || isBlockedByWall(player.position(), newPosition)) {
            return this;
        }
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(newPosition);
        return new Game(player.moveTo(newPosition), Set.copyOf(newVisibleCells), walls, exitPosition, minePositions);
    }

    private boolean isOutOfBounds(Position position) {
        return position.x() < 0 || position.x() > 4 || position.y() < 0 || position.y() > 4;
    }

    private boolean isBlockedByWall(Position from, Position to) {
        return walls.contains(Wall.between(from, to));
    }

    public Game throwGrenade(Direction direction) {
        if (isGameOver()) {
            return this;
        }
        if (!player.hasGrenades()) {
            return this;
        }
        var targetPosition = player.position().neighbor(direction);
        var wallToDestroy = Wall.between(player.position(), targetPosition);
        if (!hasWall(wallToDestroy)) {
            return new Game(player.consumeGrenade(), visibleCells, walls, exitPosition, minePositions);
        }
        if (isOutOfBounds(targetPosition)) {
            return new Game(player.consumeGrenade(), visibleCells, walls, exitPosition, minePositions);
        }
        var newWalls = new HashSet<>(walls);
        newWalls.remove(wallToDestroy);
        var newVisibleCells = new HashSet<>(visibleCells);
        newVisibleCells.add(targetPosition);
        var movedPlayer = player.consumeGrenade().moveTo(targetPosition);
        return new Game(movedPlayer, Set.copyOf(newVisibleCells), Set.copyOf(newWalls), exitPosition, minePositions);
    }

    private boolean hasWall(Wall wall) {
        return walls.contains(wall);
    }

    public boolean hasVisibleWallToRight(Position pos) {
        var rightNeighbor = pos.neighbor(Direction.EAST);
        var wallExists = walls.contains(Wall.between(pos, rightNeighbor));
        var isVisible = visibleCells.contains(pos) || visibleCells.contains(rightNeighbor);
        return wallExists && isVisible;
    }

    public boolean hasVisibleWallBelow(Position pos) {
        var belowNeighbor = pos.neighbor(Direction.SOUTH);
        var wallExists = walls.contains(Wall.between(pos, belowNeighbor));
        var isVisible = visibleCells.contains(pos) || visibleCells.contains(belowNeighbor);
        return wallExists && isVisible;
    }

    public VisibleBounds visibleBounds() {
        int minX = visibleCells.stream().mapToInt(Position::x).min().orElse(0);
        int maxX = visibleCells.stream().mapToInt(Position::x).max().orElse(0);
        int minY = visibleCells.stream().mapToInt(Position::y).min().orElse(0);
        int maxY = visibleCells.stream().mapToInt(Position::y).max().orElse(0);
        return new VisibleBounds(minX, maxX, minY, maxY);
    }

    public boolean isVisibleCell(Position pos) {
        return visibleCells.contains(pos);
    }
}
