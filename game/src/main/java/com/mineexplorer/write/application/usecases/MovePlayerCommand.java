package com.mineexplorer.write.application.usecases;

import com.mineexplorer.write.application.domain.models.Direction;

public record MovePlayerCommand(Direction direction) {
}
