package com.dt.delivery_tracker.controller.order;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        String customerName,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}