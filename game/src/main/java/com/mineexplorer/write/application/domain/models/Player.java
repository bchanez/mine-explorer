package com.mineexplorer.write.application.domain.models;

import com.mineexplorer.sharedkernel.domain.ValueObject;

public record Player(Position position, int grenadeCount) implements ValueObject {

    public Player moveTo(Position newPosition) {
        return new Player(newPosition, grenadeCount);
    }

    public Player consumeGrenade() {
        return new Player(position, grenadeCount - 1);
    }

    public boolean hasGrenades() {
        return grenadeCount > 0;
    }
}
