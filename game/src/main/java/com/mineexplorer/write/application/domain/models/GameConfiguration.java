package com.mineexplorer.write.application.domain.models;

import com.mineexplorer.sharedkernel.domain.ValueObject;

import java.util.Set;

public record GameConfiguration(Position playerPosition, int grenadeCount, Set<Wall> walls, Position exitPosition, Set<Position> minePositions) implements ValueObject {
}
