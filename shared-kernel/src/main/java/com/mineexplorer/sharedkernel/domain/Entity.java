package com.mineexplorer.sharedkernel.domain;

import java.util.Objects;

public abstract class Entity<ID> {

    private final ID id;

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id, "Entity ID must not be null");
    }

    public ID id() {
        return id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        if (!this.getClass().equals(entity.getClass())) return false;
        return id.equals(entity.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }
}
