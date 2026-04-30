package com.mineexplorer.write.application.domain.models;

public record Position(int x, int y) {

    public Position neighbor(Direction direction) {
        return new Position(x + direction.deltaX(), y + direction.deltaY());
    }
}
