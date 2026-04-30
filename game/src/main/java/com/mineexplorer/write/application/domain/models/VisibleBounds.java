package com.mineexplorer.write.application.domain.models;

import com.mineexplorer.sharedkernel.domain.ValueObject;

public record VisibleBounds(int minX, int maxX, int minY, int maxY) implements ValueObject {
}
