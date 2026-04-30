package com.mineexplorer.write.application.domain.models;

import com.mineexplorer.sharedkernel.domain.ValueObject;

public record Position(int x, int y) implements ValueObject {

    public Position neighbor(Direction direction) {
        return new Position(x + direction.deltaX(), y + direction.deltaY());
    }
}
