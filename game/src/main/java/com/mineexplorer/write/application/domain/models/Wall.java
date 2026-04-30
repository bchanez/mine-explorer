package com.mineexplorer.write.application.domain.models;

import com.mineexplorer.sharedkernel.domain.ValueObject;

public record Wall(Position position1, Position position2) implements ValueObject {

    public Wall {
        if (position1.x() > position2.x() || (position1.x() == position2.x() && position1.y() > position2.y())) {
            var temp = position1;
            position1 = position2;
            position2 = temp;
        }
    }

    public static Wall between(Position from, Position to) {
        return new Wall(from, to);
    }
}
