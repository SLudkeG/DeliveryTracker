package com.dt.delivery_tracker.controller.order;

import java.time.LocalDateTime;

import com.dt.delivery_tracker.domain.order.OrderStatus;

public record OrderResponse(
        Long id,
        String customerName,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}