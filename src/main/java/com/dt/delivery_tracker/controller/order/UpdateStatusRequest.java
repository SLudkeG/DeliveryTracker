package com.dt.delivery_tracker.controller.order;

import com.dt.delivery_tracker.domain.order.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "O novo status é obrigatório.")
        OrderStatus newStatus
) {}
 