package com.dt.delivery_tracker.domain.order;

public enum OrderStatus {
    CREATED,
    DISPATCHED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}
