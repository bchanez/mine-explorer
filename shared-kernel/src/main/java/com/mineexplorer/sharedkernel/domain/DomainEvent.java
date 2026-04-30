package com.mineexplorer.sharedkernel.domain;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
