package com.dt.delivery_tracker.domain.repository;

import java.time.LocalDateTime;

public record OrderEventResponse(
        Long id,
        String eventType,
        LocalDateTime eventTime,
        String description) {
}
