package com.mineexplorer.domain;

public record Position(int x, int y) {

    public Position translate(Direction direction) {
        return new Position(x + direction.deltaX(), y + direction.deltaY());
    }
}
